import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	public void populateBankersTableTest() {
		int[][][] actualBankersTable = {
				{ {4,4},{1,1},{3,4} },
				{ {4,4},{0,0},{4,4} },};
		//              {1,1}
		//              {3,3}
		int[] actualTotal = {0,0};
		int[] actualAvailable = {4,4};
		
		Main main = new Main();
		main.processInput("sample_input.txt");
		main.populateBankersTable();
		
		
		assertArrayEquals(main.getBankersTable(), actualBankersTable);
		assertArrayEquals(main.getTotal(), actualTotal);
		assertArrayEquals(main.getAvailable(), actualAvailable);
	}
}
