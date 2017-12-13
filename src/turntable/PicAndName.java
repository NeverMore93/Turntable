package turntable;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static turntable.Main.filePath;

public class PicAndName extends VBox {
    private Image image;
    private String name;

    public PicAndName(Image image,String name){
        this.image=image;
        this.name=name;
    }

     PicAndName(){
    }

     VBox genPicAndName(Image image,String name,String style){
        Text text = new Text(name);
        text.setStyle("-fx-alignment: center;-fx-font-size: 16;-fx-text-alignment: center;");
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        ImageView imageView = new ImageView(image);
        VBox vBox = new VBox(imageView,text);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle(style==null?"-fx-padding: 8px":style);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Desktop.getDesktop().open(new File(filePath.substring(0,filePath.length()-9)+"/候选人简历/"+name+".doc"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return vBox;
    }

    VBox genPicAndName(Image image,String name){
        return this.genPicAndName(image,name,null);
    }
}
