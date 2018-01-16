package com.example.tbichan.syaroescape.title.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2018/01/17.
 */

public class TitleLogoModel extends GlModel {

    private int flashCnt = -1;

    private float initY;


    public TitleLogoModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    @Override
    public void start() {
        initY = getY();
    }

    @Override
    public void update() {
        setPosition(getX(), initY + 20 * (float) Math.sin(getCnt()
                * 0.025));

        if (flashCnt != -1) {
            int t = getCnt() - flashCnt;
            if (t < 40) {
                setAlpha((float) (Math.cos(t / 2.0f) + 1.0f) * 0.5f);
            } else setAlpha(1.0f);
        }
    }

    public void flash() {
        flashCnt = getCnt();
    }

}
