package com.example.tbichan.syaroescape.title.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.model.GlModelColor;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class TitleLogoViewModel extends GlViewModel {

    // 背景のModel
    private GlModel bgModel;

    // fadeViewModel
    private FadeViewModel fadeViewModel;

    public void setFadeViewModel(FadeViewModel fadeViewModel) {
        this.fadeViewModel = fadeViewModel;
    }

    public TitleLogoViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        GlModelColor plane = new GlModelColor(this, "plane");
        //fadePlane.setAlpha(1.0f);
        plane.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        plane.setRGBA(1f, 1f, 1f, 1.0f);
        addModel(plane);

        bgModel = new GlModel(this, "logo") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        bgModel.setTextureId(R.drawable.name);
        bgModel.setSize(600, 300);
        bgModel.setPosition(GlView.VIEW_WIDTH * 0.5f - bgModel.getWidth() * 0.5f, GlView.VIEW_HEIGHT * 0.5f - bgModel.getHeight() * 0.5f);

        addModel(bgModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (getCnt() == 120) {
            fadeViewModel.startFadeOut();
        }
    }

}
