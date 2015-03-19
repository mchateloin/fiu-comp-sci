/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package assign2;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 *
 * @author Miguel A. Chateloin <mchat003@fiu.edu>
 */
public class Maze {
    
    public static final int INFINITY = Integer.MAX_VALUE / 5;
    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';
    
    private Square[][] grid;
    private Square start;
    private Square end;
    
    private int rows;
    private int cols;
    
    private int penalty;
    
    public class Square implements Comparable<Square>
    {
        private int row;
        private int col;
        
        private Square prev;
        public int cost;
        private boolean seen;
        private byte walls;
        
        public Square(int whichRow, int whichCol){
            reset();
            row = whichRow;
            col = whichCol;
            walls = (byte) 0;
        }
        
        public void reset()
        {
            prev = null;
            cost = INFINITY;
            seen = false;
        }
        
        public boolean isWalledOff(char direction)
        {
            switch(direction)
            {
                case NORTH:
                    return (int)(walls & 8) >> 3 == 1;
                case SOUTH:
                    return (int)(walls & 4) >> 2 == 1;
                case EAST:
                    return (int)(walls & 2) >> 1 == 1;
                case WEST:
                    return (int)(walls & 1) == 1;
                default:
                    return false;
            }
        }
        
        public boolean isWalledOff(Square other)
        {
            return isWalledOff(this.directionTo(other));
        }
        
        public char directionTo(Square other){
            if(this.row - 1 == other.row && this.col == other.col){
                return NORTH;
            } else if (this.row + 1 == other.row && this.col == other.col){
                return SOUTH;
            } else if(this.row == other.row && this.col - 1 == other.col){
                return WEST;
            } else if(this.row == other.row && this.col + 1 == other.col){
                return EAST;
            } else {
                return '\0';
            }
        }
        
        public boolean equals(Square o){
            return this.row == o.row && this.col == o.col;
        }
        
        @Override
        public String toString(){
            return row + "-" + col + "/" + cost;
        }

        public int compareTo(Square o) {
            //return o.cost - this.cost;
            return this.cost - o.cost;
        }
    }
    
    /**
        *
        * @param numRows
        * @param numCols
        * @param wallPenalty
        */
    public Maze(int numRows, int numCols, int wallPenalty){
        rows = numRows;
        cols = numCols;
        penalty = wallPenalty;
        
        grid = new Square[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                grid[i][j] = new Square(i, j);
            }
        }
        
        start = grid[0][0];
        end = grid[numRows - 1][numCols - 1];
    }
    
    /**
        *
        * @param p
        */
    public Maze setPenalty(int p)
    {
        penalty = p;
        return this;
    }
    
    
    /**
        *
        * @param s
        * @return
        */
    public LinkedList<Square> getAdjacents(Square s)
    {
        return getAdjacents(s.row, s.col);
    }
    
    /**
        *
        * @param row
        * @param col
        * @return
        */
    public LinkedList<Square> getAdjacents(int row, int col)
    {
        LinkedList adj = new LinkedList();
        
        if(row > 0){
            adj.add(grid[row-1][col]);
        }
        
        if(row < rows - 1){
            adj.add(grid[row+1][col]);
        }
        
        if(col < cols - 1){
            adj.add(grid[row][col+1]);
        }
        
        if(col > 0){
            adj.add(grid[row][col-1]);
        }
        
        return adj;
    }
    
    public boolean isEdge(int row, int col, char direction)
    {
        switch(direction)
        {
            case NORTH:
                return row <= 0;
            case SOUTH:
                return row >= rows - 1;
            case EAST:
                return col >= cols - 1;
            case WEST:
                return col <= 0;
            default:
                return false;
        }
    }
    
    /**
        *
        * @param row
        * @param col
        * @param direction
        */
    public void addWall(int row, int col, char direction){
        switch(direction)
        {
            case NORTH:
                grid[row][col].walls |= 8;
                if(!isEdge(row, col, NORTH))
                {
                    grid[row-1][col].walls |= 4;
                }
                break;
            case SOUTH:
                grid[row][col].walls |= 4;
                if(!isEdge(row, col, SOUTH))
                {
                    grid[row+1][col].walls |= 8;
                }
                break;
            case EAST:
                grid[row][col].walls |= 2;
                if(!isEdge(row, col, EAST))
                {
                    grid[row][col+1].walls |= 1;
                }
                break;
            case WEST:
                grid[row][col].walls |= 1;
                if(!isEdge(row, col, WEST))
                {
                    grid[row][col-1].walls |= 2;
                }
                break;
            default:
        }
    }
    
    /**
        *
        * @return
        */
    public int getRows(){
        return rows;
    }
    
    /**
        *
        * @return
        */
    public int getCols(){
        return cols;
    }
    
    /**
        *
        * @return
        */
    public Maze runDjisktras(){
        PriorityQueue<Square> processing;
        processing = new PriorityQueue<Square>();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                grid[i][j].reset();
                processing.add(grid[i][j]);
            }
        }
        
        start.cost = 0;
        
        Square current;
        while(!processing.isEmpty())
        {
            current = processing.remove();
            
            if(current.equals(end)){
                break;
            }
                
            current.seen = true;
            
            for(Square s : getAdjacents(current)){
                if(!s.seen)
                {
                    int travelCost = current.cost + 1 + (current.isWalledOff(s) ? penalty : 0);
                    
                    if(travelCost < s.cost){
                        s.cost = travelCost;
                        s.prev = current;
                        processing.remove(s);
                        processing.add(s);
                    }
                    
                    
                }
            }
        }  
        return this;
    }
    
    public Maze printPath()
    {
        printPath(end);
        System.out.println();
        return this;
    }
    
    private void printPath(Square s)
    {
        if(s.prev == null)
        {
            return;
        }
        
        printPath(s.prev);
        System.out.print(s.prev.directionTo(s));
        
    }
    
    @Override
    public String toString()
    {
        String out = "";
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                
                out += (grid[i][j].cost == INFINITY ? "inf" : grid[i][j].cost) + "/";
                        
                String wall = Integer.toBinaryString((int)grid[i][j].walls);
                while(wall.length() < 4)
                {
                    wall = "0" + wall;
                }
                
                out += wall + "\t";
            }
            out += "\n";
        }
        return out;
    }
    
    
}
