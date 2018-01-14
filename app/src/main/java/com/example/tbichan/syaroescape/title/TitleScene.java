package com.example.tbichan.syaroescape.title;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.ParticleModel;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.menu.viewmodel.CharaViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.PlayerDetailViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.UIChoiceViewModel;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.title.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.title.viewmodel.TitleLogoViewModel;
import com.example.tbichan.syaroescape.title.viewmodel.TitleViewModel;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5516096h on 2017/12/11.
 */

public class TitleScene extends SceneBase {

    // タップした時のカウンタ
    private int tapCnt = -1;

    private ParticleViewModel particleViewModel;

    // シーンのロード
    @Override
    public void load(GlView glView) {

        // 画像を指定
        addBitmap(R.drawable.particle);
        addBitmap(R.drawable.name);
        addBitmap(R.drawable.title_bg);
        addBitmap(R.drawable.title_logo);


        final BGViewModel bgviewmodel = new BGViewModel(glView, this, "背景");
        bgviewmodel.setVisible(false);
        glView.addViewModel(bgviewmodel);

        TitleViewModel titleviewmodel = new TitleViewModel(glView, this, "たいとる");
        glView.addViewModel(titleviewmodel);

        // ロゴ表示用
        final TitleLogoViewModel titleLogoViewModel = new TitleLogoViewModel(glView, this, "logoViewModel");

        // fadein
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel"){


            // フェードアウト後のカウンタ
            private int fadeOutEndCnt = -1;

            @Override
            public void update(GL10 gl) {
                super.update(gl);

                if (fadeOutEndCnt != -1 && getCnt() - fadeOutEndCnt == 30) {
                    startFadeIn();
                }
            }

            @Override
            public void endFadeOut() {
                fadeOutEndCnt = getCnt();
                titleLogoViewModel.setVisible(false);
                bgviewmodel.setVisible(true);

            }
        };
        fadeViewModel.setInSpeed(1.0f);
        fadeViewModel.startFadeIn();

        titleLogoViewModel.setFadeViewModel(fadeViewModel);

        glView.addViewModel(titleLogoViewModel);



        glView.addViewModel(fadeViewModel);

        particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(particleViewModel);

    }

    // シーンの更新
    @Override
    public void update(){

    }


    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);



    }
}
