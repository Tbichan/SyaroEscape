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

public class RoadViewModel extends GlViewModel {

    // 道のりのモデル(丸)
    private GlModel[] roadCircles;
    // 道のりのモデル(棒)
    private GlModel[] roads;

    public RoadViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        final int storyNum = 3;

        roadCircles = new GlModel[storyNum];
        roads = new GlModel[storyNum - 1];
        for (int i = 0; i < storyNum; i++) {

            // 丸
            roadCircles[i] = new GlModel(this, "circle_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                }
            };

            roadCircles[i].setTextureId(R.drawable.road_circle);
            roadCircles[i].setSize(250, 200);
            roadCircles[i].setPosition(i * 500, 0);

            addModel(roadCircles[i]);

        }

        for (int i = 0; i < storyNum - 1; i++) {
            // 棒
            roads[i] = new GlModel(this, "road_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                }
            };

            roads[i].setTextureId(R.drawable.road);
            roads[i].setSize(400, 125);
            roads[i].setPosition(i * 500 + 200, 100 - 62.5f);
            addModel(roads[i]);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

}
