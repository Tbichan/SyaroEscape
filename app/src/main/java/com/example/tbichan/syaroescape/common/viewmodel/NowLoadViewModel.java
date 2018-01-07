package com.example.tbichan.syaroescape.common.viewmodel;

import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/10.
 */

public class NowLoadViewModel extends GlViewModel {

    // barの中
    GlModel barMiddle;

    // barの右
    GlModel barRight;

    public NowLoadViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

        Log.d("NowLoading", "NowLoading");

        GlModel bgModel = new GlModel(this, "load_bg") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };
        bgModel.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        bgModel.setTextureId(R.drawable.load_bg);

        GlModel strModel = new GlModel(this, "load_str") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        strModel.setTextureId(R.drawable.load_str);
        strModel.setPosition(2000, 25);

        // barの左
        GlModel barLeft = new GlModel(this, "bar_l") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barLeft.setTextureId(R.drawable.bar);
        barLeft.setPosition(200, 25);
        barLeft.setSize(65, 100);
        barLeft.setU2(0.054f);

        // barの中
        barMiddle = new GlModel(this, "bar_m") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barMiddle.setTextureId(R.drawable.bar);
        barMiddle.setPosition(200+65, 25);
        barMiddle.setSize(0.1f, 100);
        barMiddle.setU1(0.054f);
        barMiddle.setU2(1.0f - 0.054f);

        // barの右
        barRight = new GlModel(this, "bar_r") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barRight.setTextureId(R.drawable.bar);
        barRight.setPosition(200+65+0.1f, 25);
        barRight.setSize(65, 100);
        barRight.setU1(1.0f - 0.054f);

        // barFrameの左
        GlModel barFrameLeft = new GlModel(this, "bar_frame_l") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barFrameLeft.setTextureId(R.drawable.bar_frame);
        barFrameLeft.setPosition(200, 25);
        barFrameLeft.setSize(65, 100);
        barFrameLeft.setU2(0.054f);

        // barFrameの中
        GlModel barFrameMiddle = new GlModel(this, "bar_frame_m") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barFrameMiddle.setTextureId(R.drawable.bar_frame);
        barFrameMiddle.setPosition(200 + 65, 25);
        barFrameMiddle.setSize(1600, 100);
        barFrameMiddle.setU1(0.054f);
        barFrameMiddle.setU2(1.0f - 0.054f);

        // barFrameの右
        GlModel barFrameRight = new GlModel(this, "bar_frame_r") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        barFrameRight.setTextureId(R.drawable.bar_frame);
        barFrameRight.setPosition(200 + 65 + 1600, 25);
        barFrameRight.setSize(65, 100);
        barFrameRight.setU1(1.0f - 0.054f);

        addModel(bgModel);
        addModel(strModel);

        addModel(barLeft);
        addModel(barMiddle);
        addModel(barRight);

        addModel(barFrameLeft);
        addModel(barFrameMiddle);
        addModel(barFrameRight);

        setVisible(false);
    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        float loadPer = SceneManager.getInstance().getNowScene().getImgLoadPersent();

        // バーの長さ変更
        float barLen = 1600 * (loadPer + 0.2f);

        if (barLen > 1600) barLen = 1600;

        barMiddle.setSize(barLen, 100);
        barRight.setPosition(200+65+barLen, 25);

        //Log.d("load", (int)(SceneManager.getInstance().getNowScene().getImgLoadPersent() * 100.0f) + "%ロード完了");

        if (barMiddle.isLoadTex()) setVisible(true);

        if (loadPer == 1.0f) setVisible(false);
    }
}
