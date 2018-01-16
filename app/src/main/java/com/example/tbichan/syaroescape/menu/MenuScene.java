package com.example.tbichan.syaroescape.menu;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.CharaViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.PlayerDetailViewModel;
import com.example.tbichan.syaroescape.menu.viewmodel.UIChoiceViewModel;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.GlStringBitmap;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.BGMManager;
import com.example.tbichan.syaroescape.sound.MyBGM;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;

/**
 * Created by tbichan on 2017/12/09.
 */

public class MenuScene extends SceneBase {

    // パーティクル
    ParticleViewModel particleViewModel;

    // シーンのロード
    @Override
    public void load(GlView glView) {

        // 効果音を指定
        addSE(R.raw.choice);
        addSE(R.raw.decision);

        // 画像を指定
        addBitmap(R.drawable.load_bg);
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);
        addBitmap(R.drawable.particle);
        addBitmap(R.drawable.chino_menu);
        addBitmap(R.drawable.syaro_menu);
        addBitmap(R.drawable.fukidashi);
        addBitmap(R.drawable.menu_0);
        addBitmap(R.drawable.menu_1);
        addBitmap(R.drawable.menu_2);
        addBitmap(R.drawable.menu_3);
        addBitmap(R.drawable.menu_bg);

        // ID、プレイヤー名をよみこみ
        String playerName = "";
        try {
            playerName = "ID:" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.NETWORK_ID) + "　" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
        } catch (Exception e) {

        }

        if (playerName.length() >= 1) {
            addBitmap(new GlStringBitmap(playerName)
                    .setColor(Color.WHITE));
        }

        NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
        nowLoadViewModel.setSceneImgLoadedDraw(false);

        particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);

        glView.addViewModel(nowLoadViewModel);
        glView.addViewModel(particleViewModel);

        // ネット接続
        MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "/sql/show/show.py") {

            // 接続成功時
            @Override
            public void success() {
                // 表示
                try {
                    Log.d("net", result());
                } catch (Exception e) {

                }
            }

            // 接続失敗時
            @Override
            public void fail(Exception e) {
                Log.d("net", "接続エラー:" + e.toString());

            }

        }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "/sql/show/show.py");

        myHttp.connect();

        // 音設定
        MediaPlayer player = MediaPlayer.create(MainActivity.getContext(),R.raw.bgm_menu);
        MyBGM myBGM = new MyBGM(player);
        BGMManager.getInstance().addSE(myBGM);

    }

    @Override
    public void start() {

    }

    // シーンの更新
    @Override
    public void update(){

    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        BGViewModel bgViewModel = new BGViewModel(glView, this, "BGViewModel");
        CharaViewModel charaViewModel = new CharaViewModel(glView, this, "CharaViewModel");
        PlayerDetailViewModel playerDetailViewModel = new PlayerDetailViewModel(glView, this, "PlayerDetailViewModel");
        UIChoiceViewModel uiChoiceViewModel = new UIChoiceViewModel(glView, this, "UIChoiceViewModel");

        // キャラVMを登録
        uiChoiceViewModel.setCharaViewModel(charaViewModel);

        // fadein
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel");
        fadeViewModel.setInSpeed(1.0f);
        fadeViewModel.startFadeIn();

        uiChoiceViewModel.setFadeViewModel(fadeViewModel);

        glView.addViewModel(bgViewModel);
        glView.addViewModel(uiChoiceViewModel);
        glView.addViewModel(charaViewModel);
        glView.addViewModel(playerDetailViewModel);
        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);



    }

}
