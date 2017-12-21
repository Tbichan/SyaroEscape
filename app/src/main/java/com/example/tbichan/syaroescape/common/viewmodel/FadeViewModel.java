package com.example.tbichan.syaroescape.common.viewmodel;

import android.content.Context;

import com.example.tbichan.syaroescape.common.model.FadePlane;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.model.GlModelColor;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/11/28.
 */

public class FadeViewModel extends GlViewModel {

    //FadePlane fadePlane;
    GlModelColor fadePlane;

    // フェードイン、アウトの速さ
    private float inSpeed = 1.0f;
    private float outSpeed = 1.0f;

    // フェードイン開始フレーム
    private int inCnt = -1;

    // フェードアウト開始フレーム
    private int outCnt = -1;

    public FadeViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);
        //fadePlane = new FadePlane(this, "FadePlane");
    }

    // 読み込み
    @Override
    public void awake() {

        fadePlane = new GlModelColor(this, "fade");
        //fadePlane.setAlpha(1.0f);
        fadePlane.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        fadePlane.setRGBA(0.01f, 0.01f, 0.01f, 1.0f);

        addModel(fadePlane);

        //addModel(fadePlane);

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (inCnt != -1) {
            // フェードイン中
            // 次のアルファ値
            //float nextAlpha = 1f + (float) Math.tanh((inCnt - getCnt()) * 0.02f * inSpeed);
            float nextAlpha = fadePlane.getAlpha() - 0.01f * inSpeed;

            if (nextAlpha < 0f) {
                nextAlpha = 0;
                inCnt = -1;
                endFadeIn();
            }

            fadePlane.setAlpha(nextAlpha);
        } else if (outCnt != -1) {
            // フェードアウト中
            // 次のアルファ値
            //float nextAlpha = (float) Math.tanh((getCnt() - outCnt) * 0.02f * inSpeed);

            float nextAlpha = fadePlane.getAlpha() + 0.01f * outSpeed;

            if (nextAlpha > 1f) {
                nextAlpha = 1f;
                outCnt = -1;
                endFadeOut();
            }
            fadePlane.setAlpha(nextAlpha);
        }
    }

    // フェードイン終了時の処理
    public void endFadeIn() {

    }

    // フェードアウト終了時の処理
    public void endFadeOut() {

    }


    public float getInSpeed() {
        return inSpeed;
    }

    public void setInSpeed(float inSpeed) {
        this.inSpeed = inSpeed;
    }

    public int getOutCnt() {
        return outCnt;
    }

    public void setOutCnt(int outCnt) {
        this.outCnt = outCnt;
    }

    public void startFadeIn() {
        inCnt = getCnt();
    }

    public void startFadeOut() {
        outCnt = getCnt();
    }

}
