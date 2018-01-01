package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * Created by 5515012o on 2017/12/19.
 */

public class StatusViewModel extends GlViewModel implements GlObservable {

    // カップ数
    private int cupNum = 0;

    // カップ数表示用
    private GlModel cafeStr;

    public StatusViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {
        // ステータス
        cafeStr = new GlModel(this, "sta") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

            /**
             * テクスチャ読み込みが終わると実行されます。
             */
            @Override
            public void endTexLoad() {
                getBitmap().recycle();
                Log.d("outbitmap", "recycle");
            }
        };

        addModel(cafeStr);

        Bitmap cafeBit = BitMapManager.createStrImage("カフェイン:" + 0, 50, 512, 1024, Color.GRAY);
        cafeStr.setOutsideBitmapTexture(cafeBit);
    }

    @Override
    public void start() {

    }

    @Override
    public void notify(Object o, String... params) {

        Log.d("notify", o.getClass().toString());

        // 環境VMからの通知
        if (o instanceof EnvironmentViewModel) {

            EnvironmentViewModel environmentViewModel = (EnvironmentViewModel)o;
            // プレイヤーのとき
            if (environmentViewModel == ((GameScene)getScene()).getPlayerViewModel()) {
                // ステータス

                // 獲得カップ数
                final int caffeinePower = environmentViewModel.getCaffeinePower();

                if (caffeinePower != cupNum) {

                    Bitmap cafeBit = BitMapManager.createStrImage("カフェイン:" + caffeinePower, 50, 512, 1024, Color.GRAY);
                    cafeStr.setOutsideBitmapTexture(cafeBit);
                    cupNum = caffeinePower;
                }
            }
        }
    }
}
