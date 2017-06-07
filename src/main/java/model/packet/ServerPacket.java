package model.packet;

import model.player.Player;

import java.util.ArrayList;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class ServerPacket extends Packet {

	private String action;
	private String keyword;
	private ArrayList<Player> players;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
}
