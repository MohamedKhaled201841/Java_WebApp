/**
 *
 * @author Mostafa Mohamed Fathy
 */
package group.webapp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import smile.data.DataFrame;

public class MostPopularSkill {

    static String data = "";

    public static String saveMostPopularSkills(List<Map.Entry<String, Long>> result) throws IOException {

        result.forEach(s -> data += "<tr><td> " + s.getKey() + "</td><td>" + s.getValue() + "</td></tr>");
        return data;

    }

    public static String getHtmlPage() throws IOException, URISyntaxException {
        JobsCSVDAO dao = new JobsCSVDAO();
        // Get path of csv file from maven classpath
        String filePath = MostPopularArea.class.getResource("/Wuzzuf_Jobs.csv").toString().replace("file:/", "");

        // Read file with DAO
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();

        // Clean data
        jobs = dao.cleanData(jobs);
        List<String> skills = new LinkedList<>();
        jobs.stringVector("Skills").stream().forEach(c -> {
            String[] sk = c.split(",");
            for (int i = 0; i < sk.length; i++) {
                skills.add(sk[i]);
            }

        });

        // Create a Frequency map for each skill
        Map<String, Long> locFreqMap = skills
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<String, Long>> locFreqList = locFreqMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Adding skills to a table
        String tableAsStream = saveMostPopularSkills(locFreqList);

        // Store html file as string to edit it
        String htmlPath = "/mostPopularSkill.html";
        File htmlFile = new File(ExploreData.class.getResource(htmlPath).toURI());
        String htmlString = FileUtils.readFileToString(htmlFile);

        // Add results to html string
        htmlString = htmlString.replace("$textTag", "Skills");
        htmlString = htmlString.replace("$SkillsTable", tableAsStream);

        //Return as a string
        return htmlString;
    }

}
