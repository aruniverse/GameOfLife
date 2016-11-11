import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;

public class GameOfLife 
{
	public static void main(String[] args) throws FileNotFoundException 
	{
		// will test all examples given by parsing the input file(s), make a 2D matrix then perform the evolution task
		for(int i=0; i<args.length; i++)												
		{
			String filePath = args[i];
			ArrayList<ArrayList<Integer>> input = parseInput(filePath);
			System.out.println("input: ");
			printGrid(input);
			System.out.println("\n" + "output: ");
			ArrayList<ArrayList<Integer>> output = evolve(input);
			printGrid(output);
			System.out.println();
		}
	}
	
	public static ArrayList<ArrayList<Integer>> parseInput(String filePath) throws FileNotFoundException
	{
		/*
		 * Chose to implement the matrix with ArrayLists because they are dynamic and we don't know the dimensions of the matrix ahead of time.
		 * The outer data structure will be an ArrayList holding an ArrayList of integers, growing every time there is a new line of input
		 */
		
		ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
		FileReader fileReader = new FileReader(filePath);						// used to read the file
		BufferedReader bufferedReader = new BufferedReader(fileReader);			// used to read the file
		
		String cellInput;														// will be reading the input line by line, storing into here
		int col = 0;
		try
		{
			boolean brHasNext = true;
			while(brHasNext)
			{
				cellInput = bufferedReader.readLine();							// get the next line from the file
				if(cellInput != null)
				{
					input.add(new ArrayList<Integer>());						 
					for(int i=0; i<cellInput.length(); i++)
					{
						int val = ((cellInput.charAt(i)) == 49) ? 1 : 0;		// for each line, get each character which we will then need to get it's ASCII file 
						input.get(col).add(val);								// ASCII for 0 and 1 is 48 and 49 respectively
					}
					col++;
				}
				else
				{
					brHasNext = false;
					bufferedReader.close();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return input;
	}
	
	public static void printGrid(ArrayList<ArrayList<Integer>> input)			// basic function to traverse through every single element of the 2d matrix and print it
	{
		for(int i=0; i<input.size(); i++)
		{
			for(int j=0; j<input.get(i).size(); j++)
				System.out.print(input.get(i).get(j));
			System.out.println();
		}
	}
	
	public static int countNeighbors(int row, int col, ArrayList<ArrayList<Integer>> grid) 
	{
	    int numNeighbors = 0;					//check all 8 possible neighbors 

	    int xDir[] = { row-1, row, row+1, 
	    			   row-1, 	   row+1, 
	    			   row-1, row, row+1 };		//an array of all the x-coordinates of the surrounding cells
	    int yDir[] = { col-1, col-1, col-1, 
	    			   col, 		 col, 
	    			   col+1, col+1, col+1 };	//an array of all the y-coordinates of the surrounding cells
	    
	    for(int i=0; i<8; i++)										// there are a max of 8 neighbors
	    {
	    	try
	    	{
	    		numNeighbors += grid.get(xDir[i]).get(yDir[i]);		// tries to get a specific adjacent cell using the x and y direction arrays. if the cell is reachable add its value to the count
	    	}
	    	catch (IndexOutOfBoundsException e)						// if one of the surrounding cells is out of bounds, thats ok ignore it and continue
	    	{
	    		continue;
	    	}
	    }

	    return numNeighbors;
	}
	
	public static ArrayList<ArrayList<Integer>> evolve(ArrayList<ArrayList<Integer>> curGrid)
	{
		/*
		 *  rule is a 2D matrix representing the next generation's outcome
		 * 	if (cell == dead) && (aliveNeighbors == 3) then this cell will be alive next gen
		 * 	if (cell == alive) && (aliveNeighbors == (2 || 3)) then this cell will be alive next gen
		 */

		
		// 				how many alive neighbors?
		//				  0,1,2,3,4,5,6,7,8
		int[][] rule = { {0,0,0,1,0,0,0,0,0} , 		// this row is if the cell is currently dead. 
						 {0,0,1,1,0,0,0,0,0} };		// this row is if the cell is currently alive. 
		
		int row = curGrid.size();
		int col = curGrid.get(0).size();
		
		// make a new matrix and assign the value for each cell by using the rule matrix above
		ArrayList<ArrayList<Integer>> nextGrid = new ArrayList<ArrayList<Integer>>();	// 2d matrix to represent after one cycle of evolution
		
		for(int i=0; i<row; i++)
		{
			nextGrid.add(new ArrayList<Integer>());
			for(int j=0; j<col; j++)
			{
				int cell = curGrid.get(i).get(j);							// get the cell from the original grid and check if its alive or not
				int liveNeighbors = countNeighbors(i, j, curGrid);			// get the number of neighbors for the cell
				nextGrid.get(i).add(rule[cell][liveNeighbors]);				// get the next stage of that cell using the rule matrix above
			}
		}		
		return nextGrid;
	}
}
