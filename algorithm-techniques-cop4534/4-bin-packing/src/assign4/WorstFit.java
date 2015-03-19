/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class WorstFit implements IPackingBehavior<Bin>{

    public String getName(){
        return "Worst Fit";
    }
    
    @Override
    public Collection<Bin> solveOnline(ItemReader input) {
        PriorityQueue<Bin> bins = new PriorityQueue<>();
        Integer w;
        Bin b;
        
        try {
            
            while((w = input.next()) != null)
            {
                if(bins.isEmpty() || bins.peek().getCapacity() < w)
                {
                    bins.add(new Bin());
                }
                
                b = bins.poll();
                b.addWeight(w);
                bins.add(b);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(NextFit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bins;
    }

    @Override
    public Collection<Bin> solveOffline(ItemReader input) {
        PriorityQueue<Bin> bins = new PriorityQueue<>();
        PriorityQueue<WeightedItem> weightsDesc = new PriorityQueue<>();
        WeightedItem item;
        Integer w;
        Bin b;
        
        try {
            
            while((w = input.next()) != null)
            {
                weightsDesc.add(new WeightedItem(w));
            }
            
            while((item = weightsDesc.poll()) != null)
            {
                if(bins.isEmpty() || bins.peek().getCapacity() < item.getWeight()) {
                    bins.add(new Bin());
                }
                
                b = bins.poll();
                b.addWeight(item);
                bins.add(b);
            }
        } catch (IOException ex) {
            Logger.getLogger(NextFit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bins;
    }


    
}
