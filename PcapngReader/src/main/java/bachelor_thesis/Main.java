/*@Author: Marciel Koerber

 *@Project: bachelor thesis "Verkehrsgeneratoren Projekt"
 *@Contact: mar.kor.pro@gmail.com
 */

package bachelor_thesis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fr.bmartel.pcapdecoder.PcapDecoder;
import fr.bmartel.pcapdecoder.structure.types.IPcapngType;
import fr.bmartel.pcapdecoder.structure.types.inter.IEnhancedPacketBLock;

public class Main {

	private static final String IPV4 = "ipv4";
	private static final String IPV6 = "ipv6";
	private static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws IOException {

		final String infile =  "Data\\normal.pcapng";
		
//		final String infileVlan ="Data\\vlan.pcapng";

//		final String fileNameVlan = "Vlan.txt";
		final String fileNameNormal = "Normal.txt";

		PcapDecoder decoder = new PcapDecoder(infile);
		decoder.decode();
		ArrayList<IPcapngType> sectionList = decoder.getSectionList();

		CreateFile(fileNameNormal);
		for (IPcapngType element : sectionList) {

			if (element instanceof IEnhancedPacketBLock) {

				IEnhancedPacketBLock section = (IEnhancedPacketBLock) element;
				vlanPresent(section);
				noVlanPresent(section);

			}
		}
		WriteToFile(fileNameNormal, sb.toString());
	}

	//	Chart to distinguish which bytes have which code(value) in case of IPV4  
	//	Protocol	byte	value(hex)	
	//	IPV4		12-13	0800
	//	UDP			23		11
	//	TCP			23		06
	//	VLAN		12-13	8100
	//	VLAN IPV4	16-17	0800		
	//	VLAN UDP	27		11
	//	VLAN TCP	27		06
	
	//	Chart to distinguish which bytes have which code(value) in case of IPV4
	//	Protocol	byte	value(hex)	
	//	IPV6		12-13	86dd
	//	UDP			20		11
	//	TCP			20		06
	//	VLAN		12-13	8100
	//	VLAN IPV6	16-17	86dd		
	//	VLAN UDP	24		11
	//	VLAN TCP	24		06
	
	// determinate if no VLAN-tag is present by bytewise comparison
	private static void noVlanPresent(IEnhancedPacketBLock sec) {
		byte myByte12 = sec.getPacketData()[12];
		byte myByte13 = sec.getPacketData()[13];
		byte myByte20 = sec.getPacketData()[20];
		byte myByte23 = sec.getPacketData()[23];

		if (checkNetworkProtocol(myByte12, myByte13).equals(IPV4)) {
			sb.append("|08|00|" + checkTransportProtocol(myByte23));
		} else if (checkNetworkProtocol(myByte12, myByte13).equals(IPV6)) {
			sb.append("|86|dd|" + checkTransportProtocol(myByte20));
		} else {
			sb.append("|0\n");
		}

	}

	// determinate if a VLAN-tag is present by bytewise comparison
	private static void vlanPresent(IEnhancedPacketBLock sec) {
		byte myByte12 = sec.getPacketData()[12];
		byte myByte13 = sec.getPacketData()[13];
		byte myByte16 = sec.getPacketData()[16];
		byte myByte17 = sec.getPacketData()[17];
		byte myByte24 = sec.getPacketData()[24];
		byte myByte27 = sec.getPacketData()[27];

		if (checkForVlan(myByte12, myByte13)) {
			if (checkNetworkProtocol(myByte16, myByte17).equals(IPV4)) {
				sb.append("|81|00| |08|00|" + checkTransportProtocol(myByte27));
			} else if (checkNetworkProtocol(myByte16, myByte17).equals(IPV6)) {
				sb.append("|81|00| |86|dd|" + checkTransportProtocol(myByte24));
			} else {
				sb.append("|81|00|\n");
			}
			// VLAN not present
		}

	}

	private static void WriteToFile(String fName, String data) {
		try {
			FileWriter myWriter = new FileWriter(fName);
			myWriter.write(data);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static void CreateFile(String fName) {

		try {
			File myObj = new File(fName);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

//Logical implementation of Vlan Check
	private static Boolean checkForVlan(byte b1, byte b2) {
		final byte byteVlanX1 = (byte) 0x81;
		final byte byteVlanX2 = (byte) 0x00;
		int comparedByte12 = Byte.compare(b1, byteVlanX1);
		int comparedByte13 = Byte.compare(b2, byteVlanX2);
		if (comparedByte12 == 0 && comparedByte13 == 0) {
			return true;
		} else {
			return false;
		}
	}

	// Logical implementation of IP Protocol Check
	private static String checkNetworkProtocol(byte b1, byte b2) {
		final byte byteIpv4X1 = (byte) 0x08;
		final byte byteIpv4X2 = (byte) 0x00;
		final byte byteIpv6X1 = (byte) 0x86;
		final byte byteIpv6X2 = (byte) 0xdd;
		int compareForIpv4x1 = Byte.compare(b1, byteIpv4X1);
		int compareForIpv4x2 = Byte.compare(b2, byteIpv4X2);
		int compareForIpv6x1 = Byte.compare(b1, byteIpv6X1);
		int compareForIpv6x2 = Byte.compare(b2, byteIpv6X2);

		if (compareForIpv4x1 == 0 && compareForIpv4x2 == 0) {
			return IPV4;
		} else if (compareForIpv6x1 == 0 && compareForIpv6x2 == 0) {
			return IPV6;
		}
		return "";
	}

	// Logical implementation of Transport Protocol
	private static String checkTransportProtocol(byte b1) {
		final byte UDP = (byte) 0x11;
		final byte TCP = (byte) 0x06;
		// The commented sections show how to implement further protocols like Icmp
		// final byte ICMP = (byte) 0x01;

		int compareForUdp = Byte.compare(b1, UDP);
		int compareForTcp = Byte.compare(b1, TCP);
		// int compareForIcmp = Byte.compare(b1, ICMP);

		if (compareForUdp == 0) {
			return " |11|\n";
		} else if (compareForTcp == 0) {
			return " |06|\n";

//		} else if (compareForIcmp == 0) {
//			return " Icmp \n";
		}

		return " |0\n";
	}

}
