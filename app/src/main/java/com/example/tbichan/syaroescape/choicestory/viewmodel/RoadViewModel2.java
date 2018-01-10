package com.example.tbichan.syaroescape.choicestory.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class RoadViewModel2 extends GlViewModel {

    final int storyNum = 3;


    public RoadViewModel2(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {


        GlModel roadEnable = new GlModel(this, "road_e") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };
        roadEnable.setTextureId(R.drawable.road_2);
        roadEnable.setSize(350 * storyNum, 125);
        roadEnable.setUV(0f, 0.5f, storyNum, 1f);
        roadEnable.setPosition(0, 0);
        addModel(roadEnable);

        GlModel roadEnable2 = new GlModel(this, "road_e") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }
        };
        roadEnable2.setTextureId(R.drawable.road_enable2);
        roadEnable2.setSize(350 * storyNum, 125);
        roadEnable2.setUV(0f, 0.5f, storyNum, 1f);
        roadEnable2.setPosition(0, 0);
        addModel(roadEnable2);

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

}
