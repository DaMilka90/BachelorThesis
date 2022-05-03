package bachelor_thesis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Comparer {
	// Normally you should be really careful with global variables but in this case
	// they are fine since the programs function is manageable
	private ArrayList<String> result = new ArrayList<String>();
	private BufferedReader rdMyFile;
	private BufferedReader rdWiresharkFile;
	// LineIn and LineWs are used for a line by line comparison
	// where in LineIn are 1,2 or 3 SubStrings that need to be in LineWS
	private String lineIn;
	private String lineWs;


	// Method is used to compare two files
	public void compareFiles(String vlanInfile, String manipulatedVlanInfile)
			throws FileNotFoundException, IOException {

		rdMyFile = new BufferedReader(new FileReader(vlanInfile));
		rdWiresharkFile = new BufferedReader(new FileReader(manipulatedVlanInfile));

		readOneLineFromEveryFile();
		// Both files have the same length so we can use this loop to get through every
		// line
		while (lineIn != null && lineWs != null) {
			// The String lineIn gets split at the space position and is saved into an array
			// example "|0 will be split into str[0]="|0"
			// example "|08|00| |11|" will be split into str[0]="|08|00|" str[1]="|11|"
			// example "|81|00| |08|00| |06|" will be split into str[0]="|81|00|"
			// str[1]="|08|00|" str[2]="|06|"
			String str[] = lineIn.split(" ");
			compareThreeMatchingString(lineWs, str);
			compareTwoMatchingString(lineWs, str);
			compareOneMatchingString(lineWs, str);

			readOneLineFromEveryFile();
		}
		closeBuffReaders();
	}

	private void readOneLineFromEveryFile() throws IOException {
		lineIn = rdMyFile.readLine();
		lineWs = rdWiresharkFile.readLine();

	}

	private void closeBuffReaders() throws IOException {
		rdMyFile.close();
		rdWiresharkFile.close();
	}

	// Method to compare if lineWs contains a single String from lineIn
	private void compareOneMatchingString(String lineWs, String[] str) {
		if (str.length == 1) {
			if (lineWs.contains(str[0])) {
				result.add("1 Match, OK \n");
			} else {
				result.add("NOT OK \n");
			}
		}
	}

	// Method to compare if lineWs contains two Strings from lineIn
	private void compareTwoMatchingString(String lineWs, String[] str) {
		if (str.length == 2) {
			if (lineWs.contains(str[0]) && lineWs.contains(str[1])) {
				result.add("2 Matches, OK  \n");
			} else {
				result.add("NOT OK \n");
			}
		}
	}

	// Method to compare if lineWs contains three Strings from lineIn
	private void compareThreeMatchingString(String lineWs, String[] str) {
		if (str.length == 3) {
			if (lineWs.contains(str[0]) && lineWs.contains(str[1]) && lineWs.contains(str[2])) {
				this.result.add("3 Matches, OK  \n");
			} else {
				this.result.add("NOT OK \n");
			}
		}
	}

	// this Method returns false if the comparison file contains at least one
	//"NOT OK"
	// and true if the false condition is not met
	public boolean getFinalResult() {

		for (int i = 0; i < result.size(); i++) {
			if (result.get(i).contains("NOT OK")) {
//				System.out.println("Final Test Failed");
				i = result.size();
				return false;
			}
		}
		return true;

	}
}
