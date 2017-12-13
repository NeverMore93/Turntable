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
import javafx.scene.text.TextAlignment;
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
    public static volatile String filePath= null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        String buttonStyle = "-fx-background-color: cyan;-fx-font-size: 16;-fx-alignment: center;-fx-padding: 20px;-fx-min-width: 100%";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择候选人相册");
        dir = directoryChooser.showDialog(null);
        filePath = dir.getPath();
        total = dir.list().length;
        timeline = new Timeline();


        for(File file: Objects.requireNonNull(dir.listFiles())){
            image = new Image(new FileInputStream(file),300,200,true,true);
            name = file.getName().split("\\.")[0];
            images.put(name,image);
            imageList.add(image);
            nameList.add(name);
        }

        ScrollPane scrollPane = new ScrollPane();
        GridPane rootPane = new GridPane();
        rootPane.setStyle("-fx-background-color: cyan");
        GridPane candidatePane = new GridPane();
        GridPane winningPane = new GridPane();
        winningPane.setStyle("-fx-background-color: gold");

        Image emblem= new Image(new FileInputStream(new File("res/default.jpg")));
        Image start= new Image(new FileInputStream(new File("res/start.jpg")),300,200,true,true);
        VBox startVBox = picAndName.genPicAndName(start,"","-fx-padding: 0px");
        winningPane.add(picAndName.genPicAndName(emblem,"","-fx-padding: 24px"),0,0);
        winningPane.add(picAndName.genPicAndName(imageList.get(0),nameList.get(0),"-fx-padding: 24px"),0,1);
        winningPane.add(startVBox,0,2);


        for(Map.Entry<String, Image> entry :images.entrySet()){
            String key = entry.getKey();
            Image image = entry.getValue();
            candidatePane.add(picAndName.genPicAndName(image,key),index%5,index/5);
            index++;
        }

        KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
            random =(int)(Math.random() * total);
            winningPane.getChildren().remove(1);
            winningPane.add(picAndName.genPicAndName(imageList.get(random),nameList.get(random),"-fx-padding: 24px"),0,1,1,1);
        });
        timeline.setCycleCount(500);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                --winningLen;
                while(winningPane.getChildren().size()>1){
                    winningPane.getChildren().remove(1);
                }
                winningPane.add(picAndName.genPicAndName(images.get(winnings[winningLen]),winnings[winningLen],"-fx-padding: 24px"),0,1);
                winningPane.add(startVBox,0,2);
            }
        });

        winningPane.setAlignment(Pos.CENTER);

        startVBox.setOnMouseClicked(event -> {
            winningPane.getChildren().remove(2);
            timeline.play();
        });

        candidatePane.setStyle("-fx-padding: 5px 20px");
        scrollPane.setContent(candidatePane);
        rootPane.add(scrollPane,0,0);
        rootPane.add(winningPane,1,0);


        primaryStage.setTitle(projectName);
        Scene scene = new Scene(rootPane,1432,800);
        primaryStage.setResizable(false);
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
