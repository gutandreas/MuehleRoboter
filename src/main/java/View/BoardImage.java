package View;

import java.awt.*;
import javax.swing.JFrame;

public class BoardImage extends Canvas{

    public void paint(Graphics g) {

        Toolkit t=Toolkit.getDefaultToolkit();
        Image i=t.getImage("Spielfeld.png");
        g.drawImage(i, 120,100,this);

    }

}