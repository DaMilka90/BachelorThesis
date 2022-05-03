package bachelor_thesis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PcapTxtManipulator {

	public static void main(String[] args) throws IOException {
		// Watch out how u name your Wireshark file
		String inFile = "Normal_from_Pcap.txt";
		String outFile = "Manipulated_Normal.txt";

		BufferedReader myBufReader = new BufferedReader(new FileReader(inFile));

		ArrayList<String> lineList = new ArrayList<>();

		lineList = readInfile(myBufReader, lineList);

		manipulateOutfile(lineList, outFile);
	}

	// This is the structure of the file red by readInFile();
	// the loop for (int i = 2; i < lineList.size(); i += 4) picks the lines that
	// are marked with ***
	// ---------+---------------+----------+
	// 09:34:29,666,573 ETHER
	// *** |0 |ff|ff|ff|ff|ff|ff|d4|3d|7e|f3|76|34|08|00|45|00|01|23|85|81|00|00|80|
	//
	// +---------+---------------+----------+
	// 09:34:30,229,027 ETHER
	// *** |0 |00|b0|52|00|00|01|e8|df|70|eb|1c|41|88|e1|00|68|a0|00|b0|52|a8|a3|a1|
	//
	// +---------+---------------+----------+
	// 09:34:30,229,754 ETHER
	// *** |0 |ff|ff|ff|ff|ff|ff|e8|df|70|eb|1c|41|88|e1|00|00|a0|00|b0|52|a8|a3|a1|
	//
	// +---------+---------------+----------+
	// 09:34:30,230,194 ETHER
	// *** |0 |ff|ff|ff|ff|ff|ff|e8|df|70|eb|1c|41|89|12|01|70|a0|00|00|00|1f|84|a3|
	
	private static void manipulateOutfile(ArrayList<String> lineList, String myOutfile) throws IOException {
		FileWriter myWriter = new FileWriter(myOutfile);
		try {
			for (int i = 2; i < lineList.size(); i += 4) {
				myWriter.write(lineList.get(i) + "\n");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		myWriter.close();
		System.out.println("Successfully wrote to the file.");
	}

	// When u read a file With a linereader it is saved in a String.
	// U can not iterate over a String so we save it line by line into an ArrayList
	// to iterate over it
	private static ArrayList<String> readInfile(BufferedReader myBufReader, ArrayList<String> myLineList)
			throws IOException {
		String line = myBufReader.readLine();
		while (line != null) {
			myLineList.add(line);
			line = myBufReader.readLine();
		}
		myBufReader.close();
		return myLineList;
	}
}