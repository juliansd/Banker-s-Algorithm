import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private int numOfTasks;
	private int numOfResources;
	private boolean consecutiveRelease;
	
	@SuppressWarnings("resource")
	protected ArrayList<ArrayList<Object>> processInput(String filePath) {
		
		ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> resources = new ArrayList<Object>();
		ArrayList<Object> tasks = new ArrayList<Object>();
		
		Scanner scan = new Scanner(System.in);
		
		File file = new File(filePath);

		
		try {
			
			scan = new Scanner(file);
			
			Path path = Paths.get(filePath);
			List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			String[] firstLine = allLines.get(0).split(" ");
			this.setNumOfTasks(Integer.parseInt(firstLine[0]));
			this.setNumOfResources(Integer.parseInt(firstLine[1]));
			for (int j = 2; j < firstLine.length; j++) {
				resources.add(Integer.parseInt(firstLine[j]));
			}
			
			for (int i = 0; i < this.getNumOfTasks(); i++) {
				LinkedList<ArrayList<String>> instructions = new LinkedList<ArrayList<String>>();
				tasks.add(new Task(instructions, this.getNumOfResources(), i));
			}
			
			for (int i = 1; i < allLines.size(); i++) {
				String[] line = allLines.get(i).split(" ");
				ArrayList<String> newLine = new ArrayList<String>();
				for (int j = 0; j < line.length; j++) {
					if (!line[j].equals(""))
						newLine.add(line[j]);
				}
				Task t = (Task) tasks.get(Integer.parseInt(newLine.get(1))-1);
				LinkedList<ArrayList<String>> instructions = t.getInstructions();
				instructions.add(newLine);
				t.setInstructions(instructions);
			}
			
			output.add(tasks);
			output.add(resources);
			
		} catch (Exception e) {}
		scan.close();
		return output;
	}
	
	private static int[] simulate(
			Task t, List<Task> tasks, ArrayList<Object> resources, 
			int[] potentialRelease, int cycle, int done, boolean bankers, 
			HashMap<Task,int[][]> bankersTable, int[] available) {
		
		LinkedList<ArrayList<String>> instructions = t.getInstructions();
		int[] currentlyAllocated = t.getCurrentlyAllocated();
		int[] cycleDone = new int[2];
		int delay = Integer.parseInt(instructions.peek().get(2));
		if (delay == 1 && instructions.peek().get(0).equals("terminate")) {
			delay--;
			t.setFinished(true);
			done++;
			cycleDone[0] = cycle;
			cycleDone[1] = done;
		}
		else if (delay > 0) {
			delay--;
			instructions.peek().set(2, Integer.toString(delay));
			cycleDone[0] = cycle;
			cycleDone[1] = done;
		} else {
			if (instructions.peek().get(0).equals("initiate")) {
				instructions.pop();
			} else if (instructions.peek().get(0).equals("request")) {
				t.setDelay(t.getDelay()+delay);
				int resourceType = Integer.parseInt(instructions.peek().get(3));
				int request = Integer.parseInt(instructions.peek().get(4));
				if ( (int) available[resourceType-1] >= request) {
					if (bankers) {
						// need to make sure in safe state
						if (isSafe(bankersTable, tasks, t, available)) {
							resources.set(resourceType-1, (int) resources.get(resourceType-1)-request);
							currentlyAllocated[resourceType-1] += request;
							t.setCurrentlyAllocated(currentlyAllocated);
							instructions.pop();
							t.setBlocked(false);
						} else {
							t.setWaitTime(t.getWaitTime()+1);
							t.setBlocked(true);
							t.setBlockCycle(cycle);
						}
					} else {
						resources.set(resourceType-1, (int) resources.get(resourceType-1)-request);
						available[resourceType-1] -= request;
						currentlyAllocated[resourceType-1] += request;
						t.setCurrentlyAllocated(currentlyAllocated);
						instructions.pop();
						t.setBlocked(false);
					}
				} else {
					t.setWaitTime(t.getWaitTime()+1);
					t.setBlocked(true);
					t.setBlockCycle(cycle);
				}
	
			} else if (instructions.peek().get(0).equals("release")) {
				t.setDelay(t.getDelay()+delay);
				int resourceType = Integer.parseInt(instructions.peek().get(3));
				int release = Integer.parseInt(instructions.peek().get(4));
				potentialRelease[resourceType-1] += release;
				currentlyAllocated[resourceType-1] -= release;
				bankersTable.get(t)[2][resourceType-1] += release;
				bankersTable.get(t)[1][resourceType-1] -= release;
				t.setCurrentlyAllocated(currentlyAllocated);
				instructions.removeFirst();
				if (instructions.peek().get(0).equals("terminate")) {
					if (Integer.parseInt(instructions.peek().get(2)) == 0) {
						delay = Integer.parseInt(instructions.peek().get(2));
						t.setDelay(t.getDelay()+delay);
						done++;
						t.setFinished(true);
					}
				} else if (instructions.peek().get(0).equals("release") && bankers) {
					while (instructions.peek().get(0).equals("release")) {
						delay = Integer.parseInt(instructions.peek().get(2));
						t.setDelay(t.getDelay()+delay);
						resourceType = Integer.parseInt(instructions.peek().get(3));
						release = Integer.parseInt(instructions.peek().get(4));
						potentialRelease[resourceType-1] += release;
						currentlyAllocated[resourceType-1] -= release;
						bankersTable.get(t)[2][resourceType-1] += release;
						bankersTable.get(t)[1][resourceType-1] -= release;
						t.setCurrentlyAllocated(currentlyAllocated);
						instructions.pop();
					}
					if (instructions.peek().get(0).equals("terminate")) {
						if (Integer.parseInt(instructions.peek().get(2)) == 0) {
							delay = Integer.parseInt(instructions.peek().get(2));
							t.setDelay(t.getDelay()+delay);
							done++;
							t.setFinished(true);
						}
					}
				}
	
			} else if (instructions.peek().get(0).equals("terminate")) {
				t.setDelay(t.getDelay()+delay);
				t.setFinished(true);
				done++;
			}
			cycleDone[0] = cycle;
			cycleDone[1] = done;
		}
		return cycleDone;
	}
	
	protected void FIFO(List<Task> tasks, ArrayList<Object> resources, boolean bankers, 
			HashMap<Task,int[][]> bankersTable, int[] available) {
		
		// deep copy tasks
		List<Task> newTasks = new ArrayList<>();
		for (int i = 0; i < tasks.size(); i++) {
			newTasks.add(tasks.get(i));
		}
		
		int done = 0;
		int aborted = 0;
		int cycle = 0;
		
		int[] potentialRelease = new int[resources.size()];
		
		while (done < this.getNumOfTasks()-aborted) {
			Collections.sort(newTasks);
			
			potentialRelease = new int[resources.size()];
			
			this.setConsecutiveRelease(false);
			
			int oldDone = 0;
			
			for (int i = 0; i < newTasks.size(); i++) {
				int[] cycleDone = new int[2];
				Task t = (Task) newTasks.get(i);
				if (!t.isAborted() && !t.isFinished()) {
					cycleDone = simulate(t, newTasks, resources, potentialRelease, cycle, done, bankers, bankersTable, available);
					cycle = cycleDone[0];
					oldDone = done;
					done = cycleDone[1];
				}
			}
			// do release
			for (int i = 0; i < resources.size(); i++) {
				resources.set(i, (int) resources.get(i) + potentialRelease[i]);
				available[i] += potentialRelease[i];
			}
			for (int i = 0; i < newTasks.size(); i++) {
				boolean nonZero = false;
				Task t = (Task) newTasks.get(i);
				if (t.isAborted()) {
					t.setBlocked(false);
					int[] currentlyAllocated = t.getCurrentlyAllocated();
					for (int j = 0; j < currentlyAllocated.length; j++) {
						if (currentlyAllocated[j] != 0) {
							nonZero = true;
							available[j] += currentlyAllocated[j];
							currentlyAllocated[j] = 0;
						}
					}
					if (nonZero)
						aborted++;
				}
			}
			cycle++;
			for (int i = 0; i < newTasks.size(); i++) {
				Task t = (Task) newTasks.get(i);
				if (t.isFinished() && t.getFinishTime() < 0) {
					if (aborted <= 1 || aborted > this.getNumOfTasks()/2)
						t.setFinishTime(cycle);
					else {
						t.setFinishTime(cycle-aborted+1);
						t.setWaitTime(t.getWaitTime()-aborted+1);
					}
				}
			}
			int count = 0;
			for (int i = 0; i < newTasks.size(); i++) {
				Task t = (Task) newTasks.get(i);	
				if (!t.isBlocked())
					break;
				else
					count++;
			}
			if (count == newTasks.size()-oldDone) {
				// abort leading task
				boolean canBreak = false;
				for (int i = 0; i < tasks.size(); i++) {
					if (!tasks.get(i).isAborted() && !tasks.get(i).isFinished()) {
						Task toAbort = (Task) tasks.get(i);
						if (!toAbort.isFinished()) {
							toAbort.setAborted(true);
							aborted++;
							for (int j = 0; j < resources.size(); j++) {
								resources.set(j, (int) resources.get(j) + toAbort.getCurrentlyAllocated()[j]);
								available[j] += toAbort.getCurrentlyAllocated()[j];
							}
							toAbort.setCurrentlyAllocated(new int[resources.size()]);
							for (int j = 0; j < newTasks.size(); j++) {
								Task t = newTasks.get(j);
								if (!t.isAborted() && !t.isFinished()) {
									if (t.getInstructions().peek().get(0).equals("request")) {
										int index = Integer.parseInt(t.getInstructions().peek().get(3));
										if (Integer.parseInt(t.getInstructions().peek().get(4)) <= (int) resources.get(index-1)) {
											canBreak = true;
											break;
										}
									}
								}
							}
						}
					}
					if (canBreak)
						break;
				}
			}
			
		}
		int totalFinish = 0;
		int totalWait = 0;
		float totalPercentageWait = 0;
		if (bankers) {
			System.out.println("\tBANKER'S");
		} else {
			System.out.println("\t   FIFO");
		}
		
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
			else {
				System.out.println(
						"Task " + index + "\t" + finishTime + 
						"\t" + waitTime + "\t" + percentageWait);
				totalFinish += finishTime;
				totalWait += waitTime;
				totalPercentageWait += waitTime;
			}
		}
		totalPercentageWait /= totalFinish;
		System.out.println("total\t" + totalFinish + "\t" + totalWait + "\t" + totalPercentageWait);
	}
	
	private static boolean isSafe(HashMap<Task,int[][]> bankersTable, List<Task> tasks, Task t, int[] available) {
		
		int[] pretendAvailable = new int[available.length];
		for (int i = 0; i < available.length; i++) {
			pretendAvailable[i] = available[i];
		}
		boolean isSafe = false;
		
		// pretend to grant request
		// check if any other process can finish
		// if no other process can finish, repeat
		
		ArrayList<String> instruction = t.getInstructions().peek();
		int request = Integer.parseInt(instruction.get(4));
		
		int resourceType = Integer.parseInt(instruction.get(3))-1;
		if (request > bankersTable.get(t)[2][resourceType]) {
			t.setAborted(true);
			return isSafe;
		}
		
		pretendAvailable[resourceType] -= request;
		for (int i = 0; i < tasks.size(); i++) {
			Task e = tasks.get(i);
			if (!e.isFinished()) {
				int[] pretendMax = new int[bankersTable.get(e)[2].length];
				for (int j = 0; j < pretendMax.length; j++) {
					pretendMax[j] = bankersTable.get(e)[2][j];
				}
				if (e.equals(t))
					pretendMax[resourceType] -= request;
				
				if (pretendAvailable[resourceType] >= pretendMax[resourceType]) {
					available[resourceType] -= request;
					bankersTable.get(t)[2][resourceType] -= request;
					bankersTable.get(t)[1][resourceType] += request;
					isSafe = true;
					break;
				}
			}
		}
		
		return isSafe;
	}
	
	private HashMap<Task,int[][]> populateBankersTable(List<Task> tasks, ArrayList<Object> resources) {
		HashMap<Task,int[][]> bankersTable = new HashMap<Task,int[][]>();
		for (Task t : tasks) {
			int[][] taskTableData = new int[3][resources.size()];
			for (int i = 0; i < taskTableData.length; i++) {
				int[] initClaimMax = new int[resources.size()];
				for (int j = 0; j < initClaimMax.length; j++) {
					if (i == 0 || i == 2) {
						initClaimMax[j] = Integer.parseInt(t.getInstructions().get(0).get(4));
					} 
				}
				taskTableData[i] = initClaimMax;
			}
			bankersTable.put(t, taskTableData);
		}
		return bankersTable;
	}
	
	private static List<Task> controlConsecutiveRelease(List<Task> tasks) {
		List<Task> output = new ArrayList<>();
		for (Task t : tasks) {
			int c = 0;
			int i = 0;
			int j = i+1;
			ArrayList<String> first = null;
			while (j < t.getInstructions().size()) {
				if (t.getInstructions().get(i).get(0).equals("release") && t.getInstructions().get(j).get(0).equals("release")) {
					c++;
					if (c == 1)
						first = t.getInstructions().get(i);
					if (!first.equals(null)) {
						first.set(2, Integer.toString(c));
					}
				}
				i++;
				j++;
			}
			output.add(t);
		}
		return output;
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		
		ArrayList<ArrayList<Object>> tasksResources = main.processInput(args[0]);
		List<Task> tasks = new ArrayList<>();
		List<Task> tasks2 = new ArrayList<>();
		for (int i = 0; i < tasksResources.get(0).size(); i++) {
			Task t = (Task) tasksResources.get(0).get(i);
			Task t2 = new Task(t);
			tasks.add(t);
			tasks2.add(t2);
		}
		
		tasks2 = controlConsecutiveRelease(tasks2);
		
		ArrayList<Object> resources = tasksResources.get(1);
		ArrayList<Object> resources2 = new ArrayList<Object>(tasksResources.get(1));
		int[] available = new int[resources.size()];
		int[] available2 = new int[resources.size()];
		HashMap<Task,int[][]> bankersTable = main.populateBankersTable(tasks, resources);
		HashMap<Task,int[][]> bankersTable2 = main.populateBankersTable(tasks2, resources2);
		for (int i = 0; i < resources.size(); i++) {
			available[i] = (int) resources.get(i);
			available2[i] = (int) resources2.get(i);
		}
		main.FIFO(tasks, resources, false, bankersTable, available);
		System.out.println("");
		main.FIFO(tasks2, resources2, true, bankersTable2, available2);
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

	public boolean isConsecutiveRelease() {
		return consecutiveRelease;
	}

	public void setConsecutiveRelease(boolean consecutiveRelease) {
		this.consecutiveRelease = consecutiveRelease;
	}
}
