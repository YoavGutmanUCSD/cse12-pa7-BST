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
		testMap.put(TEST_KEY + "a", TEST_VAL);
		ArrayList<String> keyList1 = new ArrayList();
		keyList1.add(TEST_KEY);
		keyList1.add(TEST_KEY + "a");

        ArrayList<String> keyList = new ArrayList(testMap.keys());

		System.out.println(testMap.keys());

		assertArrayEquals(keyList.toArray(), keyList1.toArray());
	}


	@Test
    public void testRemove_withKeyList(){
        DefaultMap<Integer, Integer> testMap = new BST();
		 // use this for basic tests
		/* Let us create following BST
              50
           /     \
          30      70
         /  \    /  \
        20   40  60   80 */
        testMap.put(50, 3);
        testMap.put(30, 3);
        testMap.put(20, 3);
        testMap.put(40, 3);
        testMap.put(70, 3);
        testMap.put(60, 3);
        testMap.put(80, 3);

 
        System.out.println("\n20");
        testMap.remove(20);
		System.out.println(testMap.keys());
 
        System.out.println("\n30");
        testMap.remove(30);
        System.out.println(testMap.keys());
 
        System.out.println("\n50");
        testMap.remove(50);
        System.out.println(testMap.keys());
    }

	@Test
    public void testRemove_withKeyList2(){
        DefaultMap<Integer, Integer> testMap = new BST();
		 // use this for basic tests
		/* make
              50
           /     \
          30      70
         /  \    /  \
        20   40  60   80 */
        testMap.put(50, 3);
 
        System.out.println("\n50");
        testMap.remove(50);
        System.out.println(testMap.keys());
    }

    @Test
    public void testRemoveWithKeyList_sizeOneChild(){
        DefaultMap<Integer, Integer> testMap = new BST();
		 // use this for basic tests
		/* make
              50
           /     \
          30      70
         /  \    /  \
        20   40  60   80 */
        testMap.put(50, 3);
        testMap.put(30, 3);
 
        System.out.println("\n50");
        testMap.remove(50);
        System.out.println(testMap.size());
        System.out.println(testMap.keys());
    }

    
    @Test
    public void removingNulls(){
        DefaultMap<Integer, Integer> testMap = new BST();
		 // use this for basic tests
		/* make
              50
           /     \
          30      70
         /  \    /  \
        20   40  60   80 */
        testMap.put(50, null);
        testMap.put(70, 3);
 
        //System.out.println("\n50");
        testMap.remove(70);
        testMap.remove(50);
        System.out.println(testMap.size());
        System.out.println(testMap.keys());
    }

    @Test
    public void oneSubTreeRemoval(){
        DefaultMap<Integer, Integer> testMap = new BST();
		 // use this for basic tests
		/* make
              50
           /     \
          30      70
         /  \    /  \
        20   40  60   80 */
        testMap.put(50, null);
        testMap.put(70, 3);
        //testMap.put(60, 3);
        testMap.put(80, 3);
 
        //System.out.println("\n50");
        testMap.remove(70);
        System.out.println(testMap.size());
        System.out.println(testMap.keys());
    }

}
