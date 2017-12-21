package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/16.
 */

public abstract class ArrowUI extends MainGameButton {

    public ArrowUI(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        //setAlpha(0.5f);
    }

    @Override
    public void update() {
        super.update();
        setBright(0.15f * ((float) Math.sin(getCnt() * 0.08f) + 1.0f) + 0.1f);
        setAlpha(0.15f * ((float) Math.sin(getCnt() * 0.08f) + 1.0f) + 0.75f);
    }
}
