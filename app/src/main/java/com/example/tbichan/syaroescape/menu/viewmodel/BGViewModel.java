package com.example.tbichan.syaroescape.menu.viewmodel;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.SEManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/12/09.
 */

public class BGViewModel extends GlViewModel {

    // 背景のModel
    private GlModel bgModel;

    // オプションボタン
    private GlButton optionButton;

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

        bgModel.setTextureId(R.drawable.menu_bg);
        bgModel.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
        bgModel.setPosition(0, 0);

        optionButton = new GlButton(this, "option") {
            @Override
            public void onClick() {
                if (isVisible()) {
                    MainActivity.showOptionDialog(null);
                    // 効果音再生
                    SEManager.getInstance().playSE(R.raw.button_click);
                }
            }


            @Override
            public void onTex() {

            }

            @Override
            public void upTex() {

            }
        };

        optionButton.setSize(128, 128);
        optionButton.setPosition(0f, GlView.VIEW_HEIGHT - optionButton.getHeight());
        optionButton.setTextureId(R.drawable.gear);

        addModel(bgModel);
        addModel(optionButton);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);
    }

    public void hideGear() {
        optionButton.setVisible(false);
    }

}
