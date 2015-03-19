/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import static java.lang.Math.random;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Stack;


/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Bin implements Comparable{
    
    public static int ID_COUNTER = 0;
    public static final int DEFAULT_CAPACITY = 1000000000;
//    public static final int DEFAULT_CAPACITY = 10;
    
    private int id;
    private int capacity;
    private int weight;
    private Stack<WeightedItem> contents;
    
    public Bin(){
        id = ID_COUNTER++;
        weight = 0;
        contents = new Stack<>();
        capacity = DEFAULT_CAPACITY;
    }
    
    public Bin(int cap)
    {
        this();
        capacity = cap;
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int num){
        id = num;
    }
    
    public int getWeight(){
        return weight;
    }
    
    public int getCapacity(){
        return capacity - weight;
    }
    
    public Stack<WeightedItem> getContents(){
        return contents;
    }
    
    public void addWeight(int w){
        addWeight(new WeightedItem(w));
    }
    
    public void addWeight(WeightedItem w){
        contents.add(w);
        weight += w.getWeight();
    }
    
    public boolean isFull(){
        return weight == capacity;
    }
    
    public boolean isEmpty(){
        return weight == 0;
    }
    
    public String toString(){
        String out = "";
        for(WeightedItem w : contents)
        {
            out += w + ",";
        }
        return  "[" + out.substring(0, out.length() - 1) + "]";
    }
    long x = 5;
    @Override
    public int compareTo(Object o) {
        Bin other = (Bin) o;
        int diff = this.getWeight() - other.getWeight();
        return diff == 0 ? this.getID() - other.getID() : diff;
    }
    
}
