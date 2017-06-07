package model.settings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */

public class SettingsDAO {

	private static SettingsDAO instance;

	protected SettingsDAO(){}

	public static SettingsDAO getInstance() {
		if(instance == null) {
			synchronized (SettingsDAO.class) {
				if(instance == null) {
					instance = new SettingsDAO();
				}
			}
		}
		return instance;
	}

	public Settings getSettingsFromXML(String path) {
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (Settings) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}