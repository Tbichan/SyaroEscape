package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.findbattleuser.FindBattleUserScene;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

/**
 * 文字を表示するViewModelです。
 * Created by tbichan on 2017/12/21.
 */

public class StringViewModel extends GlViewModel implements GlObservable {

    private GlModel yourTurnModel, comTurnModel;

    private GlButton endGameButton;

    public StringViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

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

        // メニューに戻る
        endGameButton = new GlButton(this, "com_turn") {

            @Override
            public void update() {
                setAlpha(0.15f * ((float)Math.sin(getCnt()*0.2f)+1.0f)+0.5f);
                setBright(0.05f * ((float) Math.sin(getCnt() * 0.2f) + 1.0f) + 0.1f);
            }

            @Override
            public void onClick() {
                SceneManager.getInstance().setNextScene(new MenuScene());
            }

            @Override
            public void onTex() {

            }

            @Override
            public void upTex() {

            }
        };

        endGameButton.setTexture(R.drawable.end_game);
        endGameButton.setPosition(GlView.VIEW_WIDTH * 0.85f - 400, 50);
        endGameButton.setSize(800, 100);
        addModel(endGameButton);

        setTurn(0);

    }

    @Override
    public void start() {}

    @Override
    public void notify(Object o, String... params) {
        Log.d("string", "aaa");
    }

    public void setTurn(int turn) {
        yourTurnModel.setVisible(turn == 0);
        comTurnModel.setVisible(turn == 1);
    }

}
