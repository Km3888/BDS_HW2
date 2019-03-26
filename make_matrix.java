import java.io.*;
import java.util.*;



//The only method you need to worry about here is get_matrix, I left some specifications in there,
//If you follow those then call that method you'll (hopefully) get the required matrix.
// You'll then want to call the tf-idf function the matrix you've gotten and then you'll be money
public class make_matrix{
	public static List<String> get_vocab(Map<String, Integer> freq_dict,int max_size) {
		Map<Integer,List> freq2word=new HashMap<Integer,List>();
		Object[] keys =freq_dict.keySet().toArray();
		List<Integer> unique_freqs=new ArrayList<Integer>();		
		//make a dictionary which maps each frequency to  list of words
		//which have that frequency
		for (int i=0;i<keys.length;i++) {
			String key= (String) keys[i];
			Integer freq=freq_dict.get(key);
			if (freq2word.containsKey(freq)){
				List<String> lists_of_freq=freq2word.get(freq);
				lists_of_freq.add(key);
				freq2word.put(freq, lists_of_freq);
			}
			else {
				unique_freqs.add(freq);
				List<String> words=new ArrayList<String>();
				words.add(key);
				freq2word.put(freq, words);
			}
		}
		Collections.sort(unique_freqs,Collections.reverseOrder());		
		//Iterate over all the unique frequencies, adding all the words with that frequency
		//Until we have our vocabulary
		List<String> WordList=new ArrayList<String>();
		int z=0;
		for (int i=0;i<unique_freqs.size();i++) {
			int freq=unique_freqs.get(i);
			List<String> words_w_freqs= freq2word.get(freq);
			for (int j=0;j<words_w_freqs.size();j++) {
				WordList.add(words_w_freqs.get(j));
				z++;
				if (WordList.size()==max_size) {
					return WordList;
				}
			}
			
		}
	return WordList;}
	public static double[][] get_matrix(int one_size,int two_size,int three_size){
		/////////THIS IS THE FUNCTION TO EDIT
		Map<Integer,Map> total_freq_bag=new Hashmap<Integer,Map>();
		//Maps integer i to a map with the frequencies of all the i-grams
		for (int i=1;i<4;i++) {
			Map<String,Integer> freq_bag=new Hashmap<String,Integer>();
			total_freq_bag.put(i, freq_bag);
			//puts the i_gram bags into the total freq bag
		}
		
		Map<Integer,Map> bagofbags=new Hashmap<Integer,Map>();
		for (int i=0;i<3;i++) {
			for (int j=0;j<8;j++) {
				String doc_path="Preprocessed"+i+"/Article0"+j;
				Map<String,Integer> bag=new Hashmap<String,Integer>();
				File f=new File(doc_path);
				String line=null;
				FileReader reader = new FileReader(f);
				BufferedReader bufferedReader = new BufferedReader(reader);
				while((line = bufferedReader.readLine()) != null) {
					int gram_type=StringUtils.countMatches(line, "_")+1;
					
					if (bag.containsKey(line)){
						bag.put(line, bag.get(line)+1);
					}
					else {
						bag.put(line, 1);
					}
					Map<String,Integer> freq_bag=total_freq_bag.get(gram_type);
					if (freq_bag.containsKey(line)){
						freq_bag.put(line, bag.get(line)+1);
					}
					else {
						freq_bag.put(line, 1);
					}
					bagofbags.put(j+3*i, bag);
				}
			}
		}
		
		
		//////EVERYTHING ABOVE THIS IS USELESS, EDIT THE ABOVE CODE SO THAT YOU GET:
		//1. A map called total_freq_bag, which maps an integer i to a map freq_map_i
		//Freq map i will map any i-gram to its total frequency within ALL the documents.
		//2. A map called bagofbags, which maps an integer -1<i<24 to a map wordbag_I
		// Wordbag_i will map any gram of any length which is in document i to the word's
		//frequency in document i
		//From there the program should be able to handle things.
		List<String> one_vocab=get_vocab(total_freq_bag.get(1),one_size);
		List<String> two_vocab=get_vocab(total_freq_bag.get(2),three_size);
		List<String> three_vocab=get_vocab(total_freq_bag.get(3),three_size);
		List<String> all_vocab=one_vocab;
		all_vocab.addAll(two_vocab);
		all_vocab.addAll(three_vocab);
		
		
		Map<Integer,String> index2word=new HashMap<Integer,String>();
		Map<String,Integer> word2index=new HashMap<String,Integer>();
		
		
		for (int i=0;i<all_vocab.size();i++) {
			String vocab_member=all_vocab.get(i);
			index2word.put(i, vocab_member);
			word2index.put(vocab_member, i);
		}
		
		double[][] matrix=new double[num_docs][vocab_size];
		for (int i=0;i<24;i++) {
			Map<String,Integer> wordbag=bagofbags.get(i);
			for(int j=0;j<all_vocab.size();j++) {
				String word=index2word.get(j);
				if(wordbag.containsKey(word)) {
					double copy=Double.valueOf(wordbag.get(word));
					matrix[i][j]= copy;
				}
				else {
					double zip=0;
					matrix[i][j]=zip;
				}
			}
		}
		return matrix;
	}
	
	public static double dot(double[] u, double[] v) {
		assert u.length==v.length;
		double total=0;
		for (int i=0;i<u.length;i++) {
			total=total+(u[i]*v[i]);
		}
		return total;
	}
	
	public static double cosine(double[] u,double[] v) {
		double val1=Math.sqrt(dot(u,u));
		double val2=Math.sqrt(dot(v,v));
		double dotted=dot(u,v);
		return 1- (dotted/(val1*val2));
	}
	public static double[][] tf_idf(double[][] matrix){
		Map<Integer,Integer> doc2total=new HashMap<Integer,Integer>();//Maps document index to total number of terms in document
		Map<Integer,Integer> word2numdocs=new HashMap<Integer,Integer>();//Maps word index to number of documents which have the word in it
		int t=matrix.length;
		int dim=matrix[0].length;
		for (int i=0;i<t;i++) {
			for (int j=0;j<dim;j++) {
				int co=(int) matrix[i][j];
				if (co!=0){
					if (word2numdocs.containsKey(j)) {
						int val=word2numdocs.get(j)+1;
						word2numdocs.put(j, val);
					}
					else {
						word2numdocs.put(j, 1);
					}
				}
				if (doc2total.containsKey(i)) {
					int curr_val=doc2total.get(i)+co;
					doc2total.put(i,curr_val);
				}
				else {
					doc2total.put(i, co);
				}
			}
		}
		double[][] tf_matrix=new double[t][dim];
		for (int i=0;i<t;i++) {
			for (int j=0;j<dim;j++) {
				double tf=matrix[i][j]/(doc2total.get(j));
				double idf_exp=t/(word2numdocs.get(i));
				double idf=Math.log(idf_exp);
				tf_matrix[i][j]=tf*idf;
			}
		}
		return tf_matrix;
		
	}
	
}