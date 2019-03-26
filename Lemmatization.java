import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Lemmatization {

	protected StanfordCoreNLP pipeline;
	
	public Lemmatization() {
		Properties props;
		props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		this.pipeline = new StanfordCoreNLP(props);
	}
	
	public List<String> lemmatize(String string){
		List<String> lemmas = new LinkedList<String>();
		
		Annotation document = new Annotation(string);
		this.pipeline.annotate(document);
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			for(CoreLabel token: sentence.get(TokensAnnotation.class)) {
				lemmas.add(token.getString(LemmaAnnotation.class));

			}
		}
		return lemmas;
	}
	
	public static void main(String[] args) {
		
		String myDirectoryPath = "C:\\Users\\alexk\\OneDrive\\Documents\\Spring 2019\\Big Data Science\\HW2\\dataset3\\dataset_3\\data\\C1\\";
		File dir = new File(myDirectoryPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String words = createString(child);
				Lemmatization slem = new Lemmatization();
				System.out.println(slem.lemmatize(words));
			}
		}
	}
	
	public static String createString(File child) {
		String content = null;
		try {
			content = new Scanner(child).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(NoSuchElementException e) {
			return "NULL";
		}
		return content;
	}
}