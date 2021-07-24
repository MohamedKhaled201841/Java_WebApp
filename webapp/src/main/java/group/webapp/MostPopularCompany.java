/**
 *
 * @author mohamed khaled
 */
package group.webapp;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.knowm.xchart.*;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.style.*;
import smile.data.DataFrame;

public class MostPopularCompany {
    public static String getHtmlPage() throws IOException, URISyntaxException {
        JobsCSVDAO dao = new JobsCSVDAO();
        // Get path of csv file from maven classpath
        String filePath = MostPopularArea.class.getResource("/Wuzzuf_Jobs.csv").toString().replace("file:/", "");
        
        // Read file with DAO
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();

        // Create a Frequency map for each location
                Map<String, Long> comFreqMap = jobs.stringVector("Company")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sort the Frequency Map and convert it to list of map entries
            List<Map.Entry<String, Long>> comFreqList = comFreqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Plotting the histogram of each location
        String imageAsStream = graphCompanysFreqPie(comFreqMap);
        
        // Store html file as string to edit it
        String htmlPath = "/mostPopularCompany.html";
        File htmlFile = new File(MostPopularCompany.class.getResource(htmlPath).toURI());
        String htmlString = FileUtils.readFileToString(htmlFile);
        
        String textTag = "<p>Most popular company is: " + comFreqList.get(0).getKey() + ", With a total number of occurance: " + comFreqList.get(0).getValue() + "</p>";
        
        // Add results to html string
        htmlString = htmlString.replace("$textTag", textTag);
        htmlString = htmlString.replace("$companiesPieChart", imageAsStream);
        
        //Return as a string
        return htmlString;
    }
    
    public static String graphCompanysFreqPie(Map<String, Long> comFreqMap) throws IOException{
        // Filter Company with more than a 15 occurence
        comFreqMap = comFreqMap.entrySet().stream().filter(e -> e.getValue() > 15).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        // Create Chart
        PieChart chart =
                new PieChartBuilder().width(800).height(600).title("Company counts").build();
        // Customize Chart
        chart.getStyler().setCircular(false);
        // Series
        for (Map.Entry<String, Long> entry : comFreqMap.entrySet()) {
            chart.addSeries(entry.getKey(), entry.getValue());
        }
        
        // Save plot as SVG file in a temporary file and return its path
        File tempFile = File.createTempFile("plot", ".svg");
        VectorGraphicsEncoder.saveVectorGraphic(chart, tempFile.getAbsolutePath(), VectorGraphicsFormat.SVG);
        String plotPath = tempFile.getAbsolutePath();
        
        String imageAsStream;
        FileInputStream inputStream = new FileInputStream(plotPath);
        try {
            imageAsStream = IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }
        
        // Delete temporary file
        tempFile.deleteOnExit();
        
        return imageAsStream;
    }
}
