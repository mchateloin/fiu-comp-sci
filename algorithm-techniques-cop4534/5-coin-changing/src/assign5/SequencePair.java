/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign5;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class SequencePair {
    
    private final String seqA;
    private final String seqB;
    private int[][] table;
    
    public SequencePair(String a, String b){
        seqA = a;
        seqB = b;
        table = new int[seqA.length()+1][seqB.length()+1];
    }
    
    public void reset(){
        for(int row = 0; row < table.length; row++){
            for(int col = 0; col < table[0].length; col++){
                table[row][col] = 0;
            } 
        }
    }
    
    public void fill(){
        for(int row = 1; row < table.length; row++){
            for(int col = 1; col < table[1].length; col++){
                if(seqA.charAt(row-1) == seqB.charAt(col-1)){
                    table[row][col] = 1 + table[row-1][col-1];
                } else {
                    table[row][col] = Integer.max(table[row-1][col], table[row][col-1]);
                }
            } 
        }
    }
    
    public String getLongestSubSequence(){
        fill();
        
        StringBuilder answer = new StringBuilder();
        
        // Trace back the answer, from lowest right corner space.
        int row = seqA.length();
        int col = seqB.length();
        while(row > 0 && col > 0){
            if(table[row][col] == table[row-1][col]) { // left
                row--;
            } else if(table[row][col] == table[row][col-1]) { // right
                col--;
            } else { // diagonal
                answer.append(seqA.charAt(row - 1));
                row--;
                col--;
            }
        }
        
        return answer.reverse().toString();
    }
    
}
