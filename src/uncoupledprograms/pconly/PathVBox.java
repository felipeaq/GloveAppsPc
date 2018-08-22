package uncoupledprograms.pconly;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PathVBox extends VBox {
    private List<VBox> objects= new ArrayList<>();
    public PathVBox(int n, Color c) {
        super();
        for (int i = 0; i < n; i++) {
            VBox child = new VBox();
            VBox.setVgrow(child, Priority.ALWAYS);
            this.getChildren().add(child);
            objects.add(child);
        }
        setBackground(new Background(new BackgroundFill(c, new CornerRadii(0), new Insets(0))));
    }


    public boolean colide(Node node, Color colorToPaint) {
        if (node.getBoundsInParent().intersects(this.getBoundsInParent())) {
            for (Node child : objects) {
                if (child.localToScene(child.getBoundsInLocal()).intersects(node.localToScene(node.getBoundsInLocal()))) {
                    ((VBox) child).setBackground(new Background(new BackgroundFill(colorToPaint, new CornerRadii(0), new Insets(0))));
                    objects.remove(child);
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
