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
        };

        addModel(cafeStr);
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
                Bitmap cafeBit = BitMapManager.createStrImage("カフェイン:" + caffeinePower, 50, 512, 1024, Color.GRAY);
                cafeStr.setOutsideBitmapTexture(cafeBit);
            }
        }
    }
}
