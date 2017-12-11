package turntable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class Main extends Application {

    private static PicAndName picAndName = new PicAndName();
    private static Map<String, Image> images = new HashMap<String, Image>();
    private static String projectName = null;
    private static String winnings[] = null;
    private static int winningLen = 0;
    private static int index = 0;
    private static int total = 0;
    private static int random = 0;
    private static File dir;
    private static List<Image> imageList= new ArrayList<>();
    private static List<String> nameList= new ArrayList<>();
    private static Image image = null;
    private static String name = null;
    private static Timeline timeline;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String buttonStyle = "-fx-background-color: cyan;-fx-padding: 20px;-fx-end-margin: 20px;-fx-start-margin: 20px;-fx-max-width: 100;-fx-min-width: 60px;-fx-font-size: 16";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择候选人相册");
        dir = directoryChooser.showDialog(null);
        total = dir.list().length;
        timeline = new Timeline();


        for(File file: Objects.requireNonNull(dir.listFiles())){
            image = new Image(new FileInputStream(file),600,400,true,true);
            name = file.getName().split("\\.")[0];
            images.put(name,image);
            imageList.add(image);
            nameList.add(name);
        }

        Button start = new Button("开始");
        start.setAlignment(Pos.CENTER);
        start.setStyle(buttonStyle);
        Button stop = new Button("结束");
        stop.setStyle(buttonStyle);
        stop.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        GridPane rootPane = new GridPane();
        rootPane.setStyle("-fx-background-color: cyan");
        GridPane candidatePane = new GridPane();
        GridPane winningPane = new GridPane();
//        rootPane.setGridLinesVisible(true);
//        candidatePane.setGridLinesVisible(true);
//        winningPane.setGridLinesVisible(true);
        winningPane.setStyle("-fx-background-color: gold");

        Image emblem= new Image(new FileInputStream(new File("res/default.jpg")));
        winningPane.add(picAndName.genPicAndName(emblem,"","-fx-padding: 24px"),0,0);
        winningPane.setAlignment(Pos.CENTER);
        winningPane.add(picAndName.genPicAndName(imageList.get(0),nameList.get(0),"-fx-padding: 24px"),0,1);


        for(Map.Entry<String, Image> entry :images.entrySet()){
            String key = entry.getKey();
            Image image = entry.getValue();
            candidatePane.add(picAndName.genPicAndName(image,key),index%4,index/4);
            index++;
        }

        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), event -> {
            random =(int)(Math.random() * total);
            winningPane.add(picAndName.genPicAndName(imageList.get(random),nameList.get(random),"-fx-padding: 24px"),0,1,1,1);
        });
        timeline.setCycleCount(10);
        timeline.getKeyFrames().add(keyFrame);

        start.setOnMouseClicked(event -> {
            start.setDisable(true);
            timeline.play();
            --winningLen;
            winningPane.add(picAndName.genPicAndName(images.get(winnings[winningLen]),winnings[winningLen],"-fx-padding: 24px"),0,1);
            start.setDisable(false);
        });

//        stop.setOnMouseClicked(event -> {
//            timeline.stop();
//            start.setDisable(false);
//            winningPane.add(picAndName.genPicAndName(images.get(winnings[winningLen]),winnings[winningLen],"-fx-padding: 24px"),0,1);
//        });

        VBox vBox = new VBox(start,stop);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        winningPane.add(vBox,0,2,1,1);

        scrollPane.setContent(candidatePane);
        rootPane.add(scrollPane,0,0);
        rootPane.add(winningPane,1,0);


        primaryStage.setTitle(projectName);
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public static void main(String[] args) throws Exception {
        preinit();
        launch(args);
    }

    static void preinit() throws Exception {
        Properties properties = new Properties();
        File file = new File("config.properties");
        InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        properties.load(reader);
        projectName = properties.getProperty("projectName");
        winnings = properties.getProperty("winning").split("\\,");
        winningLen = winnings.length;
    }



}
