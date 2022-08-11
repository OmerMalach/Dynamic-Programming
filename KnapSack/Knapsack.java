package knapSackDP;
import java.util.Scanner;

public class Knapsack {
	private static int numOfOrders;
	private static int capacity;
	private static Solution solution;
	private static Order[] orders;  
	private static Scanner scan;

	public Knapsack(int numOfOrders ) {  ////constructor 
		Knapsack.numOfOrders = numOfOrders;
		Knapsack.orders = new Order [numOfOrders];	
	}

	public void display() { // method is responsible of printing the given problem
		if(orders!=null && orders.length>0) {
			System.out.println("Knapsack problem");
			System.out.println("Capacity = " + capacity);
			System.out.println("Orders:");
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
			System.out.println("How many types of orders?");
			int numOfOrders = scan.nextInt();  
			System.out.println("What is the weight limit?");
			capacity = scan.nextInt();  
			int [] weightsC = new int[numOfOrders];
			int [] tipsC = new int[numOfOrders];
			System.out.println("Please submit orders weights:"); 
			for(int i=0;i<numOfOrders; i++){
				weightsC[i] = scan.nextInt();
			}
			System.out.println("Please submit orders prices:"); 
			for(int j=0;j<numOfOrders;j++){
				tipsC[j] = scan.nextInt();
			}	
			Knapsack kC=InitiateProblem(weightsC,tipsC,tipsC.length); // method returns a knapsack object with the wanted parameters
			return kC;
		}
		else if(choice == 'f'|| choice == 'F') {
			capacity = 80;
			int[] weightsF = {3,1,3,2,8,6,3,3,8,2,10,5,4,9,3,5,9,6};
			int[] tipsF = {2,10,5,4,9,3,5,9,6,3,1,3,2,8,6,3,3,8};
			Knapsack kF=InitiateProblem(weightsF,tipsF,tipsF.length); // method returns a knapsack object with the wanted parameters
			return kF;
		}
		else 
			System.out.println("Please follow the given instruction and try again ;)" );
		return  initializeParameters(); // in case of wrong input
	}

	private static Knapsack  InitiateProblem (int[] weights ,int[] profits, int orderCount) { // method creates the wanted orders and returns a knapsack object with all needed fields
		Knapsack knapsack = new Knapsack(orderCount);
		solution = new Solution(numOfOrders,capacity);
		for (int i = 0; i < weights.length; i++) {
			orders[i] = new Order ("Order " +(i+1),weights[i], profits[i]); // create order and place in knapsack array
			solution.getOrders()[i]=orders[i]; // place order in solution array as well
		}
		return knapsack;
	}

	public void createDPMatrixByStage() { // method is responsible of dividing the "work" into separate stages
		int[][] matrix = null;
		for (int n = orders.length - 1; n > 0; n--) {
			if (n == orders.length - 1) // first matrix 
				matrix = fillFirst(n);
			else
				matrix = fillRest(n); // all the rest of the matrices beside the last one
			solution.optimalMatrices.add(0, fillOptimalPolicyTable(matrix)); // adding the matrix into vector of matrices
		}
	}

	private int[][] fillFirst(int n) { // method is responsible of filling the matrix of step 1.
		int[][] matrix;
		matrix = new int[capacity + 1][capacity / orders[n].getWeight() + 1];
		for (int w = 1; w < matrix.length; w++)
			for (int i = 1; i < matrix[w].length; i++) {
				if (w >= i * orders[n].getWeight()) // condition on weight by row
					matrix[w][i] = orders[n].getValue() * i; // if the wait is acceptable add order value into matrix
			}
		return matrix;
	}

	private int[][] fillRest(int n) { // method is responsible of filling the  rest of the matrices until n-1
		int[][] matrix;
		matrix = new int[capacity + 1][capacity / orders[n].getWeight() + 1];
		for (int w = 1; w < matrix.length; w++) {
			for (int i = 0; i < matrix[w].length; i++) {
				if (i == 0)
					matrix[w][i] = solution.optimalMatrices.elementAt(0)[w][1]; // taking the optimal value from the n-1 matrix
				else {
					if (w >= i * orders[n].getWeight()) { // condition on weight by row
						matrix[w][i] = orders[n].getValue() * i
								+ solution.optimalMatrices.elementAt(0)[w - i * orders[n].getWeight()][1]; // if the wait is acceptable add order value into matrix + add more orders to fill the rest of the given weight limit 
					}
				}
			}
		}
		return matrix;
	}

	private void fillLastMatrix() {// method is responsible of filling the  n=0 matrix (the last one)
		int[][] matrix;
		int n = 0;
		matrix = new int[1][capacity/ orders[n].getWeight() + 1]; // one row needed
		for (int i = 0; i < matrix[0].length; i++) {
			if (capacity>= i * orders[n].getWeight()) {
				matrix[0][i] = orders[n].getValue() * i + solution.optimalMatrices.elementAt(0)[capacity - i * orders[n].getWeight()][1]; // getting the optimal value from the n=1 matrix
			}
		}
		solution.optimalMatrices.add(0, fillOptimalPolicyTable(matrix)); // adding the last matrix into vector of matrices (field of solution object)
	}

	private int[][] fillOptimalPolicyTable (int[][] matrix) { // method is responsible of filling the actual optimal policy tables
		int[][] result = new int[matrix.length][2]; // first - X*k, second - f*k
		for (int i = 0; i < matrix.length; i++) {
			int optimalOrderIndex = 0;
			int optimalOrderValue = matrix[i][0];
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

