/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Main {
    
    public static final int COIN_CHANGE_DEFAULT = 100;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main proc = new Main();
        Stopwatch runningTime = new Stopwatch();
        
        if(args.length < 2){
            System.out.println(
                "You must provide 2 filenames, and one integer as arguments."
                + "The first will be the input filename for the submatrix problem."
                + "The second will be the input filename for the subsequence problem."
                + "The optional third argument will be the amount of cents for the coin changing problem. Default is 100."
            );
            return;
        }
        try {
            
            // #1
            BufferedReader submatrixInput = new BufferedReader(new FileReader(args[0]));
            LinkedList<char[]> lines = new LinkedList<>();
            String line;
            Integer lineLength = null;
            while((line = submatrixInput.readLine()) != null){
                lines.add(line.toCharArray());
                lineLength = line.length();
                if(line.length() != lineLength && null != lineLength){
                    throw new Exception("All lines in file must have the same number of characters");
                }
            }
            
            char[][] matrix = new char[lines.size()][lineLength];
            for(int iRow = 0; iRow < lines.size(); iRow++){
                matrix[iRow] = lines.get(iRow);
            }
           
            SubMatrixFinder problem1 = new SubMatrixFinder(matrix);
            System.out.println(String.format("Largest sub matrix size: %dx%d", problem1.getLargestSubHeight(), problem1.getLargestSubWidth()));
            
            
            // #2
            BufferedReader subsequenceInput = new BufferedReader(new FileReader(args[1]));
            SequencePair problem2 = new SequencePair(subsequenceInput.readLine(), subsequenceInput.readLine());
            System.out.println(String.format("2. Answer to subsequence problem:\n%s", problem2.getLongestSubSequence()));
            
            
            // #3
            int amountToChange;
            if(args.length >= 3){
                amountToChange = Integer.parseInt(args[2]);
            } else {
                amountToChange = COIN_CHANGE_DEFAULT;
            }
            
            CoinChanger problem3 = new CoinChanger(amountToChange);
            
            String out = "";
            for(int amount = 1; amount <= amountToChange; amount++){
                String list = "";
                HashMap<Integer, Integer> counts = problem3.getMinCoinList(amount);
                for(int denom : counts.keySet()){
                    if(counts.get(denom) != 0){
                        list += String.format(" %d\u00A2 (%d),", denom, counts.get(denom));
                    }
                }
                out += String.format("%d\u00A2 in %d coins:\t%s\n", amount, problem3.getMinCoins(amount), list);
            }
            System.out.println(String.format("3. Answer to coin changing problem:\n%s", out));
        }
        catch(FileNotFoundException e)
        {
            System.out.println(String.format(
                    "File not found: %s", e.getMessage()));
        }
        catch(IOException e)
        {
            System.out.println(String.format(
                    "An I/O error occurred. Check your file permissions: %s", 
                    e.getMessage()));
        }
        catch(Exception e)
        {
            System.out.println(String.format(
                    "An error occurred: %s", 
                    e.getMessage()));
        }
    }


}
