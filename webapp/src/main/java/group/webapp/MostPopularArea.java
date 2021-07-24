/**
 *
 * @author Omar Safwat
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

public class MostPopularArea {

    public static String getHtmlPage() throws IOException, URISyntaxException {
        JobsCSVDAO dao = new JobsCSVDAO();
        // Get path of csv file from maven classpath
        String filePath = MostPopularArea.class.getResource("/Wuzzuf_Jobs.csv").toString().replace("file:/", "");
        
        // Read file with DAO
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();

        // Clean data
        jobs = dao.cleanData(jobs);

        // Create a Frequency map for each location
        Map<String, Long> locFreqMap = jobs.stringVector("Location")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sort the Frequency Map and convert it to list of map entries
        List<Map.Entry<String, Long>> locFreqList = locFreqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Plotting the histogram of each location
        String imageAsStream = graphLocationsFreq(locFreqMap);
        
        // Store html file as string to edit it
        String htmlPath = "/mostPopularArea.html";
        File htmlFile = new File(MostPopularArea.class.getResource(htmlPath).toURI());
        String htmlString = FileUtils.readFileToString(htmlFile);
        
        String textTag = "<p>Most popular area is: " + locFreqList.get(0).getKey() + ", With a total number of occurance: " + locFreqList.get(0).getValue() + "</p>";
        
        // Add results to html string
        htmlString = htmlString.replace("$textTag", textTag);
        htmlString = htmlString.replace("$locationsHistogram", imageAsStream);
        
        //Return as a string
        return htmlString;
    }
    
    public static String graphLocationsFreq(Map<String, Long> frequencyMap) throws IOException {
        
        // Filter Locations with more than a 150 occurence
        frequencyMap = frequencyMap.entrySet().stream().filter(e -> e.getValue() > 70).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
       
        List<String> locations = frequencyMap.keySet().stream().collect(Collectors.toList());
        List<Long> freq = frequencyMap.values().stream().collect(Collectors.toList());
        
        // Create and customize chart
        CategoryChart chart = new CategoryChartBuilder ().width (1024).height (768).title ("Locations Histogram").xAxisTitle ("Location").yAxisTitle ("Frequency").build ();
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart.getStyler ().setHasAnnotations (true);
        chart.getStyler ().setStacked (true);
        
        chart.addSeries ("Location counts", locations, freq);
        
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
