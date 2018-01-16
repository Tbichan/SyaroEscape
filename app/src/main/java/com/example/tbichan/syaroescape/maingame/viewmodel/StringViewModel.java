package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.ui.UIListener;

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

        yourTurnModel.setTextureId(R.drawable.your_turn);
        yourTurnModel.setPosition(GlView.VIEW_WIDTH * 0.5f - 400, 300);
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

        comTurnModel.setTextureId(R.drawable.com_turn);
        comTurnModel.setPosition(GlView.VIEW_WIDTH * 0.5f - 400, 300);
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

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.button_click);

                MainActivity.showOKCancelDialog(new UIListener() {
                    /**
                     * OKをクリックしたとき
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        SceneManager.getInstance().setNextScene(new MenuScene());
                    }
                }, new UIListener() {
                    /**
                     * キャンセルを押したとき
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {

                    }
                });
                //SceneManager.getInstance().setNextScene(new MenuScene());
            }

            @Override
            public void onTex() {

            }

            @Override
            public void upTex() {

            }
        };

        endGameButton.setTextureId(R.drawable.end_game);
        endGameButton.setPosition(0f, GlView.VIEW_HEIGHT - 70);
        endGameButton.setSize(500, 70);
        addModel(endGameButton);

        setTurn(0);

    }

    @Override
    public void start() {}

    @Override
    public void notify(Object o, String... params) {

    }

    public void setTurn(int turn) {
        yourTurnModel.setVisible(turn == 0);
        comTurnModel.setVisible(turn == 1);
    }

}
