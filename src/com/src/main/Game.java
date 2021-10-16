package com.src.main;

import javafx.scene.input.KeyCode;
import sun.awt.image.BufferedImageDevice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements  Runnable {

    public static final int WIDTH = 320;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 2;
    public static String TITLE = "DamnGame";

    private boolean isRunning = false;
    private Thread gameThread;

    private BufferedImage image =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private BufferedImage playerSpriteSheet = null;

    private Player player;

    public static void main(String[] args) {
        Game game = new Game();

        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.Start();
    }

    public synchronized void Start() {
        if(isRunning)
            return;

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void Init() {
        requestFocus();

        BufferedImageLoader loader = new BufferedImageLoader();
        try {
            playerSpriteSheet = loader.LoadImage("/testplayer.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        addKeyListener(new KeyInput(this));

        player = new Player(100, 100, this);
    }

    public synchronized void Stop() {
        if(!isRunning)
            return;

        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void run() {
        Init();
        long lastTime = System.nanoTime();
        final float amountOfTicks = 60.0f;
        float ns = 1000000000 / amountOfTicks;
        float delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1) {
                Tick();
                updates++;
                delta--;
            }
            frames++;
            Render();

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " Ticks, FPS " + frames);
                updates = 0;
                frames = 0;
            }
        }

        Stop();
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) {
            player.setRotateAngle(0.05f);
        } else if (key == KeyEvent.VK_LEFT) {
            player.setRotateAngle(-0.05f);
        } else if (key == KeyEvent.VK_DOWN) {
            player.setVelocity(5, true);
        } else if (key == KeyEvent.VK_UP) {
            player.setVelocity(-5, true);
        }
    }

    public void keyReleased(KeyEvent event) {
        int key = event.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) {
            player.setRotateAngle(0);
        } else if (key == KeyEvent.VK_LEFT) {
            player.setRotateAngle(0);
        } else if (key == KeyEvent.VK_DOWN) {
            player.setVelocity(0, false);
        } else if (key == KeyEvent.VK_UP) {
            player.setVelocity(0, false);
        }
    }

    private void Tick() {
        player.Tick();
    }
    private void Render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();

        if(bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.drawImage(image, 0, 0, getWidth(), getHeight(), this );

        player.Render(graphics);

        graphics.dispose();
        bufferStrategy.show();

    }

    public BufferedImage getSpriteSheet() {
        return playerSpriteSheet;
    }
}
