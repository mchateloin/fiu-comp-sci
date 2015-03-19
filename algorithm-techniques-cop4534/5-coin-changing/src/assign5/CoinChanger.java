/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign5;

import java.util.HashMap;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class CoinChanger {
    
    private int[] denoms = {25, 18, 10, 5, 1};
    private int[][] solutions;
    private int maxAmountToChange;
    
    public CoinChanger(int maxAmount){
        solutions = new int[denoms.length][maxAmount + 1];
        maxAmountToChange = maxAmount;
        fill();
    }
    
    public void fill(){
        for(int amountIndex = 0; amountIndex <= maxAmountToChange; amountIndex++){
            solutions[denoms.length - 1][amountIndex] = amountIndex; // Setting the penny solutions
        }
        
        for(int iCoin = denoms.length - 2; iCoin >= 0; iCoin--){
            for(int iAmount = 0; iAmount <= maxAmountToChange; iAmount++){
                if( denoms[iCoin] > iAmount || 
                    solutions[iCoin+1][iAmount] < (1+solutions[iCoin][iAmount - denoms[iCoin]])){
                    
                    solutions[iCoin][iAmount] = solutions[iCoin+1][iAmount];
                    
                } else {
                    
                    solutions[iCoin][iAmount] = 1+solutions[iCoin][iAmount - denoms[iCoin]];
                    
                }
            }
        }
    }
    
    public int getMinCoins(int amount){
        return solutions[0][amount];
    }
    
    public HashMap<Integer,Integer> getMinCoinList(int amount){
        HashMap<Integer, Integer> coinsToCounts = new HashMap();
        for(int iCoin = 0; iCoin < denoms.length; iCoin++){
            if(iCoin != denoms.length - 1 && solutions[iCoin][amount] == solutions[iCoin+1][amount]){
                coinsToCounts.put(denoms[iCoin], 0);
            } else {
                coinsToCounts.put(denoms[iCoin], amount/denoms[iCoin]);
                amount %= denoms[iCoin];
            }
        }
        
        return coinsToCounts;
    }
    
    public int getMinCoins(){
        return solutions[0][maxAmountToChange];
    }
    
    
}
