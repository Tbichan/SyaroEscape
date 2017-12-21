package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.tbichan.syaroescape.R;
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

    private GlModel cafeStr;

    private GlModel yourTurnModel, comTurnModel;

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

        // ステータス
        //Bitmap strBit = BitMapManager.createStrImage("hoge", 50, Color.YELLOW);
        //cafeStr.setOutsideBitmapTexture(strBit);

        addModel(cafeStr);

        // あなたのターンUI
        yourTurnModel = new GlModel(this, "your_turn") {
            @Override
            public void start() {

            }

            @Override
            public void update() {
                setAlpha(0.15f * ((float)Math.sin(getCnt()*0.2f)+1.0f)+0.5f);
                setBright(0.05f * ((float) Math.sin(getCnt() * 0.2f) + 1.0f) + 0.1f);
            }
        };

        yourTurnModel.setTexture(R.drawable.your_turn);
        yourTurnModel.setPosition(GlView.VIEW_WIDTH * 0.5f - 400, 100);
        yourTurnModel.setSize(800, 100);
        addModel(yourTurnModel);

        // 相手のターンUI
        comTurnModel = new GlModel(this, "com_turn") {
            @Override
            public void start() {

            }

            @Override
            public void update() {
                setAlpha(0.15f * ((float)Math.sin(getCnt()*0.2f)+1.0f)+0.5f);
                setBright(0.05f * ((float) Math.sin(getCnt() * 0.2f) + 1.0f) + 0.1f);
            }
        };

        comTurnModel.setTexture(R.drawable.com_turn);
        comTurnModel.setPosition(GlView.VIEW_WIDTH * 0.5f - 400, 100);
        comTurnModel.setSize(800, 100);
        addModel(comTurnModel);

        setTurn(0);
    }

    @Override
    public void start() {

    }

    @Override
    public void notify(Object o, String... params) {

        if (o instanceof Environment) {
            // 環境からの通知
            Environment env = (Environment)o;

            // ステータス
            Bitmap strBit = BitMapManager.createStrImage(env.getGetCupCnt() + "", 50, Color.YELLOW);
            cafeStr.setOutsideBitmapTexture(strBit);
        }
    }

    /**
     * ターンを設定します。
     * @param turn
     */
    public void setTurn(int turn) {
        yourTurnModel.setVisible(turn == 0);
        comTurnModel.setVisible(turn == 1);
    }

}
