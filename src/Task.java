
public class Task {

	private int[][] initiate;
	private int[][] request;
	private int[][] release;
	private int terminate;
	
	public Task() {}

	public int[][] getInitiate() {
		return initiate;
	}

	public void setInitiate(int[][] initiate) {
		this.initiate = initiate;
	}

	public int[][] getRequest() {
		return request;
	}

	public void setRequest(int[][] request) {
		this.request = request;
	}

	public int[][] getRelease() {
		return release;
	}

	public void setRelease(int[][] release) {
		this.release = release;
	}

	public int getTerminate() {
		return terminate;
	}

	public void setTerminate(int terminate) {
		this.terminate = terminate;
	}

}
