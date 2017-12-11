package turntable;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
    private static GridPane winningPane = new GridPane();
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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择候选人相册");
        dir = directoryChooser.showDialog(null);
        total = dir.list().length;
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        for(File file: Objects.requireNonNull(dir.listFiles())){
            image = new Image(new FileInputStream(file));
            name = file.getName().split("\\.")[0];
            images.put(name,image);
            imageList.add(image);
            nameList.add(name);
        }

        Button start = new Button("开始");
        start.setAlignment(Pos.CENTER);
        start.setStyle("-fx-background-color: cyan");
        Button stop = new Button("结束");
        stop.setStyle("-fx-background-color: cyan");
        stop.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        GridPane rootPane = new GridPane();
        GridPane candidatePane = new GridPane();
        winningPane.setGridLinesVisible(true);
        winningPane.setStyle("-fx-background-color: gold");
        ImageView emblem = new ImageView(new Image(new FileInputStream(new File("res/default.jpg"))));
        winningPane.add(emblem,0,0);
        winningPane.add(picAndName.genPicAndName(imageList.get(0),nameList.get(0),"-fx-padding: 24px"),0,1);
        rootPane.setGridLinesVisible(true);
        candidatePane.setGridLinesVisible(true);

        rootPane.setStyle("-fx-background-color: cyan");


        for(Map.Entry<String, Image> entry :images.entrySet()){
            String key = entry.getKey();
            Image image = entry.getValue();
            candidatePane.add(picAndName.genPicAndName(image,key),index%5,index/5);
            index++;
        }

        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), event -> {
            random =(int)(Math.random() * total);
            winningPane.add(picAndName.genPicAndName(imageList.get(random),nameList.get(random),"-fx-padding: 24px"),0,1);
        });
        timeline.getKeyFrames().add(keyFrame);

        start.setOnMouseClicked(event -> {
            for(int i=0;i<10;i++){
                timeline.play();
            }
        });

        stop.setOnMouseClicked(event -> {
            timeline.stop();
            winningLen-=1;
            winningPane.add(picAndName.genPicAndName(images.get(winnings[winningLen]),winnings[winningLen],"-fx-padding: 24px"),0,1);
        });

        VBox vBox = new VBox(start,stop);
        vBox.setAlignment(Pos.CENTER);
        winningPane.add(vBox,0,2);
        rootPane.add(candidatePane,0,0);
        rootPane.add(winningPane,1,0);
        scrollPane.setContent(rootPane);
        primaryStage.setTitle(projectName);
        Scene scene = new Scene(scrollPane,600,550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public static void main(String[] args) throws IOException {


        Properties properties = new Properties();
        File file = new File("config.properties");
        InputStream inputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        properties.load(reader);
        projectName = properties.getProperty("projectName");
        winnings = properties.getProperty("winning").split("\\,");
        winningLen = winnings.length;
        launch(args);
    }
}
