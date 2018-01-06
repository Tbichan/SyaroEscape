package com.example.tbichan.syaroescape.maingame;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.viewmodel.ActButtonUIViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentNetworkPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentOtherPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StatusViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StringViewModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.scene.timer.SceneTimer;
import com.example.tbichan.syaroescape.ui.UIListener;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class NetworkGameScene extends GameScene implements GlObservable {

    // シーンのロード
    @Override
    public void load(GlView glView) {
        super.load(glView);
        // サーバに送る
        MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/deleter.py") {

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
                Log.d("net", "接続エラーss:" + e.toString());

            }

        }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/deleter.py");

        myHttp.connect();
    }

    /**
     * 共通シード作成
     */
    @Override
    public int createGlobalSeed() {
        return StoreManager.restoreInteger("globalSeed");
    }

    /**
     * Player用VM作成
     */
    @Override
    public EnvironmentViewModel createPlayerViewModel(GlView glView) {

        int playerId = -1;

        // 待ちリストに登録
        if (StoreManager.containsKey("player_id")) {

            // プレイヤー名をよみこみ
            playerId = StoreManager.restoreInteger("player_id");
        }

        // 環境を作成26656
        return new EnvironmentViewModel(glView, this, "Env_0", playerId);
    }

    /**
     * 相手用VM作成
     */
    @Override
    public EnvironmentViewModel createOtherViewModel(GlView glView) {

        int otherId = -1;

        // 待ちリストに登録
        if (StoreManager.containsKey("other_id")) {

            // プレイヤー名をよみこみ
            otherId = StoreManager.restoreInteger("other_id");
        }

        // 環境を作成26656
        return new EnvironmentNetworkPlayerViewModel(glView, this, "Env_1", otherId);
    }

}
