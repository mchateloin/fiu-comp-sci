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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class DiGraph {
    
    private HashMap<String,Vertex> vertices;
    private boolean insProcessed;
    
    public DiGraph()
    {
        vertices = new HashMap();
        insProcessed = false;
    }
    
    public class Vertex {

        private String name;
        private HashSet<Vertex> ins;
        private HashSet<Vertex> outs;
        private int inDegree;
        
        public Vertex(String vertexName)
        {
            this(vertexName, new HashSet<>());
        }

        public Vertex(String vertexName, HashSet<Vertex> vertexOuts)
        {
            name = vertexName;
            outs = vertexOuts;
            ins = new HashSet(); //Not processed yet
            inDegree = 0;
        }
    }
    
    public void addEdge(String nameFrom, String nameTo)
    {
        if(containsEdge(nameFrom, nameTo))
        {
            return;
        }
        
        Vertex from = getOrPutVertex(nameFrom);
        Vertex to = getOrPutVertex(nameTo);
        from.outs.add(to);
        addInDegreeOf(nameTo, 1);
    }
    
    public boolean containsEdge(String nameFrom, String nameTo)
    {
        Vertex from, to;
        if((from = getVertex(nameFrom)) == null || (to = getVertex(nameTo)) == null)
        {
            return false;
        }
        
        return from.outs.contains(to);
    }
    
    public int getInDegreeOf(String vertexName)
    {
        if(!vertices.containsKey(vertexName))
        {
            return 0;
        }
        
        return vertices.get(vertexName).inDegree;
    }
    
    public void addInDegreeOf(String vertexName, int num)
    {
        setInDegreeOf(vertexName, getInDegreeOf(vertexName) + num);
    }
    
    public void setInDegreeOf(String vertexName, int deg)
    {
        getVertex(vertexName).inDegree = deg;
    }
    
    
    public HashMap<String, Vertex> getVertices()
    {
        return vertices;
    }
    
    public Vertex getVertex(String name)
    {
        if(vertices.containsKey(name)){
            return vertices.get(name);
        }
        
        return null;
    }
    
    public Vertex getOrPutVertex(String name)
    {
        Vertex requested;
        if((requested = getVertex(name)) != null)
        {
            return requested;
        }
        
        vertices.put(name, new Vertex(name));
        setInDegreeOf(name, 0);
        return getVertex(name);
    }
    
    public void deleteVertex(String name)
    {
        vertices.remove(name);
    }
    
    public List<String> getOutAdjacentsOf(String vertexName)
    {
        Vertex center = getVertex(vertexName);
        if(center == null || center.outs.isEmpty())
        {
            return new LinkedList<>();
        }
        
        return center.outs.parallelStream().map(v -> v.name).collect(Collectors.toList());
    }
    
    public List<String> getInAdjacentsOf(String vertexName)
    {
        Vertex center = getVertex(vertexName);
        if(!insProcessed)
        {
            createInAdjacents();
        }
        
        return center.ins.parallelStream().map(v -> v.name).collect(Collectors.toList());
    }
    
    private void createInAdjacents()
    {
        insProcessed = true;
        for(Entry<String, Vertex> e : vertices.entrySet())
        {
            for(Vertex v : e.getValue().outs)
            {
                v.ins.add(getVertex(e.getKey()));
            }
        }
    }

}
