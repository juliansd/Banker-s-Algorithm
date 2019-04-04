import java.util.ArrayList;

public class Task {

	private ArrayList<int[]> initiate;
	private ArrayList<int[]> request;
	private ArrayList<int[]> release;
	
	public Task() {}

	public ArrayList<int[]> getInitiate() {
		return initiate;
	}

	public void setInitiate(ArrayList<int[]> initiate) {
		this.initiate = initiate;
	}

	public ArrayList<int[]> getRequest() {
		return request;
	}

	public void setRequest(ArrayList<int[]> request) {
		this.request = request;
	}

	public ArrayList<int[]> getRelease() {
		return release;
	}

	public void setRelease(ArrayList<int[]> release) {
		this.release = release;
	}
}
