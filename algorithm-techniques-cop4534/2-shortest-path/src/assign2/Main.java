package assign2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Main {
    
    public static final String PARAM_FLAG_FILE = "-f";
    public static final char PARAM_FLAG_FILE_C = 'f';
    public static final String PARAM_FLAG_PENALTY = "-p";
    public static final char PARAM_FLAG_PENALTY_C = 'p';
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        char currentParamFlag = '\0';
        LinkedList<Integer> penalties = new LinkedList<Integer>();
        LinkedList<String> filePaths = new LinkedList<String>();
        for (String arg : args) 
        {
            if(arg.equals(PARAM_FLAG_FILE) || arg.equals(PARAM_FLAG_PENALTY)) {
                currentParamFlag = arg.charAt(1);
                continue;
            }
            
            switch(currentParamFlag)
            {
                case PARAM_FLAG_FILE_C:
                    filePaths.add(arg);
                    break;
                case PARAM_FLAG_PENALTY_C:
                    try {
                        penalties.add(Integer.parseInt(arg));
                    } catch (NumberFormatException e){
                        System.out.println(String.format(
                            "Skipping penalty %s. Not an integer.", arg
                        ));
                    }
                    break;
                default:
                    System.out.println(
                        "Invalid command line arguments. Please penalties with -p and file paths with -f.\n"
                    );
                    return;
            }     
        }
        
        new Main().run(penalties, filePaths);
    }
    
    public void run(LinkedList<Integer> penalties, LinkedList<String> filePaths){
        
        RunTimeStopWatch stopWatch = new RunTimeStopWatch();
        
        stopWatch.start();
        
        for(String filePath : filePaths)
        {
            
            try {
                Maze m = importMazeFromFile(filePath, 0);
                System.out.println(String.format(
                    "Running on file: %s",
                    filePath
                ));
                for(int penalty : penalties)
                {
                    System.out.print(String.format(
                        "\tUsing penalty: %d\n\tResult path: ",
                        penalty
                    ));
                    
                    m.setPenalty(penalty)
                     .runDjisktras()
                     .printPath();

                    System.out.println(String.format(
                        "\tRuntime: %dms\n",
                        stopWatch.split()
                    ));
                }
            }
            catch(DataFormatException e)
            {
                System.out.println(String.format(
                    "Skipping %s. Invalid format: %s", 
                    filePath, e.getMessage()
                ));
                continue;
            }
            catch(FileNotFoundException e)
            {
                System.out.println(String.format(
                    "Skipping %s. File not found: %s", 
                    filePath, e.getMessage()
                ));
                continue;
            }
            catch(IOException e)
            {
                System.out.println(String.format(
                    "Skipping %s. An I/O error occurred. Check your file permissions: %s", 
                    filePath, e.getMessage()
                ));
                continue;
            }
//            catch(Exception e)
//            {
//                System.out.println(String.format(
//                    "Skipping %s. An error occurred: %s", 
//                    filePath, e.getMessage()
//                ));
//                continue;
//            }
        }
        
        System.out.println(String.format(
            "Total runtime: %dms", stopWatch.getTotalElapsed()
        ));
    }
    
    public Maze importMazeFromFile(String filePath, int penalty) throws FileNotFoundException, IOException, DataFormatException
    {
        BufferedReader input = new BufferedReader(new FileReader(filePath));
        String[] dimensions = input.readLine().split(" ");
        
        if(dimensions.length < 2){
            throw new DataFormatException(
                "Please enter space-delimited positive integers for width and height at beginning of the file"
            );
        }
        
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        Maze m = new Maze(width, height, penalty);
        
        String line;
        int nLine = 0;
        while((line = input.readLine()) != null)
        {
            String[] roomDetails = line.split(" ");
            if(roomDetails.length < 3)
            {
                System.out.println(String.format(
                    "Skipping invalid line %d in %s. Need space delimited row, column and walls (N|S|E|W)",
                    nLine, filePath
                ));
            }
            else
            {
                int row = Integer.parseInt(roomDetails[0]);
                int col = Integer.parseInt(roomDetails[1]);
                char[] directions = roomDetails[2].toCharArray();
                for(char direction : directions){
                    try{
                        m.addWall(row, col, direction);
                    } catch(IndexOutOfBoundsException e){
                        System.out.println("Skipping line " + nLine + " in next file becaue it's invalid.");
                        System.out.println(String.format(
                            "Skipping invalid line %d in %s. row %d, column %d is out of bounds in the specified %dX%d maze.",
                            nLine, filePath, row, col, width, height
                        ));
                    }
                    
                }
            }
            
            nLine++;
        }
        
        return m;
    }
    
}
