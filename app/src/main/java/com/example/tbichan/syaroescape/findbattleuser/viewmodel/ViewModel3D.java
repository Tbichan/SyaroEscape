package com.example.tbichan.syaroescape.findbattleuser.viewmodel;

import android.content.Context;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel3D;
import com.example.tbichan.syaroescape.opengl.model.GlModel3DCube;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * Created by tbichan on 2017/12/14.
 */

public class ViewModel3D extends GlViewModel {

    public ViewModel3D(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        // 表示する文字
        GlModel3D bgStr = new GlModel3D(this, "bgStr_wait") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };

        bgStr.setTexture(R.drawable.tushin);
        bgStr.setSize(1500, 300);

        // ���S�ɏo��
        bgStr.setPosition(GlView.VIEW_WIDTH*0.5f-750, GlView.VIEW_HEIGHT*0.5f-150);

        addModel(bgStr);
    }

    @Override
    public void start() {
    }
}
