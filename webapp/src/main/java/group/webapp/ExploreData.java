/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package group.webapp;

import java.io.*;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Omar Safwat
 */
public class ExploreData {
    
    public static String exploreData() throws IOException, URISyntaxException {
        
        String htmlPath = "/exploreData.html";
        File htmlFile = new File(ExploreData.class.getResource(htmlPath).toURI());
        
        String htmlString = FileUtils.readFileToString(htmlFile);
        String body = "This is body";
        htmlString = htmlString.replace("$body", body);
        //Return as a string
        return htmlString;
    }
}
