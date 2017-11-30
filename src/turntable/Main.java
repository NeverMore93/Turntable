package turntable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Properties properties = new Properties();
        File file = new File("config.properties");
        InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        properties.load(reader);

        String projectName = properties.getProperty("projectName");
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle(projectName);
        Scene scene = new Scene(root,600,550);
        scene.getStylesheets().add("src/turntable/main.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
