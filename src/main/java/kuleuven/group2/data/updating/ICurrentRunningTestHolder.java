package kuleuven.group2.data.updating;

import kuleuven.group2.data.Test;

/**
 * An interface for classes that can give back the test currently being run.
 * 
 * @author Ruben Pieters
 */
public interface ICurrentRunningTestHolder {

        public Test getCurrentRunningTest();
        
}
