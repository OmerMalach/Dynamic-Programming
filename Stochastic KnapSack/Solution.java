package knapSackDP;
import java.util.Vector;

public class Solution {
	public  Vector <double[][]> optimalMatrices; // a vector containing all optimal matrices- we derive the end solution by recording each and every stage
	private Order [] orders; 
	private double weightLimit;
	private int Limit;

	public Solution(int numOfOrders, int weightLimit) {
		this.optimalMatrices = new Vector <double[][]>();
		this.orders= new Order[numOfOrders]; // check if need
		this.weightLimit=weightLimit;
	}

	private void printAllStages() { // method is responsible of sending tables for print
		System.out.println("Optimal Policy Tables:");
		for (int k = optimalMatrices.size() - 1; k >= 0; k--) {
			double[][] temp = optimalMatrices.elementAt(k);
			printTemp(k,temp);
		}
	}

	private void titleGenarator(int k) { // Generates title for each stage when presenting optimal tables
		System.out.println("The optimum table for stage N=" + (k + 1));
		System.out.print("\t" + "     "+"X*" + (k + 1) + "\t" + "     "+ "f*" + (k + 1) + "\n");
		System.out.println("--------------------------------");
	}

	private void printTemp(int k, double[][] temp) { // // method is responsible of actually printing matrix by matrix 
		titleGenarator(k);
		for (int i = 0; i < temp.length; i++) {
				System.out.print("S" + (k + 1) + "=" + i + ":\t|" + "\t");
			for (int j = 0; j < temp[i].length; j++) {
				System.out.print(temp[i][j] + "\t|\t");
			}
			System.out.println();
		}
		System.out.println();
	}

	private void printFinal() { // method is responsible of running final prints
		double[][] first = optimalMatrices.elementAt(0); // calls the first matrix- first matrix would be the one representing final stage
		double[] optimal = reverseEngineerForFinalSolution(first); // method return an array 
		printFinalConclusion(first, optimal);
	}

	private double[] reverseEngineerForFinalSolution(double[][] first) { // // method is responsible of generating the final solution from tables
		double[] optimal = new double[orders.length];
		optimal[0] = first[0][0];
		double doubleLimit = weightLimit - optimal[0] * orders[0].getWeight();
		Limit = (int)doubleLimit;
		for (int i = 1; i < optimalMatrices.size(); i++) { 
			double[][] currentMatrix = optimalMatrices.elementAt(i);// by traversing through the matrices 
			optimal[i] = currentMatrix[Limit][0]; // we take the optimal value given the current limit 
			Limit -= optimal[i] * orders[i].getWeight(); //and subtract the all-ready taken weight from the total capacity. 
		}
		return optimal; 
	}

	private void printFinalConclusion(double[][] first, double[] optimal) {  // method is responsible of printing final results
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
