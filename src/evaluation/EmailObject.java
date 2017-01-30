package evaluation;

import java.util.ArrayList;

public class EmailObject {

	public String provenance;
	public ArrayList<String> categories;
	
	public String emailId;
	public String subject;
	public String body;
	public String attachment;
	public String sender;
	public ArrayList<String> recipients;
	
	public EmailObject(String provenance, ArrayList<String> categories, String subject, String body, 
			String attachment, String sender, ArrayList<String> recipients ) {

		this.emailId = provenance+"|"+categories.get(0);
		this.provenance = provenance;
		this.categories = categories;
		this.subject = subject;
		this.body = body;
		this.attachment = attachment;
		this.sender = sender;
		this.recipients = recipients;
	}
	
	public void displayEmail(){
		System.out.print("Provenance: "+this.provenance+"\tCategories: "+this.categories+"\n");
		System.out.println("From: "+sender);
		for(int i=0;i<recipients.size();i++){
			System.out.println("Recipient"+(i+1)+": "+recipients.get(i));
		}
		System.out.println("Subject: "+this.subject+"\n");
		System.out.println("Body: "+this.body+"\n\nAttachment:"+this.attachment);
	}
	
}
