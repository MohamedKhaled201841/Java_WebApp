package com.mycompany.java_ml_webapp;

import java.util.List;

/**
 * Data Access Object interface for wuzzuf.com Jobs
 * 
 * @author Omar Safwat
 */
public interface JobsDAO<T> {
    
    // Implementation should include a private attribute, manipulated by the methods below
    
    // Returns the dataset to user
    public T getAllJobs();
    
    // Method should remove null rows and duplicates
    public T cleanData(T data);
    
    // Method returns feature as int array with encoded values
    public int[] encodeFeature(T data, String featureName);
}
