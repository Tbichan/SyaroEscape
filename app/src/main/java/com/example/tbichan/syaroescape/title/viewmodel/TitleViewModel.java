package com.example.tbichan.syaroescape.title.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.common.model.MoveModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.title.*;
import com.example.tbichan.syaroescape.title.TitleScene;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5516096h on 2017/12/11.
 */

public class TitleViewModel extends GlViewModel {

    GlModel titlebutton;

    // タップした時のカウンタ
    private int tapCnt = -1;

    public TitleViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        titlebutton = new GlModel(this, "タイトルボタン") {

            @Override
            public void start() {

            }
            @Override
            public void update() {

            }
        };
        titlebutton.setTextureId(R.drawable.title_logo);

        titlebutton.setPosition(GlView.VIEW_WIDTH * 0.5f - 750, GlView.VIEW_HEIGHT * 0.5f - 200 - 325 - 100);
        titlebutton.setSize(1500, 400);

        addModel(titlebutton);

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        titlebutton.setPosition(GlView.VIEW_WIDTH * 0.5f - 750, GlView.VIEW_HEIGHT * 0.5f - 200 - 325 - 100 + 20 * (float) Math.sin(getCnt()
                * 0.025));

        if (tapCnt != -1 && getCnt() - tapCnt >= 60) {
            SceneManager.getInstance().setNextScene(new MenuScene());
        }

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);

        if (ev.getAction() == MotionEvent.ACTION_DOWN && isVisible() && getCnt() >= 300) {
            if (tapCnt == -1) tapCnt = getCnt();

        }

        return true;
    }
}
