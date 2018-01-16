package com.example.tbichan.syaroescape.maingame.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.MainGameButton;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.SEManager;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/16.
 */

public class ActButtonUIViewModel extends GlViewModel {

    // 環境VMリスト
    private ArrayList<EnvironmentViewModel> environmentViewModelArrayList = new ArrayList<>();

    // 技ViewModel
    private WazaUIViewModel wazaUIViewModel;

    // 移動速度
    private final float VX = 100f;
    private float vx = VX;

    // 表示か
    private boolean show = false;

    public ActButtonUIViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        // 移動ボタン
        MainGameButton moveButton = new MainGameButton(this, "moveButton") {
            @Override
            public void onClick() {

                // 移動を通知
                for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {
                    environmentViewModel.moveButtonClick();
                }

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.button_click);
            }


        };

        moveButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-300);
        moveButton.setSize(500, 250);
        moveButton.setAlpha(0.75f);
        moveButton.setTextureId(R.drawable.move_button);
        addModel(moveButton);

        // 技ボタン
        MainGameButton wazaButton = new MainGameButton(this, "moveButton") {
            @Override
            public void onClick() {

                // カメラ移動モードに
                for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {
                    environmentViewModel.cameraCanMoveMode();
                }

                wazaUIViewModel.appear();
                // 効果音再生
                SEManager.getInstance().playSE(R.raw.button_click);
            }


        };

        wazaButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-600);
        wazaButton.setSize(500, 250);
        wazaButton.setAlpha(0.75f);
        wazaButton.setTextureId(R.drawable.waza_button);
        addModel(wazaButton);

        // ターン終了ボタン
        MainGameButton turnEndButton = new MainGameButton(this, "turnEndButton") {
            @Override
            public void onClick() {


                for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {

                    // 同時反応防止
                    if (environmentViewModel.isTurn() && environmentViewModel.getCnt() - environmentViewModel.getTurnCnt() > 0) {
                        environmentViewModel.endTurn(true);
                    }

                    // カメラ移動モードに
                    environmentViewModel.cameraCanMoveMode();
                    // 効果音再生
                    SEManager.getInstance().playSE(R.raw.button_click);
                }
            }
        };

        turnEndButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-900);
        turnEndButton.setSize(500, 250);
        turnEndButton.setAlpha(0.75f);
        turnEndButton.setTextureId(R.drawable.turnend_button);
        addModel(turnEndButton);

        // キャンセルボタン
        MainGameButton cancelButton = new MainGameButton(this, "cancelButton") {
            @Override
            public void onClick() {

                // カメラ移動モードに
                for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {
                    environmentViewModel.cameraCanMoveMode();
                }

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.button_click);
            }
        };

        cancelButton.setPosition(GlView.VIEW_WIDTH-750, GlView.VIEW_HEIGHT-1200);
        cancelButton.setSize(500, 250);
        cancelButton.setAlpha(0.75f);
        cancelButton.setTextureId(R.drawable.cancel_button);
        addModel(cancelButton);



        // VMの位置を設定
        setX(1000);

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl){
        super.update(gl);

        // 次の座標
        float nextX = getX() + vx;

        if (nextX < 0f) {
            nextX = 0f;
            show = true;

        } else if (nextX > 1000f) {
            nextX = 1000f;
        }

        setX(nextX);
    }

    public void addEnvironmentViewModel(EnvironmentViewModel environmentViewModel) {
        environmentViewModelArrayList.add(environmentViewModel);
    }

    // 通知を受け取ります。
    public void notify(Object o) {

    }

    // プレイヤーをタップした時の処理です。
    public void tapPlayer() {

        // 自分のターンのときのみ
        if (((GameScene)getScene()).getTurn() == 1) return;
        for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {
            environmentViewModel.uiViewShowButton();
        }
    }

    // プレイヤーが動いたあとの処理です。
    public void movedPlayer() {
        for (EnvironmentViewModel environmentViewModel: environmentViewModelArrayList) {
            environmentViewModel.cameraCanMoveMode();
        }
    }

    public void enter() {
        vx = -VX;
    }

    public void hide() {
        vx = VX;
        show = false;
    }

    public WazaUIViewModel getWazaUIViewModel() {
        return wazaUIViewModel;
    }

    public void setWazaUIViewModel(WazaUIViewModel wazaUIViewModel) {
        this.wazaUIViewModel = wazaUIViewModel;
    }
}
