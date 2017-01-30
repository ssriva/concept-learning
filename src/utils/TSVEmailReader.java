package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.common.collect.Lists;

import evaluation.EmailObject;

public class TSVEmailReader {

	/*@author: ssrivastava
	 * */
	
	public static EmailObject parseLineToEmail(String line){
		
		String[] fields = line.split("\t");
		
		String provenance = fields[0];
		ArrayList<String> categories = Lists.newArrayList(fields[1].trim());
		String subject = fields[2].trim();
		String body = fields[3].trim();
		ArrayList<String>recipients = new ArrayList<String>(Arrays.asList(fields[4].trim().split("\\s*(\\s|,)\\s*")));
		String attachment = fields[5].matches("NONE") ? "" : fields[5].trim();
		String sender = fields[6].trim();
		return new EmailObject(provenance, categories, subject, body, attachment, sender, recipients);
	}
	
	public static ArrayList<EmailObject> parseTSVFileToEmails(String fileName){
		ArrayList<EmailObject> emails = Lists.newArrayList();
		try {
			for(String line:Files.readAllLines(Paths.get(fileName))){
				emails.add(parseLineToEmail(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emails;
	}
	
	public static void main(String[] args) {
		
		//emailReader.parseLineToEmail("AGRKG3YT3KMD8	POLICY	New office policy	So, yeah, there will be a new policy at the office today. Everyone needs to make sure that TPS Cover letters are stocked at all times. That way, when we need them, they're there. Tina, make sure to tell Joe where the box is.	wanda@initech-corp.com, joe@initech-corp.com, tina@initech-corp.com, greg@initech-corp.com, asuk@initech-corp.com, dilbert@initech-corp.com	NONE	mike@initech-corp.com").displayEmail();
		ArrayList<EmailObject> emails = TSVEmailReader.parseTSVFileToEmails("/Users/shashans/Work/ConceptLearning/mturk/Emails/email_Dec20.log");
		
	}

}
