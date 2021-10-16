package com.src.main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player {
    private float x;
    private float y;

    private float rotation = 0;
    private float rotateAngle = 0;

    private float velocityX = 0;
    private float velocity= 0;

    public boolean isMoving = false;

    private final BufferedImage player;

    public Player(float _x, float _y, Game game) {
        this.x = _x;
        this.y = _y;

        SpriteSheet spriteSheet = new SpriteSheet(game.getSpriteSheet());
        player = spriteSheet.GrabImage(1,1,75,75);
    }

    public void Tick() {
        float dirX;
        float dirY;

        //todo сраное дерьмо, найди формулу как находить х относительно угла прямой 
        if(isMoving) {
            dirX = velocity / rotation * 57;
            dirY = velocity / rotation * 57;
        }
        else {
            dirX = velocity;
            dirY = velocity;
        }

        x += dirX;
        y += dirY;

        System.out.println(rotation * 57);

        rotation += rotateAngle;

        if(rotation * 57 > 360) {
            rotation = 0 / 57;
        }
        else if(rotation * 57 < 0) {
            rotation = 6.3f;
        }

        if(x < 0) {
            x = Game.WIDTH * Game.SCALE - 33;
        }
        else if(x >= Game.WIDTH * Game.SCALE - 32) {
            x = 0;
        }
        else if(y < 0) {
            y = Game.HEIGHT * Game.SCALE - 33;
        }
        else if(y >= Game.HEIGHT * Game.SCALE - 32) {
            y = 0;
        }
    }

    public void Render(Graphics graphics) {
//        graphics.drawImage(player, (int)x, (int)y, 32, 32, null );
        drawRotated(player, rotation, graphics);
    }

    public void drawRotated(BufferedImage img, double rotationRequired, Graphics g) {
        //double rotationRequired = Math.toRadians(degrees);
        double locationX = img.getWidth() / 2;
        double locationY = img.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage rotatedImage = op.filter(img, null);

        g.drawImage(rotatedImage , (int) x, (int) y, 32, 32,null);
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
//    public void setVelocityX(float velocityX) {
//        this.velocityX = velocityX;
//    }
    public void setVelocity(float velocity, boolean _isMoving) {
        this.isMoving = _isMoving;
        this.velocity = velocity;
    }

    public float getRotateAngle() {
        return rotateAngle;
    }
    public void setRotateAngle(float rotateAngle) {
        this.rotateAngle = rotateAngle;
    }
}
