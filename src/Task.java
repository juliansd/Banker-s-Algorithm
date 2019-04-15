import java.util.ArrayList;
import java.util.LinkedList;

public class Task implements Comparable<Task>{
	
	private LinkedList<ArrayList<String>> instructions;
	private boolean isBlocked;
	private boolean isAborted;
	private boolean isFinished;
	private int[] currentlyAllocated;
	private int index;
	private int finishTime;
	private int waitTime;
	private int blockCycle;
	private int delay;
	private float percentageWait;

	public Task() {}
	
	public Task(LinkedList<ArrayList<String>> instructions, int numOfResources, int index) {
		this.setBlocked(false);
		this.setAborted(false);
		this.setFinished(false);
		this.setCurrentlyAllocated(new int[numOfResources]);
		this.setDelay(0);
		this.setWaitTime(0);
		this.setFinishTime(-1);
		this.setIndex(index);
		this.setInstructions(instructions);
	}
	
	public Task(Task t) {
		LinkedList<ArrayList<String>> newInstructions = new LinkedList<ArrayList<String>>();
		for (int i = 0; i < t.getInstructions().size(); i++) {
			ArrayList<String> instruction = new ArrayList<String>();
			for (String s : t.getInstructions().get(i)) {
				String newS = new String(s);
				instruction.add(newS);
			}
			newInstructions.add(instruction);
		}
		this.setInstructions(newInstructions);
		this.setBlocked(t.isBlocked());
		this.setAborted(t.isAborted());
		this.setFinished(t.isFinished());
		int[] currentlyAllocated = new int[t.getCurrentlyAllocated().length];
		for (int i = 0; i < currentlyAllocated.length; i++) {
			currentlyAllocated[i] = t.getCurrentlyAllocated()[i];
		}
		this.setCurrentlyAllocated(currentlyAllocated);
		this.setIndex(t.getIndex());
		this.setFinishTime(t.getFinishTime());
		this.setWaitTime(t.getWaitTime());
		this.setBlockCycle(t.getBlockCycle());
		this.setDelay(t.getDelay());
		this.setPercentageWait(t.getPercentageWait());
	}

	public LinkedList<ArrayList<String>> getInstructions() {
		return instructions;
	}

	public void setInstructions(LinkedList<ArrayList<String>> instructions) {
		this.instructions = instructions;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isAborted() {
		return isAborted;
	}

	public void setAborted(boolean isAborted) {
		this.isAborted = isAborted;
	}

	public int[] getCurrentlyAllocated() {
		return currentlyAllocated;
	}

	public void setCurrentlyAllocated(int[] currentlyAllocated) {
		this.currentlyAllocated = currentlyAllocated;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public float getPercentageWait() {
		return percentageWait;
	}

	public void setPercentageWait(float percentageWait) {
		this.percentageWait = percentageWait;
	}

	@Override
	public int compareTo(Task t) {
		if (this.isBlocked() && !t.isBlocked()) 
			return -1;
		else if (!this.isBlocked() && t.isBlocked())
			return 1;
		else if (this.isBlocked() && this.isBlocked()) {
			if (this.getBlockCycle() < t.getBlockCycle())
				return 1;
			else if (this.getBlockCycle() > t.getBlockCycle())
				return -1;
			else
				return 0;
		} else {
			if (this.getIndex() < t.getIndex())
				return -1;
			else
				return 1;
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getBlockCycle() {
		return blockCycle;
	}

	public void setBlockCycle(int blockCycle) {
		this.blockCycle = blockCycle;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}