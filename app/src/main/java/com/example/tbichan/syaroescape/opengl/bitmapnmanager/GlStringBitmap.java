package com.example.tbichan.syaroescape.opengl.bitmapnmanager;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by tbichan on 2018/01/11.
 */

public class GlStringBitmap {

    private String text;

    private int color = Color.GRAY;

    // 画像サイズ
    private int width = 512;
    private int height = 128;

    public GlStringBitmap(String text) {
        this.text = text;
    }

    public Bitmap loadImage() {
        return BitMapManager.createStrImage(text, 20, width, height, color);
    }

    public int getColor() {
        return color;
    }

    public GlStringBitmap setColor(int color) {
        this.color = color;

        return this;
    }

    public GlStringBitmap setWidth(int width) {
        this.width = width;

        return this;
    }

    public GlStringBitmap setHeight(int height) {
        this.height = height;

        return this;
    }

    public String getText() {
        return text;
    }
}
