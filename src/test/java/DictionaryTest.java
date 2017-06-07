import model.dictionary.Dictionary;
import model.dictionary.DictionaryDAO;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Piotr Staśkiewicz on 17.01.2017.
 */
public class DictionaryTest {

	@Test
	public void loadDictionaryFromFileTest() throws IOException {
		Dictionary dictionary = DictionaryDAO.getInstance().getDictionaryFromCSV("test_keywords");
		dictionary.specifyDictionary("EASY");
		List<String> words = new ArrayList<>();
		words.add("Samochód");
		words.add("Słońce");
		words.add("Szkoła");
		words.add("Kreda");
		words.add("Straż pożarna");
		Assert.assertThat(dictionary.getDictionaryDifficultyWords(), is(words));
	}
}
