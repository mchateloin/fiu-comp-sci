
package assign3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class PreferenceMatrix 
{
    
    private int size;
    private int lastIndex;
    
    private int[][] matrix;
    private HashSet<String> setA;
    private HashSet<String> setB;
    private BiHashMap<String, Integer> keyIndexBinding;
    
    public PreferenceMatrix(int matrixSize){
        size = matrixSize;
        lastIndex = -1;
        matrix = new int[size][size];
        keyIndexBinding = new BiHashMap<>();
        setA = new HashSet<String>();
        setB = new HashSet<String>();
    }

    public int[] getRow(int r){
        return matrix[r];
    }
    
    public int getCell(int r, int c){
        return matrix[r][c];
    }
    
    public boolean suitorExists(String suitorName){
        return indexOf(suitorName) >= 0;
    }
    
    public void addSuitor(String suitorName){
        keyIndexBinding.put(suitorName, ++lastIndex);
    }
    
    public void addSuitor(String suitorNameA, String suitorNameB, int rank){
        int aIndex = getOrAddSuitor(suitorNameA);
        setA.add(suitorNameA);
        int bIndex = getOrAddSuitor(suitorNameB);
        setB.add(suitorNameB);
        
        matrix[aIndex][bIndex] = rank;
    }
    
    public int getSize(){
        return size;
    }
    
    public HashSet<String> getLeft(){
        return setA;
    }
    
    public HashSet<String> getRight(){
        return setB;
    }
    
    public int indexOf(String key){
        Integer index = keyIndexBinding.getForward(key);
        return index == null ? -1 : index;
    }
    
    public String keyOf(int index){
        return keyIndexBinding.getBackward(index);
    }
    
    private int getOrAddSuitor(String suitorName){
        if(!suitorExists(suitorName)){
             PreferenceMatrix.this.addSuitor(suitorName);
        }
        
        return indexOf(suitorName);
    }
    
    @Override
    public String toString(){
        String out = "\t";
        
        for(int i = 0; i < size; i++){
            out += "[" + i + "]" + "\t"; 
        }
        
        out += "\n";
        
        for(int i = 0; i < size; i++){
            out += "[" + i + "]" + "\t"; 
            for(int j = 0; j < size; j++){
                out += (matrix[i][j] == 0 ? " " : matrix[i][j]) + "\t";
            }
            out += "\n";
        }
        
        
        
        return out;
    }
}
