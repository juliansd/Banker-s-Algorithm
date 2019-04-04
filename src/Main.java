import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	private ArrayList<Resource> resources;
	private int[][][] bankersTable;
	private ArrayList<Task> tasks;
	private int[][] processedInput;
	private int[] total;
	private int[] available;
	private int numOfTasks;
	private int numOfResources;
	private int mod;
	
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
					this.setNumOfResources(differentResourceTypes);
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
			
			this.setMod((this.getNumOfResources()*3)+1);
			this.setProcessedInput(processedInput);
			
		} catch (Exception e) {}
		
	}
	
	protected void populateBankersTable() {
		int numOfResources = this.getNumOfResources();
		int[][][] bankersTable = new int[this.getNumOfTasks()][3][numOfResources];
		int[][] processedInput = this.getProcessedInput();
		int[][] initialClaim = new int[this.getNumOfTasks()][numOfResources];
		int[] total = new int[numOfResources];
		int[] available = new int[numOfResources];
		int[] currentClaim = new int[numOfResources];
		int m = numOfResources*3 + 1;
		int n = 0;
		int p = 0;
		
		for (int i = 0; i < processedInput.length; i++) {
			if (i % m >= 0 && i % m < numOfResources) {
				
				currentClaim[n] = processedInput[i][3];
				n++;
				
				if (n == currentClaim.length)
					n = 0;
			}
			
			else if (i % m == numOfResources) {
				initialClaim[p] = currentClaim;
				p++;
				currentClaim = new int[numOfResources];
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
	
	protected void populateTasks() {
		int[][] processedInput = this.getProcessedInput();
		ArrayList<Task> tasks = new ArrayList<Task>(this.getNumOfTasks());
		int mod = this.getMod();
		int numOfResources = this.getNumOfResources();
		ArrayList<int[]> initiate = new ArrayList<int[]>(numOfResources);
		ArrayList<int[]> request = new ArrayList<int[]>(numOfResources);
		ArrayList<int[]> release = new ArrayList<int[]>(numOfResources);
		Task t = new Task();
		for (int i = 0; i < processedInput.length; i++) {
			int index = (i+1) % mod;
			if (index == 0) {
				t = new Task();
			} else {
				if (index >= 1 && index <= numOfResources) {
					initiate.add(processedInput[i]);
					t.setInitiate(initiate);
				} else if (index >= numOfResources+1 && index <= numOfResources*2) {
					request.add(processedInput[i]);
					t.setRequest(request);
				} else if (index >= (numOfResources*2)+1 && index <= numOfResources*3) {
					release.add(processedInput[i]);
					t.setRelease(release);
				}
				if (index == mod-1)
					tasks.add(t);
			}
		}
		this.setTasks(tasks);
	}
	
	protected void BankersAlgorithm() {
		int[][] processedInput = this.getProcessedInput();
		ArrayList<Task> tasks = this.getTasks();
		LinkedList<Task> granted = new LinkedList<Task>();
		LinkedList<Task> waiting = new LinkedList<Task>();
		int[] total = this.getTotal();
		int[] available = this.getAvailable();
		int numOfResources = this.getNumOfResources();
		int numOfTasks = this.getNumOfTasks();
		int finished = 0;
		int cycle = numOfResources;
		while (finished < numOfTasks) {
			for (int i = 0; i < numOfResources; i++) {
				for (int j = 0; j < numOfTasks; j++) {
					
				}
			}
			cycle++;
		}
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.processInput(args[0]);
		main.populateBankersTable();
		main.populateTasks();
		main.BankersAlgorithm();
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

	public int getNumOfResources() {
		return numOfResources;
	}

	public void setNumOfResources(int numOfResources) {
		this.numOfResources = numOfResources;
	}
	
	public int getMod() {
		return mod;
	}

	public void setMod(int mod) {
		this.mod = mod;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}
}
