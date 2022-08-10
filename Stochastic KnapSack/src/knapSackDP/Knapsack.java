package knapSackDP;
import java.util.Scanner;

public class Knapsack {
	private static int numOfOrders;
	private static int capacity;
	private static double q;
	private static Solution solution;
	private static Order[] orders;  
	private static Scanner scan;

	public Knapsack(int numOfOrders, double q) {  ////constructor 
		Knapsack.numOfOrders = numOfOrders;
		Knapsack.orders = new Order [numOfOrders];	
		Knapsack.q=q;
	}

	public void display() { // method is responsible of printing the given problem
		if(orders!=null && orders.length>0) {
			System.out.println("Knapsack problem");
			System.out.println("Capacity = " + capacity);
			System.out.println("q = " + q);
			System.out.println("Containers:");
			for(Order order : orders) {
				System.out.println("- " + order.str());
			}
		}
	}

	public static Knapsack initializeParameters() { // method is responsible of getting problem details from user 
		scan = new Scanner(System.in);	
		System.out.println("Press 'f' for fixed or 'c' for custom paramaters?");
		char choice = scan.next().charAt(0);
		if(choice == 'c'|| choice == 'C'){
			System.out.println("How many types of containers?");
			int numOfOrders = scan.nextInt();  
			System.out.println("What is the weight limit?");
			capacity = scan.nextInt();  
			System.out.println("What is the probability of lossing a container?");
			q = scan.nextInt();  
			int [] weightsC = new int[numOfOrders];
			int [] tipsC = new int[numOfOrders];
			System.out.println("Please submit containers weights:"); 
			for(int i=0;i<numOfOrders; i++){
				weightsC[i] = scan.nextInt();
			}
			System.out.println("Please submit containers prices:"); 
			for(int j=0;j<numOfOrders;j++){
				tipsC[j] = scan.nextInt();
			}	
			Knapsack kC=InitiateProblem(weightsC,tipsC,tipsC.length,q); // method returns a knapsack object with the wanted parameters
			return kC;
		}
		else if(choice == 'f'|| choice == 'F') {
			capacity = 67;
			int[] weightsF = {3,1,3,2,8,6,3,3,8,2,10,5,5,9,1,5,3,10};
			int[] tipsF = {2,10,5,5,9,1,5,3,10,3,1,3,2,8,6,3,3,8};
			double q = 0.2;  // 5-3/10=0.2
			Knapsack kF=InitiateProblem(weightsF,tipsF,tipsF.length,q); // method returns a knapsack object with the wanted parameters
			return kF;
		}
		else 
			System.out.println("Please follow the given instruction and try again ;)" );
		return  initializeParameters(); // in case of wrong input
	}

	private static Knapsack  InitiateProblem (int[] weights ,int[] profits, int orderCount,double q) { // method creates the wanted orders and returns a knapsack object with all needed fields
		Knapsack knapsack = new Knapsack(orderCount, q);
		solution = new Solution(numOfOrders,capacity);
		for (int i = 0; i < weights.length; i++) {
			orders[i] = new Order ("Containers " +(i+1),weights[i], profits[i]); // create order and place in knapsack array
			solution.getOrders()[i]=orders[i]; // place order in solution array as well
		}
		return knapsack;
	}

	public void createDPMatrixByStage() { // method is responsible of dividing the "work" into separate stages
		double[][] matrix = null;
		for (int n = orders.length - 1; n > 0; n--) {
			if (n == orders.length - 1) // first matrix 
				matrix = fillFirst(n);
			else
				matrix = fillRest(n); // all the rest of the matrices beside the last one
			solution.optimalMatrices.add(0, fillOptimalPolicyTable(matrix)); // adding the matrix into vector of matrices
		}
	}

	private double[][] fillFirst(int n) { // method is responsible of filling the matrix of step 1.
		double[][] matrix;
		matrix = new double[capacity + 1][capacity / orders[n].getWeight() + 1];
		for (int w = 1; w < matrix.length; w++)
			for (int i = 1; i < matrix[w].length; i++) {
				if (w >= i * orders[n].getWeight()) // condition on weight by row if the wait is acceptable add order value into matrix
					if(i==1)
						matrix[w][i] = (1-q)*orders[n].getValue();
					else
						matrix[w][i] = (1-q)*orders[n].getValue() + (i-1) *orders[n].getValue();
					
			}
		return matrix;
	}

	private double[][] fillRest(int n) { // method is responsible of filling the  rest of the matrices until n-1
		double[][] matrix;
		matrix = new double[capacity + 1][capacity / orders[n].getWeight() + 1];
		for (int w = 1; w < matrix.length; w++) {
			for (int i = 0; i < matrix[w].length; i++) {
				if (i == 0)
					matrix[w][i] = solution.optimalMatrices.elementAt(0)[w][1]; // taking the optimal value from the n-1 matrix
				else {
					if (w >= i * orders[n].getWeight()) { // condition on weight by row
						if(i==1){
							matrix[w][i] = (1-q)*(orders[n].getValue()+solution.optimalMatrices.elementAt(0)[w - i * orders[n].getWeight()][1])
									+ q * (solution.optimalMatrices.elementAt(0)[w][1]) ;
								}
						else {
							matrix[w][i] = (1-q)*(orders[n].getValue() + solution.optimalMatrices.elementAt(0)[w - i * orders[n].getWeight()][1])
							+ q*(solution.optimalMatrices.elementAt(0)[w-orders[n].getWeight()*(i-1)][1]) + (i-1)*orders[n].getValue(); //insert the profit from this order + the profit from the last optimal solution (with the right weight)
			
						}
					}	
				}
			}
		}
		return matrix;
	}

	private void fillLastMatrix() {// method is responsible of filling the  n=0 matrix (the last one)
		double[][] matrix;
		int n = 0;
		matrix = new double[1][capacity/ orders[n].getWeight() + 1]; // one row needed
		for (int i = 0; i < matrix[0].length; i++) {
			if(i==0)
				matrix[0][i] = solution.optimalMatrices.elementAt(0)[capacity][1];
			else if(i==1){
				matrix[0][i] = (1-q)*(orders[n].getValue()+solution.optimalMatrices.elementAt(0)[capacity - i * orders[n].getWeight()][1]) 
						+ q * (solution.optimalMatrices.elementAt(0)[capacity][1]) ;
			}
			else{
					matrix[0][i] = (1-q)*(orders[n].getValue() + solution.optimalMatrices.elementAt(0)[capacity - i * orders[n].getWeight()][1]) 
						+ q*(solution.optimalMatrices.elementAt(0)[(capacity-orders[n].getWeight()*(i-1))][1]) + (i-1)*orders[n].getValue(); //insert the profit from this order + the profit from the last optimal solution (with the right weight)
			}	
		}
		solution.optimalMatrices.add(0, fillOptimalPolicyTable(matrix)); // adding the last matrix into vector of matrices (field of solution object)
	}

	private double[][] fillOptimalPolicyTable (double[][] matrix) { // method is responsible of filling the actual optimal policy tables
		double[][] result = new double[matrix.length][2]; // first - X*k, second - f*k
		for (int i = 0; i < matrix.length; i++) {
			int optimalOrderIndex = 0;
			double optimalOrderValue = matrix[i][0];
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] > optimalOrderValue) {
					optimalOrderValue = matrix[i][j];
					optimalOrderIndex = j;
				}
			}
			result[i][0] = optimalOrderIndex; // saving optimal index
			result[i][1] = optimalOrderValue; // saving optimal value
		}
		return result;
	}

	public Solution solve() { // method is responsible of stating the filling process and returns a solution to the problem
		createDPMatrixByStage();
		fillLastMatrix();
		return solution;
	}

	public static void run () { // method is responsible of running the "show" ;)
		Knapsack K = initializeParameters(); // returns a knapsack filled with all orders
		K.display(); // display the given parameters
		System.out.println();
		Solution solution = K.solve(); // method returns a solution
		solution.showMeWhatYouGot(); // method will present final results 
	}

	public static void main(String[] args){
		run();
	}
}

