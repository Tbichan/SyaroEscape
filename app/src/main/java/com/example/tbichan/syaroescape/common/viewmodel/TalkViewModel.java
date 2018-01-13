package com.example.tbichan.syaroescape.common.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.VibrationScene;
import com.example.tbichan.syaroescape.common.model.TalkMojiModel;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.common.model.TalkCharaModel;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sound.SEManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/10.
 */

public abstract class TalkViewModel extends GlViewModel {

    // キャラ名表示モデル
    private GlModel nameModel;

    // キャラモデル
    private TalkCharaModel leftTalkCharaModel, rightTalkCharaModel, centerTalkCharaModel;

    // 会話文表示用モデル
    private TalkMojiModel[] talkModels;

    // 会話文の文字列配列
    private String[] storyStrs;

    // 表示キャラ
    final int[] imgIds = {R.drawable.syaro_menu, R.drawable.chino_menu, R.drawable.cocoa_menu};

    // 今の表示数
    private int nowIndex = 0;

    public TalkViewModel(GlView glView, SceneBase sceneBase, String name, String storyFile){
        super(glView, sceneBase, name);

        // ファイル読み込み
        storyStrs = MainActivity.loadFile(storyFile).split("\n");
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

        // キャラ表示
        leftTalkCharaModel = new TalkCharaModel(this, "chara1");
        leftTalkCharaModel.setTextureId(R.drawable.syaro_menu);
        leftTalkCharaModel.setPosition(400, 300);
        leftTalkCharaModel.setSize(510, 1000);
        addModel(leftTalkCharaModel);

        rightTalkCharaModel = new TalkCharaModel(this, "chara2");
        rightTalkCharaModel.setTextureId(R.drawable.chino_menu);
        rightTalkCharaModel.setPosition(1500, 300);
        rightTalkCharaModel.setSize(510, 1000);
        addModel(rightTalkCharaModel);

        centerTalkCharaModel = new TalkCharaModel(this, "chara3");
        centerTalkCharaModel.setTextureId(R.drawable.chino_menu);
        centerTalkCharaModel.setPosition(1000, 300);
        centerTalkCharaModel.setSize(510, 1000);
        addModel(centerTalkCharaModel);

        GlModel bgModel = new GlModel(this, "load_bg") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };
        bgModel.setSize(GlView.VIEW_WIDTH - 500, 500);
        bgModel.setX(250);
        bgModel.setTextureId(R.drawable.flower_button);

        addModel(bgModel);


        // 会話文
        nextTalk();

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 効果音再生
            SEManager.getInstance().playSE(R.raw.choice);

            if (isAllShow()) {

                // 次の文章表示
                if (nowIndex < storyStrs.length && nowIndex != -1) {
                    nextTalk();
                } else {
                    nowIndex = -1;
                    endStory();
                }
            }
        }
        return true;
    }

    /**
     * 次の文章を表示します。
     */
    public void nextTalk() {

        String[] hoges = storyStrs[nowIndex].split(":");
        String name = hoges[0];
        String text = hoges[1];
        int leftId = Integer.parseInt(hoges[2].replace("id,", ""));
        int leftAction = Integer.parseInt(hoges[3].replace("action,", ""));
        int rightId = Integer.parseInt(hoges[4].replace("id,", ""));
        int rightAction = Integer.parseInt(hoges[5].replace("action,", ""));
        int centerId = Integer.parseInt(hoges[6].replace("id,", ""));
        int centerAction = Integer.parseInt(hoges[7].replace("action,", ""));

        // 振動させるかどうか
        int allAction = Integer.parseInt(hoges[8].replace("allAction,", ""));
        if (allAction == 1) ((VibrationScene)getScene()).setVibration(true);
        else ((VibrationScene)getScene()).setVibration(false);

        setTalk(name, text, leftId, leftAction, rightId, rightAction, centerId, centerAction);
        nowIndex++;
    }


    /**
     * テキストを表示させます。
     * @param text
     */
    public void setTalk(String name, String text, int leftId, int leftAction, int rightId, int rightAction, int centerId, int centerAction) {

        // 前の名前から変更
        if (nameModel == null) {
            nameModel = new GlModel(this, "nameModel") {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                }

            };

            nameModel.setPosition(300f, 60f);
            nameModel.setSize(2048, 512);
            addModel(nameModel);
        }

        // キャラにアクションさせる。
        if (leftId != -1) {

            if (leftTalkCharaModel.getTextureId() != imgIds[leftId])leftTalkCharaModel.setTextureId(imgIds[leftId]);
            leftTalkCharaModel.setVisible(true);
        } else {
            leftTalkCharaModel.setVisible(false);
        }
        leftTalkCharaModel.action(leftAction);

        // キャラにアクションさせる。
        if (rightId != -1) {
            if (rightTalkCharaModel.getTextureId() != imgIds[rightId])rightTalkCharaModel.setTextureId(imgIds[rightId]);
            rightTalkCharaModel.setVisible(true);
        } else {
            rightTalkCharaModel.setVisible(false);
        }
        rightTalkCharaModel.action(rightAction);

        // キャラにアクションさせる。
        if (centerId != -1) {
            if (centerTalkCharaModel.getTextureId() != imgIds[centerId])centerTalkCharaModel.setTextureId(imgIds[centerId]);
            centerTalkCharaModel.setVisible(true);
        } else {
            centerTalkCharaModel.setVisible(false);
        }
        centerTalkCharaModel.action(centerAction);

        // キャラ名画像
        nameModel.setTextureText(name);

        String[] lines = text.split(",");

        // 前の会話を削除
        if (talkModels != null) {
            for (TalkMojiModel talkModel: talkModels) {
                talkModel.delete();
            }
        }

        talkModels = new TalkMojiModel[lines.length];

        for (int i = 0; i < lines.length; i++) {

            final int tmp = i;

            talkModels[i] = new TalkMojiModel(this, "talkModel_" + i, lines[i]){
                @Override
                public void allShow() {
                    if (tmp + 1 < talkModels.length) talkModels[tmp + 1].setStop(false);
                }
            };
            talkModels[i].setPosition(300f, -50f - i * 100f);
            if (i != 0) talkModels[i].setStop(true);
            addModel(talkModels[i]);
        }

    }

    /**
     * 会話文が全表示されているかを判定します。
     */
    public boolean isAllShow() {
        if (talkModels == null) return false;
        return talkModels[talkModels.length - 1].isAllShow();
    }

    /**
     * 会話が終了したときの処理です。
     */
    public abstract void endStory();
}
