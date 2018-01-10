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

public class BGViewModel extends GlViewModel {

    // 背景のModel
    private GlModel bgModel;

    // ストーリー選択の文字
    private GlModel choiceStrModel;

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

        bgModel.setTextureId(R.drawable.story_choice_bg2);
        bgModel.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        bgModel.setPosition(0, 0);

        addModel(bgModel);

        // ストーリー選択の文字
        choiceStrModel = new GlModel(this, "choice") {

            private float initY = 0f;

            @Override
            public void start() {
                initY = getY();
            }

            @Override
            public void update() {

                // ゆらゆら揺れる
                setY(20f * ((float)Math.sin(getCnt() / 20f)) + initY);

            }
        };
        choiceStrModel.setTextureId(R.drawable.choice_story);
        choiceStrModel.setSize(1536, 200);
        choiceStrModel.setPosition(GlView.VIEW_WIDTH * 0.5f - 768f, 400f);
        choiceStrModel.setUV(0f, 0.75f, 1f, 1f);
        addModel(choiceStrModel);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

    public void choiceStoryId(int id) {
        choiceStrModel.setUV(0f, 3f / 4f - id / 4f, 1f, 1f - id / 4f);
    }

}
