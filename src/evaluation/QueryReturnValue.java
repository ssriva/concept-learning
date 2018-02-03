package evaluation;

import java.util.List;

import com.google.common.base.Preconditions;


public class QueryReturnValue {

	final public static String TYPE_BOOLEAN = "BOOLEAN";
	final public static String TYPE_STRING = "STRING";
	final public static String TYPE_POS = "POS";
	final public static String TYPE_NER = "NER";
	
	final public static String TYPE_INTEGER = "INTEGER";
	
	final public static String TYPE_TARGETTEXT="TARGETTEXT";
	final public static String TYPE_TARGETTEXT_SUBJECT = "TARGETTEXT.SUBJECT";
	final public static String TYPE_TARGETTEXT_BODY = "TARGETTEXT.BODY";
	final public static String TYPE_TARGETTEXT_SENDER = "TARGETTEXT.SENDER";
	final public static String TYPE_TARGETTEXT_ATTACHMENT = "TARGETTEXT.ATTACHMENT";
	final public static String TYPE_TARGETTEXT_RECIPIENT = "TARGETTEXT.RECIPIENT";
	final public static String TYPE_TARGETTEXT_EMAIL = "TARGETTEXT.EMAIL";
		
	/**
	 * Author: ssrivastava
	 * Class that subsumes different types of Objects 
	 * involved in evaluating a query on an email
	 * 1. predicates names
	 * 2. constants
	 * 3. Boolean values
	 * 4. Lists of tokens 
	 **/
	
	String type;
	
	EmailObject email;
	String stringValue;
	public Boolean booleanValue;
	int intValue;
	
	public QueryReturnValue(EmailObject emailObj, String str) {
		
		this.email=emailObj;
		
		if(str.endsWith(":s") || str.startsWith("null")){
			this.type = QueryReturnValue.TYPE_STRING;
			this.stringValue = str.startsWith("null") ? "" : str.substring(0, str.lastIndexOf(":s")).replaceAll("_", " ");
		}
		else if(str.startsWith("\"") && str.endsWith("\"")){
			this.type = QueryReturnValue.TYPE_STRING;
			this.stringValue = str.substring(1, str.length()-1);
		}
		else if(str.equals("STAR")){
			this.type = QueryReturnValue.TYPE_STRING;
			this.stringValue = "";
		}
		else if(str.endsWith(":p")){
			this.type = QueryReturnValue.TYPE_STRING;
			if(str.startsWith("exclamation")){
				this.stringValue = "!";
			}else if(str.startsWith("question")){
				this.stringValue = "?";
			}
		}
		else if(str.endsWith(":ne")){
			this.type = QueryReturnValue.TYPE_NER;
			this.stringValue = str.substring(0, str.lastIndexOf(":ne"));
		}
		else if(str.endsWith(":pos")){
			this.type = QueryReturnValue.TYPE_POS;
			this.stringValue = str.substring(0, str.lastIndexOf(":pos"));
		}
		else if(str.endsWith(":num")){
			this.type = QueryReturnValue.TYPE_INTEGER;
			this.intValue = Integer.parseInt(str.substring(0, str.lastIndexOf(":num")));
		}
		else if(str.endsWith(":tt")){
			if(str.startsWith("subject")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_SUBJECT;
			}
			else if(str.startsWith("body")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_BODY;
			}
			else if(str.startsWith("sender")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_SENDER;
			}
			else if(str.startsWith("attachment")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_ATTACHMENT;
			}
			else if(str.startsWith("recipient")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_RECIPIENT;
			}
			else if(str.startsWith("email")){
				this.type = QueryReturnValue.TYPE_TARGETTEXT_EMAIL;
			}		
		}else{
			System.err.println("Shouldn't be here. "+str);
			System.exit(0);
		}
		
		System.out.println("Creating new instance of class QueryReturnValue for string "+str+", and setting type to :"+type);
	}
	
	public QueryReturnValue(Boolean bool) {
		this.type = QueryReturnValue.TYPE_BOOLEAN;
		this.booleanValue = bool;
	}
	
	public QueryReturnValue(int val){
		this.type = QueryReturnValue.TYPE_INTEGER;
		this.intValue = val;
	}
	
	public QueryReturnValue(List<String> list){
		this.type = QueryReturnValue.TYPE_STRING;
		this.stringValue = String.join("|", list);
	}
	
	public String getText(){
		
		Preconditions.checkState(type.matches(TYPE_STRING)||type.startsWith(TYPE_TARGETTEXT)||type.startsWith(TYPE_NER)||type.startsWith(TYPE_POS));

		String text=null;
		if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_ATTACHMENT)){
			text = email.attachment;
		}else if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_BODY)){
			text = email.body;
		}else if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_SENDER)){
			text = email.sender;
		}else if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_SUBJECT)){
			text = email.subject;
		}else if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_RECIPIENT)){
			text = String.join("|", email.recipients);
		}else if(this.type.matches(QueryReturnValue.TYPE_TARGETTEXT_EMAIL)){
			text = email.subject+"|"+email.body+"|"+email.attachment;
		}else if(this.type.matches(QueryReturnValue.TYPE_STRING)){
			text = stringValue;
		}else if(this.type.matches(QueryReturnValue.TYPE_NER)){
			text = stringValue;
		}else if(this.type.matches(QueryReturnValue.TYPE_POS)){
			text = stringValue;
		}else{
			System.err.println("Shouldn't be here");
			System.exit(-1);
		}
		
		this.stringValue = text;
		return text;	
	}
	 
}
