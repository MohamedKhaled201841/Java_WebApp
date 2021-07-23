/**
 *
 * @author Mostafa Fathy
 */
package group.webapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import smile.data.DataFrame;

public class MostPopularSkill {

    public static void main(String[] args) throws IOException {
        JobsCSVDAO dao = new JobsCSVDAO();
        String filePath = "/Wuzzuf_Jobs.csv";
        Path path = new File(MostPopularArea.class.getClass().getResource(filePath).getFile()).toPath();
        dao.readCSV(filePath);//MostPopularArea.class.getResource(filePath).toString());
        DataFrame jobs = dao.getAllJobs();

        // Clean data
        jobs = dao.cleanData(jobs);

        // Create a Frequency map for each Skill
        Map<String, Long> locFreqMap = jobs.stringVector("Skills")
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
//save Skills to a file and Print them
        saveMostPopularSkills(locFreqMap);
    }

    public static void saveMostPopularSkills(Map<String, Long> result) throws IOException {
        FileWriter f = new FileWriter("src/main/resources/skills_count.txt");

        result.entrySet().stream().forEach(entry -> {
            try {
                f.write(entry.getKey() + ":" + entry.getValue().toString() + "\n");
                System.out.println(entry.getKey() + ":" + entry.getValue().toString() + "\n");
            } catch (IOException ex) {
                Logger.getLogger(MostPopularSkill.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        f.close();
    }

}
