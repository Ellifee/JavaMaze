/**
 * This class is the template class for the Maze solver.
 * 
 * @author (Elke) 
 * @version (9.8.19)
 */
import java.util.LinkedList; 
import java.util.Queue; 
import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
import java.util.*;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.*;

public class MazeSolverAlgo
{
    // Instanzvariablen 
    private static int EMPTY = 0;       // empty cell
    private static int OBSTACLE = 1;    // cell with obstacle / blocked cell
    private static int START = 2;       // the start position of the maze (red color)
    private static int TARGET = 3;      // the target/end position of the maze (green color)
    private int dimCols, dimRows, startCol, startRow, endRow, endCol;
    private int[][] grid;

    /**
     * Konstruktor für Objekte der Klasse MazeSolverAlgo
     */
    public MazeSolverAlgo()
    {
        // Instanzvariable initialisieren
        this.dimCols = 0;
        this.dimRows = 0;
        this.startCol = 0;
        this.startRow = 0;
        this.endCol = 0;
        this.endRow = 0;
        this.grid = new int[][]{{}};
    }

    // Setter method for the maze dimension of the rows
    public void setDimRows(int rows){
        this.dimRows = rows;
    }

    // Setter method for the maze dimension of the columns
    public void setDimCols(int cols){
        this.dimCols = cols;
    }
        
    // Setter method for the column of the start position 
    public void setStartCol(int col){
        this.startCol = col;
    }

    // Setter method for the row of the start position 
    public void setStartRow(int row){
        this.startRow = row;
    }

    // Setter method for the column of the end position 
    public void setEndCol(int col){
        this.endCol = col;
    }

    // Setter method for the row of the end position 
    public void setEndRow(int row){
        this.endRow = row;
    }

    // Setter method for blocked grid elements
    public void setBlocked(int row, int col){
        this.grid[row][col] = this.OBSTACLE;
    }
    
    public void startMaze(){
        //populate empty grid 
            this.startCol = 0;
            this.startRow = 0;
            this.endCol = 0;
            this.endRow = 0;
            this.grid = new int[0][0]; //initialise empty grid.
        
    }
    
    public void startMaze(int columns, int rows){
        //populate grid with dimension row,column with zeros
        if((columns == 0) && (rows == 0)){
            this.startCol = 0;
            this.startRow = 0;
            this.endCol = 0;
            this.endRow = 0;
            this.grid = new int[][]{{}}; //initialise empty grid.
        }

        if ((columns>0) && (rows>0)){
            this.grid = new int[rows][columns]; //initialise empty grid.
            for(int i=0; i<rows; i++){
                for(int j=0; j<columns; j++){
                    this.grid[i][j]=EMPTY;
                }
            }
        }
        System.out.println("\n"+Arrays.deepToString(this.grid).replace("], [", "\n")
                                .replace("]]", "").replace("[[", "").replace(",", ""));
        
    }
    
    //loads a maze from a file pathToConfigFile
    public void loadMaze(String filepath) throws FileNotFoundException , IOException{
        // check whether a function numpy.loadtxt() could be useful
        
        ArrayList<int[]> list = new ArrayList<int[]>();
       
        BufferedReader in = new BufferedReader(new FileReader(filepath));
        
        String line = null; 
        int size = 0;
        
        while ((line = in.readLine()) != null) {
            //split each line by " " AND map it into an Integer list line AND convert it to an array
            int[] row= Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
            size = row.length;
            list.add(row);
        }
        
        
        //copy array list to a 2d array
        this.dimRows = size;
        this.dimCols = list.size();
        this.grid = new int[this.dimRows][this.dimCols];
        list.toArray(grid); 
        
        for (int row = 0; row < dimRows; row++) {
            for (int col = 0; col < dimCols; col++) {
                if (grid[row][col] == 2) {
                    this.startRow=row;
                    this.startCol=col;
                }
                if (grid[row][col] == 3) {
                    this.endRow=row;
                    this.endCol=col;
                }
            }
        }       

    }
    
    //Define what shall happen after the full information of a maze has been received
    public void endMaze(){
        this.grid[startRow][startCol] = this.START;
        this.grid[endRow][endCol] = this.TARGET;
    }
    
    // just prints a maze on the command line
    public void printMaze(){
        System.out.println("\n"+Arrays.deepToString(this.grid).replace("], [", "\n")
                                .replace("]]", "").replace("[[", "").replace(",", ""));
    }
    
    public void clearMaze(){
        startMaze();
    }
    
    public boolean isInGrid(int row, int column){
        if (row < 0){
            return false;
        }

        if (column < 0){
            return false;
        }
        if (row >= this.grid[0].length){
            return false;
        }
        if (column >= this.grid[1].length){
            return false;
        }

        return true;
    }
    
    
    // Returns a list of all grid elements neighboured to the grid element row,column
    public int[][] getNeighbours(int row, int column){
        ArrayList<int[]> neighbours = new ArrayList<int[]>();

        //no neighbours for out-of-grid elements
        if (isInGrid(row,column) == false){
            return transfListToArr(neighbours);
        }

        // no neighbours for blocked grid elements
        if ((this.grid!=null)&&(this.grid[row][column] == this.OBSTACLE)){
            return transfListToArr(neighbours);
        }
    
        int nextRow = row + 1;    
        if ((isInGrid(nextRow,column) == true) && (this.grid[nextRow][column] != this.OBSTACLE)){
            neighbours.add(new int[]{nextRow,column}) ;
        }

        int previousRow = row - 1;   
        if ((isInGrid(previousRow,column) == true)&& (this.grid[previousRow][column] != this.OBSTACLE)){
            neighbours.add(new int[]{previousRow,column});
        }

        int nextColumn = column + 1;   
        if ((isInGrid(row,nextColumn) == true )&&(this.grid[row][nextColumn] != this.OBSTACLE)){
            neighbours.add(new int[]{row,nextColumn});
        }

        int previousColumn = column - 1;
        if ((isInGrid(row,previousColumn) == true )&&(this.grid[row][previousColumn] != this.OBSTACLE)){
            neighbours.add(new int[]{row,previousColumn});
        }

        return transfListToArr(neighbours);
        // TODO: Add a Unit Test Case --> Very good example for boundary tests and condition coverage
    }
    
    //Gives a grid element as string, the result should be a string row,column
    public String gridElementToString(int row, int col){
        String result = "" + String.valueOf(row) + "," + String.valueOf(col);
        return result;
    }
    
    //transforming arraylist into a 2d array
    public int[][] transfListToArr(ArrayList<int[]> list){
        
        int rows = list.size();
        int cols = list.get(0).length;
        int[][] arr= new int[rows][cols];
            for(int i=0; i<rows; i++){
                arr[i]=list.get(i);
            }
        return arr;
    }
    
    // Generates the resulting path as string from the came_from list
    public int[][] generateResultPath(Map<String, String> came_from){
        ArrayList<int[]> result_path = new ArrayList<int[]>();
       
        ///////////////////////////////
        // Here Creation of Path starts
        ///////////////////////////////
        String startKey = gridElementToString(this.startRow, this.startCol);
        String currentKey = gridElementToString(this.endRow, this.endCol);
           
        ArrayList<String> path =new ArrayList<>(); //flexible Liste an strings
        
        //////!!!SOMETHING IS NOT RIGHT HERE!!!  //////   
        
        while (currentKey != null){ 
            path.add(currentKey);
            currentKey = came_from.get(currentKey);
                      
        }
        
        Collections.reverse(path);
        ///////////////////////////////
        // Here Creation of Path ends
        ///////////////////////////////
        
        for (String next : path){
            String[] nextPath = next.split(",");
            int[] pathInt={Integer.valueOf(nextPath[0]),Integer.valueOf(nextPath[1])};
            result_path.add(pathInt);
        
        }
        
                
        return transfListToArr(result_path);
    }
    
    public int[][] myMazeSolver(){
        int[] start = {this.startRow, this.startCol}; //Anfangskoordinaten
        
        Queue<int[]> frontier = new LinkedList<>();        
        frontier.add(start);            //legt Anfangskoordinaten in die Queue
        String startKey = gridElementToString(this.startRow, this.startCol);
                                        //Key for Map
                                        
        HashMap<String, String> came_from = new HashMap<String, String>();
                                        //Speichert, wo man schon war: ["aktuelle Koordinaten", Koordinaten "wo man schon war"]
        came_from.put(startKey, null); //Startwert hat keine vorigen Neighbours
        
        int[] current;
        while (frontier.size() != 0){
            current = frontier.poll();   //firstINfirstOUT nimmt die nächste Position und löscht sie
            
            //System.out.println(String.valueOf(current[0])+","+String.valueOf(current[1]));  
            
            for (int[] next : getNeighbours(current[0], current[1])){
                                        //wandert alle Variablen des neighbour-arrays ab (getNeighbours wird nur einmal aufgerufen)
                                        //next ist iterator über die Liste von Koordinaten;
                String nextKey = gridElementToString(next[0], next[1]);
                if (!came_from.containsKey(nextKey)){        //sucht in Key in der Dictionary, ob wir dort schon waren
                    frontier.add(next);              //legt neue Nachbarkoordinaten in frontierQueue
                    came_from.put(nextKey,gridElementToString(current[0], current[1]));   //speichert die vorige Position ab
                }
            }
        }
        
        
        //System.out.println(came_from);
                
        int[][] result_path = generateResultPath(came_from);
                      
        return result_path;
       
    }
    
    // Command for starting the solving procedure
    public int[][] solveMaze(){
        return myMazeSolver();
    }
    
    public static void main() throws FileNotFoundException , IOException{
        
        MazeSolverAlgo mg = new MazeSolverAlgo();
        //mg.startMaze(5, 5);
       
        mg.loadMaze("MazeExamples/maze2.txt");
        mg.printMaze();
                
        
        System.out.println("gridsize="+String.valueOf(mg.grid.length));
        System.out.println("start="+"["+String.valueOf(mg.startRow)+","+String.valueOf(mg.startCol)+"]");
        
        int[][] steps =mg.solveMaze();
        System.out.println(Arrays.deepToString(steps));

    }
}








