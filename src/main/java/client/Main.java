package client;

import controller.LoginFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginForm.fxml"));
        Parent root = loader.load();
        LoginFormController controller = (LoginFormController) loader.getController();
        controller.setPrevStage(primaryStage);
        primaryStage.setTitle("Kalambury");
        primaryStage.setScene(new Scene(root, 643, 266));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
