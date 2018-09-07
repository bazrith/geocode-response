import java.io.IOException;
import java.lang.IllegalStateException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Formatter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;

public class PlotMyAddress {
	public static void main(String... args) {
		Person person = new Person();
		Address address = new Address();
		Scanner inputRaw = null;
		Formatter inputFormat = null;
		Scanner inputForURL = null;
		Formatter outputLatLong = null;
		
		String baseUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String mainUrl = null;
		String apiUrl = "&key=AIzaSyA5ob8CsoU0t3h_jkAdAkUN9HnHRG0ZVzA";
		
		String[] records = null;
		String[] names = null;					
		String[] streetInfo = null;
		String[] cityProv = null;
		String nextLine;
		
		String[] splitLine = null;
		URL url = null;

		//Asst 1
		try {
			inputRaw = new Scanner(Paths.get("files\\input\\InputAddresses.txt"));
			inputFormat = new Formatter("files\\input\\OutputAddresses.csv");

			while(inputRaw.hasNext()) {
				nextLine = inputRaw.nextLine();

				while (nextLine != null) {
					records = new String[4];

					for (int i = 0; i< 4; i++) {
						records[i] = nextLine;
						if (inputRaw.hasNext()) 
						{
							nextLine=inputRaw.nextLine();
						}						
					}

					names = records[0].split("\\s*(\\s|,)\\s*");					
					streetInfo = records[1].split("\\s+");
					cityProv = records[2].split("\\s*(\\s|,)\\s*");		

					person.setFirstName(names[0]);
					person.setLastName(names[1]);

					if (names[1].equalsIgnoreCase("and")) {
						person.setLastName(names[3]);
					}

					if (names.length == 2) {						
						person.setSpouseFirstName("");
						person.setSpouseLastName("");
					} else {
						person.setSpouseFirstName(names[2]);
						person.setSpouseLastName(names[3]);
					}

					address.setStreetNumber(streetInfo[0]);
					address.setStreetName(streetInfo[1]);
					address.setStreetType(streetInfo[2]);

					if(streetInfo.length == 3) {
						address.setStreetOrientation("");
					} else {
						address.setStreetOrientation(streetInfo[3]);
					}

					address.setCityName(cityProv[0]);
					address.setProvince(cityProv[1]);

					//records[3] is postal code, ignore

					inputFormat.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n", 
							person.getFirstName(), 
							person.getLastName(),
							person.getSpouseFirstName(),
							person.getSpouseLastName(),
							address.getStreetNumber(),
							address.getStreetName(),
							address.getStreetType(),
							address.getStreetOrientation(),
							address.getCityName(),
							address.getProvince());

					if (nextLine.length() == 0 && inputRaw.hasNext())
						nextLine = inputRaw.nextLine();

					if (!inputRaw.hasNext()) {
						break;
					}
				}
			}
		}
		catch(IOException | NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
		}
		finally {
			inputRaw.close();
			inputFormat.close();		
		}
		
		
		//Asst 2
		try {
			//Opening files for input and output
			inputForURL = new Scanner(Paths.get("files\\input\\OutputAddresses.csv"));
			outputLatLong = new Formatter("files\\output\\LatLong.csv");
			
			//header
			outputLatLong.format("Latitude,Longitude,Name,Icon,IconScale,IconAltitude\r\n");
			
			//Loop to read all records
			while(inputForURL.hasNext()) {
				nextLine = inputForURL.nextLine();
				
				//Splitting each record to make a URL for Google's geocode response
				while (nextLine != null) {
					splitLine = new String[9];
					splitLine = nextLine.split(",");
					
					mainUrl = new String(baseUrl + splitLine[4] + "+" + splitLine[5] + "+" + splitLine[6] + "+" + splitLine[7] + "+" + splitLine[8] + "+" +splitLine[9] + apiUrl);
					System.out.println(mainUrl);
					
					//Forming URL
					url = new URL(mainUrl);

					//Opening url connection
			        HttpURLConnection web = (HttpURLConnection) url.openConnection();
			        web.setRequestMethod("GET");
			        web.setRequestProperty("Accept", "application/json");

			        BufferedReader webStream = new BufferedReader(new InputStreamReader((web.getInputStream())));

			        String line = "";
			        String webOutput = "";
			        while ((line = webStream.readLine()) != null) {
			            System.out.println(line);
			            webOutput += line;
			        }
			        webStream.close();
			        
			        //Make Gson object
			        Gson jsonObj = new Gson();
			        
			        //Navigate through json object to extract latitude and longitude
			        JsonObject data = jsonObj.fromJson(webOutput, JsonObject.class);
			        JsonArray results = data.get("results").getAsJsonArray();
			        JsonObject info = results.get(0).getAsJsonObject();
			        JsonObject geometry = info.getAsJsonObject("geometry");
			        JsonObject location = geometry.getAsJsonObject("location");  
			        JsonElement lat = location.get("lat");
			        JsonElement lng = location.get("lng");
			        System.out.println("Latitude: " + lat.getAsString() + ", Longitude: " + lng.getAsString());

			        web.disconnect();
					
			        //Formatting name for output
			        String name = "";
			        if(splitLine[2].equals("")) {
			        	name = name.concat(splitLine[0] + " " + splitLine[1]);
			        } else {
			        	name = name.concat(splitLine[0] + " " + splitLine[1] + " and " + splitLine[2] + " " + splitLine[3]);
			        }
			        
			        //Output of each record
			        outputLatLong.format(lat.getAsString() + "," + lng.getAsString() + "," + name + "," + "111,1,1\r\n");
					
			        if (inputForURL.hasNext())
						nextLine = inputForURL.nextLine();
			        else
			        	break;
				}
			}
		}
		catch(IOException | NoSuchElementException | IllegalStateException e) {
			e.printStackTrace();
		}
		finally {	
			inputForURL.close();
			outputLatLong.close();
		}
	}	
}