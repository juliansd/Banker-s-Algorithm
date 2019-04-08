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
//			System.out.println(allLines);
			
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
						t.setBlocked(false);
						t.setAborted(false);
						t.setFinished(false);
						t.setCurrentlyAllocated(new int[this.getNumOfResources()]);
						t.setWaitTime(0);
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
	
	private static int[] simulate(Task t, ArrayList<Object> tasks, ArrayList<Object> resources, int cycle, int done) {
		LinkedList<ArrayList<String>> instructions = t.getInstructions();
		int[] currentlyAllocated = t.getCurrentlyAllocated();
		int[] cycleDone = new int[2];
		if (instructions.peek().get(0).equals("initiate")) {
			instructions.pop();
		} else if (instructions.peek().get(0).equals("request")) {
			int delay = Integer.parseInt(instructions.peek().get(2));
			int resourceType = Integer.parseInt(instructions.peek().get(3));
			int request = Integer.parseInt(instructions.peek().get(4));
			if ( (int) resources.get(resourceType-1) >= request) {
				resources.set(resourceType-1, (int) resources.get(resourceType-1)-request);
				currentlyAllocated[resourceType-1] += request;
				t.setCurrentlyAllocated(currentlyAllocated);
				instructions.pop();
				t.setBlocked(false);
			} else {
				t.setBlocked(true);
			}
			cycle += delay;
		} else if (instructions.peek().get(0).equals("release")) {
			int delay = Integer.parseInt(instructions.peek().get(2));
			int resourceType = Integer.parseInt(instructions.peek().get(3));
			int release = Integer.parseInt(instructions.peek().get(4));
			resources.set(resourceType-1, (int) resources.get(resourceType-1)+release);
			currentlyAllocated[resourceType-1] -= release;
			t.setCurrentlyAllocated(currentlyAllocated);
			instructions.pop();
			if (instructions.peek().get(0).equals("terminate")) {
				done++;
				t.setFinished(true);
				t.setFinishTime(cycle+1);
			}
			cycle += delay;
		}
		cycleDone[0] = cycle;
		cycleDone[1] = done;
		return cycleDone;
	}
	
	protected void FIFO(ArrayList<Object> tasks, ArrayList<Object> resources) {
		int done = 0;
		int aborted = 0;
		int cycle = 0;
		
		while (done < this.getNumOfTasks()-aborted) {
			for (int i = 0; i < tasks.size(); i++) {
				int[] cycleDone = new int[2];
				Task t = (Task) tasks.get(i);
				if (!t.isAborted() && !t.isFinished()) {
					if (t.isBlocked()) {
						t.setWaitTime(t.getWaitTime()+1);
						cycleDone = simulate(t, tasks, resources, cycle, done);
					} else
						cycleDone = simulate(t,tasks, resources, cycle, done);
					
					cycle = cycleDone[0];
					done = cycleDone[1];
				}
			}
			cycle++;
			int count = 0;
			for (int i = 0; i < tasks.size(); i++) {
				Task t = (Task) tasks.get(i);
				if (!t.isBlocked())
					break;
				else
					count++;
			}
			if (count == tasks.size()) {
				// abort leading task
				Task toAbort = (Task) tasks.get(0);
				toAbort.setAborted(true);
				aborted++;
				for (int i = 0; i < resources.size(); i++) {
					resources.set(i, (int) resources.get(i) + toAbort.getCurrentlyAllocated()[i]);
				}
			}
		}
		int totalFinish = 0;
		int totalWait = 0;
		float totalPercentageWait = 0;
		System.out.println("\t\t FIFO");
		for (int i = 0; i < tasks.size(); i++) {
			Task t = (Task) tasks.get(i);
			int finishTime = t.getFinishTime();
			int waitTime = t.getWaitTime();
			float percentageWait;
			if (t.getFinishTime() != 0)
				percentageWait = t.getWaitTime()/(float) t.getFinishTime();
			else
				percentageWait = 0;
			int index = i+1;
			if (t.isAborted())
				System.out.println("Task " + index + "\taborted");
			else
				System.out.println(
						"Task " + index + "\t" + finishTime + 
						"\t" + waitTime + "\t" + percentageWait);
			
			totalFinish += finishTime;
			totalWait += waitTime;
			totalPercentageWait += waitTime;
		}
		totalPercentageWait /= totalFinish;
		System.out.println("total\t" + totalFinish + "\t" + totalWait + "\t" + totalPercentageWait);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		ArrayList<ArrayList<Object>> tasksResources = main.processInput(args[0]);
		ArrayList<Object> tasks = tasksResources.get(0);
		ArrayList<Object> resources = tasksResources.get(1);
		
		main.FIFO(tasks,resources);
	}
	
	public int getNumOfTasks() {
		return numOfTasks;
	}

	public void setNumOfTasks(int numOfTasks) {
		this.numOfTasks = numOfTasks;
	}
	
	public int getNumOfResources() {
		return numOfResources;
	}

	public void setNumOfResources(int numOfResources) {
		this.numOfResources = numOfResources;
	}
}
