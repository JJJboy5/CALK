import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class CatGame extends MIDlet {
    private CatGameCanvas gameCanvas;

    public CatGame() {
        super();
    }

    public void startApp() {
        gameCanvas = new CatGameCanvas(this);
        Display.getDisplay(this).setCurrent(gameCanvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void quit() {
        destroyApp(true);
        notifyDestroyed();
    }
}
