/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.IOException;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class BestFit implements IPackingBehavior<Bin>{
    
    public String getName(){
        return "Best Fit";
    }
    
    @Override
    public Collection<Bin> solveOnline(ItemReader input) {
        NavigableSet<Bin> bins = new TreeSet<>();
        Integer w;
        Bin b;
        Bin idealBin;

        try {
            
            while((w = input.next()) != null)
            {
                idealBin = new Bin();
                idealBin.setID(Integer.MAX_VALUE);
                idealBin.addWeight(idealBin.getCapacity() - w);
                
                if(bins.isEmpty() || (b = bins.lower(idealBin)) == null){
                   b = new Bin();
                } else {
                   bins.remove(b);
                }
                
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
        NavigableSet<Bin> bins = new TreeSet<>();
        PriorityQueue<WeightedItem> weightsDesc = new PriorityQueue<>();
        WeightedItem item;
        Integer w;
        Bin b;
        Bin idealBin;
        
        try {
            
            while((w = input.next()) != null)
            {
                weightsDesc.add(new WeightedItem(w));
            }
            
            while((item = weightsDesc.poll()) != null)
            {
                idealBin = new Bin();
                idealBin.setID(Integer.MAX_VALUE);
                idealBin.addWeight(idealBin.getCapacity() - item.getWeight());
                
                if(bins.isEmpty() || (b = bins.lower(idealBin)) == null){
                   b = new Bin();
                } else {
                   bins.remove(b);
                }
                
                b.addWeight(item);
                bins.add(b);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(NextFit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bins;
    }



    
}
