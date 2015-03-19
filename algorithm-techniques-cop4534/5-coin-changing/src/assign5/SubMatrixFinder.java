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
public class SubMatrixFinder {
    
    private char[][] originalMatrix;
    private int[][] sumMatrix;
    private Coordinate sub;
    
    public class Coordinate {
        public int row;
        public int col;
        
        public Coordinate(int r, int c){
            row = r;
            col = c;
        }
    }
    
    public SubMatrixFinder(char[][] matrix){
        originalMatrix = matrix;
        sumMatrix = new int[originalMatrix.length][originalMatrix[0].length];
        for(int row = 0; row < originalMatrix.length; row++)
        {
            sumMatrix[row][0] = Character.getNumericValue(originalMatrix[row][0]);
        }
        
        for(int col = 0; col < originalMatrix[0].length; col++)
        {
            sumMatrix[0][col] = Character.getNumericValue(originalMatrix[0][col]);
        }
        
        sub = new Coordinate(0, 0);
        
        fill();
    }
    
    private void fill(){
        
        int maxVal = sumMatrix[0][0];
        
        for(int row = 1; row < originalMatrix.length; row++){
            for(int col = 1; col < originalMatrix[0].length; col++)
            {
                if(Character.getNumericValue(originalMatrix[row][col]) == 1)
                {
                    sumMatrix[row][col] = 1 + Integer.min(Integer.min(sumMatrix[row][col-1], sumMatrix[row-1][col]), sumMatrix[row-1][col-1]);
                    if(maxVal < sumMatrix[row][col] ){
                        sub.row = row;
                        sub.col = col;
                        maxVal = sumMatrix[row][col];
                    }
                    
                } else {
                    sumMatrix[row][col] = 0;
                }
            }
        }
    }
    
    public void print(){
        for(int i = 0; i < sumMatrix.length; i++){
            System.out.print(i + "| ");
            for(int j = 0; j < sumMatrix[0].length; j++){
                System.out.print(sumMatrix[i][j] + "\t");
            }
            System.out.println();
                
        }
            
    }
    
    public Coordinate getLargestSubStart(){
        return sub;
    }
    
    public int getLargestSubHeight(){
        int height = 0;
        while(sub.row != height+1 && sumMatrix[sub.row - height][sub.col] != 0){
            height++;
        }
        return height;
    }
    
    
    public int getLargestSubWidth(){
        int width = 0;
        while(sub.col != width+1 && sumMatrix[sub.row][sub.col - width] != 0){
            width++;
        }
        return width;
    }
    
}
