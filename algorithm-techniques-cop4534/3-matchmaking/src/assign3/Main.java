
package assign3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Main 
{

    public PreferenceMatrix preferences;
    public FlowGraph graph;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main proc = new Main();
        double startTime = System.currentTimeMillis();
        
        for(String filename : args)
        {
             double fileStartTime = System.currentTimeMillis();
            
            String output = "";
            try 
            {
                proc.importFile(filename);
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
            
            //output += proc.solution();
            
            System.out.println(String.format("%s:", filename));
            //System.out.println(output);
            System.out.println("File runtime: " + (System.currentTimeMillis() - fileStartTime) + "ms");
        }
    }
    
    public String solution()
    {
        int desiredFlow = preferences.getSize() / 2;
        int lowestRank = 0;
        HashSet<String> unpaired = preferences.getRight();
        while(graph.getFlowValue() < desiredFlow)
        {
            graph.buildNetwork(++lowestRank, preferences);
            graph.runMaxFlow();
        }
        
        //output

        return "";
    }
    
    public void importFile(String filePath) throws FileNotFoundException, IOException
    {
        graph = new FlowGraph();
        
        BufferedReader input = new BufferedReader(new FileReader(filePath));
        String line;
        int nLine = 0;
        boolean suitorSwitch = false;
        
        while((line = input.readLine()) != null)
        {
            if(line.isEmpty())
            {
                suitorSwitch = true;
                continue;
            }
            
            int colonIndex = line.indexOf(':');
            String from = line.substring(0, colonIndex);
            String[] tos = line.substring(colonIndex + 1, line.length()).split(",");
            
            if(nLine == 0){
                preferences = new PreferenceMatrix(tos.length * 2);
            }
            
            if(tos.length < preferences.getSize() / 2){
                System.out.println("Skipping line " + nLine + " in file. Not enough preferences specified.");
                continue;
            }
            
            if(suitorSwitch){
                graph.addEdge(from, graph.sink);
            } else {
                graph.addEdge(graph.source, from);
            }
            
            int rank = 0;
            for(String to : tos)
            {
                preferences.addSuitor(from, to, ++rank);
            }
                       
            nLine++;
        }
    }
    
    
}
