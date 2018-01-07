package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/24.
 */

public class Rabbit extends EnvSprite {


    public Rabbit(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setTextureId(R.drawable.anko);
    }
}
