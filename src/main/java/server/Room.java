package server;

import model.dictionary.Dictionary;
import model.dictionary.DictionaryDAO;
import model.dictionary.DifficultyLevels;
import model.packet.ServerPacket;
import model.player.Actions;
import model.player.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.*;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */

public class Room implements Runnable {

	private ServerSocket socket;
	private Server server;
	private List<ObjectOutputStream> objectOutputStreams = new ArrayList<>();
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Player> sortedPlayers = new ArrayList<>();
	private ServerPacket serverPacket = new ServerPacket();
	private volatile int playersReady = 0;
	private volatile int playersConnected = 1;
	private boolean votePhase = false;
	private boolean correctAnswer = false;
	private Dictionary dictionary;
	private String currentKeyword;
	private String lastWinnerNickname;
	private boolean endOfTheGame = false;
	

	public void run() {
		System.out.println("New room!");
		serverPacket.getChatText().setNickname("Server");
		waitForBeginning();
		while (true) {
			try {
				new Thread(new PlayerHandler(server, this, socket.accept())).start();
				if (playersConnected - server.getSettings().getMaxPlayersInRoom() == 0) break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Room(Server server, ServerSocket socket) {
		this.server = server;
		this.socket = socket;
	}

	private void waitForBeginning() {
		new Thread(() -> {
			while (true) {
				if (getPlayersReady() - server.getSettings().getMaxPlayersInRoom() == 0 && playersReady != 0) {
					try {
						sendToAll(Actions.CHAT, Actions.GAME_STARTED);
						sortedPlayers.addAll(players);
						serverPacket.setPlayers(sortedPlayers);
						sendToAll(Actions.CHAT, "You have " + server.getSettings().getVotePhaseDuration() + " seconds to vote on the difficulty of this game. Type 'easy', 'medium' or 'hard'.");
						break;
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				voteOnDifficulty();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void initializeDictionary() throws IOException {
		dictionary = DictionaryDAO.getInstance().getDictionaryFromCSV(server.getSettings().getDictionary());
	}

	private void voteOnDifficulty() throws IOException, InterruptedException {
		initializeDictionary();
		votePhase = true;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0L;

		while (elapsedTime < server.getSettings().getVotePhaseDuration() * 1000) {
			elapsedTime = (new Date()).getTime() - startTime;
		}
		votePhase = false;
		int[] results = dictionary.getVoteCount();
		int highestId = 0;
		for (int i = 1; i < 3; i++) {
			if (results[i] > results[highestId]) highestId = i;
		}

		sendToAll(Actions.CHAT, Actions.VOTE_ENDED + DifficultyLevels.getDifficultyName(highestId));
		sendToAll(Actions.BEGIN_GAME, null);

		dictionary.specifyDictionary(DifficultyLevels.getDifficultyName(highestId));
		dictionary.shuffleList();
		Iterator<String> iterator = dictionary.getLinkedDictionaryDifficultyWords().iterator();
		playTheGame(iterator);
	}

	private void playTheGame(Iterator<String> iterator) throws IOException, InterruptedException {
		for (int i = 0; i < players.size(); i++) {
			if (endOfTheGame) break;
			if (iterator.hasNext()) {
				currentKeyword = iterator.next().toLowerCase();
			} else {
				dictionary.shuffleList();
				iterator = dictionary.getLinkedDictionaryDifficultyWords().iterator();
				currentKeyword = iterator.next().toLowerCase();
			}
			serverPacket.setAction(Actions.DRAW);
			serverPacket.setKeyword(currentKeyword);
			objectOutputStreams.get(i).reset();
			objectOutputStreams.get(i).writeObject(serverPacket);
			Thread.sleep(25);
			sendToAll(Actions.CHAT, "Now drawing: " + players.get(i).getNickname());
			Thread.sleep(25);
			waitForSolution();
			if (i + 1 >= players.size()) i = -1;
		}
	}

	private void sendToAll(String action, String msg) throws IOException, InterruptedException {
		for (ObjectOutputStream oos : objectOutputStreams) {
			serverPacket.setAction(action);
			serverPacket.getChatText().setMessage(msg);
			oos.reset();
			oos.writeObject(serverPacket);
		}
	}

	private void waitForSolution() throws IOException, InterruptedException {
		while (true) {
			synchronized (this) {
				if (correctAnswer) {
					players.get(searchForPlayer()).setScore(players.get(searchForPlayer()).getScore() + server.getSettings().getIncreaseScoreBy());
					updatePlayers();
					if (sortedPlayers.get(0).getScore() >= server.getSettings().getMaxScore()) {
						sendToAll(Actions.CHAT, "End of the game! The winner is: " + sortedPlayers.get(0).getNickname());
						Thread.sleep(25);
						sendToAll(Actions.UPDATE_PLAYERS, null);
						sendToAll(Actions.END_OF_DRAW, null);
						sendToAll(Actions.CLEAR_CANVAS, null);
						endOfTheGame = true;
						break;
					}
					sendToAll(Actions.UPDATE_PLAYERS, null);
					sendToAll(Actions.END_OF_DRAW, null);
					sendToAll(Actions.CLEAR_CANVAS, null);
					Thread.sleep(25);
					sendToAll(Actions.CHAT, players.get(searchForPlayer()).getNickname() + " has given correct answer!");
					correctAnswer = false;
					break;
				}
			}
		}
	}

	public Integer searchForPlayer() {
		int index = 0;
		for (Player player : players) {
			if (player.getNickname().equals(lastWinnerNickname)) {
				return index;
			}
			index++;
		}
		return null;
	}

	private void updatePlayers() {
		Collections.sort(sortedPlayers, (p1, p2) -> {
			if (p1.getScore() > p2.getScore())
				return -1;
			if (p1.getScore() < p2.getScore())
				return 1;
			return 0;
		});
	}

	public synchronized List<ObjectOutputStream> getObjectOutputStreams() {
		return objectOutputStreams;
	}

	public synchronized int getPlayersReady() {
		return playersReady;
	}

	public synchronized void setPlayersReady(Integer playersReady) {
		this.playersReady = playersReady;
	}

	public synchronized int getPlayersConnected() {
		return playersConnected;
	}

	public synchronized void setPlayersConnected(int playersConnected) {
		this.playersConnected = playersConnected;
	}

	public synchronized List<Player> getPlayers() {
		return players;
	}

	public synchronized Dictionary getDictionary() {
		return dictionary;
	}

	public synchronized boolean isVotePhase() {
		return votePhase;
	}

	public synchronized void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
	public synchronized String getCurrentKeyword() {
		return currentKeyword;
	}

	public synchronized String getLastWinnerNickname() {
		return lastWinnerNickname;
	}

	public synchronized void setLastWinnerNickname(String lastWinnerNickname) {
		this.lastWinnerNickname = lastWinnerNickname;
	}
}
