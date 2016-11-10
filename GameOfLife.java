import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.*;

public class GameOfLife 
{
	public static void main(String[] args) throws FileNotFoundException 
	{
		// will test all 4 examples given by parsing the input file, make a 2D matrix then perform the evolution task
		for(int i=0; i<4; i++)												
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
		 * Chose to implement the matrix with ArrayLists because they are dynamic
		 * and we don't know the dimensions of the matrix ahead of time
		 */
		
		ArrayList<ArrayList<Integer>> input = new ArrayList<ArrayList<Integer>>();
		FileReader fileReader = new FileReader(filePath);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String cellInput;
		int col = 0;
		try
		{
			boolean brHasNext = true;
			while(brHasNext)
			{
				cellInput = bufferedReader.readLine();
				if(cellInput != null)
				{
					input.add(new ArrayList<Integer>());						 
					for(int i=0; i<cellInput.length(); i++)
					{
						int val = ((cellInput.charAt(i)) == 49) ? 1 : 0;		// ASCII for 0 and 1 is 48 and 49 respectively
						input.get(col).add(val);
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
	
	public static void printGrid(ArrayList<ArrayList<Integer>> input)
	{
		for(int i=0; i<input.size(); i++)
		{
			for(int j=0; j<input.get(i).size(); j++)
				System.out.print(input.get(i).get(j));
			System.out.println();
		}
	}
	
	private static int countNeighbors(int row, int col, ArrayList<ArrayList<Integer>> grid) 
	{
	    int numNeighbors = 0;		//check all 8 possible neighbors 

	    if ((row - 1 >= 0) && (col - 1 >= 0)) 								// check NW
	        numNeighbors += grid.get(row-1).get(col-1);
	    if ((row - 1 >= 0) && (col < grid.get(row).size())) 				// check N
	    	numNeighbors += grid.get(row-1).get(col);
	    if ((row - 1 >= 0) && (col + 1 < grid.get(row).size())) 			// check NE
	    	numNeighbors += grid.get(row-1).get(col+1);
	    if ((row >= 0) && (col - 1 >= 0)) 									// check W
	    	numNeighbors += grid.get(row).get(col-1);
	    if ((row < grid.size()) && (col + 1 < grid.get(row).size())) 		// check E
	    	numNeighbors += grid.get(row).get(col+1);
	    if ((row + 1 < grid.size()) && (col - 1 >= 0)) 						// check SW
	    	numNeighbors += grid.get(row+1).get(col-1);
	    if ((row + 1 < grid.size()) && (col < grid.get(row).size())) 		// check S
	    	numNeighbors += grid.get(row+1).get(col);;
	    if ((row + 1 < grid.size()) && (col + 1 < grid.get(row).size())) 	// check SE
	    	numNeighbors += grid.get(row+1).get(col+1);

	    return numNeighbors;
	}
	
	public static ArrayList<ArrayList<Integer>> evolve(ArrayList<ArrayList<Integer>> curGrid)
	{
		// 2D matrix representing the possible next generation outcome
		int[][] rule = { {0,0,0,1,0,0,0,0,0} , 		// if (cell == dead) && (aliveNeighbors == 3) then this cell will be alive next gen
						 {0,0,1,1,0,0,0,0,0} };		// if (cell == alive) && (aliveNeighbors == (2 || 3)) then this cell will be alive next gen
		
		int row = curGrid.size();
		int col = curGrid.get(0).size();
		
		// make a new matrix and assign the value for each cell by using the rule matrix above
		ArrayList<ArrayList<Integer>> nextGrid = new ArrayList<ArrayList<Integer>>();
		
		for(int i=0; i<row; i++)
		{
			nextGrid.add(new ArrayList<Integer>());
			for(int j=0; j<col; j++)
			{
				int cell = curGrid.get(i).get(j);
				int liveNeighbors = countNeighbors(i, j, curGrid);
				nextGrid.get(i).add(rule[cell][liveNeighbors]);
			}
		}		
		return nextGrid;
	}
}