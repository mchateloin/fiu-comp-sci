/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import static javafx.scene.input.KeyCode.T;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class WeightedItem implements Comparable
{
    private final int weight;
    
    public WeightedItem(int w){
        weight = w;
    }
    
    public int getWeight(){
        return weight;
    }

    @Override
    public int compareTo(Object o) {
        return ((WeightedItem) o).getWeight() - this.getWeight();
    }
    
    @Override
    public String toString(){
        return Integer.toString(weight);
    }

}
