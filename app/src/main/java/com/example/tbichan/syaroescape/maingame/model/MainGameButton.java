package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/16.
 */

public abstract class MainGameButton extends GlButton {

    public MainGameButton(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        //setAlpha(0.5f);
    }


    @Override
    public void onTex() {

    }

    @Override
    public void upTex() {

    }
}
