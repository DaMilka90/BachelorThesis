package bachelor_thesis;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class TestNormalTrue {

	@Test
	public void test() throws FileNotFoundException, IOException {

		String infileNormal =				"TestFiles\\Normal.txt";
		String manipulatedWiresharkNormal = "TestFiles\\Manipulated_Normal.txt";

		Comparer myComparer = new Comparer();
		myComparer.compareFiles(infileNormal, manipulatedWiresharkNormal);
		assertTrue(myComparer.getFinalResult());
	}

}
