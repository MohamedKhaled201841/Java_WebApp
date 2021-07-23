package group.webapp;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import smile.data.DataFrame;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MostPopularCompany {
    public static void main(String[] args) {
        JobsCSVDAO dao=new JobsCSVDAO();
        String filePath = "src/main/resources/data/Wuzzuf_Jobs.csv";
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();
        // Clean data
        jobs = dao.cleanData(jobs);
        // Create a Frequency map for each company
        Map<String, Long> comFreqMap = jobs.stringVector("Company")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        // Sort the Frequency Map and convert it to list of map entries
        List<Map.Entry<String, Long>> comFreqList = comFreqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        System.out.println("Most popular company is: " + comFreqList.get(0).getKey() + ", With a total number of occurance: " + comFreqList.get(0).getValue());
        // Plotting the histogram of each company
        graphCompanysFreqHistogram(comFreqMap);
        // Plotting the pieChart of each company
        graphCompanysFreqPie(comFreqMap);
    }
    public static void graphCompanysFreqHistogram(Map<String, Long> frequencyMap) {

        // Filter Company with more than a 15 occurence
        frequencyMap = frequencyMap.entrySet().stream().filter(e -> e.getValue() > 15).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        List<String> companys = frequencyMap.keySet().stream().collect(Collectors.toList());
        List<Long> freq = frequencyMap.values().stream().collect(Collectors.toList());

        // Create and customize chart
        CategoryChart chart = new CategoryChartBuilder().width (1024).height (768).title ("Companys Histogram").xAxisTitle ("Company").yAxisTitle ("Frequency").build ();
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart.getStyler ().setHasAnnotations (true);
        chart.getStyler ().setStacked (true);

        chart.addSeries ("Company counts",companys, freq);
        // Show it
        new SwingWrapper(chart).displayChart ();
    }
    public static void graphCompanysFreqPie(Map<String, Long> comFreqMap){
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
        new SwingWrapper (chart).displayChart ();
    }
}
