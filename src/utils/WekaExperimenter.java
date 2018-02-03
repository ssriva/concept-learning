package utils;

import java.util.List;
import java.util.Random;

import evaluation.EmailObject;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class WekaExperimenter {


	public static void runExperiment(String arffFile, double percentage, int numSplits){

		try {
			//Load data instances
			DataSource source = new DataSource(arffFile);
			Instances data = source.getDataSet();
			data.setClassIndex(data.numAttributes() - 1);

			double avgAcc = 0.0;
			double avgF1 = 0.0;
			double avgroc = 0.0;

			for(int i=1; i<=numSplits; i++){
				//Get data split
				data.randomize(new Random());
				int trainSize = (int) Math.round(data.numInstances() * percentage);
				int testSize = data.numInstances() - trainSize;
				Instances train = new Instances(data, 0, trainSize);
				Instances test = new Instances(data, trainSize, testSize);

				//Train
				Logistic scheme = new Logistic();
				scheme.setOptions(weka.core.Utils.splitOptions(" -R 1.0E-8 -M -1 -num-decimal-places 4"));
				scheme.buildClassifier(train);

				//Test
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(scheme, test);

				avgAcc =  (avgAcc * (i-1) + eval.pctCorrect())/i;
				avgF1 =  (avgF1 * (i-1) + eval.fMeasure(0))/i;
				avgroc = (avgF1 * (i-1) + eval.areaUnderROC(0))/i;
				
			}

			System.out.println("Test results on "+arffFile);
			System.out.println("Acc:\t"+avgAcc);
			System.out.println("Fmeas:\t"+avgF1);
			System.out.println("ROC:\t"+avgroc);
			System.out.println("Attributes:\t"+data.numAttributes());
			System.out.println("Instances:\t"+data.numInstances());
			System.out.println();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runBowExperiments(List<EmailObject> emails, double percentage, int numSplits, String category){
		try{

			//Load data
			Instances data;
			FastVector classValues = new FastVector(2); classValues.addElement("POSITIVE"); classValues.addElement("NEGATIVE");
			FastVector attributes = new FastVector(2);  attributes.addElement(new Attribute("text", (FastVector) null)); attributes.addElement(new Attribute("class", classValues));
			data = new Instances("Classification", attributes, 100);
			data.setClassIndex(data.numAttributes() - 1);

			for(EmailObject email:emails){
				Instance instance = makeInstance(email.body, data);
				instance.setClassValue(email.categories.get(0).equals(category) ? "POSITIVE" :  "NEGATIVE");
				data.add(instance);
			}

			// Apply filter
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(data);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			
			Instances filteredData = Filter.useFilter(data, filter);
			
			double avgAcc = 0.0;
			double avgF1 = 0.0;
			double avgroc = 0.0;

			for(int i=1; i<=numSplits; i++){
				//Get data split
				filteredData.randomize(new Random());
				int trainSize = (int) Math.round(filteredData.numInstances() * percentage);
				int testSize = filteredData.numInstances() - trainSize;
				Instances train = new Instances(filteredData, 0, trainSize);
				Instances test = new Instances(filteredData, trainSize, testSize);

				//Train
				Classifier classifier = new Logistic();
				classifier.setOptions(weka.core.Utils.splitOptions(" -R 1.0E-8 -M -1 -num-decimal-places 4"));
				classifier.buildClassifier(train);

				//Test
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(classifier, test);

				avgAcc =  (avgAcc * (i-1) + eval.pctCorrect())/i;
				avgF1 =  (avgF1 * (i-1) + eval.fMeasure(0))/i;
				avgroc = (avgF1 * (i-1) + eval.areaUnderROC(0))/i;
			}
			
			System.out.println("BOW results on category: "+category);
			System.out.println("Acc:\t"+avgAcc);
			System.out.println("Fmeas:\t"+avgF1);
			System.out.println("ROC:\t"+avgroc);
			System.out.println("Attributes:\t"+filteredData.numAttributes());
			System.out.println("Instances:\t"+filteredData.numInstances());
			System.out.println();

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private static Instance makeInstance(String text, Instances data) {
		Instance instance = new Instance(2);
		Attribute messageAtt = data.attribute("text");
		instance.setValue(messageAtt, messageAtt.addStringValue(text));
		instance.setDataset(data);
		return instance;
	}

	public static void main(String[] args){
		int numSplits = 100; //20;
		double perc = 0.08; //0.1;
		String[] categories = {"CONTACT", "EMPLOYEE", "EVENT", "HUMOR", "MEETING", "POLICY", "REMINDER"};
		
		List<EmailObject> emails = TSVEmailReader.parseTSVFileToEmails("data/emails.dataset");
		for(String category:categories){
			runExperiment("data/"+category+"_features.arff", perc, numSplits);
			//runBowExperiments(emails, perc, numSplits, category);
			//runJointExperiment(emails, perc, numSplits, category, "data/"+category+"_features.arff");
		}

	}
	
	public static void runJointExperiment(List<EmailObject> emails, double percentage, int numSplits, String category, String arffFile){
		try{

			//Load data
			Instances data;
			FastVector classValues = new FastVector(2); classValues.addElement("POSITIVE"); classValues.addElement("NEGATIVE");
			FastVector attributes = new FastVector(2);  attributes.addElement(new Attribute("text", (FastVector) null)); attributes.addElement(new Attribute("class", classValues));
			data = new Instances("Classification", attributes, 100);
			data.setClassIndex(data.numAttributes() - 1);

			for(EmailObject email:emails){
				Instance instance = makeInstance(email.body, data);
				instance.setClassValue(email.categories.get(0).equals(category) ? "POSITIVE" :  "NEGATIVE");
				data.add(instance);
			}

			// Apply filter
			StringToWordVector filter = new StringToWordVector();
			filter.setInputFormat(data);
			filter.setTFTransform(true);
			filter.setIDFTransform(true);
			Instances filteredData1 = Filter.useFilter(data, filter);
			
			AttributeSelection filter2 = new AttributeSelection();
			filter2.setInputFormat(filteredData1);
			Instances filteredData = Filter.useFilter(filteredData1, filter2);
			
			DataSource source = new DataSource(arffFile);
			Instances data1 = source.getDataSet();
			for(int attIndex=0; attIndex<data1.numAttributes()-1;attIndex++){
				filteredData.insertAttributeAt(new Attribute("NumericFeature_"+attIndex), filteredData.numAttributes());
				for (int i = 0; i < filteredData.numInstances(); i++) {
					filteredData.instance(i).setValue(filteredData.numAttributes() - 1, data1.instance(i).value(attIndex));
			     }
			}
			
			double avgAcc = 0.0;
			double avgF1 = 0.0;
			double avgroc = 0.0;

			for(int i=1; i<=numSplits; i++){
				//Get data split
				filteredData.randomize(new Random());
				int trainSize = (int) Math.round(filteredData.numInstances() * percentage);
				int testSize = filteredData.numInstances() - trainSize;
				Instances train = new Instances(filteredData, 0, trainSize);
				Instances test = new Instances(filteredData, trainSize, testSize);

				//Train
				Classifier classifier = new Logistic();
				classifier.setOptions(weka.core.Utils.splitOptions(" -R 1.0E-8 -M -1 -num-decimal-places 4"));
				classifier.buildClassifier(train);

				//Test
				Evaluation eval = new Evaluation(train);
				eval.evaluateModel(classifier, test);

				avgAcc =  (avgAcc * (i-1) + eval.pctCorrect())/i;
				avgF1 =  (avgF1 * (i-1) + eval.fMeasure(0))/i;
				avgroc = (avgF1 * (i-1) + eval.areaUnderROC(0))/i;
			}
			
			System.out.println("Joint results on category: "+category);
			System.out.println("Acc:\t"+avgAcc);
			System.out.println("Fmeas:\t"+avgF1);
			System.out.println("ROC:\t"+avgroc);
			System.out.println("Attributes:\t"+filteredData.numAttributes());
			System.out.println("Instances:\t"+filteredData.numInstances());
			System.out.println();

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
