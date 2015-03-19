/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class BinPacker {
    
    IPackingBehavior behavior;
    ItemReader input;
    String path;
    
    public BinPacker(String filePath, IPackingBehavior b) throws FileNotFoundException{
        path = filePath;
        setBehavior(b);
        reset();
    }
    
    public void reset() throws FileNotFoundException
    {
       input = new ItemReader(path);
    }
    
    public void setBehavior(IPackingBehavior b)
    {
        behavior = b;
    }
    
    public void solveOnline() throws IOException{
        if(!input.ready()){
            reset();
        }
        
        System.out.println(String.format("%s Online Algorithm", behavior.getName()));
        System.out.println(behavior.solveOnline(input).toString().substring(0, 100));
    }
    
    public void solveOffline() throws IOException{
        if(!input.ready()){
            reset();
        }
        
        System.out.println(String.format("%s Offline Algorithm", behavior.getName()));
        System.out.println(behavior.solveOffline(input).toString().substring(0, 100));
    }
}
