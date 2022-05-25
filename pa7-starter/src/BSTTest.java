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
        testMap.put(TEST_KEY, TEST_VAL);
        assertEquals(testMap.size(), 1);
    }
    @Test
    public void testReplace_oneVal(){
        testMap.put(TEST_KEY, TEST_VAL);
        testMap.replace(TEST_KEY, TEST_VAL + "1234QWERASDFZXCV");
        assertEquals(testMap.get(TEST_KEY), TEST_VAL + "1234QWERASDFZXCV");
    }
    @Test
    public void testRemove_oneVal(){
        testMap.put(TEST_KEY, TEST_VAL);
        testMap.remove(TEST_KEY);
        assertNull(testMap.get(TEST_KEY));
    }
    @Test
    public void testRemove_fourVal(){
        testMap.put(TEST_KEY, TEST_VAL);
        testMap.put(TEST_KEY + "a", TEST_VAL);
        testMap.put(TEST_KEY + "b", TEST_VAL);
        testMap.put(TEST_KEY + "c", TEST_VAL);
        testMap.remove(TEST_KEY);
        assertNull(testMap.get(TEST_KEY));
    }


	@Test
    public void testKeyList(){
        testMap.put(TEST_KEY, TEST_VAL);
		ArrayList<String> keyList1 = new ArrayList();
		keyList1.add(TEST_KEY);

        ArrayList<String> keyList = new ArrayList(testMap.keys());

		for (int i =0; i<keyList.size(); i++) {
			System.out.print(keyList.get(i));
		}

		assertArrayEquals(keyList.toArray(), keyList1.toArray());
	}
}
