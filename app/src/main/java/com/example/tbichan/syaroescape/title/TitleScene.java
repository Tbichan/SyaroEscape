package com.example.tbichan.syaroescape.title;

import android.content.Context;
import android.util.Log;

import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.title.viewmodel.TitleViewModel;

import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.title.viewmodel.UIViewModel;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class TitleScene extends SceneBase {

    // シーンのロード
    @Override
    public void load(GlView glView) {
        System.out.println("title");
        TitleViewModel titleViewModel = new TitleViewModel(glView, this, "TitleViewModel");
        UIViewModel uiViewModel = new UIViewModel(glView, this, "UIViewModel");
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel");
        ParticleViewModel particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");

        glView.addViewModel(titleViewModel);
        glView.addViewModel(uiViewModel);
        glView.addViewModel(fadeViewModel);
        glView.addViewModel(particleViewModel);

    }

    // シーンの更新
    @Override
    public void update(){

        if (getCnt() == 300) {

            System.out.println(getCnt());

            MyHttp myHttp = new MyHttp("http://192.168.3.130") {

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
                    System.out.println("接続エラー:" + e.toString());
                }

            };
            myHttp.connect();

        }

    }
}
