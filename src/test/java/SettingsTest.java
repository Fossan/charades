import model.settings.Settings;
import model.settings.SettingsDAO;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Piotr Staśkiewicz on 16.01.2017.
 */
public class SettingsTest {

	@Test
	public void getSettingsFromXMLTest() {
		Settings settings = SettingsDAO.getInstance().getSettingsFromXML("settings.xml");
		Assert.assertEquals(15, settings.getVotePhaseDuration());
		Assert.assertEquals(3, settings.getMaxPlayersInRoom());
		Assert.assertEquals(60, settings.getMaxScore());
		Assert.assertEquals(4321, settings.getPort());
		Assert.assertEquals("127.0.0.1", settings.getIp());
		Assert.assertEquals("test_keywords", settings.getDictionary());
		Assert.assertEquals(20, settings.getIncreaseScoreBy());
	}
}
