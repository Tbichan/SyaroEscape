package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.NetworkGameScene;
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

public class EnvironmentNetworkOtherPlayerViewModel extends EnvironmentViewModel {

    // クエリ全体
    private String[] querys;

    // クエリ行数
    private int queryCnt = 0;

    // リプレイモードか
    private boolean replay = false;

    // サーバ問い合わせ用
    private MyHttp myHttp = null;

    public EnvironmentNetworkOtherPlayerViewModel(GlView glView, SceneBase sceneBase, String name, int id, int level) {
        super(glView, sceneBase, name, id, level);

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
                    NetworkGameScene sceneBase = (NetworkGameScene)getScene();

                    // ファイルの名前
                    String fileName = sceneBase.getFileName();

                    // 相手のIDを取得
                    final int otherId = sceneBase.getOtherPlayerViewModel().getId();

                    Log.d("net", NetWorkManager.DOMAIN + "sql/send/receive.py?id=" + otherId + "&num=" + queryCnt + "&filename=" + fileName);
                    Log.d("net", NetWorkManager.DOMAIN_SECOND + "sql/send/receive.py?id=" + otherId + "&num=" + queryCnt + "&filename=" + fileName);
                    // サーバから行動を取得する。
                    myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/receive.py?id=" + otherId + "&num=" + queryCnt + "&filename=" + fileName) {

                        // 接続成功時
                        @Override
                        public void success() {
                            // 表示
                            try {
                                String query = result().replace("\n", "");

                                Log.d("net", "query:" + query);

                                if (query.length() <= 2) {
                                    myHttp = null;
                                    return;
                                }

                                // ID情報を分割

                                // end, fastだけ特別
                                if (query.contains("fast")) {
                                    threeMove(false);
                                } else if (query.contains("end")) {
                                    endTurn(false);
                                } else {
                                    queryEnv(query, false);
                                }
                                Log.d("net:" + queryCnt, query);
                                queryCnt++;
                                myHttp = null;
                            } catch (Exception e) {

                            }
                        }

                        // 接続失敗時
                        @Override
                        public void fail(Exception e) {
                            Log.d("net", "接続エラー:" + e.toString());

                        }

                    }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/receive.py?id=" + otherId + "&num=" + queryCnt + "&filename=" + fileName);

                    myHttp.connect();
                }

            }
        }
    }
    /*

    // 環境にクエリとして送ります。(ネットワーク送信)
    public void queryEnv(String query, boolean network) {
        queryEnv(query);

        if (network == true) {

            // リプレイファイル取得
            String fileName = ((NetworkGameScene)getScene()).getFileName();

            // サーバに送る
            MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/send.py?query=" + query + "&id=" + getId() + "&filename=" + fileName) {

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

            }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/send.py?query=" + query + "&id=" + getId() + "&filename=" + fileName);

            myHttp.connect();
        }
    }

    // 移動しないで終了します。
    @Override
    public void endTurn(boolean net) {

        String query = "end";

        queryEnv(query, net);

    }

    /**
     * 交代間隔を設定します。
     *
    protected void setActInterval(int interval, boolean net) {
        setActInterval(interval);

        // クエリ送信
        queryEnv("fast:" + interval, net);
    }*/
}
