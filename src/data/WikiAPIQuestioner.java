package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public class WikiAPIQuestioner {

	public static String query(String pagetitle) {

		StringBuffer response = new StringBuffer();		
		try {
			
			String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&titles="+pagetitle;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			//System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);
			if(responseCode != 200){
				System.err.println(responseCode + " for title "+pagetitle);
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return getWikiPageId(response.toString());
	}

	public static String getWikiPageId(String htmlText){
		return (new JSONObject(htmlText).getJSONObject("query").getJSONObject("pages").keySet().toArray()[0]).toString();
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		for(String line : Files.readAllLines(Paths.get("/Users/shashans/Desktop/movieIds.txt"))){
			Thread.sleep(500);
			String[] urls = line.split("\t");
			String[] pageIds = new String[urls.length];
			for(int i=0; i<urls.length; i++){
				Thread.sleep(500);
				pageIds[i] = WikiAPIQuestioner.query(urls[i]);
			}
			System.out.println(String.join("\t", pageIds));
		}

	}
}
