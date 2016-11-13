package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SampleObjectTest {

	SampleObject test1;
	SampleObject test2;
	
	int id1 = 3;
	int id2 = 4;
	
	String name1 = "Jeff";
	String name2 = "George";
	
	String greeting1 = "Hello, I am Jeff";
	
	boolean mod1 = false;
	boolean mod2 = true;
	@Before
	public void setUp() throws Exception
	{
		test1 = new SampleObject(id1, name1);
		test2 = new SampleObject(id2, name2);
	}
	
	@Test
	public void testGreeting() {
		assertTrue(test1.greeting().equals(greeting1));
	}

	@Test
	public void testIdEven() {
		assertTrue(test1.idEven() == false);
		assertTrue(test2.idEven() == true);
	}

}

