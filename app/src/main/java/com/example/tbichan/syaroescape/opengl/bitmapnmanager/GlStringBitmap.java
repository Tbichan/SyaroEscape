package com.example.tbichan.syaroescape.opengl.bitmapnmanager;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by tbichan on 2018/01/11.
 */

public class GlStringBitmap {

    private String text;

    public GlStringBitmap(String text) {
        this.text = text;
    }

    public Bitmap loadImage() {
        return BitMapManager.createStrImage(text, 20, 512, 128, Color.GRAY);
    }

    public String getText() {
        return text;
    }
}
