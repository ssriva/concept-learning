package test;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.jayantkrish.jklol.ccg.lambda2.Expression2;

import utils.EnumLists;
import utils.TSVEmailReader;
import utils.TSVStatementReader;
import utils.Utilities;
import utils.WekaExperimenter;
import evaluation.EmailObject;
import evaluation.LogicalExpressionExecutor;
import evaluation.Statement;

public class GoldParsesExperiment {
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	static HashMap<String,String> goldParses = new HashMap<String, String>();
	static{
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("data/logicalForms.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i=0; i< lines.size()-1;i++){
			if(lines.get(i).trim().length()>0 && lines.get(i+1).trim().startsWith("(")){
				goldParses.put(Utilities.getStandardizedKey(lines.get(i)), lines.get(i+1).trim());
			}
		}
	}

	public void run() throws Exception{
		
		// For each category, read emails and statements. Keep unique statements only.
		for(String category:EnumLists.categories){
			
			List<EmailObject> emails = TSVEmailReader.parseTSVFileToEmails("data/emails.dataset"); //.stream().filter( e -> e.categories.get(0).equals(category)).collect(Collectors.toList());
			
			//emails.addAll(TSVEmailReader.parseTSVFileToEmails("/Users/shashans/Work/ConceptLearning/mturk/Emails/email_Dec20.log").stream().filter( e -> (Math.random()<=1.0)).collect(Collectors.toList()));
			List<Statement> statements = TSVStatementReader.parseTSVFileToStatements("data/evaluation_email_Dec23.sorted.txt", 2).stream().filter( s -> s.category.equals(category)).collect(Collectors.toList()).stream().filter(distinctByKey(s -> s.statement)).collect(Collectors.toList());

			// Get parses for statements
			for(Statement s:statements){
				String lf = goldParses.containsKey(Utilities.getStandardizedKey(s.statement)) ? goldParses.get(Utilities.getStandardizedKey(s.statement)) : "( unknown )";
				if(lf.contains("unknown")){
					System.out.println("NO PARSE FOUND FOR:"+s.statement);
				}
				s.setLogicalForm(lf);
			}
			System.exit(0);
			
			// Evaluate each email on each statement, and print Fvec to a file.
			ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>();
			for(EmailObject email:emails){
				ArrayList<Integer> fvec = new ArrayList<Integer>();
				for(Statement statement:statements){
					System.out.println("NOW EXECUTING " + statement.statement+statement.getLogicalForm());
					Expression2 expression = LogicalExpressionExecutor.getExpressionFromString(statement.getLogicalForm());
					fvec.add(LogicalExpressionExecutor.evaluateOnEmail(expression, email).booleanValue ? 1:0);
				}				
				fvec.add(email.categories.get(0).equals(category)? 1:0 );
				data.add(fvec);
			}
			String fileName = "data/"+category+"_features.arff";
			writeToArff(fileName, data, statements);
			WekaExperimenter.runExperiment(fileName, 0.2, 10);
			//WekaExperimenter.runBowExperiments(emails, 0.2, 10, category);
			//WekaExperimenter.runJointExperiment(emails, 0.2, 10, category, fileName);
			
		}	
		
		
	}
	
	private void writeToArff(String fileName, ArrayList<ArrayList<Integer>> data, List<Statement> statements) {
		try {
			PrintWriter writer = new PrintWriter(fileName);
			writer.println("@relation "+fileName);
			for(Statement statement:statements){
				writer.println("@attribute "+Utilities.getStandardizedKey(statement.statement).replaceAll("\"", "").replaceAll(",", "").replaceAll("'", "")+" numeric");
			}
			writer.println("@attribute label {POSITIVE,NEGATIVE}");
			writer.println("@data");
			for(ArrayList<Integer>fvec:data){
				String label = ( fvec.get(fvec.size()-1) == 1 ) ? "POSITIVE":"NEGATIVE";
				writer.println(StringUtils.join(fvec.subList(0, fvec.size()-1), ",")+","+ label);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public static void main(String[] args) {
		try {
			new GoldParsesExperiment().run();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

}
