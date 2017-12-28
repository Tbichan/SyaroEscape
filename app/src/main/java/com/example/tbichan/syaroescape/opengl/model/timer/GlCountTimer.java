package com.example.tbichan.syaroescape.opengl.model.timer;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/12/28.
 */

public abstract class GlCountTimer extends GlModel {

    private int counter_interval = 30;
    private GlModel glModel;

    public GlCountTimer(GlModel glModel){
        super(glModel.getGlViewModel(), glModel.getName() + "_Counter");
        this.glModel = glModel;

    }

    public void setCounter_interval(int counter_interval) {
        this.counter_interval = counter_interval;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        // 時間がたったら発火
        if (getCnt() >= counter_interval) {
            timerEnd(glModel);
            delete();
        }
    }

    /**
     * 時間が経過したときの処理です。
     */
    public abstract void timerEnd(GlModel glModel);
}
