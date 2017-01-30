package data;

import java.util.Arrays;
import java.util.List;

import edu.illinois.cs.cogcomp.edison.sentences.TextAnnotation; 
import edu.illinois.cs.cogcomp.wikifier.common.GlobalParameters; 
import edu.illinois.cs.cogcomp.wikifier.inference.InferenceEngine; 
import edu.illinois.cs.cogcomp.wikifier.models.LinkingProblem; 
import edu.illinois.cs.cogcomp.wikifier.models.ReferenceInstance;

public class WikiAnnotator {

    public static void main(String[] args) throws Exception {                 
    	
    	// Initialization         
    	GlobalParameters.loadConfig("configs/STAND_ALONE_NO_INFERENCE.xml");                
    	String text = "Michael Jordan was the best player in the history of the NBA.";         
    	
    	// Define the mentions of interest by their offsets into the document         
    	// These instances will contain pointers to annotation result after inference. Could be null if you don't have specific mentions in mind         
    	List<ReferenceInstance> instances = Arrays.asList(new ReferenceInstance("Michael Jordan", 0, 14));                         
    	// You can also load them from file by ReferenceInstance.loadReferenceProblem(filename).instances;         
    	// See the evaluation data for details         
    	
    	// Annotate the text with POS, Chunking and NER etc., as defined in config         
    	TextAnnotation ta = GlobalParameters.curator.getTextAnnotation(text);         
    	
    	// Construct the problem for Wikifier         
    	LinkingProblem problem = new LinkingProblem("sample",ta,instances);         
    	
    	// Actually annotate the text with Wiki links         
    	InferenceEngine engine = new InferenceEngine(false);  
    	engine.annotate(problem,instances,true,true,0.0);         
    	
    	// Now you can collect results from the variables "instances" or "problem"         
    	System.out.println(instances.get(0).getAssignedEntity().finalCandidate); 
    }

}
