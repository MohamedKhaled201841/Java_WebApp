/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group.webapp;

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
