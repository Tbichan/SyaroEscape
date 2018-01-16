package com.example.tbichan.syaroescape.choicestory.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.common.model.MoveModel;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.story.StoryScene;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class RoadViewModel extends GlViewModel {

    private final int storyNum = 3;

    // 道のりのモデル(丸)
    private GlModel[] roadCircles;
    // 道のりのモデル(棒)
    private GlModel[] roads;

    // 道のりのモデル(丸)
    private GlModel[] roadCirclesEnable;
    // 道のりのモデル(棒)
    private GlModel[] roadsEnable;

    // キャラModel
    private MoveModel charaModel;

    // 背景VM
    private BGViewModel bgViewModel;

    // FadeViewModel
    private FadeViewModel fadeViewModel;

    // 選択ストーリーId
    private int choiceId = 0;

    // 選択したかどうか
    private boolean select = false;

    public RoadViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        // キャラ作成
        charaModel = new MoveModel(this, "chara") {
            @Override
            public void start() {

            }

            @Override
            public void onClick() {
                if (!isMove() && choiceId > 0 && !select) {

                    // 効果音再生
                    SEManager.getInstance().playSE(R.raw.decision);

                    StoreManager.save("story", 1);

                    fadeViewModel.startFadeOut();

                    select = true;


                }
            }

        };
        charaModel.setTextureId(R.drawable.syaro_sd);
        charaModel.setPosition(0, 80);
        charaModel.setSize(250, 300);

        createRoad(0 + 1);
        addModel(charaModel);

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

    /**
     * 道を作成します。
     */
    public void createRoad(int clearNum) {
        roadCircles = new GlModel[storyNum + 1];
        roadCirclesEnable = new GlModel[clearNum + 1];
        roads = new GlModel[storyNum];
        roadsEnable = new GlModel[clearNum];
        for (int i = 0; i < storyNum + 1; i++) {

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

        for (int i = 0; i < storyNum; i++) {
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

        for (int i = 0; i < clearNum + 1; i++) {

            final int num = i;

            // 丸
            roadCirclesEnable[i] = new GlButton(this, "circle_enable" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                }

                @Override
                public void onClick() {

                    if (choiceId != num) {

                        choiceId = num;

                        // キャラ移動
                        charaModel.startMove(num * 500, 80f, 10);

                        // 背景に通知
                        bgViewModel.choiceStoryId(choiceId);

                        // 効果音再生
                        SEManager.getInstance().playSE(R.raw.button_click);
                    }
                }


                // 押したときのテクスチャ変更
                @Override
                public void onTex() {
                }

                // 離したときのテクスチャ変更
                @Override
                public void upTex() {
                }
            };

            roadCirclesEnable[i].setTextureId(R.drawable.road_circle_enable);
            roadCirclesEnable[i].setSize(250 *  0.8f, 200 * 0.8f);
            roadCirclesEnable[i].setPosition(i * 500 + 25f, 20f);
            //roadCirclesEnable[i].setAlpha(0.5f);
            addModel(roadCirclesEnable[i]);

        }

        for (int i = 0; i < clearNum; i++) {
            // 棒
            roadsEnable[i] = new GlModel(this, "road_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {
                    float nextAlpha = (float) (0.5f * (Math.cos(getCnt() / 20.0f) + 1));
                    nextAlpha = 0.75f * nextAlpha + 0.25f;
                    //setAlpha(nextAlpha);
                }
            };

            roadsEnable[i].setTextureId(R.drawable.road_enable);
            roadsEnable[i].setSize(350, 125 * 0.7f);
            roadsEnable[i].setPosition(i * 500 + 200, 100 - 62.5f + 18.75f);
            addModel(roadsEnable[i]);
        }
    }

    public void setBgViewModel(BGViewModel bgViewModel) {
        this.bgViewModel = bgViewModel;
    }

    public void setFadeViewModel(FadeViewModel fadeViewModel) {
        this.fadeViewModel = fadeViewModel;
    }
}
