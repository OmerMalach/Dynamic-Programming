package knapSackDP;
	public class Order{
		private String indexName; 
		private int weight;
		private int value;

		public Order (String index, int weight, int profit) { //constructor 
			this.indexName = index;
			this.weight = weight;
			this.value = profit;
		}
		public String str() {                             // all of these method are the getters needed for order object
			return indexName + "[value = " + value + ", weight = " + weight + "]";
		}
		public int getValue() {
			return value;
		}
		public int getWeight() {
			return weight;
		}
		public Order getOrder() {
			return this;
		}
		public String getIndexName() {
			return indexName;
		}
	}

