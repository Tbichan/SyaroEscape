package com.example.tbichan.syaroescape.common.model;

import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import java.util.HashSet;

/**
 * Created by tbichan on 2017/10/16.
 */

public abstract class MoveModel extends GlButton {

    // 移動開始カウンタ
    private int moveCnt = -1;

    // 移動前の位置
    private float preX, preY;

    // 目的地
    private float nextX, nextY;

    // 到達までの時間
    private int interval;

    private HashSet<GlObservable> glObservableHashSet = new HashSet<>();

    public MoveModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
    }

    // 更新
    @Override
    public void update() {


        if (isMove()) {
            float time = (getCnt() - moveCnt) / (float)interval;
            if (time < 1f) {
                float tmpX = (1 - time) * preX + time * nextX;
                float tmpY = (1 - time) * preY + time * nextY;
                setPosition(tmpX, tmpY);
            } else {
                setPosition(nextX, nextY);
                moveCnt = -1;
                endMove();
            }
        }
    }

    /**
     * 移動中かどうかを判定します。
     */
    public final boolean isMove() {
        return moveCnt != -1;
    }

    /**
     * 移動を開始します。
     */
    public final void startMove(float nextX, float nextY, int interval) {
        preX = getX();
        preY = getY();
        this.nextX = nextX;
        this.nextY = nextY;
        this.interval = interval;

        moveCnt = getCnt();
    }

    public void endMove(){

        for (GlObservable glObservable: glObservableHashSet) {
            glObservable.notify(this);
        }
        glObservableHashSet.clear();
    }

    public void addGlObservable(GlObservable glObservable) {
        glObservableHashSet.add(glObservable);
    }

    @Override
    public void onTex() {

    }

    @Override
    public void upTex() {

    }

}
