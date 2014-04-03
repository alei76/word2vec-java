package org.insight.word2vec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.insight.word2vec.util.VectorMath;
import org.insight.word2vec.util.WordSim;


/*
 * A Java wrapper for Word2Vec - Only Reads a pre trained model!
 */
public class Word2Vec extends HashMap<String, float[]> {

	private static final long serialVersionUID = 1L;

	/*
	 * Check if word is in vocab:
	 */
	public boolean contains(String word) {
		return this.containsKey(word);
	}

	/*
	 * Get Vector Representation of a word
	 */
	public float[] vector(String word) {
		return this.get(word);
	}


	/*
	 * Cosine Distance between 2 words:
	 */
	public Double cosineDistance(String word1, String word2) {
		if (!this.contains(word1)) {
			//System.err.println( "Out of vocab word: " + word1 );
			//word1 = "</s>";
			return Double.NaN;
		}
		if (!this.contains(word2)) {
			//System.err.println( "Out of vocab word: " + word2 );
			//word2 = "</s>";
			return Double.NaN;
		}
		return VectorMath.cosineDistance(vector(word1), vector(word2));
	}

	
	// Adding several words:
	public float[] sentenseVector(String... words) {
		List<float[]> vectors = new ArrayList<float[]>();
		for (String w : words) {
			if (this.contains(w)) {
				vectors.add(vector(w));
			}
		}
		return VectorMath.addAll(vectors);
	}
	
	
	// Linear search k Nearest Neighbour:		
	public List<String> knn(String word, int k) {
		if (!this.contains(word)) {
			System.err.println( "Out of vocab word: " + word );
			return new ArrayList<String>();
		}
		return knn(this.vector(word), k);
	}	
	
	// Used by System, tweets are vecs - together withsentenseVector
	public List<String> knn(float[] vec, int k) {
		List<WordSim> words = knn(vec, k, true);
		
		List<String> ret = new ArrayList<String>();
		for (WordSim w : words) {
				ret.add(w.getString());
		}
		return ret;
	}
	
	// Internal! but can be called to get similarities!
	public List<WordSim> knn(float[] vec, int k, boolean withScores) {
		PriorityQueue<WordSim> kSimilarWords = new PriorityQueue<WordSim>(k);
		for (Entry<String, float[]> e : this.entrySet()) {
			WordSim sim = new WordSim(e.getKey(), VectorMath.cosineDistance(vec, e.getValue()));
			kSimilarWords.add(sim);
		}
		List<WordSim> col = new ArrayList<WordSim>();
		//col.clear();
		for (int i=0; i < k; i++  ) {
				col.add(kSimilarWords.poll());		
		}
		return col;
	}
	
	
	/*
	 * Test:
	 */

	 public static void main(String[] args) {
		
	 }

}


