import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Full_Process {
	
	public static void main(String[] args) throws IOException {
		String myDirectoryPath = "/Users/kellymarshall/eclipse-workspace/BdsHW2/data/dataset_3/data/C1";
		File dir = new File(myDirectoryPath);
		File[] directoryListing = dir.listFiles();
		System.out.println(directoryListing[3]);
		String writeDirectoryPath = "/Users/kellymarshall/eclipse-workspace/BdsHW2/data/preprocessed/";
		File dir_pre = new File(writeDirectoryPath);
		File[] directoryPre = dir_pre.listFiles();
		if (directoryListing != null) {
			int count = 1;
			for (File child : directoryListing) {
				String words = Lemmatization.createString(child);
				if (words!="NULL") {
					Lemmatization slem = new Lemmatization();
					List<String> lem_list = slem.lemmatize(words);
					Files.write(Paths.get(writeDirectoryPath + "Article0" + Integer.toString(count) + ".txt"), lem_list);
					count++;
				}
			}
			if(directoryPre != null) {
				count = 1;
				for (File child : directoryPre) {
					if  (!child.getAbsolutePath().contains("DS_Store")){
						String[] array_words = StopWords.createArrayList(child);
						//removing stop words from array_words
						ArrayList<String> no_stop = StopWords.removeStop(array_words);
						Files.write(Paths.get(writeDirectoryPath + "Article0" + Integer.toString(count) + ".txt"), no_stop);
						count++;
					}
				}
				count = 1;
				for (File child : directoryPre) {
					int ngram_num = 1;
					int frequency = 3;
					String[] ngram_rem = StopWords.createArrayList(child);
					if (ngram_rem[0]!="NULL") {
						ArrayList<String> ngram_removed = new ArrayList(Arrays.asList(ngram_rem));
						String[] array_words = NER.nGram(ngram_num, child);
						Map <String, Integer> ngram_count = StopWords.wordCount(array_words);
						Map<Integer, ArrayList<String>> ngram_map = StopWords.countWord(ngram_count);
						NER.appendNgram(frequency,ngram_map,ngram_removed);
						Files.write(Paths.get(writeDirectoryPath + "Article0" + Integer.toString(count) + ".txt"), ngram_removed);
						count++;
						}
					System.out.println("done");
				}
			}
			//Need some external Integer Map
			//In the code above, I need to merge the files 
			//into one large NER hashmap, then pass it onto the 
			//NER to get the total set of repeating words (smart)
			
		}
	}
	
}
//Next is going to be to do this for all directories in the directory 
//clean through everything and then last part will be creating both 
//sets of methods (n-gram) method, and the NLP method

//Iterator it = ngram_map.entrySet().iterator();
//System.out.println(count);
//while (it.hasNext()) {
	//Map.Entry pair = (Map.Entry)it.next();
	//System.out.println(pair.getKey() + "=" + pair.getValue());
	//it.remove();
//}
//count++;