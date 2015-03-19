/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        System.out.println("Running. Tournament tree not implemented for First Fit. ");
        BinPacker packer;
        IPackingBehavior[] algorithms = 
            {new NextFit(), new WorstFit(), new BestFit()/*, new FirstFit()*/}; 
        
        Stopwatch total = new Stopwatch();
        Stopwatch individual = new Stopwatch();
        for(String filePath : args)
        {
            System.out.println(String.format("Working on %s",filePath));
            packer = new BinPacker(filePath, null);
            for(IPackingBehavior b : algorithms){
                packer.setBehavior(b);
                
                individual.reset();
                packer.solveOnline();
                System.out.println("Running time: " + individual.elapsedTime() + "ms");
               
                individual.reset();
                packer.solveOffline();
                System.out.println("Running time: " + individual.elapsedTime() + "ms");
            }
        }
        
        System.out.println("Total running time (with I/O): " + total.elapsedTime() + "ms");
        
    }
}
