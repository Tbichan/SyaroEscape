package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * 相手用の環境
 * Created by tbichan on 2017/12/10.
 */

public class EnvironmentNetworkPlayerViewModel extends EnvironmentViewModel {

    // クエリ全体
    private String[] querys;

    // クエリ行数
    private int queryCnt = 0;

    // リプレイモードか
    private boolean replay = false;

    // サーバ問い合わせ用
    private MyHttp myHttp = null;

    public EnvironmentNetworkPlayerViewModel(GlView glView, SceneBase sceneBase, String name, int id) {
        super(glView, sceneBase, name, id);

    }

    @Override
    public void start() {
        super.start();

        // ファイル読み込み
        String text = MainActivity.loadFile("replay_001.replay");
        querys = text.split("\n");
    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (isTurn()) {
            if ((getCnt() - getTurnCnt()) >= 120 && (getCnt() - getTurnCnt()) % 60 == 0) {

                if (myHttp == null) {

                    // 今のシ－ン
                    GameScene sceneBase = (GameScene)getScene();

                    // サーバから行動を取得する。
                    myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/receive.py?num=" + sceneBase.getActCnt()) {

                        // 接続成功時
                        @Override
                        public void success() {
                            // 表示
                            try {
                                String query = result().replace("\n", "");
                                queryEnv(query, false);
                                Log.d("net", query);
                                myHttp = null;
                            } catch (Exception e) {

                            }
                        }

                        // 接続失敗時
                        @Override
                        public void fail(Exception e) {
                            Log.d("net", "接続エラー:" + e.toString());

                        }

                    }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/receive.py?num=" + sceneBase.getActCnt());

                    myHttp.connect();
                }

            }
        }
    }
}
