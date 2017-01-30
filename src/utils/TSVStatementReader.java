package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.common.collect.Lists;

import evaluation.Statement;

public class TSVStatementReader {
	
	public static ArrayList<Statement> parseTSVFileToStatements(String fileName, int threshold){
		ArrayList<Statement> statements = Lists.newArrayList();
		try {
			for(String line:Files.readAllLines(Paths.get(fileName))){
				
				String[] fields = line.split("\t");
				String provenance = fields[0].trim();
				String category = fields[1].trim();
				int quality = Integer.parseInt(fields[2].trim().substring(0, 1));
				String statement = fields[3].trim();
				
				if(quality>=threshold){
					statements.add(new Statement(provenance, category, statement, quality));
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statements;
	}

	public static void main(String[] args) {
		ArrayList<Statement> statements = TSVStatementReader.parseTSVFileToStatements(
				"/Users/shashans/Work/ConceptLearning/mturk/Statements/evaluation_email_Dec23.sorted.txt", 2);
		System.out.println(statements.size());

	}

}
