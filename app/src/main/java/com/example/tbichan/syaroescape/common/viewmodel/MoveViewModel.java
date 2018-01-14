package com.example.tbichan.syaroescape.common.viewmodel;

import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.model.ParticleModel;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/11/28.
 */

public abstract class MoveViewModel extends GlViewModel {

    // 移動前
    private float preX, preY;

    // 移動後
    private float nextX, nextY;

    // インターバル
    private int interval;

    // 開始カウンタ
    private int actCnt = -1;

    // 通知用
    private HashSet<GlObservable> glObservableHashSet = new HashSet<>();

    public MoveViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);
    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (isMove()) {
            float t = (getCnt() - actCnt) / (float)interval;
            if (t < 1) {
                float tmpX = (1 - t) * preX + t * nextX;
                float tmpY = (1 - t) * preY + t * nextY;
                setPosition(tmpX, tmpY);
            } else {
                setPosition(nextX, nextY);
                actCnt = -1;
                endMove();
            }
        }

    }

    public void startMove(float nextX, float nextY, int interval) {
        preX = getX();
        preY = getY();
        this.nextX = nextX;
        this.nextY = nextY;
        this.interval = interval;
        actCnt = getCnt();
    }

    public boolean isMove() {
        return actCnt != -1;
    }

    public void addGlObserverable(GlObservable glObservable) {
        glObservableHashSet.add(glObservable);
    }

    public void endMove() {
        for (GlObservable glObservable: glObservableHashSet) {
            glObservable.notify(this);
        }
        glObservableHashSet.clear();
    }

}
