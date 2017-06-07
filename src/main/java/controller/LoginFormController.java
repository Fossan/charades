package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Piotr Staśkiewicz on 09.12.2016.
 */
public class LoginFormController {
	
	Stage prevStage;
	 
	public void setPrevStage(Stage stage) {
		this.prevStage = stage;
	}

	@FXML
	private TextField textField;

	@FXML
	private Button submitButton;

	@FXML
	private void handleTextEnter(KeyEvent event) throws IOException {
		if(event.getCode() == KeyCode.ENTER) {
			goFurther();
		}
	}

	@FXML
	private void handleSubmitButton(MouseEvent event) throws IOException {
		if (!textField.getText().isEmpty()) {
			goFurther();
		}
	}
	
	@FXML
	private void initialize() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textField.requestFocus();
			}
		});
	}
	
	private void goFurther() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainWindow.fxml"));
		Pane myPane = loader.load();
		MainWindowController controller = (MainWindowController) loader.getController();
		controller.getPlayer().setNickname(textField.getText());
		Stage stage = new Stage();
		stage.setTitle("Kalambury");
		Scene scene = new Scene(myPane);
		stage.setScene(scene);
		prevStage.close();
		stage.show();
	}

}
