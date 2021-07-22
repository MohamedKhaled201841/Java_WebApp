/**
 *
 * @author Mostafa Fathy
 */
package com.mycompany.mavenproject2;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import smile.data.DataFrame;

public class MostPopularTitle {

    public static void main(String[] args) throws IOException {
        JobsCSVDAO dao = new JobsCSVDAO();
        String filePath = "src/main/resources/data/Wuzzuf_Jobs.csv";
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();

        // Clean data
        jobs = dao.cleanData(jobs);

        // Create a Frequency map for each Titles
        Map<String, Long> locFreqMap = jobs.stringVector("Title")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        graphLocationsFreq(locFreqMap);
    }

    public static void graphLocationsFreq(Map<String, Long> titlesCounts) throws IOException {
        int limit = 7;

        List<String> t = titlesCounts.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                //                .filter(x -> x.getValue() >= 20)
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Long> v = titlesCounts.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(limit)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder().width(1024).height(768).title("Popular Jobs").xAxisTitle("Names").yAxisTitle("Age").build();
        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        // Series
        chart.addSeries("Popular Jobs", t, v);
        // Show it
        VectorGraphicsEncoder.saveVectorGraphic(chart, "src/main/resources/Sample_Chart", VectorGraphicsEncoder.VectorGraphicsFormat.SVG);

        new SwingWrapper(chart).displayChart();
    }

}
