package com.src.main;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage image;

    public SpriteSheet(BufferedImage _image) {
        this.image = _image;
    }

    public BufferedImage GrabImage(int col, int row, int weight, int height) {
        BufferedImage img = image.getSubimage((col * 32) - 32, (row * 32) - 32, weight, height);
        return img;
    }
}
