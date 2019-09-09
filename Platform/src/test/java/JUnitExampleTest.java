import org.junit.*;
import static org.junit.Assert.*;

import tartan.smarthome.TartanHomeApplication;

public class JUnitExampleTest {
	@Test
	public void printing() {
		System.out.println("JUnit Test Success 2!");
		assertEquals("1", "1");
	}

	@Test
	public void testGetNameSuccess() {
		assertEquals(new TartanHomeApplication().getName(),
				"smarthome");
	}
	
//	@Test
//        public void testGetNameFail() {
//                assertEquals(new TartanHomeApplication().getName(),
//                                "smart");
//        }
}
