/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.webapp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.measure.NominalScale;
import smile.io.Read;

/**
 *
 * @author Omar Safwat
 */
public class JobsCSVDAO implements JobsDAO<DataFrame> {
    
    private DataFrame jobs = null;
    
    public void readCSV(String path) {
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            this.jobs = Read.csv(path, format);
        } catch (URISyntaxException | IOException ex){}
        // Display some of the Data
        System.err.println(jobs.slice(0, 6));

        // Structure of DataFrame
        System.err.println(jobs.structure());

        // Summary of DataFrame
        System.err.println(jobs.summary());
    }
     
    @Override
    public DataFrame getAllJobs() {
        return this.jobs;
    }
    
    // Method should remove null rows and duplicates
    @Override
    public DataFrame cleanData(DataFrame df) {
        jobs = df.omitNullRows();
        jobs = DataFrame.of(jobs.stream().distinct());
        return jobs;
    }
    
    // Method returns feature as int array with encoded values
    @Override
    public int[] encodeFeature(DataFrame df, String featureName) {
        String[] values = df.stringVector (featureName).distinct ().toArray (new String[]{});
        int[] encodedValues = df.stringVector (featureName).factorize (new NominalScale (values)).toIntArray ();
        return encodedValues;
    }
    
    // Method returns DataFrame as a list of job objects
    @Override
    public List<Job> getJobsList() {
        assert jobs != null;
        List<Job> jobsList = new ArrayList<> ();
        ListIterator<Tuple> iterator = jobs.stream ().collect (Collectors.toList ()).listIterator ();
        while (iterator.hasNext ()) {
            Tuple t = iterator.next ();
            Job j = new Job();
            // Set Job class attributes
            j.setTitle((String)t.get("Title"));
            j.setCompany((String)t.get("Company"));
            j.setCountry((String)t.get("Country"));
            j.setLocation(t.getAs("Location"));
            j.setType(t.getAs("Type"));
            j.setYearsExp(t.getAs("YearsExp"));
            String[] skillsArray = t.get("Skills").toString().split(",");
            j.setSkills(Arrays.asList(skillsArray));
 
            // Append new job object to List
            jobsList.add (j);
        }
        return jobsList;
    }
}
