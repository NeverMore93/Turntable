package turntable;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;

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
        text.setStyle("-fx-alignment: center");
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        ImageView imageView = new ImageView(image);
        VBox vBox = new VBox(imageView,text);
        vBox.setStyle(style==null?"-fx-padding: 8px":style);
        return vBox;
    }

    VBox genPicAndName(Image image,String name){
        return this.genPicAndName(image,name,null);
    }
}
