# BachelorThesis
The software ist used to verify the function of the module that checks the header of packets for Vlan, IPV4, IPV6, TCP and UDP protokols.
How to replicate the Test:
The test compares two files file A and file B with one another.
File A is output by reading a PCAPNG with the main.java program found in src/main/bachelor_thesis
File B needs to be from the same PCAPNG as File A.
Open the PCAPNG in Wireshark and export it as TXT-File e.g I name it TXT-File B.
Be sure to change the value of String inFile in PcapTxtManipulator.java according to the filename of the file you exported in Wireshark.
Now manipulate the The TXT-File B with PcapTxtManipulator.java found in src/main/bachelor_thesis the result is manipulated-file B.
Finally you can compare file A and manipulated-file B.
The files that are used for the JUnit tests under src/test/bachelor_thesis where generated in the way described above.
The pcapng's are from https://wiki.wireshark.org/Development/PcapNg#example-pcapng-capture-files

About the tests:
The tests TestNormalTrue and TestVlanTrue are test where all substrings from line file A are present in file B the return value from getFinalResult() is True.
In the tests TestNormalFalse and TestVlanFalse I manipulated the first line from file A so they do not match file B anymore the return value from getFinalResult() is False.
