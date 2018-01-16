package com.example.tbichan.syaroescape.title;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
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
import com.example.tbichan.syaroescape.sound.BGMManager;
import com.example.tbichan.syaroescape.sound.MyBGM;
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

        addSE(R.raw.decision);

        // 画像を指定
        addBitmap(R.drawable.particle);
        addBitmap(R.drawable.name);
        addBitmap(R.drawable.title_bg);
        addBitmap(R.drawable.title_logo);
        addBitmap(R.drawable.game_name);


        final BGViewModel bgviewmodel = new BGViewModel(glView, this, "背景");
        bgviewmodel.setVisible(false);
        glView.addViewModel(bgviewmodel);

        TitleViewModel titleviewmodel = new TitleViewModel(glView, this, "たいとる");


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
                if (fadeOutEndCnt == -1) {
                    fadeOutEndCnt = getCnt();
                    titleLogoViewModel.setVisible(false);
                    bgviewmodel.setVisible(true);
                    // 音設定
                    MediaPlayer player = MediaPlayer.create(MainActivity.getContext(),R.raw.bgm_title);
                    MyBGM myBGM = new MyBGM(player);
                    myBGM.setLoop(true);
                    BGMManager.getInstance().addSE(myBGM);
                } else {
                    // 2週目はメインへ
                    SceneManager.getInstance().setNextScene(new MenuScene());;
                }

            }
        };
        fadeViewModel.setInSpeed(1.0f);
        fadeViewModel.startFadeIn();

        glView.addViewModel(titleviewmodel);

        titleLogoViewModel.setFadeViewModel(fadeViewModel);
        titleviewmodel.setFadeViewModel(fadeViewModel);

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
