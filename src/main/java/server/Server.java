package server;

import model.settings.Settings;
import model.settings.SettingsDAO;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */

public class Server implements Runnable {

	private volatile int playersOnline = 0;
	private volatile boolean isPlayerJoining = true;
	private Settings settings;
	private List<Room> roomsList = new ArrayList<>();

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		new Thread(new Server()).start();
	}

	@Override
	public void run() {
		settings = SettingsDAO.getInstance().getSettingsFromXML("settings.xml");
		System.out.println("Awaiting for connection");
		try (ServerSocket serverSocket = new ServerSocket(settings.getPort())) {
			while (true) {
				if (playersOnline % settings.getMaxPlayersInRoom() == 0 && isPlayerJoining) {
					Room room = new Room(this, serverSocket);
					roomsList.add(room);
					new Thread(room).start();
					isPlayerJoining = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void setPlayersOnline(int playersOnline) {
		this.playersOnline = playersOnline;
	}

	public synchronized int getPlayersOnline() {
		return playersOnline;
	}

	public synchronized void setPlayerJoining(boolean val) {
		this.isPlayerJoining = val;
	}

	public synchronized Settings getSettings() {return settings; }

	public synchronized List<Room> getRoomsList() {return roomsList; }
}
