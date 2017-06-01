/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		
		// TODO: Add more tests here
        try {
            emptyList.remove(0);
            fail("Should not be able to remove from empty list");
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            longerList.remove(1000);
            fail("Should not be able to remove from too big index");
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            longerList.remove(-1);
            fail("Should not be able to remove from negative index");
        } catch (IndexOutOfBoundsException e) {
        }

        int b = longerList.remove(5);
        assertEquals("Remove: check b is correct ", 5, b);
        assertEquals("Remove: check element new value at this index", (Integer) 6, longerList.get(5));
        assertEquals("Remove: check size is correct ", 9, longerList.size());

    }
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
        // TODO: implement this test
        String oldValue = shortList.get(1);
        shortList.add("C");
        int size = shortList.size();
        String newValue = shortList.get(2);

        assertEquals("Add to End: size should increase", 3, size);
        assertEquals("Add to End: last element is correct", "C", newValue);
        assertEquals("Add to End: old values are the same", oldValue, shortList.get(1));

        try {
            list1.add(null);
            fail("Should not accept null values");
        } catch (NullPointerException e) {

        }

	}

	
	/** Test the size of the list */
	@Test
	public void testSize()
	{
		assertEquals("Empty list should show correct size", 0, emptyList.size());
		assertEquals("Non-empty list should show correct size", 3, list1.size());

		list1.add(43);
        assertEquals("Add to list should show correct size", 4, list1.size());

		list1.add(1,543);
        assertEquals("Add to list at index should show correct size", 5, list1.size());

        list1.remove(2);
        assertEquals("Remove from list should show correct size", 4, list1.size());
    }



	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
        // TODO: implement this test
		shortList.add(0, "X");
		shortList.add(2, "Y");
		shortList.add(4, "Z");

        int size = shortList.size();
        assertEquals("Add at index: size should be correct", 5, size);

        StringBuilder result = new StringBuilder();
        for (String aShortList : shortList) result.append(aShortList);

        assertEquals("Add at index: order should be correct", "XAYBZ", result.toString());

        try {
            emptyList.add(1, 100);
            fail("Empty list non zero index should throw Out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            longerList.add(-1, 100);
            fail("Negative index should throw Out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }

        try {
            list1.add(1, null);
            fail("Null values not allowed");
        } catch (NullPointerException e) {
        }

        try {
            longerList.add(10000, 100);
            fail("Non empty list index bigger than size should throw Out of bounds");
        } catch (IndexOutOfBoundsException e) {
        }

        emptyList.add(0, 100);
        assertEquals("Add at index: should be able to add to empty list", 1, emptyList.size());
    }
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
	    // TODO: implement this test
        try {
            emptyList.set(0, 123);
            fail("Should fail on empty list");
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            list1.set(-1, 123);
            fail("Should fail on negative index");
        } catch (IndexOutOfBoundsException e) {

        }

        try {
            longerList.set(10, 321);
            fail("Should fail on too big index");
        } catch (IndexOutOfBoundsException e) {

        }

        Integer a = longerList.set(5, 50);
        assertEquals("Set should return correct value", (Integer)5, a);
        assertEquals("Set should change value", (Integer) 50, longerList.get(5));
    }
	
	
	// TODO: Optionally add more test methods.
	
}
