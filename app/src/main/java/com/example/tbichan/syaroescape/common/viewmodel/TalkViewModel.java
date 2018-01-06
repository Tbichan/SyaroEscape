package com.example.tbichan.syaroescape.common.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.TalkModel;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/10.
 */

public class TalkViewModel extends GlViewModel {

    private TalkModel[] talkModels;

    private Bitmap[] strBits;

    public TalkViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

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
        bgModel.setTexture(R.drawable.flower_button);

        addModel(bgModel);

        // 会話文
        setTalk("こんにちはー\nココアいるー？");
        /*
        talkModels = new TalkModel[2];
        talkModels[0] = new TalkModel(this, "talkModel", "こんにちはー！こんにちはー！こんにちはー！こんに"){
            @Override
            public void allShow() {
                talkModels[1].setStop(false);
            }
        };
        talkModels[0].setPosition(300f, -560f);
        addModel(talkModels[0]);

        talkModels[1] = new TalkModel(this, "talkModel", "ラビットハウスはここ？");
        talkModels[1].setPosition(300f, -660f);
        talkModels[1].setStop(true);
        addModel(talkModels[1]);
        */

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
            if (isAllShow()) {
                setTalk("こんにちはー\nココアいるー？あ");
            }
        }
        return true;
    }

    /**
     * テキストを表示させます。
     * @param text
     */
    public void setTalk(String text) {
        String[] lines = text.split("\n");

        // 前の会話を削除
        if (talkModels != null) {
            for (TalkModel talkModel: talkModels) {
                talkModel.delete();
            }
        }

        talkModels = new TalkModel[lines.length];

        for (int i = 0; i < lines.length; i++) {

            final int tmp = i;

            talkModels[i] = new TalkModel(this, "talkModel_" + i, lines[i]){
                @Override
                public void allShow() {
                    if (tmp + 1 < talkModels.length) talkModels[tmp + 1].setStop(false);
                }
            };
            talkModels[i].setPosition(300f, -560f - i * 100f);
            if (i != 0) talkModels[i].setStop(true);
            addModel(talkModels[i]);
        }

    }

    /**
     * 会話文が全表示されているかを判定します。
     */
    public boolean isAllShow() {
        return talkModels[talkModels.length - 1].isAllShow();
    }
}
