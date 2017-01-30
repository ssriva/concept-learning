package utils;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
/*
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Paragraph2vec {

	private static final Logger log = LoggerFactory.getLogger(Paragraph2vec.class);

	public static void train(){
		/*
		try {

			TokenizerFactory t = new DefaultTokenizerFactory(); t.setTokenPreProcessor(new CommonPreprocessor());

			ParagraphVectors vec = new ParagraphVectors.Builder()
			.minWordFrequency(1)
			.iterations(5)
			.epochs(1)
			.layerSize(100)
			.learningRate(0.025)
			.labelsSource(new LabelsSource("DOC_"))
			.windowSize(5)
			.iterate(new BasicLineIterator("data/raw_sentences.txt"))
			.trainWordVectors(false)
			.vocabCache(new AbstractCache<VocabWord>())
			.tokenizerFactory(t)
			.sampling(0)
			.build();

			vec.fit();

			INDArray fvec = vec.inferVector("This is a test .");
			System.out.println(StringUtils.join(fvec, " "));
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	public static void infer(){

		/*
		try {
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new CommonPreprocessor());

			ParagraphVectors vectors;

			vectors = WordVectorSerializer.readParagraphVectors("data/simple.pv");

			vectors.setTokenizerFactory(t);
			vectors.getConfiguration().setIterations(1); // please note, we set iterations to 1 here, just to speedup inference

			INDArray inferredVectorA = vectors.inferVector("This is my world .");
			INDArray inferredVectorA2 = vectors.inferVector("This is my world .");
			INDArray inferredVectorB = vectors.inferVector("This is my way .");

			// high similarity expected here, since in underlying corpus words WAY and WORLD have really close context
			log.info("Cosine similarity A/B: {}", Transforms.cosineSim(inferredVectorA, inferredVectorB));

			// equality expected here, since inference is happening for the same sentences
			log.info("Cosine similarity A/A2: {}", Transforms.cosineSim(inferredVectorA, inferredVectorA2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public static void main(String[] args){
		infer();
	}
}
