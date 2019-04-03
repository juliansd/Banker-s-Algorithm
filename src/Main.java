import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	private ArrayList<Resource> resources;
	private int[][][] bankersTable;
	private int[][] processedInput;
	private int[] total;
	private int[] available;
	private int numOfTasks;
	
	@SuppressWarnings("resource")
	protected void processInput(String filePath) {
		
		Scanner scan = new Scanner(System.in);
		
		ArrayList<Resource> resources = new ArrayList<Resource>();
		
		File file = new File(filePath);
		
		int count = 0;
		int row = 0;
		int col = 0;
		
		int[][] processedInput = null;
		
		try {
			
			scan = new Scanner(file);
			
			while (scan.hasNext()) {
				String current = scan.next();
				if (count == 0) {
					this.setNumOfTasks(Integer.parseInt(current));
					count++;
				} else if (count == 1) {
					int differentResourceTypes = Integer.parseInt(current);
					for (int i = 0; i < differentResourceTypes; i++) {
						Resource r = new Resource(Integer.parseInt(scan.next()));
						resources.add(r);
					}
					this.setResources(resources);
					count = -1;
					processedInput = new int[(this.getResources().size()*this.getNumOfTasks()*3)+this.getNumOfTasks()][4];
				} else {
					if (current.length() == 1) {
						processedInput[row][col] = Integer.parseInt(current);
						
						if (col == 3) {
							col = 0;
							row++;
						} else {
							col++;
						}
						if (row == (this.getResources().size()*this.getNumOfTasks()*3)+this.getNumOfTasks()-1 && col == 3)
							break;
					}
				}
			}
			
			this.setProcessedInput(processedInput);
			
		} catch (Exception e) {}
		
	}
	
	protected void populateBankersTable() {
		int[][][] bankersTable = new int[this.getNumOfTasks()][3][this.getResources().size()];
		int[][] processedInput = this.getProcessedInput();
		int[][] initialClaim = new int[this.getNumOfTasks()][this.getResources().size()];
		ArrayList<Resource> resources = this.getResources();
		int[] total = new int[this.getResources().size()];
		int[] available = new int[this.getResources().size()];
		int[] currentClaim = new int[this.getResources().size()];
		int m = this.getResources().size()*3 + 1;
		int n = 0;
		int p = 0;
		
		for (int i = 0; i < processedInput.length; i++) {
			if (i % m >= 0 && i % m < this.getResources().size()) {
				
				currentClaim[n] = processedInput[i][3];
				n++;
				
				if (n == currentClaim.length)
					n = 0;
			}
			
			else if (i % m == this.getResources().size()) {
				initialClaim[p] = currentClaim;
				p++;
				currentClaim = new int[this.getResources().size()];
			}
		}
		
		for (int i = 0; i < bankersTable.length; i++) {
			for (int j = 0; j < bankersTable[i].length; j++) {
				if (j == 0 || j == 2)
					bankersTable[i][j] = initialClaim[i];
			}
		}
		
		for (int i = 0; i < available.length; i++) {
			available[i] = this.getResources().get(i).getVal();
		}
		
		this.setBankersTable(bankersTable);
		this.setTotal(total);
		this.setAvailable(available);
	}
	
	protected void BankersAlgorithm() {
		ArrayList<Resource> resources = this.getResources();
		int[][] processedInput = this.getProcessedInput();
		int finished = 0;
		int cycle = 0;
		
		while (finished < this.getNumOfTasks()) {
			if (cycle == 0) {
				
			}
		}
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.processInput(args[0]);
		main.populateBankersTable();
//		main.BankersAlgorithm();
	}
	
	public ArrayList<Resource> getResources() {
		return resources;
	}
	
	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}
	
	public int[][] getProcessedInput() {
		return processedInput;
	}
	
	public void setProcessedInput(int[][] processedInput) {
		this.processedInput = processedInput;
	}

	public int getNumOfTasks() {
		return numOfTasks;
	}

	public void setNumOfTasks(int numOfTasks) {
		this.numOfTasks = numOfTasks;
	}

	public int[] getTotal() {
		return total;
	}

	public void setTotal(int[] total) {
		this.total = total;
	}

	public int[] getAvailable() {
		return available;
	}

	public void setAvailable(int[] available) {
		this.available = available;
	}

	public int[][][] getBankersTable() {
		return bankersTable;
	}

	public void setBankersTable(int[][][] bankersTable) {
		this.bankersTable = bankersTable;
	}
	
}
