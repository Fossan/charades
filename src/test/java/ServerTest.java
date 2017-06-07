import model.packet.ClientPacket;
import model.player.Actions;
import model.player.Player;
import org.junit.Assert;
import org.junit.Test;
import server.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Piotr Staśkiewicz on 16.01.2017.
 */
public class ServerTest {

	@Test
	public void socketConnectionTest() throws IOException {
		new Thread(new Server()).start();
		Socket socket = new Socket("127.0.0.1", 4321);
		Assert.assertTrue(socket.isConnected());
	}

	@Test
	public void serverPlayersOnlineTest() throws IOException, InterruptedException {
		Server server = new Server();
		new Thread(server).start();
		Socket socket = new Socket("127.0.0.1", 4321);
		Thread.sleep(200);
		Assert.assertEquals(1, server.getPlayersOnline());
	}

	@Test
	public void loadSettingsTest() throws IOException {
		Server server = new Server();
		new Thread(server).start();
		Socket socket = new Socket("127.0.0.1", 4321);
		Assert.assertNotNull(server.getSettings());
	}

	@Test
	public void addPlayerToRoom() throws IOException, InterruptedException {
		Server server = new Server();
		new Thread(server).start();
		Socket client = new Socket("127.0.0.1", 4321);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
		ClientPacket clientPacket = new ClientPacket(new Player());
		clientPacket.getPlayer().setNickname("Test");
		clientPacket.getPlayer().setLastAction(Actions.CONNECTED);
		objectOutputStream.reset();
		objectOutputStream.writeObject(clientPacket);
		Thread.sleep(200);
		Assert.assertEquals(clientPacket.getPlayer().getNickname(), server.getRoomsList().get(0).getPlayers().get(0).getNickname());
	}
}
