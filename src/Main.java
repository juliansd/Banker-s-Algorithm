import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private ArrayList<Object> processedInput;
	private int numOfTasks;
	private int numOfResources;
	private int resourceVal;
	
	@SuppressWarnings("resource")
	protected void processInput(String filePath) {
		
		ArrayList<Object> processedInput = new ArrayList<Object>();
		
		Scanner scan = new Scanner(System.in);
		
		File file = new File(filePath);
		
		try {
			ArrayList<Task> tasks = new ArrayList<Task>();
			ArrayList<Resource> resources = new ArrayList<Resource>();
			scan = new Scanner(file);
			Task t = new Task();
			int[] instanceVar = new int[4]; 
			int count = 0;
			int count2 = 0;
			String dataState = null;
			while (scan.hasNext()) {
				String current = scan.next();
				if (count == 0) {
					this.setNumOfTasks(Integer.parseInt(current));
					count++;
				} else if (count == 1) {
					this.setNumOfResources(Integer.parseInt(current));
					count++;
				} else if (count == 2) {
					this.setResourceVal(Integer.parseInt(current));
					count++;
				} else {
					if (current.equals("initiate"))
						t = new Task();
					
					if (count2 % 5 == 0) {
						instanceVar = new int[4];
						dataState = current;
					}
					
					else if (count2 % 5 == 1)
						instanceVar[0] = Integer.parseInt(current);
					
					else if (count2 % 5 == 2)
						instanceVar[1] = Integer.parseInt(current);
					
					else if (count2 % 5 == 3)
						instanceVar[2] = Integer.parseInt(current);
					
					else {
						instanceVar[3] = Integer.parseInt(current);
						if (dataState.equals("initiate"))
							t.setInitiate(instanceVar);
						
						else if (dataState.equals("request"))
							t.setRequest(instanceVar);
						
						else if (dataState.equals("release"))
							t.setRelease(instanceVar);
						
						else {
							t.setTerminate(instanceVar[0]);
							tasks.add(t);
						}
					}
					
					count2++;
				}
			}
			for (int i = 0; i < this.getNumOfResources(); i++) {
				Resource r = new Resource(this.getResourceVal());
				resources.add(r);
			}
			
			processedInput.add(tasks);
			processedInput.add(resources);
			scan.close();
			this.setProcessedInput(processedInput);
			
		} catch (Exception e) {}
		
	}
	
	protected void BankersAlgorithm() {
		@SuppressWarnings("unchecked")
		ArrayList<Task> tasks = (ArrayList<Task>) this.getProcessedInput().get(0);
		@SuppressWarnings("unchecked")
		ArrayList<Resource> resources = (ArrayList<Resource>) this.getProcessedInput().get(1);
		
		
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.processInput(args[0]);
		
	}
	
	public void setNumOfTasks(int numOfTasks) {
		this.numOfTasks = numOfTasks;
	}
	
	public int getNumOfTasks() {
		return this.numOfTasks;
	}

	public int getNumOfResources() {
		return numOfResources;
	}

	public void setNumOfResources(int numOfResources) {
		this.numOfResources = numOfResources;
	}

	public int getResourceVal() {
		return resourceVal;
	}

	public void setResourceVal(int resourceVal) {
		this.resourceVal = resourceVal;
	}

	public ArrayList<Object> getProcessedInput() {
		return processedInput;
	}

	public void setProcessedInput(ArrayList<Object> processedInput) {
		this.processedInput = processedInput;
	}
	
}
