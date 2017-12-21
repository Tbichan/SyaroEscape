package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/10.
 */

public class Player extends EnvSprite {

    public Player(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    // プレイヤークリック時
    @Override
    public void onClick() {
        getEnvironmentViewModel().notify(this);
    }
}
