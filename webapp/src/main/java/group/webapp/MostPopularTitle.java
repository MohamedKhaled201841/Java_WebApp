/**
 *
 * @author Mostafa Mohamed Fathy
 */
package group.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import smile.data.DataFrame;

public class MostPopularTitle {

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
        Map<String, Long> locFreqMap = jobs.stringVector("Title")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Sort the Frequency Map and convert it to list of map entries
        List<Map.Entry<String, Long>> locFreqList = locFreqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Plotting the histogram of each Titles
        String imageAsStream = graphLocationsFreq(locFreqMap);

        // Store html file as string to edit it
        String htmlPath = "/mostPopularTitle.html";
        File htmlFile = new File(ExploreData.class.getResource(htmlPath).toURI());
        String htmlString = FileUtils.readFileToString(htmlFile);

        String textTag = "<p>Most popular Title is:<strong> " + locFreqList.get(0).getKey() + "</strong><br> With a total number of occurance: <strong>" + locFreqList.get(0).getValue() + "<strong></p>";

        // Add results to html string
        htmlString = htmlString.replace("$textTag", textTag);
        htmlString = htmlString.replace("$locationsHistogram", imageAsStream);

        //Return as a string
        return htmlString;
    }

    public static String graphLocationsFreq(Map<String, Long> titlesCounts) throws IOException {

        int limit = 5;

        List<String> t = titlesCounts.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Long> v = titlesCounts.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(limit)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder().width(1024).height(768).title("Popular Jobs").xAxisTitle("Titles").yAxisTitle("Count").build();
        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        // Series
        chart.addSeries("Popular Jobs", t, v);

        // Save plot as SVG file in a temporary file and return its path
        File tempFile = File.createTempFile("plot", ".svg");
        VectorGraphicsEncoder.saveVectorGraphic(chart, tempFile.getAbsolutePath(), VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
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
