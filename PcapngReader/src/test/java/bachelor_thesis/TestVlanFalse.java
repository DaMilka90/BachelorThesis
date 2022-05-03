package bachelor_thesis;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class TestVlanFalse {

	@Test
	public void test() throws FileNotFoundException, IOException {
		
		String infileVlan = "TestFiles\\Vlan.txt";
		String manipulatedWiresharkVlan = "TestFiles\\Manipulated_VLAN.txt";
		
		Comparer myComparer = new Comparer();
		myComparer.compareFiles(infileVlan, manipulatedWiresharkVlan);
		assertFalse(myComparer.getFinalResult());
	}

}
