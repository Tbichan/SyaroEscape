package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class ResultViewModel extends GlViewModel {

    // 背景のModel
    private GlModel bgModel;

    // fade
    private FadeViewModel fadeViewModel;

    public ResultViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        bgModel = new GlModel(this, "resultModel") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        bgModel.setTextureId(R.drawable.win);
        bgModel.setSize(1700, 350);
        bgModel.setPosition((GlView.VIEW_WIDTH - bgModel.getWidth()) * 0.5f, (GlView.VIEW_HEIGHT - bgModel.getHeight()) * 0.5f);

        addModel(bgModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (isVisible() && ev.getAction() == MotionEvent.ACTION_DOWN) {
            // フェードアウト
            fadeViewModel.startFadeOut();
        }
        return true;
    }

    /**
     * 結果を表示します。
     */
    public void showResult(boolean winFlg) {
        setVisible(true);
        if (winFlg) bgModel.setTextureId(R.drawable.win);
        else bgModel.setTextureId(R.drawable.lose);
    }

    public FadeViewModel getFadeViewModel() {
        return fadeViewModel;
    }

    public void setFadeViewModel(FadeViewModel fadeViewModel) {
        this.fadeViewModel = fadeViewModel;
    }
}
