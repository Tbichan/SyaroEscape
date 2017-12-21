package com.example.tbichan.syaroescape.menu.model;

import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/09.
 */

public class Fukidashi extends GlModel {


    public Fukidashi(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    @Override
    public void start() {

        setAlpha(0.75f);
    }

    @Override
    public void update() {
        // 拡大率(微分してcosに)
        float v = 0.02f * (float)Math.cos(getCnt()*0.025f);

        setSize(getWidth() + v , getHeight() + v);
    }

}
