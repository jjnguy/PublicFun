package edu.iastate.cs228.hw3.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iastate.cs228.hw3.CS228LinkedList;

public class CS228LinkedListTest {

	List<String> test1;
	List<String> test2;
	List<String> ref1;
	List<String> ref2;
	
	@Before
	public void setUp() throws Exception {
		test1 = new CS228LinkedList<String>();
		test2 = new CS228LinkedList<String>();
		ref1 = new LinkedList<String>();
		ref2 = new LinkedList<String>();
	}
	
	@After
	public void tearDown() throws Exception {
		test1 = null;
		test2 = null;
		ref1 = null;
		ref2 = null;
	}

	@Test
	public void testSize() {
		assertEquals(ref1.size(), test1.size());
		for (int i = 0; i < 50; i++){
			test1.add("" + i);
			ref1.add("" + i);
			assertEquals(ref1.size(), test1.size());
		}
		assertEquals(ref1.size(), test1.size());
	}

	/**
	 * Constructor
	 */
	@Test
	public void testCS228LinkedList() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddIntE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetInt() {
		test1.add("happy");
		ref1.add("happy");
		assertEquals(ref1.get(0), test1.get(0));
		for(int i = 0; i < 50; i++){
			test1.add("" + i);
			ref1.add("" + i);
		}
		assertEquals(ref1.get(0), test1.get(0));
		assertEquals(ref1.get(4), test1.get(4));
		assertEquals(ref1.get(ref1.size() - 1), test1.get(test1.size() - 1));
		assertEquals(ref1.get(ref1.size()/2), test1.get(test1.size()/2));
		assertEquals(ref1.get(0), test1.get(0));
	}

	@Test
	public void testListIteratorInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetInt1() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testSet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddIntE1() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRemoveInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testAddAllIntCollectionOfQextendsE() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testListIteratorInt1() {
		fail("Not yet implemented"); // TODO
	}

}
