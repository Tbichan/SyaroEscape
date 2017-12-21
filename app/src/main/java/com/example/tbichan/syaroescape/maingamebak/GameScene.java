package com.example.tbichan.syaroescape.maingamebak;

import android.content.Context;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.BackGroundViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.OpponentViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.PlayerViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.UIViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class GameScene extends SceneBase {

    // ビュー
    //TitleGlView glTitleView;

    int cnt = 0;

    // シーンのロード
    @Override
    public void load(GlView glView) {
        System.out.println("game");

        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.load_bg);
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);

        addBitmap(R.drawable.cup);
        addBitmap(R.drawable.button);
        addBitmap(R.drawable.com);
        addBitmap(R.drawable.player);
        addBitmap(R.drawable.desk);
        addBitmap(R.drawable.sand);

        // vmの追加
        NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
        nowLoadViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(nowLoadViewModel);

        BackGroundViewModel backGroundViewModel = new BackGroundViewModel(glView, this, "BG");
        glView.addViewModel(backGroundViewModel);
        PlayerViewModel playerViewModel = new PlayerViewModel(glView, this, "PlayerViewModel");
        playerViewModel.setPosition(100, 256);
        glView.addViewModel(playerViewModel);
        OpponentViewModel enemyViewModel = new OpponentViewModel(glView, this, "EnemyViewModel");
        enemyViewModel.setPosition(1252, 256);
        glView.addViewModel(enemyViewModel);
        UIViewModel uiViewModel = new UIViewModel(glView, this, "ActButtonUIViewModel"); // UIVM
        glView.addViewModel(uiViewModel);

        ParticleViewModel particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(particleViewModel);

        //FadeViewModel fadeViewModel = new FadeViewModel(MainActivity.getGlView(), mContext, "FadeViewModel2");
        //fadeViewModel.setFadeInCount(1000);
        //glView.addViewModel(fadeViewModel);

    }

    // シーンの更新
    @Override
    public void update(){
        //Log.d("a", "aaaaaaaaaaaaaaaaaaa");
        cnt++;

    }
}
