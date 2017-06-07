package model.settings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */

@XmlRootElement
public class Settings {

	private String ip;
	private int port;
	private String dictionary;
	private int maxPlayersInRoom;
	private int votePhaseDuration;
	private int increaseScoreBy;
	private int maxScore;

	public String getIp() {
		return ip;
	}

	@XmlElement
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}

	public String getDictionary() {
		return dictionary;
	}

	@XmlElement
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	public int getMaxPlayersInRoom() {
		return maxPlayersInRoom;
	}

	@XmlElement
	public void setMaxPlayersInRoom(int maxPlayersInRoom) {
		this.maxPlayersInRoom = maxPlayersInRoom;
	}

	public int getVotePhaseDuration() {
		return votePhaseDuration;
	}

	@XmlElement
	public void setVotePhaseDuration(int votePhaseDuration) {
		this.votePhaseDuration = votePhaseDuration;
	}

	public int getIncreaseScoreBy() {
		return increaseScoreBy;
	}

	@XmlElement
	public void setIncreaseScoreBy(int increaseScoreBy) {
		this.increaseScoreBy = increaseScoreBy;
	}

	public int getMaxScore() {
		return maxScore;
	}

	@XmlElement
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
}
