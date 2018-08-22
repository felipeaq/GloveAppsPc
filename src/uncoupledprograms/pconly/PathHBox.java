package uncoupledprograms.pconly;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PathHBox extends HBox {
    List<HBox> objects = new ArrayList<>();

    public PathHBox(int n, Color c) {
        super();
        for (int i = 0; i < n; i++) {
            HBox child = new HBox();
            HBox.setHgrow(child, Priority.ALWAYS);
            this.getChildren().add(child);
            objects.add(child);
        }
        setBackground(new Background(new BackgroundFill(c, new CornerRadii(0), new Insets(0))));

    }


    public boolean colide(Node node, Color colorToPaint) {
        if (node.getBoundsInParent().intersects(this.getBoundsInParent())) {
            for (Node child : this.getChildren()) {
                if (child.localToScene(child.getBoundsInLocal()).intersects(node.localToScene(node.getBoundsInLocal()))) {
                    ((HBox) child).setBackground(new Background(new BackgroundFill(colorToPaint, new CornerRadii(0), new Insets(0))));
                    objects.remove(child);
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
