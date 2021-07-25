/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.webapp;

import java.io.*;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import smile.data.DataFrame;

/**
 *
 * @author Mostafa Mohamed Fathy
 */
public class ExploreData {

    public static String exploreData() throws IOException, URISyntaxException {
        JobsCSVDAO dao = new JobsCSVDAO();
        // Get path of csv file from maven classpath
        String filePath = MostPopularArea.class.getResource("/Wuzzuf_Jobs.csv").toString().replace("file:/", "");

        // Read file with DAO
        dao.readCSV(filePath);
        DataFrame jobs = dao.getAllJobs();

        String htmlPath = "/exploreData.html";
        File htmlFile = new File(ExploreData.class.getResource(htmlPath).toURI());

        String htmlString = FileUtils.readFileToString(htmlFile);
        String body = "<p> Number Of Columns : <strong>" + String.valueOf(jobs.ncols()) + "</strong></p>";
        body += "<br><p> Number Of Rows : <strong>" + String.valueOf(jobs.nrows()) + "</strong></p>";

        String[] col = jobs.names();
        String names = "";
        for (String st : col) {
            names = names + "<tr><td> " + st + "</td><td>" + jobs.column(st).type() + "</td></tr>";
        }
String rows="";
        for (int i=0; i < 5; i++) {
            rows += "<tr><td> " + jobs.get(0).getAs("Title") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("Company") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("Location") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("Level") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("YearsExp") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("Country") + "</td>";
            rows += "<td> " + jobs.get(i).getAs("Skills") + "</td></tr>";
        }

        htmlString = htmlString.replace("$textTag", body);

        htmlString = htmlString.replace("$tablebody", names);

        htmlString = htmlString.replace("$rows", rows);
        //Return as a string
        return htmlString;
    }
}
