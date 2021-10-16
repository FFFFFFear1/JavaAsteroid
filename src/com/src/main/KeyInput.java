package com.src.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Game game;

    public KeyInput(Game _game) {
        this.game = _game;
    }

    public void keyPressed(KeyEvent event) {
        game.keyPressed(event);
    }

    public void keyReleased(KeyEvent event) {
        game.keyReleased(event);
    }
}
