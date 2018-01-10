package com.example.tbichan.syaroescape.choicestory;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.choicestory.viewmodel.RoadViewModel;
import com.example.tbichan.syaroescape.choicestory.viewmodel.RoadViewModel2;
import com.example.tbichan.syaroescape.common.VibrationScene;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.choicestory.viewmodel.BGViewModel;

/**
 * Created by tbichan on 2017/12/09.
 */

public class ChoiceStoryScene extends VibrationScene {

    // パーティクル
    private ParticleViewModel particleViewModel;

    // シーンのロード
    @Override
    public void load(GlView glView) {

        // 画像を指定
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);
        addBitmap(R.drawable.load_bg);

        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.story_choice_bg2);
        addBitmap(R.drawable.road_circle);
        addBitmap(R.drawable.road_circle_enable);
        addBitmap(R.drawable.road);
        addBitmap(R.drawable.road_enable);

        addBitmap(R.drawable.syaro_sd);
        addBitmap(R.drawable.choice_story);

        NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
        nowLoadViewModel.setSceneImgLoadedDraw(false);

        particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);

        glView.addViewModel(nowLoadViewModel);
        glView.addViewModel(particleViewModel);

    }

    // シーンの更新
    @Override
    public void update(){
        super.update();

    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        // 背景
        BGViewModel bgViewModel = new BGViewModel(glView, this, "BGViewModel");
        glView.addViewModel(bgViewModel);

        // fadein
        final FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel"){
            @Override
            // フェードアウト終了時の処理
            public void endFadeOut() {
                SceneManager.getInstance().setNextScene(new GameScene());
            }
        };
        fadeViewModel.setInSpeed(1.0f);
        fadeViewModel.startFadeIn();

        RoadViewModel roadViewModel = new RoadViewModel(glView, this, "RoadViewModel");
        roadViewModel.setPosition(200, GlView.VIEW_HEIGHT * 0.5f - 100f + 200f);
        roadViewModel.setBgViewModel(bgViewModel);
        glView.addViewModel(roadViewModel);
        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);

    }

}
