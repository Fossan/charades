package model.dictionary;

import java.util.*;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class Dictionary {

	private List<String[]> dictionaryWords = new ArrayList<>();
	private List<String> dictionaryDifficultyWords = new ArrayList<>();
	private List<String> linkedDictionaryDifficultyWords;
	private int[] voteCount = new int[] {0, 0, 0};
	
	public void specifyDictionary(String difficultyLevel) {
		for (String[] single : dictionaryWords) {
			if (single[1].toUpperCase().equals(difficultyLevel)) dictionaryDifficultyWords.add(single[0]);
		}
	}
	
	public void shuffleList() {
		Collections.shuffle(dictionaryDifficultyWords, new Random(System.nanoTime()));
		linkedDictionaryDifficultyWords = new LinkedList<>();
		linkedDictionaryDifficultyWords.addAll(dictionaryDifficultyWords);
	}

	public List<String[]> getDictionaryWords() {
		return dictionaryWords;
	}

	public void setDictionaryWords(List<String[]> dictionaryWords) {
		this.dictionaryWords = dictionaryWords;
	}

	public List<String> getDictionaryDifficultyWords() {
		return dictionaryDifficultyWords;
	}

	public void setDictionaryDifficultyWords(List<String> dictionaryDifficultyWords) {
		this.dictionaryDifficultyWords = dictionaryDifficultyWords;
	}

	public synchronized int[] getVoteCount() {
		return voteCount;
	}

	public synchronized void setVoteCount(int id, int val) {
		this.voteCount[id] = val;
	}

	public List<String> getLinkedDictionaryDifficultyWords() {
		return linkedDictionaryDifficultyWords;
	}

	public void setLinkedDictionaryDifficultyWords(List<String> linkedDictionaryDifficultyWords) {
		this.linkedDictionaryDifficultyWords = linkedDictionaryDifficultyWords;
	}
}
