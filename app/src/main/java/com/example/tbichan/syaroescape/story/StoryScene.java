package com.example.tbichan.syaroescape.story;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.VibrationScene;
import com.example.tbichan.syaroescape.common.model.StoryLoader;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.TalkViewModel;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.GlStringBitmap;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.story.viewmodel.BGViewModel;

import java.util.HashSet;

/**
 * Created by tbichan on 2017/12/09.
 */

public class StoryScene extends VibrationScene {

    // パーティクル
    private ParticleViewModel particleViewModel;

    // ストーリーid
    private int storyId = 1;

    // シーンのロード
    @Override
    public void load(GlView glView) {

        // 画像を指定
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);
        addBitmap(R.drawable.load_bg);

        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.flower_button);
        addBitmap(R.drawable.story1_bg);
        addBitmap(R.drawable.syaro_menu);
        addBitmap(R.drawable.chino_menu);
        addBitmap(R.drawable.cocoa_menu);

        // 読み込む必要のある文字列を取得
        StoryLoader storyLoader = new StoryLoader("story" + storyId + ".txt");
        storyLoader.load();

        HashSet<String> needStrings = storyLoader.getLoadNeedStringList();

        for (String text: needStrings) {
            addBitmap(new GlStringBitmap(text));
        }

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

        // ボタン部分
        glView.addViewModel(new TalkViewModel(glView, this, "TalkViewModel", "story" + storyId + ".txt"){

            /**
             * 会話が終了したときの処理です。
             */
            @Override
            public void endStory(){
                // フェードアウト
                if (fadeViewModel != null) {
                    fadeViewModel.startFadeOut();
                }
            }
        });

        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);

    }

    public void setStoryId(int id) {
        storyId = id;
    }

}
