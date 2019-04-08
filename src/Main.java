import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private HashMap<Task, int[][]> bankersTable;
	private ArrayList<Task> tasks;
	private ArrayList<Task> tasks2;
	private int[][] processedInput;
	private int[] total;
	private int[] available;
	private int numOfTasks;
	private int numOfResources;
	private int mod;
	
	@SuppressWarnings("resource")
	protected ArrayList<ArrayList<Object>> processInput(String filePath) {
		
		ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> resources = new ArrayList<Object>();
		ArrayList<Object> tasks = new ArrayList<Object>();
		
		Scanner scan = new Scanner(System.in);
		
		File file = new File(filePath);
		
		int count = 0;
		int row = 0;
		int col = 0;
		
		int[][] processedInput = null;
		
		try {
			
			scan = new Scanner(file);
			
			Path path = Paths.get(filePath);
			byte[] bytes = Files.readAllBytes(path);
			List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			System.out.println(allLines);
			
			LinkedList<ArrayList<String>> instructions = new LinkedList<ArrayList<String>>();
			Task t = new Task();
			
			for (int i = 0; i < allLines.size(); i++) {
				String[] line = allLines.get(i).split(" ");
				if (i == 0) {
					int numOfTasks = Integer.parseInt(line[0]);
					this.setNumOfTasks(numOfTasks);
					int numOfResources = Integer.parseInt(line[1]);
					this.setNumOfResources(numOfResources);
					for (int j = 2; j < line.length; j++) {
						resources.add(Integer.parseInt(line[j]));
					}
				} else {
					ArrayList<String> newLine = new ArrayList<String>();
					for (int j = 0; j < line.length; j++) {
						if (!line[j].equals(""))
							newLine.add(line[j]);
					}
					instructions.addLast(newLine);
					if (line[0].equals("terminate")) {
						t.setInstructions(instructions);
						tasks.add(t);
						t = new Task();
						instructions = new LinkedList<ArrayList<String>>();
					}
				}
			}
			output.add(tasks);
			output.add(resources);
			
		} catch (Exception e) {}
		scan.close();
		return output;
	}
	
	protected void FIFO(ArrayList<Object> tasks, ArrayList<Object> resources) {
		ArrayList<Object> running = new ArrayList<Object>();
		ArrayList<Object> blocked = new ArrayList<Object>();
		ArrayList<Object> done = new ArrayList<Object>();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		ArrayList<ArrayList<Object>> tasksResources = main.processInput(args[0]);
		ArrayList<Object> tasks = tasksResources.get(0);
		ArrayList<Object> resources = tasksResources.get(1);
		
		main.FIFO(tasks,resources);
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

	public HashMap<Task,int[][]> getBankersTable() {
		return bankersTable;
	}

	public void setBankersTable(HashMap<Task,int[][]> bankersTable) {
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

	public ArrayList<Task> getTasks2() {
		return tasks2;
	}

	public void setTasks2(ArrayList<Task> tasks2) {
		this.tasks2 = tasks2;
	}
}
