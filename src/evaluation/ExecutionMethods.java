package evaluation;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import utils.StanfordAnnotator;
import utils.Token;

import com.google.common.base.Preconditions;


public class ExecutionMethods {
	
	//Logical NOT
	public static QueryReturnValue not(EmailObject email, QueryReturnValue a ){
		System.out.println("Answer is "+(!a.booleanValue));
		return new QueryReturnValue(!a.booleanValue);
	}
	
	// Logical OR: update to preconvert lists to booleans
	public static QueryReturnValue or(EmailObject email, QueryReturnValue a, QueryReturnValue b ){
		System.out.println("Answer is "+(a.booleanValue || b.booleanValue));
		return new QueryReturnValue(a.booleanValue || b.booleanValue);
	}
	public static QueryReturnValue or(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c ){
		System.out.println("Answer is "+(a.booleanValue || b.booleanValue || c.booleanValue));
		return new QueryReturnValue(a.booleanValue || b.booleanValue || c.booleanValue);
	}
	public static QueryReturnValue or(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c, QueryReturnValue d ){
		System.out.println("Answer is "+(a.booleanValue || b.booleanValue || c.booleanValue || d.booleanValue));
		return new QueryReturnValue(a.booleanValue || b.booleanValue || c.booleanValue || d.booleanValue);
	}
	
	/*
	public static QueryReturnValue orn(EmailObject email, QueryReturnValue... a ){
		System.out.println("HERE");
		for(int i=0; i < a.length ; i++){
			Preconditions.checkState(a[i].type.matches(QueryReturnValue.TYPE_BOOLEAN));
			QueryReturnValue qv = a[i];
			if(qv.booleanValue){
				return new QueryReturnValue(true);
			}
		}
		return new QueryReturnValue(false);
	}*/
	
	// Logical AND
	public static QueryReturnValue and(EmailObject email, QueryReturnValue a, QueryReturnValue b ){
		System.out.println("Answer is "+(a.booleanValue && b.booleanValue));
		return new QueryReturnValue(a.booleanValue && b.booleanValue);
	}	
	public static QueryReturnValue and(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c ){
		System.out.println("Answer is "+(a.booleanValue && b.booleanValue && c.booleanValue));
		return new QueryReturnValue(a.booleanValue && b.booleanValue && c.booleanValue);
	}
	public static QueryReturnValue and(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c, QueryReturnValue d ){
		System.out.println("Answer is "+(a.booleanValue && b.booleanValue && c.booleanValue && d.booleanValue));
		return new QueryReturnValue(a.booleanValue && b.booleanValue && c.booleanValue && d.booleanValue);
	}
	
	//stringVal
	public static QueryReturnValue stringVal(EmailObject email, QueryReturnValue a){
		Preconditions.checkState(a.type.matches(QueryReturnValue.TYPE_STRING));
		return a;
	}
			
	// stringEquals
	public static QueryReturnValue stringEquals(EmailObject email, QueryReturnValue a, QueryReturnValue b){

		System.out.println(a.type);
		Preconditions.checkState( 
				(a.type.matches(QueryReturnValue.TYPE_STRING) || a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
			 && (b.type.matches(QueryReturnValue.TYPE_STRING) || b.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
		);

		String[] textFields1 = a.getText().split("\\|");
		String[] textFields2 = b.getText().split("\\|");
		
		for(String text1:textFields1){
			for(String text2:textFields2){
				if(text1.toLowerCase().equals(text2.toLowerCase())){
					System.out.println("string equality between "+text1 + " and "+text2);
					return new QueryReturnValue(true);
				}
			}
		}	
		return new QueryReturnValue(false);
	}
	
	// stringMatch
	public static QueryReturnValue stringMatch(EmailObject email, QueryReturnValue a, QueryReturnValue b){

		Preconditions.checkState( 
				(a.type.matches(QueryReturnValue.TYPE_STRING) || a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
			 && (b.type.matches(QueryReturnValue.TYPE_STRING) || b.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
		);

		String[] textFields1 = a.getText().split("\\|");
		String[] textFields2 = b.getText().split("\\|");
		
		for(String text1:textFields1){
			for(String text2:textFields2){
				if(text1.toLowerCase().contains(text2.toLowerCase()) || text2.toLowerCase().contains(text1.toLowerCase())){
					if(text1.length()+text2.length()>0){
					System.out.println("string match between "+text1 + " and "+text2);
					return new QueryReturnValue(true);
					}
				}
			}
		}
		return new QueryReturnValue(false);
	}
	
	// getPhraseMention is currently implemented exactly the same as stringMatch.
	public static QueryReturnValue getPhraseMention(EmailObject email, QueryReturnValue a, QueryReturnValue b){

		Preconditions.checkState( 
				(a.type.matches(QueryReturnValue.TYPE_STRING) || a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
			 && (b.type.matches(QueryReturnValue.TYPE_STRING) || b.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
		);

		String[] textFields1 = a.getText().split("\\|");
		String[] textFields2 = b.getText().split("\\|");
		
		for(String text1:textFields1){
			for(String text2:textFields2){
				if(text1.toLowerCase().contains(text2.toLowerCase()) || text2.toLowerCase().contains(text1.toLowerCase())){
					System.out.println("string match between "+text1 + " and "+text2);
					return new QueryReturnValue(true);
				}
			}
		}
		return new QueryReturnValue(false);
	}
	
	// getPhrasesLike: to be udpated to incorporate lexical semantic resources
	public static QueryReturnValue getPhrasesLike(EmailObject email, QueryReturnValue a, QueryReturnValue b){

		Preconditions.checkState( 
				(a.type.matches(QueryReturnValue.TYPE_STRING) || a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
			 && (b.type.matches(QueryReturnValue.TYPE_STRING) || b.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT)) 
		);

		String[] textFields1 = a.getText().split("\\|");
		String[] textFields2 = b.getText().split("\\|");
		
		for(String text1:textFields1){
			for(String text2:textFields2){
				if(text1.toLowerCase().contains(text2.toLowerCase()) || text2.toLowerCase().contains(text1.toLowerCase())){
					System.out.println("string match between "+text1 + " and "+text2);
					return new QueryReturnValue(true);
				}
			}
		}
		return new QueryReturnValue(false);
	}
	
	// getCatMentions: UPDATE
	public static QueryReturnValue getCatInstance(EmailObject email, QueryReturnValue a, QueryReturnValue b){

		//here a is a target text
		Preconditions.checkState( 
				(a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT) || a.type.startsWith(QueryReturnValue.TYPE_STRING)) 
			 && (b.type.matches(QueryReturnValue.TYPE_NER) || b.type.startsWith(QueryReturnValue.TYPE_POS) || b.type.startsWith(QueryReturnValue.TYPE_STRING)) 
		);

		String[] targetTexts = a.getText().split("\\|");
		String[] targetPhrases = b.getText().split("\\|");
		
		for(String catName:targetPhrases){

			for(String text:targetTexts){
			
				if(StringUtils.isAllUpperCase(catName)){
					ArrayList<Token> tokens = StanfordAnnotator.annotate(text);
					for(Token token:tokens){
						//token.display();
						if(token.getNer().equals(catName) || token.getPos().equals(catName) || token.getPos().equals(catName)){
							System.out.println("Category match for "+catName+" at token : "+token.getSurfaceForm());
							return new QueryReturnValue(true);
						}else if(catName.startsWith("PHONE") && token.getSurfaceForm().matches("^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$")){
							System.out.println("Category match for "+catName+" at token : "+token.getSurfaceForm());
							return new QueryReturnValue(true);
						}else if(catName.startsWith("EMAIL") && token.getSurfaceForm().matches(".+\\@.+")){
							System.out.println("Category match for "+catName+" at token : "+token.getSurfaceForm());
							return new QueryReturnValue(true);
						}else if(catName.startsWith("URL") && token.getSurfaceForm().matches("(http:.*)|(www.*)|(.*\\.com)")){
							System.out.println("Category match for "+catName+" at token : "+token.getSurfaceForm());
							return new QueryReturnValue(true);
						}
					}
				}else{
					if(catName.toLowerCase().contains(text.toLowerCase()) || text.toLowerCase().contains(catName.toLowerCase())){
						System.out.println("Category match between targetText: "+text + " and target phrase: "+catName);
						return new QueryReturnValue(true);
					}
				}
			}
		}
		return new QueryReturnValue(false);
	}
	
	// Merge
	public static QueryReturnValue merge(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		// Input is two instances of QueryReturnValue with type string or targettext. :s/:t/:p
		return new QueryReturnValue(Arrays.asList(a.getText(), b.getText()));
	}
	
	public static QueryReturnValue merge(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c){
		return new QueryReturnValue(Arrays.asList(a.getText(), b.getText(), c.getText()));
	}
	
	public static QueryReturnValue merge(EmailObject email, QueryReturnValue a, QueryReturnValue b, QueryReturnValue c, QueryReturnValue d){
		return new QueryReturnValue(Arrays.asList(a.getText(), b.getText(), c.getText(), d.getText()));
	}
	
	//len
	public static QueryReturnValue len(EmailObject email, QueryReturnValue a){
		Preconditions.checkState(a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT));
		int val;	
		if(a.type.startsWith(QueryReturnValue.TYPE_TARGETTEXT_RECIPIENT)){
			val = email.recipients.size();
		}else{
			val = StanfordAnnotator.annotate(a.getText()).size();
		}
		
		System.out.println("Answer is "+val);
		return new QueryReturnValue(val);
	}
	
	// greaterThan
	public static QueryReturnValue greaterThan(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		return new QueryReturnValue(a.intValue >= b.intValue);
	}
	
	// equals: arithmetic
	public static QueryReturnValue equals(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		return new QueryReturnValue(a.intValue == b.intValue);
	}
	
	// beginWith: Update later
	public static QueryReturnValue beginWith(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		return new QueryReturnValue(false);
	}
	public static QueryReturnValue endWith(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		return new QueryReturnValue(false);
	}
	
	//before: UPDATE (always returns false for now)
	public static QueryReturnValue before(EmailObject email, QueryReturnValue a, QueryReturnValue b){
		return new QueryReturnValue(false);
	}
	
	//unknown: always return false
	public static QueryReturnValue unknown(EmailObject email){
		return new QueryReturnValue(false);
	}
	
}
