package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/30.
 */

public class Carrot extends EnvSprite {


    public Carrot(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setTextureId(R.drawable.carrot);
        //setTag("cup");

    }

    // プレイヤークリック時
    @Override
    public void onClick() {

    }

    @Override
    public void start() {
        super.start();
        //setGlScale(1f, 0.8f);
    }

    @Override
    public void update() {
        super.update();

    }
}
