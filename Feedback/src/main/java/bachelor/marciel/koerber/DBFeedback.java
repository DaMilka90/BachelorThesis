/*@Author: Marciel Koerber
 *@Project: bachelor thesis "Verkehrsgeneratoren Projekt"
 *@Contact:  
 */

package bachelor.marciel.koerber;

import java.io.File;
import bachelor.marciel.koerber.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBFeedback {
	public static void main(String[] args) {
		
		final String GETTER_SCRIPT = "http://192.168.178.29/data.json";

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(GETTER_SCRIPT)).build();
		client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
				.thenApply(DBFeedback::parse).join();

	}
	/*
	 * Handels the respone body and changes the JSON Object into an Array so we can
	 * process the date The JSON object we get looks like this
	 * 
	 * { "result": 
	 * 	[
	 * 		{"Id":"1","PackageID":"0","RaspyID":"0","DepatureTime":" ","ArrivalTime": " ","ProtocolType":" XXX","Application":" ","Length":"0"},
	 *	 	{"Id":"2","PackageID":"0","RaspyID":"0","DepatureTime":" ","ArrivalTime":" ","ProtocolType":" XXX","Application":" ","Length":"0"} 
	 * 	] 
	 * }
	 * 
	 */

	public static String parse(String responseBody) {

		final String FILE_DIRECTORY = "feedback_output\\test.txt";
		final String jsonArrayName = "result";

		JSONObject testDataObject = new JSONObject(responseBody);
		JSONArray testDataArray = testDataObject.getJSONArray(jsonArrayName);

		createFile(FILE_DIRECTORY);
		parseJsonData(testDataArray, FILE_DIRECTORY);
		return null;
	}

	/*
	 * creates a file with name = fileDirectory; if the file already exists just
	 * prints out "File already exists" and throws an IOerror if an IOerror occurs
	 */
	public static void createFile(String fDir) {

		try {
			File myObj = new File(fDir);
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred");
			e.printStackTrace();
		}
	}

	/*
	 * Filewriter with true appends to the file with false or no information
	 * overwrites
	 */
	public static void writeToFile(String CsvData, String fDir) {
		try {

			FileWriter myWriter = new FileWriter(fDir, true);
			myWriter.append(CsvData);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/*
	 * Iterates through the jsonArray and writes a line to the generated file for
	 * every entry in the array. Array Looks like 
	 * [
	 * 	{ "Id": "1", "PackageID": "1", "RaspyID": "1", "DepartureTime": "00:00:00", "ArrivalTime": "00:00:20", "ProtocolType": "UDP", "Application": "XXX", "Length": "XXX" }, 
	 * 	{ "Id": "2", "PackageID": "2", "RaspyID": "2", "DepartureTime": "00:00:20", "ArrivalTime": "00:00:30", "ProtocolType": "TCP", "Application": "XXX", "Length": "XXX" }
	 * ]
	 */
	public static void parseJsonData(JSONArray arrayData, String fDir) {

		final String querryParameterProtocol = "ProtocolType";
		final String querryParameterArrival = "ArrivalTime";
		final String querryParameterDeparture = "DepatureTime";
		final String querryParameterLength = "Length";

		for (int i = 0; i < arrayData.length(); i++) {
			JSONObject data = arrayData.getJSONObject(i);
			String protocol = data.getString(querryParameterProtocol);
			String arrival = data.getString(querryParameterArrival);
			String departure = data.getString(querryParameterDeparture);
			int byteLenght = data.getInt(querryParameterLength);

			writeToFile((protocol + ", " + arrival + ", " + departure + ", " + byteLenght + "\n"), fDir);
		}
	}

}
