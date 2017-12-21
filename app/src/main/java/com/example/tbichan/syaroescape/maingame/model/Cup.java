package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/17.
 */

public class Cup extends EnvSprite {

    // 画像枚数
    private final int ANIM_NUM = 30;


    public Cup(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setTexture(R.drawable.cups);
        setTag("cup");

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
        int animIndex = (getCnt() / 3) % ANIM_NUM;

        float u1 = animIndex % 8 / 8f;
        float v1 = 1f - animIndex / 8 / 4f - 1 / 4f;

        setUV(u1, v1, u1 + 1 / 8f, v1 + 1 / 4f);


    }
}
