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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class TopSortAlgorithm {
    
    public static LinkedList<String> solve(DiGraph searchGraph) throws NotAcyclicException
    {
        Stack<String> traveables = new Stack<>();
        LinkedList<String> path = new LinkedList<>();
        int numProcessed = 0;
        String lastProcessed = null;
        
        for(String v : searchGraph.getVertices().keySet())
        {
            if (searchGraph.getInDegreeOf(v) == 0) {
                traveables.push(v);
            }
        }
        
        while(!traveables.empty())
        {
            path.add(lastProcessed = traveables.pop());
            numProcessed++;
            
            for(String vAdj : searchGraph.getOutAdjacentsOf(lastProcessed))
            {
                searchGraph.addInDegreeOf(vAdj, -1);
                if(searchGraph.getInDegreeOf(vAdj) == 0)
                {
                    traveables.push(vAdj);
                }
            }
        }
        
        if(numProcessed != searchGraph.getVertices().size())
        {
            path.stream().forEach((v) -> {
                searchGraph.deleteVertex(v);
            });
            throw new NotAcyclicException();
        }
        
        return path;
    }
    
    public static LinkedList<String> getCycle(DiGraph searchGraph, String startVertexName)
    {
        LinkedList<String> path = new LinkedList<>();
        HashSet<String> seen = new HashSet<>();
        String nextVertexName = startVertexName;
        
        while(!seen.contains(nextVertexName))
        {
            path.add(nextVertexName);
            seen.add(nextVertexName);
            nextVertexName = searchGraph.getInAdjacentsOf(nextVertexName).get(0);
        }
        path.add(nextVertexName);
        
        int cycleStart = path.indexOf(nextVertexName);
        for(int i = 0; i < cycleStart; i++)
        {
            path.removeFirst();
        }
        
        return path;
    }
}
