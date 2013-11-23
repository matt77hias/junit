package kuleuven.group2.data.updating;

import kuleuven.group2.data.Test;


/**
 * An interface for classes that can give back the test currently being run.
 * 
 * @author Group2
 * @version 9 November 2013
 */
public interface CurrentRunningTestHolder {

        public Test getCurrentRunningTest();
        
}
