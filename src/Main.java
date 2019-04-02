import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	private ArrayList<Resource> resources;
	private int[][] processedInput;
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
	
	public static void main(String[] args) {
		Main main = new Main();
		main.processInput(args[0]);
		
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
	
}
