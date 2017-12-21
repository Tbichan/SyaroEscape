package com.example.tbichan.syaroescape.maingamebak.viewmodel;

import android.content.Context;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.maingamebak.model.COM;
import com.example.tbichan.syaroescape.maingamebak.model.Enemy;
import com.example.tbichan.syaroescape.maingamebak.model.Player;
import com.example.tbichan.syaroescape.maingamebak.model.StageModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * UI用のビューです。
 * Created by tbichan on 2017/10/16.
 */

public class UIViewModel extends GlViewModel {

    // ステージモデル
    private StageModel stageModel;

    public UIViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    // 読み込み
    @Override
    public void awake() {

        // rightButton
        GlButton rightButton = new GlButton(this, "RightButton"){

            // プレイヤー検索
            Player player;

            @Override
            public void start() {
                super.start();
                // プレイヤー検索
                player = (Player)getGlView().findModel("Player");

            }

            // クリック時
            @Override
            public void onClick(){

                // プレイヤーにクリック処理を伝達
                if (player.rightButtonClick()) {

                    // COMを検索
                    COM com = (COM) getGlView().findModel("COM");

                    // comにクリック処理を伝達
                    com.playerMove();

                    // 2回に一回
                    if (player.getMoveCnt() % 2 == 1) {

                        // 敵にクリック処理を伝達
                        ArrayList<GlModel> enemyList = getGlView().containModelAll("Enemy");
                        for (GlModel enemy : enemyList) {
                            ((Enemy) enemy).playerMove();
                        }
                    }
                }
            }
        };

        // upButton
        GlButton upButton = new GlButton(this, "UpButtonButton"){

            // プレイヤー検索
            Player player;

            @Override
            public void start() {
                super.start();
                // プレイヤー検索
                player = (Player)getGlView().findModel("Player");

            }

            // クリック時
            @Override
            public void onClick(){

                // 相手にクリック処理を伝達
                if (player.upButtonClick()) {

                    // COMを検索
                    COM com = (COM) getGlView().findModel("COM");

                    // comにクリック処理を伝達
                    com.playerMove();

                    // 2回に一回
                    if (player.getMoveCnt() % 2 == 1) {

                        // 敵にクリック処理を伝達
                        ArrayList<GlModel> enemyList = getGlView().containModelAll("Enemy");
                        for (GlModel enemy : enemyList) {
                            ((Enemy) enemy).playerMove();
                        }
                    }
                }
            }
        };

        // leftButton
        GlButton leftButton = new GlButton(this, "LeftButton"){

            // プレイヤー検索
            Player player;

            @Override
            public void start() {
                super.start();
                // プレイヤー検索
                player = (Player)getGlView().findModel("Player");

            }

            // クリック時
            @Override
            public void onClick(){

                // 相手にクリック処理を伝達
                if (player.leftButtonClick()) {

                    // COMを検索
                    COM com = (COM) getGlView().findModel("COM");

                    // comにクリック処理を伝達
                    com.playerMove();

                    // 2回に一回
                    if (player.getMoveCnt() % 2 == 1) {

                        // 敵にクリック処理を伝達
                        ArrayList<GlModel> enemyList = getGlView().containModelAll("Enemy");
                        for (GlModel enemy : enemyList) {
                            ((Enemy) enemy).playerMove();
                        }
                    }
                }
            }
        };

        // downButton
        GlButton downButton = new GlButton(this, "DownButton"){

            // プレイヤー検索
            Player player;

            @Override
            public void start() {
                super.start();
                // プレイヤー検索
                player = (Player)getGlView().findModel("Player");

            }

            // クリック時
            @Override
            public void onClick(){

                // 相手にクリック処理を伝達
                if (player.downButtonClick()) {

                    // COMを検索
                    COM com = (COM) getGlView().findModel("COM");

                    // comにクリック処理を伝達
                    com.playerMove();

                    // 2回に一回
                    if (player.getMoveCnt() % 2 == 1) {
                        // 敵にクリック処理を伝達
                        ArrayList<GlModel> enemyList = getGlView().containModelAll("Enemy");
                        for (GlModel enemy : enemyList) {
                            ((Enemy) enemy).playerMove();
                        }
                    }
                }
            }
        };

        // endButton
        GlButton endButton = new GlButton(this, "EndButton"){

            // クリック時
            @Override
            public void onClick(){
                SceneManager.getInstance().setNextScene(new MenuScene());
            }
        };

        stageModel = new StageModel(this, "stagemodel");

        rightButton.setPosition(GlView.TILE_SIZE * 4, 80);
        upButton.setPosition(GlView.TILE_SIZE * 2, GlView.TILE_SIZE + 80);
        leftButton.setPosition(0, 80);
        downButton.setPosition(GlView.TILE_SIZE * 2, -GlView.TILE_SIZE + 80);
        endButton.setPosition(1024, 80);
        addModel(rightButton);
        addModel(upButton);
        addModel(leftButton);
        addModel(downButton);
        addModel(endButton);
        addModel(stageModel);


    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl){
        super.update(gl);
    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}


