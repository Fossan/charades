package server;

import model.dictionary.DifficultyLevels;
import model.packet.ClientPacket;
import model.packet.ServerPacket;
import model.player.Actions;
import org.apache.commons.lang3.EnumUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by Piotr Staśkiewicz on 10.12.2016.
 */
public class PlayerHandler implements Runnable {

	private Socket socket;
	private BufferedReader in;
    private Room room;
    private Server server;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private ClientPacket packet;
	private ServerPacket serverPacket = new ServerPacket();
	private boolean hasVoted = false;

	@Override
	public void run() {
		try {
			server.setPlayersOnline(server.getPlayersOnline() + 1);
			room.setPlayersConnected(room.getPlayersConnected() + 1);
			server.setPlayerJoining(true);
			System.out.println("player connected");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			room.getObjectOutputStreams().add(objectOutputStream);
			while (true) {
				packet = (ClientPacket) objectInputStream.readObject();
				switch (packet.getPlayer().getLastAction()) {
					case(Actions.CHAT):
						if (!packet.getChatText().getMessage().isEmpty()) {
							if (packet.getChatText().getMessage().toLowerCase().equals(room.getCurrentKeyword())) {
								for (ObjectOutputStream oos : room.getObjectOutputStreams()) {
									serverPacket.setAction(Actions.CHAT);
									serverPacket.setChatText(packet.getChatText());
									oos.reset();
									oos.writeObject(serverPacket);
								}
								room.setLastWinnerNickname(packet.getPlayer().getNickname());
								room.setCorrectAnswer(true);
								break;
							}
							for (ObjectOutputStream oos : room.getObjectOutputStreams()) {
								serverPacket.setAction(Actions.CHAT);
								serverPacket.setChatText(packet.getChatText());
								if (EnumUtils.isValidEnum(DifficultyLevels.class, packet.getChatText().getMessage().toUpperCase()) && room.isVotePhase() && !hasVoted) {
									int singleVote = room.getDictionary().getVoteCount()[DifficultyLevels.getDifficultyId(packet.getChatText().getMessage().toUpperCase())] + 1;
									room.getDictionary().setVoteCount(DifficultyLevels.getDifficultyId(packet.getChatText().getMessage().toUpperCase()), singleVote);
									hasVoted = true;
								}

								oos.reset();
								oos.writeObject(serverPacket);
							}
						}
						break;
					case(Actions.READY):
						if (packet.getPlayer().isReady()) room.setPlayersReady(room.getPlayersReady() + 1);
						else room.setPlayersReady(room.getPlayersReady() - 1);
						break;
					case(Actions.IMAGE):
						for (ObjectOutputStream oos : room.getObjectOutputStreams()) {
							if (oos.equals(objectOutputStream)) continue;
							serverPacket.setAction(Actions.IMAGE);
							serverPacket.setImage(packet.getImage());
							oos.reset();
							oos.writeObject(serverPacket);
						}
						break;
					case(Actions.CONNECTED):
						room.getPlayers().add(packet.getPlayer());
						break;
					}


			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public PlayerHandler(Server server, Room room, Socket socket) {
		this.socket = socket;
		this.room = room;
		this.server = server;
	}
}
