import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	public void processInputTest() {
		Main m = new Main();
		assertEquals(m.processInput("input.txt").size(), 2);
	}

}
