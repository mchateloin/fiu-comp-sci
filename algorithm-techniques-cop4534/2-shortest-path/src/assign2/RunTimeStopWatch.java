package assign2;

import java.util.ArrayList;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class RunTimeStopWatch {
    
    private ArrayList<Long> splitTimes;
    
    public RunTimeStopWatch(){
        splitTimes = new ArrayList<Long>();
    }
    
    public void start(){
        split();
    }
    
    public long split(){
        splitTimes.add(System.currentTimeMillis());
        if(splitTimes.size() < 2)
        {
            return 0;
        } else {
            return splitTimes.get(splitTimes.size()-1) - splitTimes.get(splitTimes.size()-2);
        }
    }
    
    public long getTotalElapsed(){
        if(splitTimes.size() < 1)
        {
            return -1;
        }
        
        return  System.currentTimeMillis() - splitTimes.get(0);
    }
    
}
