package com.example.tbichan.syaroescape.menu.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * キャラの立ち絵用
 * Created by tbichan on 2017/12/09.
 */

public class CharaModel extends GlModel {


    public CharaModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

        // 拡大率(微分してcosに)
        //float v = 0.4f * (float)Math.cos(getCnt()*0.025f);

        //setSize(getWidth() + v , getHeight() + v);
    }

}