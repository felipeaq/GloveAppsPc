package uncoupledprograms.pconly;

import java.awt.image.BufferedImage;

public interface IPathScreen {

    void moveObject(int x, int y) ;

    void moveObject(int y);

    void setCreatedImage(BufferedImage combined);

    void serviceStopped();
}
