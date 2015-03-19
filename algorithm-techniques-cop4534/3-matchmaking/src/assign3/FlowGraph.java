/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.UUID;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class FlowGraph
{
    public HashMap<String, LinkedHashSet<String>> residual;
    String source;
    String sink;
    int lastRankValue;
    int flowValue;
    
    public FlowGraph()  {
        source = generateVertexName();
        sink = generateVertexName();
        residual = new HashMap<>();
        lastRankValue = 0;
        flowValue = 0;
    }
    
    private String generateVertexName(){
        return UUID.randomUUID().toString();
    }
    
    public boolean exists(String v){
        return residual.keySet().contains(v);
    }
    
    public void add(String v){
        residual.put(v, new LinkedHashSet<>());
    }
    
    
    public void addEdge(String a, String b){
        if(!exists(a))
        {
            add(a);
        }
        
        if(!exists(b))
        {
            add(b);
        }
        
        if(edgeExists(a, b)){
            return;
        }
        
        residual.get(a).add(b);
    }
    
    public void flipEdge(String a, String b){
        if(!edgeExists(a, b)){
            return;
        }
        
        residual.get(a).remove(b);
        residual.get(b).add(a);
    }
    
    public boolean edgeExists(String a, String b){
        if(!exists(a) || !exists(b))
        {
            return false;
        }
        
        return residual.get(a).contains(b);
    }
    
    void buildNetwork(int rankValue, PreferenceMatrix prefs){
        for(String a : prefs.getLeft()){
            int iA = prefs.indexOf(a);
            for(int iB : prefs.getRow(iA)){
                int cellRank = prefs.getCell(iA, iB);
                if(cellRank <= rankValue && cellRank > lastRankValue){
                    addEdge(prefs.keyOf(iA), prefs.keyOf(iB));
                }
            }
        }
        
        lastRankValue = rankValue;
    }
    
    public int runMaxFlow(){
        int max = 0;
        int pathCap;
        while(hasPath()){
            max += 1;
        }
        return max;
    }
    
    private boolean hasPath()
    {
        Stack<String> queue = new Stack();
        HashSet<String> visited = new HashSet<>();
        HashMap<String, LinkedList<String>> prevs = new HashMap<>();
        
        queue.push(source);
        visited.add(source);
        prevs.add(source);
        
        while(!queue.isEmpty()){
            String v = queue.pop();
            for(String next : residual.get(v)){
                if(!visited.contains(next)){
                    
                    queue.push(next);
                    visited.add(next);
                    if(next.equals(sink)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public String toString(){
        if(residual.isEmpty())
        {
            return "";
        }
            
        String out = "";
        
        out += source.substring(0,3) + "... -| ";
        for(String v: residual.get(source)){
            out += v + ", ";
        }
        out += "\n";
                
        for(String k : residual.keySet()){
            if(k.equals(source) || k.equals(sink)){
                continue;
            }
            
            out += k + " -| ";
            for(String v: residual.get(k)){
                out += v + ", ";
            }
            out += "\n";
        }
        
        
        out += sink.substring(0,3) + "... -| ";
        
        return out;
    }
}
