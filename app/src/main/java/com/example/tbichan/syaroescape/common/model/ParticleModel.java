package com.example.tbichan.syaroescape.common.model;

import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

/**
 * Created by tbichan on 2017/10/16.
 */

public class ParticleModel extends GlModel {

    // アニメーション開始時刻
    private int animCnt = -1;

    public ParticleModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {
        setSize(320, 320);
        //setTextureId(R.drawable.particle);
        // UV
        setU1(0.0f);
        setU2(1.0f/8.0f);
        setV1(7.0f/8.0f);
        setV2(8.0f/8.0f);

    }

    // 更新
    @Override
    public void update() {
        //x += vx;

        if (animCnt != -1) {

            int num = (getCnt() - animCnt) % 30 * 2;


            int tx = num % 8;
            int ty = num / 8;

            // UV
            setU1(tx / 8.0f);
            setU2((tx + 1) / 8.0f);
            setV1(1.0f - (ty + 1) / 8.0f);
            setV2(1.0f - ty / 8.0f);

            if (getCnt() - animCnt >= 16) {

                float nextAlpha = getAlpha() - 1f/14f;

                setAlpha(nextAlpha);
            }


            if (isEndAnim()) {
                animCnt = -1;
                setVisible(false);
            }
        }

    }

    // アニメーションが終わっているか取得
    public boolean isEndAnim() {
        return getCnt() - animCnt >= 30;
    }

    // アニメーションリセット
    public void resetAnim() {
        animCnt = getCnt();
        setAlpha(1.0f);
        setVisible(true);
    }
}
