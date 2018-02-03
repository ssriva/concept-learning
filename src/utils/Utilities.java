package utils;

public class Utilities {

	
	public static String getStandardizedKey(String statement){
		return statement.toLowerCase().replaceAll("\\s+","").replaceAll("[^A-Za-z0-9]","");
	}
}
