/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;


import com.mycompany.java_ml_webapp.JobsDAO;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.data.measure.NominalScale;
import smile.io.Read;

/**
 *
 * @author Omar Safwat
 */
public class JobsCSVDAO implements JobsDAO<DataFrame> {
    
    private DataFrame jobs = null;
    
    public void readCSV(String path){
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
}
