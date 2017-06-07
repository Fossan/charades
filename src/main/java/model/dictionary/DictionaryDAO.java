package model.dictionary;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class DictionaryDAO {

	private static DictionaryDAO instance;

	protected DictionaryDAO(){}

	public static DictionaryDAO getInstance() {
		if(instance == null) {
			synchronized (DictionaryDAO.class) {
				if(instance == null) {
					instance = new DictionaryDAO();
				}
			}
		}
		return instance;
	}

	public Dictionary getDictionaryFromCSV(String filename) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(filename + ".csv"), '|');
		Dictionary dictionary = new Dictionary();
		dictionary.setDictionaryWords(reader.readAll());
		return dictionary;
	}
}
