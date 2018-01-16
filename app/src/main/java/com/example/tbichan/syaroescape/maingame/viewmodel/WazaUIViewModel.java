package com.example.tbichan.syaroescape.maingame.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.*;
import com.example.tbichan.syaroescape.common.viewmodel.MoveViewModel;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.MainGameButton;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.SEManager;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/16.
 */

public class WazaUIViewModel extends MoveViewModel {

    // 環境VMリスト
    private ArrayList<EnvironmentViewModel> environmentViewModelArrayList = new ArrayList<>();

    // 移動速度
    private final float VX = 100f;

    // 表示か
    private boolean show = false;

    // 高速ボタン
    private MainGameButton fastButton;

    public WazaUIViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        // 高速ボタン
        fastButton = new MainGameButton(this, "fast_Button") {
            @Override
            public void onClick() {
                // 3回移動モードかどうか判定
                if (((GameScene)getScene()).getPlayerViewModel().isThreeMode() || ((GameScene)getScene()).getPlayerViewModel().getCaffeinePower() == 0) {
                    return;
                }

                // プレイヤーVM取得
                EnvironmentViewModel environmentViewModel = ((GameScene)getScene()).getPlayerViewModel();
                environmentViewModel.threeMove(true);

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.hitension);



                hide();
            }


        };

        fastButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-300);
        fastButton.setSize(500, 250);
        fastButton.setAlpha(0.75f);
        fastButton.setU1(0.0f);
        fastButton.setU2(0.5f);
        fastButton.setTextureId(R.drawable.fast_button);
        addModel(fastButton);

        // キャンセルボタン
        MainGameButton cancelButton = new MainGameButton(this, "cancelButton") {
            @Override
            public void onClick() {

                // プレイヤーVM取得
                EnvironmentViewModel environmentViewModel = ((GameScene)getScene()).getPlayerViewModel();
                environmentViewModel.cameraCanMoveMode();
                environmentViewModel = ((GameScene)getScene()).getOtherPlayerViewModel();
                environmentViewModel.cameraCanMoveMode();

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.button_click);

                hide();
            }
        };

        cancelButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-600);
        cancelButton.setSize(500, 250);
        cancelButton.setAlpha(0.75f);
        cancelButton.setTextureId(R.drawable.cancel_button);
        addModel(cancelButton);

        // VMの位置を設定
        setX(1000);
        show = false;

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl){
        super.update(gl);

    }

    public void addEnvironmentViewModel(EnvironmentViewModel environmentViewModel) {
        environmentViewModelArrayList.add(environmentViewModel);
    }

    // 出現させます。
    public void appear() {
        setVisible(true);
        startMove(0, 0f, 10);
        show = true;

        // 3回移動モードかどうか判定
        if (((GameScene)getScene()).getPlayerViewModel().isThreeMode() || ((GameScene)getScene()).getPlayerViewModel().getCaffeinePower() == 0) {
            // ボタン無効
            fastButton.setU1(0.5f);
            fastButton.setU2(1.0f);
        } else {
            // ボタン有効
            fastButton.setU1(0.0f);
            fastButton.setU2(0.5f);
        }
    }

    // 隠します
    public void hide() {
        addGlObserverable(new GlObservable() {
            @Override
            public void notify(Object o, String... param) {
                setVisible(false);
            }
        });
        startMove(1000f, 0f, 10);
        show = false;
    }

    // プレイヤーをタップした時の処理です。
    public void tapPlayer() {

        // 自分のターンのときのみ
        if (((GameScene)getScene()).getTurn() == 1) return;
        if (show) hide();
    }
}
