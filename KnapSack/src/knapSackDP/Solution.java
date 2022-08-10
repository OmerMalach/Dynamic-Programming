package knapSackDP;
import java.util.Vector;

public class Solution {
	public  Vector <int[][]> optimalMatrices; // a vector containing all optimal matrices- we derive the end solution by recording each and every stage
	private Order [] orders; 
	private int weightLimit;

	public Solution(int numOfOrders, int weightLimit) {
		this.optimalMatrices = new Vector <int[][]>();
		this.orders= new Order[numOfOrders]; // check if need
		this.weightLimit=weightLimit;
	}

	private void printAllStages() { // method is responsible of sending tables for print
		System.out.println("Optimal Policy Tables:");
		for (int k = optimalMatrices.size() - 1; k >= 0; k--) {
			int[][] temp = optimalMatrices.elementAt(k);
			printTemp(k,temp);
		}
	}

	private void titleGenarator(int k) { // Generates title for each stage when presenting optimal tables
		System.out.println("The optimum table for stage N=" + (k + 1));
		System.out.print("\t" + "X*" + (k + 1) + "\t" + "f*" + (k + 1) + "\n");
	}

	private void printTemp(int k, int[][] temp) { // // method is responsible of actually printing matrix by matrix 
		titleGenarator(k);
		for (int i = 0; i < temp.length; i++) {
				System.out.print("S" + (k + 1) + "=" + i + ":" + "\t");
			for (int j = 0; j < temp[i].length; j++) {
				System.out.print(temp[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}

	private void printFinal() { // method is responsible of running final prints
		int[][] first = optimalMatrices.elementAt(0); // calls the first matrix- first matrix would be the one representing final stage
		int[] optimal = reverseEngineerForFinalSolution(first); // method return an array 
		printFinalConclusion(first, optimal);
	}

	private int[] reverseEngineerForFinalSolution(int[][] first) { // // method is responsible of generating the final solution from tables
		int[] optimal = new int[orders.length];
		optimal[0] = first[0][0];
		int Limit = weightLimit - optimal[0] * orders[0].getWeight();
		for (int i = 1; i < optimalMatrices.size(); i++) { 
			int[][] currentMatrix = optimalMatrices.elementAt(i);// by traversing through the matrices 
			optimal[i] = currentMatrix[Limit][0]; // we take the optimal value given the current limit 
			Limit -= optimal[i] * orders[i].getWeight(); //and subtract the all-ready taken weight from the total capacity. 
		}
		return optimal; 
	}

	private void printFinalConclusion(int[][] first, int[] optimal) {  // method is responsible of printing final results
		System.out.println("Optimal Solution:");
		System.out.println();
		String orderName = "";
		for (int i = 0; i < optimal.length; i++) {
			String order = orders[i].getIndexName() + " = " + optimal[i]; 
			if(i < optimal.length-1)
				orderName += order + ",\r\n";
			else
				orderName += order;
		}
		System.out.println(orderName);
		System.out.println();
		System.out.println("Optimal Profit: " + first[0][1]);
	}

	public void showMeWhatYouGot() { // method is responsible of running the solution until final results are printed.
		 printAllStages();
		 printFinal(); 
	}

	public Order[] getOrders() { 
		return orders;
	}
}
