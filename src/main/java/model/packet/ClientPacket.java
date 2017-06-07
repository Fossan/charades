package model.packet;

import model.player.Player;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class ClientPacket extends Packet {

	private Player player;

	public ClientPacket(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
