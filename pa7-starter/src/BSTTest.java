import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.*;

public class BSTTest {
	private DefaultMap<String, String> testMap; // use this for basic tests
	public static final String TEST_KEY = "Test Key";
	public static final String TEST_VAL = "Test Value";


	/* TODO: Add your own tests */
	@Before
	public void setUp() {
		testMap = new BST();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPut_nullKey() {
		testMap.put(null, TEST_VAL);
	}

	@Test
	public void puttingTest() {
		
	}
}
