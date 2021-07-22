/**
 *
 * @author Omar Safwat
 */
package com.mycompany.mavenproject2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import smile.data.DataFrame;

public class MostPopularArea {

    public static void main(String[] args) {
        JobsCSVDAO dao = new JobsCSVDAO();
        String filePath = "src/main/resources/data/Wuzzuf_Jobs.csv";
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
        System.out.println("Most popular area is: " + locFreqList.get(0).getKey() + ", With a total number of occurance: " + locFreqList.get(0).getValue());

        // Plotting the histogram of each location
        graphLocationsFreq(locFreqMap);
    }
    
    public static void graphLocationsFreq(Map<String, Long> frequencyMap) {
        
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
        // Show it
        new SwingWrapper (chart).displayChart ();
    }

}
