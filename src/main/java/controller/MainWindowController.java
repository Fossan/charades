package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.image.SerializableImage;
import model.packet.ClientPacket;
import model.packet.ServerPacket;
import model.player.Actions;
import model.player.Player;
import model.settings.Settings;
import model.settings.SettingsDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class MainWindowController {

	private Player player = new Player();
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private ClientPacket clientPacket;
	private ServerPacket serverPacket;
	private Socket serverSocket;
	private SerializableImage image = new SerializableImage();

	@FXML
	private Canvas canvas;

	@FXML
	private TextField textField;

	@FXML
	private TextArea chat;

	@FXML
	private ToggleButton startButton;

	@FXML
	private Label keyword;

	@FXML
	private Label staticLabel;

	@FXML
	private TableView<Player> playerTableView;

	@FXML
	private TableColumn<Player, String> nicknameColumn;

	@FXML
	private TableColumn<Player, Integer> scoreColumn;

	private GraphicsContext graphicsContext;

	@FXML
	private void handleEnterButton(KeyEvent event) throws IOException {
		objectOutputStream.reset();
		if(!textField.getText().isEmpty() && event.getCode() == KeyCode.ENTER) {
			clientPacket.getChatText().setMessage(textField.getText());
			clientPacket.getPlayer().setLastAction(Actions.CHAT);
			objectOutputStream.writeObject(clientPacket);
			textField.clear();
		}
	}

	@FXML
	private void handleMouseDragged(MouseEvent event) {
		graphicsContext.lineTo(event.getX(), event.getY());
		graphicsContext.stroke();
	}

	@FXML
	private void handleMousePressed(MouseEvent event) {
		graphicsContext.beginPath();
		graphicsContext.moveTo(event.getX(), event.getY());
		graphicsContext.stroke();
	}

	@FXML
	private void handleMouseReleased(MouseEvent event) throws IOException {
		if (player.isDrawing()) {
			image.imageToByteArray(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
			clientPacket.setImage(image);
			clientPacket.getPlayer().setLastAction(Actions.IMAGE);
			objectOutputStream.reset();
			objectOutputStream.writeObject(clientPacket);
		}
	}

	@FXML
	private void handleStartButton(MouseEvent event) throws IOException {
		objectOutputStream.reset();

		if (startButton.isSelected()) {
			startButton.setSelected(true);
			clientPacket.getPlayer().setReady(true);
		}
		else if (!startButton.isSelected()){
			startButton.setSelected(false);
			clientPacket.getPlayer().setReady(false);
		}
		clientPacket.getPlayer().setLastAction(Actions.READY);
		objectOutputStream.writeObject(clientPacket);
	}

	@FXML
	private void initialize() throws IOException {

		Platform.runLater(() -> {
			try {
				Settings settings = SettingsDAO.getInstance().getSettingsFromXML("settings.xml");
				serverSocket = new Socket(settings.getIp(), settings.getPort());
				clientPacket = new ClientPacket(player);
				objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());
				objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
				clientPacket.getChatText().setNickname(player.getNickname());
				clientPacket.getPlayer().setLastAction(Actions.CONNECTED);
				objectOutputStream.writeObject(clientPacket);
				listenFromServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		initUI();
	}

	private void initUI() {
		graphicsContext = canvas.getGraphicsContext2D();
		initDraw(graphicsContext);
		chat.setEditable(false);
		chat.setWrapText(true);
		playerTableView.setEditable(false);
		playerTableView.setVisible(false);
		keyword.setVisible(false);
		canvas.setDisable(true);
		staticLabel.setVisible(false);
		startButton.setSelected(false);
		nicknameColumn.setSortable(false);
		nicknameColumn.setResizable(false);
		scoreColumn.setSortable(false);
		scoreColumn.setResizable(false);
	}

	private void initDraw(GraphicsContext gc){
		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(5);

		gc.fill();
		gc.fillRect(0,0, canvasWidth,canvasHeight);
		gc.strokeRect(0, 0, canvasWidth, canvasHeight);

		gc.setStroke(Color.BLUE);
		gc.setLineWidth(1);

	}

	private void listenFromServer() {
		new Thread(() -> {
			try {
				while (true) {
					serverPacket = (ServerPacket) objectInputStream.readObject();
					switch(serverPacket.getAction()) {
						case(Actions.CHAT):
							Platform.runLater(() -> chat.appendText(serverPacket.getChatText()+"\n"));
							break;
						case(Actions.IMAGE):
							graphicsContext.drawImage(SwingFXUtils.toFXImage(serverPacket.getImage().byteArrayToImage(), null), 0, 0);
							break;
						case(Actions.BEGIN_GAME):
							playerTableView.setVisible(true);
							startButton.setVisible(false);
							nicknameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("nickname"));
							scoreColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
							Platform.runLater(() -> playerTableView.setItems(FXCollections.observableArrayList(serverPacket.getPlayers())));
							break;
						case(Actions.DRAW):
							keyword.setVisible(true);
							player.setDrawing(true);
							Platform.runLater(() -> keyword.setText(serverPacket.getKeyword()));
							staticLabel.setVisible(true);
							textField.setEditable(false);
							canvas.setDisable(false);
							break;
						case(Actions.END_OF_DRAW):
							keyword.setVisible(false);
							player.setDrawing(false);
							staticLabel.setVisible(false);
							textField.setEditable(true);
							canvas.setDisable(true);
							break;
						case(Actions.CLEAR_CANVAS):
							graphicsContext.clearRect(0,0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
							initDraw(graphicsContext);
							break;
						case(Actions.UPDATE_PLAYERS):
							Platform.runLater(() -> playerTableView.setItems(FXCollections.observableArrayList(serverPacket.getPlayers())));
							break;
					}
				}
			} catch (SocketException e) {}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
