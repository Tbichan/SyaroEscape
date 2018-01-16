package com.example.tbichan.syaroescape.experience.model;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by 5516103m on 2017/12/11.
 */

public class TextModel extends GlModel {

    private String text = "CPUとの勝率";

    public void setText(String text) {
        this.text = text;
        Bitmap strBitmap = BitMapManager.createStrImage(text, 100, Color.GREEN);
        setOutsideBitmapTexture(strBitmap);
    }

    public TextModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }
}
