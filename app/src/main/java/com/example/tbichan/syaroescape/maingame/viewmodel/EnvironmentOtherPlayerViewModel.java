package com.example.tbichan.syaroescape.maingame.viewmodel;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.ai.NormalAI;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * 相手用の環境
 * Created by tbichan on 2017/12/10.
 */

public class EnvironmentOtherPlayerViewModel extends EnvironmentViewModel {

    // クエリ全体
    private String[] querys;

    // クエリ行数
    private int queryCnt = 0;

    // リプレイモードか
    private boolean replay = false;

    // サーバ問い合わせ用
    private MyHttp myHttp = null;

    // 乱数発生器
    private Random r;

    public EnvironmentOtherPlayerViewModel(GlView glView, SceneBase sceneBase, String name, int id, int level) {
        super(glView, sceneBase, name, id, level);

    }

    @Override
    public void start() {
        super.start();

        // ファイル読み込み
        String text = MainActivity.loadFile("replay_001.replay");
        querys = text.split("\n");

        // シード値設定
        r = new Random(getEnv().getSeed());
    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        if (isTurn()) {
            if ((getCnt() - getTurnCnt()) >= 240 && (getCnt() - getTurnCnt()) % 120 == 0) {


                if (!replay) {
                    // 移動
                    final Environment env = getEnv();

                    // AIに計算させる。
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            NormalAI normalAI = new NormalAI(r);
                            int nextIndex = normalAI.calc(env);

                            if (nextIndex == -1) {
                                endTurn(false);
                            } else {

                                String query = "move:" + env.getPlayerMapPlayerIndex() + "," + nextIndex;
                                queryEnv(query, false);
                            }
                        }
                    }).start();


                    /*

                    // プレイヤー位置取得
                    final int playerX = getPlayer().getMapX();
                    final int playerY = getPlayer().getMapY();
                    // プレイヤーの移動できるところ一覧を取得します。
                    final ArrayList<Integer> spriteEnableMoveList = new ArrayList<>(getSpriteEnableMoveList(playerY * Environment.MAP_SIZE + playerX, 1));

                    if (spriteEnableMoveList.size() != 0) {

                        // 移動をランダムに選択
                        int nextIndex = spriteEnableMoveList.get((int) (Math.random() * spriteEnableMoveList.size()));

                        while (playerY * Environment.MAP_SIZE + playerX == nextIndex) {
                            nextIndex = spriteEnableMoveList.get((int) (Math.random() * spriteEnableMoveList.size()));
                        }

                        final int nextX = nextIndex % Environment.MAP_SIZE;
                        final int nextY = nextIndex / Environment.MAP_SIZE;

                        // 環境にクエリ通達
                        final int playerIndex = getPlayer().getMapIndex();

                        String query = "move:" + playerIndex + "," + nextIndex;
                        queryEnv(query);
                    } else  {
                        endTurn();
                    }
                    */

                } else {
                    // リプレイ
                    queryEnv(querys[queryCnt++], false);
                }


                /*
                if (myHttp == null) {

                    // 今のｓ－ン
                    GameScene sceneBase = (GameScene)SceneManager.getInstance().getNowScene();

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
                }*/

            }
        }
    }
}
