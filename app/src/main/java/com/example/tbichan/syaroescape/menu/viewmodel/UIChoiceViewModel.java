package com.example.tbichan.syaroescape.menu.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.findbattleuser.FindBattleUserScene;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.menu.model.MenuButton;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.story.StoryScene;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * UI用のビューです。
 * Created by tbichan on 2017/10/16.
 */

public class UIChoiceViewModel extends GlViewModel {

    // メニューボタンのリスト
    private ArrayList<MenuButton> menuButtonList = new ArrayList<>();

    // 選択中のボタンid
    private int actId = 0;

    // 選択されたボタン
    private MenuButton activeButton = null;

    // 退場フラグ
    private boolean retireFlg = false;

    // 退場したカウンタ
    private int retireCnt = -1;

    // CharaViewModel
    private CharaViewModel charaViewModel = null;

    // FadeViewModel
    private FadeViewModel fadeViewModel = null;

    public UIChoiceViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        final int[] imgIds = {R.drawable.menu_0,R.drawable.menu_1,R.drawable.menu_2,R.drawable.menu_3};

        for (int i = 0; i < 4; i++) {

            MenuButton menuButton = new MenuButton(this, "menu" + i, i) {

                // カーソルが当たった瞬間かどうか
                private boolean cursor = false;

                @Override
                public void onClick() {

                }

                @Override
                public void onCursor() {
                    super.onCursor();

                    if (!isAdmission() && !retireFlg && !cursor) onMenuButtonCursor(this);
                    cursor = true;
                }

                @Override
                public void onTap() {
                    super.onTap();

                    // 移動しきっていれば
                    if (!isAdmission() && !retireFlg && isMoveFinal()) onMenuButtonTap(this);
                }

                @Override
                public void notCursor() {
                    super.notCursor();
                    cursor = false;
                }
            };

            menuButton.setTexture(imgIds[i]);
            menuButton.setSize(1300, 250);
            menuButton.setPosition(2800.0f, 1100 - i * 250);
            menuButton.setDefultPosition(1266, 1100 - i * 250);

            addModel(menuButton);

            // リストに追加
            menuButtonList.add(menuButton);
        }

        // 最初のボタンを選択状態に
        if (StoreManager.containsKey("menu_actionbutton")) {
            actId = Integer.parseInt(StoreManager.restoreString("menu_actionbutton"));
            menuButtonList.get(actId).startMove();
        } else {
            actId = 0;
            menuButtonList.get(0).startMove();
        }

        // 初期位置
        //setX(1500.0f);



    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

        charaViewModel.waitAwake();
        // 吹き出し出現
        if (StoreManager.containsKey("menu_actionbutton")) {
            int act = Integer.parseInt(StoreManager.restoreString("menu_actionbutton"));
            charaViewModel.appearFukidashi(act);
        } else {
            charaViewModel.appearFukidashi(0);
        }


    }

    @Override
    public void update(GL10 gl){
        super.update(gl);

        if (retireFlg) {

            // しばらくしたらキャラも選択ボタンも退場
            if (getCnt() - retireCnt == 45) {
                activeButton.retire();
                charaViewModel.retire();



            } else if (getCnt() - retireCnt == 80) {

                // フェードアウト
                if (fadeViewModel != null) {
                    fadeViewModel.startFadeOut();
                }
            }

            else if (getCnt() - retireCnt == 200) {
                setVisible(false);

                // ゲームシーンヘ
                //MainActivity.showTextDialog("テストー");

                // 最後に選んだモードを記憶
                StoreManager.saveString("menu_actionbutton", String.valueOf(actId));

                if (actId == 0) SceneManager.getInstance().setNextScene(new GameScene());
                else if (actId == 1)  SceneManager.getInstance().setNextScene(new FindBattleUserScene());
                else if (actId == 2)  SceneManager.getInstance().setNextScene(new StoryScene());
            }
        }

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    // メニューボタンにカーソルが当たったときに呼ばれます。
    public void onMenuButtonCursor(MenuButton menuButton) {
        Log.d("menuBtn", "メニューボタン:" + menuButton.getName() + "が選択。");

        for (MenuButton button: menuButtonList) {
            if (menuButton != button) button.returnPosition();
        }

        // 吹き出し出現
        if (actId != menuButton.getId()) {

            // 選択する
            actId = menuButton.getId();
            charaViewModel.appearFukidashi(actId);
        }
    }

    // メニューボタンがタップされたときに呼ばれます。
    public void onMenuButtonTap(MenuButton menuButton) {
        Log.d("menuBtn", "メニューボタン:" + menuButton.getName() + "がタップ。");

        // 選択ボタンを代入
        activeButton = menuButton;

        // 押されていないボタンを退場させる。
        for (MenuButton button: menuButtonList) {

            // ボタンのアニメーションを止める。
            button.animStop();
            if (menuButton != button) button.retire();
        }

        // 退場
        retire();
    }

    // CharaViewModelを設定
    public void setCharaViewModel(CharaViewModel charaViewModel) {
        this.charaViewModel = charaViewModel;
    }

    // FadeViewModelを設定
    public void setFadeViewModel(FadeViewModel fadeViewModel) {
        this.fadeViewModel = fadeViewModel;
    }

    // 退場します。
    public void retire() {
        retireCnt = getCnt();
        retireFlg = true;
    }
}


