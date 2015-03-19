package assign5;

/**
 *  @author Robert Sedgewick
 *  Taken from http://introcs.cs.princeton.edu/java/stdlib/Stopwatch.java.html
 */
public class Stopwatch { 

    private long start;

   /**
     * Create a stopwatch object.
     */
    public Stopwatch() {
        reset();
    } 

    public void reset(){
        start = System.currentTimeMillis();
    }
        
   /**
     * Return elapsed time (in milliseconds) since this object was created.
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start);
    }
    

} 