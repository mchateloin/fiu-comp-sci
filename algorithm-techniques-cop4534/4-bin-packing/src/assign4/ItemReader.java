/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class ItemReader extends BufferedReader{
    
    private String currentWeight;

    public ItemReader(InputStreamReader reader){
        super(reader);
    }
    
    public ItemReader(String filePath) throws FileNotFoundException{
        super(new FileReader(filePath));
        currentWeight = null;
    }
    
    public Integer next() throws IOException{
        
        if((currentWeight = readLine()) != null){
            return Integer.parseInt(currentWeight);
        }
        
        return null;
    }
    
}
