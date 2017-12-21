package com.example.tbichan.syaroescape.menu.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.model.GlModelColor;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class BGViewModel extends GlViewModel {

    // 背景のModel
    private GlModel bgModel;

    public BGViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        bgModel = new GlModel(this, "BGModel") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        bgModel.setTexture(R.drawable.menu_bg);
        bgModel.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        bgModel.setPosition(0, 0);

        addModel(bgModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

}
