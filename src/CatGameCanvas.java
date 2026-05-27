import javax.microedition.lcdui.*;
import java.util.Random;

public class CatGameCanvas extends Canvas implements Runnable {
    private CatGame midlet;
    private Thread gameThread;
    private volatile boolean running;

    // Game state
    private int catX = 80;
    private int catY = 60;
    private int foodX;
    private int foodY;
    private int score = 0;
    private int lives = 3;
    private Random random;

    // Game constants
    private static final int CAT_SIZE = 8;
    private static final int FOOD_SIZE = 4;
    private static final int MOVE_SPEED = 2;
    private static final int SCREEN_WIDTH = 176;
    private static final int SCREEN_HEIGHT = 144;

    public CatGameCanvas(CatGame midlet) {
        this.midlet = midlet;
        this.random = new Random();
        setFullScreenMode(true);
        generateFood();
        startGame();
    }

    private void startGame() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        while (running) {
            update();
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }

    private void update() {
        // Game logic
        checkCollision();
    }

    private void checkCollision() {
        if (Math.abs(catX - foodX) < CAT_SIZE + FOOD_SIZE &&
            Math.abs(catY - foodY) < CAT_SIZE + FOOD_SIZE) {
            score++;
            generateFood();
        }
    }

    private void generateFood() {
        foodX = random.nextInt(SCREEN_WIDTH - FOOD_SIZE * 2) + FOOD_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT - FOOD_SIZE * 2) + FOOD_SIZE;
    }

    protected void keyPressed(int keyCode) {
        // Nokia 3110 keypad mapping:
        // 2 = forward, 4 = left, 6 = right, 5 = backward
        if (keyCode == KEY_NUM2) {  // Forward
            catY -= MOVE_SPEED;
            if (catY < 0) catY = 0;
        } else if (keyCode == KEY_NUM4) {  // Left
            catX -= MOVE_SPEED;
            if (catX < 0) catX = 0;
        } else if (keyCode == KEY_NUM6) {  // Right
            catX += MOVE_SPEED;
            if (catX > SCREEN_WIDTH - CAT_SIZE) catX = SCREEN_WIDTH - CAT_SIZE;
        } else if (keyCode == KEY_NUM5) {  // Backward
            catY += MOVE_SPEED;
            if (catY > SCREEN_HEIGHT - CAT_SIZE) catY = SCREEN_HEIGHT - CAT_SIZE;
        } else if (keyCode == KEY_STAR) {  // Quit
            running = false;
            midlet.quit();
        }
    }

    protected void paint(Graphics g) {
        // Clear screen
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Draw border
        g.setColor(0, 0, 0);
        g.drawRect(0, 0, SCREEN_WIDTH - 1, SCREEN_HEIGHT - 1);

        // Draw cat
        g.setColor(255, 165, 0);
        g.fillRect(catX, catY, CAT_SIZE, CAT_SIZE);
        g.setColor(0, 0, 0);
        g.drawRect(catX, catY, CAT_SIZE, CAT_SIZE);

        // Draw food
        g.setColor(255, 0, 0);
        g.fillRect(foodX, foodY, FOOD_SIZE, FOOD_SIZE);

        // Draw HUD
        g.setColor(0, 0, 0);
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        g.drawString("Score: " + score, 5, 5, Graphics.LEFT | Graphics.TOP);
        g.drawString("Lives: " + lives, SCREEN_WIDTH - 35, 5, Graphics.LEFT | Graphics.TOP);
    }
}
