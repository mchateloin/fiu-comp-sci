/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class NextFit implements IPackingBehavior<Bin> {
        
    public String getName(){
        return "Next Fit";
    }
    
    @Override
    public Collection<Bin> solveOnline(ItemReader input) {
        LinkedList<Bin> bins = new LinkedList<>();
        Integer w;
        
        try {
            while((w = input.next()) != null)
            {
                if(bins.isEmpty() || bins.getLast().getCapacity() < w)
                {
                    bins.add(new Bin());
                }
                
                bins.getLast().addWeight(w);
            }
        } catch (IOException ex) {
            Logger.getLogger(NextFit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bins;
    }
    

    @Override
    public Collection<Bin> solveOffline(ItemReader input) {
        PriorityQueue<WeightedItem> weightsDesc = new PriorityQueue<>();
        LinkedList<Bin> bins = new LinkedList<>();
        Integer w;
        WeightedItem item;
        try {
            
            while((w = input.next()) != null)
            {
                weightsDesc.add(new WeightedItem(w));
            }
            
            while((item = weightsDesc.poll()) != null)
            {
                if(bins.isEmpty() || bins.getLast().getCapacity() < item.getWeight())
                {
                    bins.add(new Bin());
                }
                
                bins.getLast().addWeight(item);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(NextFit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bins;
    }

    
}
