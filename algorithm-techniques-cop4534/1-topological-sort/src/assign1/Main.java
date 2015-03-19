/*
 * The MIT License
 *
 * Copyright 2014 Miguel A. Chateloin <mchat003@fiu.edu>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package assign1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;


/**
 * 
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Main {

    /**
     * Entry point of the assignment application which handles basic file i/o.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Main proc = new Main();
        
        double startTime = System.currentTimeMillis();
        
        for(String filename : args)
        {
            double fileStartTime = System.currentTimeMillis();
            DiGraph graphFromFile;
            
            String output = "";
            try 
            {
                graphFromFile = proc.importFile(filename);
            }
            catch(FileNotFoundException e)
            {
                System.out.println(String.format(
                        "Skipping %s. File not found: %s", 
                        filename, e.getMessage()));
                continue;
            }
            catch(IOException e)
            {
                System.out.println(String.format(
                        "Skipping %s. An I/O error occurred. Check your file permissions: %s", 
                        filename, e.getMessage()));
                continue;
            }
            catch(Exception e)
            {
                System.out.println(String.format(
                        "Skipping %s. An error occurred: %s", 
                        filename, e.getMessage()));
                continue;
            }
            
            try
            {
                LinkedList<String> results = TopSortAlgorithm.solve(graphFromFile);
                output = results.stream()
                            .map((v) -> results
                                            .getLast()
                                            .equals(v) ? v : v + ", ")
                            .reduce(output, String::concat);
            } 
            catch(NotAcyclicException e){
                //Cycle detected. We pick any vertex to start trying to find a cycle.
                //Easiest to just get first vertex available.
                Object[] verticesArr = graphFromFile.getVertices().keySet().toArray();
                String startVertex = verticesArr[0].toString();
                LinkedList<String> cycle = TopSortAlgorithm
                        .getCycle(graphFromFile, startVertex);
                output = cycle.stream().map((v) -> "->" + v).reduce(output, String::concat).substring(2);
            }
            
            System.out.println(String.format("%s: %s", filename, output));
            System.out.println("Runtime for " + filename + ": " + (System.currentTimeMillis() - fileStartTime) + "ms");
        }
        
        System.out.println("Total runtime: " + (System.currentTimeMillis() - startTime) + "ms");
    }
    
    
    
    public DiGraph importFile(String filePath) throws IOException, FileNotFoundException
    {
        BufferedReader input = new BufferedReader(new FileReader(filePath));
        DiGraph resultGraph = new DiGraph();
        String line;
        int nLine = 0;
        while((line = input.readLine()) != null)
        {
            String[] vertexNames = line.split(" ");
            if(vertexNames.length < 2)
            {
                System.out.println("Skipping line " + nLine + " in next file becaue it's invalid.");
            }
            else
            {
                resultGraph.addEdge(vertexNames[0], vertexNames[1]);
            }
            
            nLine++;
        }
        
        return resultGraph;
    }

}