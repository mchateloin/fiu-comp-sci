/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.util.Collection;
import java.util.Iterator;

/**
 * As much as I like reusability, I'm going to skip making this generic and implementing Collection 
 * so I can actually turn this assignment in on time.
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class TournamentTree implements Iterable<Bin>{
    public static final int DEFAULT_START_CAP = 256;
    public static final int DEFAULT_GROWTH_FACTOR = 2;
    
    private int startCap = DEFAULT_START_CAP;
    private int growthFactor = DEFAULT_GROWTH_FACTOR;
    private Bin[] contestants;
    private Bin[] winners;

    public TournamentTree(){
        startCap = DEFAULT_START_CAP;
        growthFactor = DEFAULT_GROWTH_FACTOR;
         
        for(int i = 0; i < contestants.length; i++){
            contestants[i] = new Bin();
        }
        
        for(int i = 0; i < contestants.length; i += 2){
            update(i);
        }
        
        updateAll();
    }
    
    public TournamentTree(Collection<Bin> bins){
        startCap = bins.size();
        growthFactor = DEFAULT_GROWTH_FACTOR;
        contestants = new Bin[startCap];
        winners = new Bin[bins.size()%2 == 0 ? bins.size() + 1 : bins.size() + 2]; // extra for phantom bin,  always even size
        
        int i = 0;
        for(Bin b : bins){
           contestants[i] = b; 
           i++;
        }
        
        for(; i < contestants.length - 1; i++)
        {
            contestants[i] = new Bin();
        }
        
        updateAll();
        
    }
    
    private void updateAll(){
        for(int i = 0; i < contestants.length; i += 2){
            update(i);
        }
    }
    
    private void ensureCapacity(){
        if(!contestants[contestants.length - 1].isEmpty()){
            int enlargedSize = growthFactor * contestants.length;
            Bin[] enlargedContestants = new Bin[enlargedSize];
            Bin[] enlargedWinners = new Bin[enlargedSize - 1];
            
            for(int i = 0; i < contestants.length; i++){
                enlargedContestants[i] = contestants[i];
                if(i != contestants.length - 1){
                    enlargedWinners[i] = winners[i];
                }
            }
            
            for(int i = contestants.length; i < enlargedContestants.length; i++){
                enlargedContestants[i] = new Bin();
            }
            
            for(int i = contestants.length; i < enlargedContestants.length; i += 2){
                update(i);
            }
            
            contestants = enlargedContestants;
            winners = enlargedWinners;
        }
    }
    
    public Bin getContestant(int index){
        return contestants[index];
    }
    
    private Bin getWinner(int index){
        return winners[index];
    }
    
    public int getFirstLowerIndexOf(int constraintWeight){
        return getFirstLowerIndexOf(0, constraintWeight);
    }
    
    private int getFirstLowerIndexOf(int start, int constraintWeight){
        int iLeft, iRight;
        
        //base case is if we're a leaf
        if(start >= winners.length/2 && start <= winners.length){
            //do contestant check
        }
        
        //check left
        if(getWinner(start*2).getWeight() <= constraintWeight){
            return getFirstLowerIndexOf(start*2, constraintWeight);
        }
        
        //check right
        if(getWinner((start*2) + 1).getWeight() <= constraintWeight){
            return getFirstLowerIndexOf(start*2, constraintWeight);
        }
        
        return -1; //unfinished
    }
    
    private void update(int index)
    {
        
    }
    
    public void pack(WeightedItem w){
        int i = getFirstLowerIndexOf(Bin.DEFAULT_CAPACITY -w.getWeight());
        Bin found = getContestant(i);
        found.addWeight(w);
        update(i);
    }
    
    @Override
    public Iterator<Bin> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
