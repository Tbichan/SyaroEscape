package com.example.tbichan.syaroescape.story;

import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.TalkViewModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.story.model.BGViewModel;

/**
 * Created by tbichan on 2017/12/09.
 */

public class StoryScene extends SceneBase {

    // パーティクル
    private ParticleViewModel particleViewModel;

    // シーンのロード
    @Override
    public void load(GlView glView) {

        // 画像を指定
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);
        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.flower_button);
        addBitmap(R.drawable.menu_bg);


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

    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        // 背景
        BGViewModel bgViewModel = new BGViewModel(glView, this, "BGViewModel");
        glView.addViewModel(bgViewModel);

        // ボタン部分
        glView.addViewModel(new TalkViewModel(glView, this, "TalkViewModel"));

        // fadein
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel");
        fadeViewModel.setInSpeed(1.0f);
        fadeViewModel.startFadeIn();
        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);

    }

}
