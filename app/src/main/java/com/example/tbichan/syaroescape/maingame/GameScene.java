package com.example.tbichan.syaroescape.maingame;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentOtherPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StatusViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.UIViewModel;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class GameScene extends SceneBase implements GlObservable {

    // パーティクル
    ParticleViewModel particleViewModel;

    // 環境VM
    private EnvironmentViewModel environmentViewModel;
    private EnvironmentOtherPlayerViewModel environmentOtherPlayerViewModel;

    // ステータス
    private StatusViewModel statusViewModel;

    // 行動総回数
    private int actCnt = 0;

    // タップ座標
    private  float downX, downY;

    // 現在のプレイヤー(0...左、1...右)
    private int turn = 0;

    // カップに視点を置いているかどうか
    private boolean cupLook = false;

    // シーンのロード
    @Override
    public void load(GlView glView) {
        System.out.println("game");

        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.load_bg);
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);

        addBitmap(R.drawable.menu_bg);

        addBitmap(R.drawable.tile_0);
        addBitmap(R.drawable.syaro_menu);
        addBitmap(R.drawable.syaro_icon);
        addBitmap(R.drawable.enable);
        addBitmap(R.drawable.desk);
        addBitmap(R.drawable.cups);
        addBitmap(R.drawable.arrow);
        addBitmap(R.drawable.move_button);
        addBitmap(R.drawable.turnend_button);
        addBitmap(R.drawable.cancel_button);
        addBitmap(R.drawable.your_turn);
        addBitmap(R.drawable.com_turn);

        // vmの追加
        NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
        nowLoadViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(nowLoadViewModel);

        particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(particleViewModel);

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

    // シーンの更新
    @Override
    public void update(){

    }

    // タップ処理
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // シーンが読み込み切れていたら
        if (isSceneImgLoaded()) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = ev.getX();
                downY = ev.getY();
            }

            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                // タップしたら移動
                float vx = (ev.getX() - downX) * 2.0f;
                float vy = (downY - ev.getY()) * 2.0f;
                float tmpX = environmentViewModel.getX();
                float tmpY = environmentViewModel.getY();
                environmentViewModel.setPosition(tmpX + vx, tmpY + vy);
                environmentOtherPlayerViewModel.setPosition(2400f + tmpX + vx, tmpY + vy);
                //environmentViewModel.move(environmentViewModel.getX() + vx, environmentViewModel.getY() + vy, 1, true);
                //environmentOtherPlayerViewModel.move(2400f + environmentViewModel.getX() + vx, environmentViewModel.getY() + vy, 1, true);

                downX = ev.getX();
                downY = ev.getY();
            }
        }
        return true;
    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        // 背景
        glView.addViewModel(new BGViewModel(glView, this, "BG"));

        // 環境を作成
        environmentViewModel = new EnvironmentViewModel(glView, this, "Env_0", 26656);

        glView.addViewModel(environmentViewModel);

        // 環境を作成
        environmentOtherPlayerViewModel = new EnvironmentOtherPlayerViewModel(glView, this, "Env_1", 33146);
        environmentOtherPlayerViewModel.setX(2400);
        //environmentOtherPlayerViewModel.setDefaultX(2400);
        glView.addViewModel(environmentOtherPlayerViewModel);

        // UI作成
        UIViewModel uiViewModel = new UIViewModel(glView, this, "UIViewModel");
        uiViewModel.addEnvironmentViewModel(environmentViewModel);
        uiViewModel.addEnvironmentViewModel(environmentOtherPlayerViewModel);
        environmentViewModel.setUiViewModel(uiViewModel);
        glView.addViewModel(uiViewModel);

        // ステータス
        statusViewModel = new StatusViewModel(glView, this, "StatusViewModel");

        // 通知に追加
        environmentViewModel.addEnvGlObserver(statusViewModel);
        environmentOtherPlayerViewModel.addEnvGlObserver(statusViewModel);
        glView.addViewModel(statusViewModel);

        // fadein
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel");
        fadeViewModel.setInSpeed(1.2f);
        fadeViewModel.startFadeIn();

        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);

        // 自分を移動可能に
        setTurn(0);

    }

    // 報告を受けます。
    public void notify(Object o, String... params) {

        if (o instanceof EnvironmentViewModel) {

            // ターン交代時かどうか
            boolean turnFlg = false;

            // パラメータがないとき
            if (params == null || params.length == 0) {

                Log.d("hoge", "aaaab");

                // 自分の環境から
                if (o == environmentViewModel) {

                    if (environmentViewModel.getMoveCnt() % 2 == 0) {
                        Log.d("交代", environmentViewModel.getMoveCnt() + "");

                        // VM移動

                        // プレイヤー位置
                        float playerX = environmentOtherPlayerViewModel.getPlayer().getX();
                        float playerY = environmentOtherPlayerViewModel.getPlayer().getY();

                        // 相手のplayerの中心にVMを移動
                        /*
                        environmentViewModel.move(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                        environmentOtherPlayerViewModel.move(-2400f + 2400f  - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f),
                                -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                                */
                        lookAt(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                        // 相手を移動可能に
                        setTurn(1);
                        cupLook = false;

                    }

                    actCnt++;

                }

                // 相手の環境から
                else if (o == environmentOtherPlayerViewModel) {

                    if (environmentOtherPlayerViewModel.getMoveCnt() % 2 == 0) {
                        Log.d("交代", environmentOtherPlayerViewModel.getMoveCnt() + "");

                        // プレイヤー位置
                        float playerX = environmentViewModel.getPlayer().getX();
                        float playerY = environmentViewModel.getPlayer().getY();

                        // 相手のplayerの中心にVMを移動
                        /*
                        environmentViewModel.move(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                        environmentOtherPlayerViewModel.move(2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f),
                                -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                                */
                        lookAt(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                        // 自分を移動可能に
                        setTurn(0);
                        cupLook = false;

                    }

                    actCnt++;

                }
            }

            // パラメータがあるとき
            else if (params != null && params.length > 0) {

                Log.d("hoge", params[0]);
                // カップ追加時
                if (params[0].startsWith("cup:")) {

                    // 獲得したカップの位置を取得
                    int cupIndex = Integer.parseInt(params[0].replace("cup:", ""));
                    // プレイヤー位置
                    final float cupX = EnvSprite.parseX(cupIndex);
                    final float cupY = EnvSprite.parseY(cupIndex);

                    // 移動量
                    float moveParam = -2400f;
                    if (o == environmentViewModel) moveParam = 0f;

                    /*
                    environmentViewModel.move(moveParam - (cupX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(cupY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                    environmentOtherPlayerViewModel.move(moveParam + 2400f - (cupX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f),
                            -(cupY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                    */

                    lookAt(moveParam - (cupX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(cupY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                    cupLook = true;

                }

                // プレイヤーを見る
                if (cupLook && params[0].startsWith("playerLook")) {

                    if (o == environmentViewModel) {
                        // プレイヤー位置
                        float playerX = environmentViewModel.getPlayer().getX();
                        float playerY = environmentViewModel.getPlayer().getY();

                        // playerの中心にVMを移動
                        /*
                        environmentViewModel.move(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                        environmentOtherPlayerViewModel.move(2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f),
                                -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                                */

                        lookAt(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));
                    }

                    else if (o == environmentOtherPlayerViewModel) {

                        // プレイヤー位置
                        float playerX = environmentOtherPlayerViewModel.getPlayer().getX();
                        float playerY = environmentOtherPlayerViewModel.getPlayer().getY();

                        // 相手のplayerの中心にVMを移動
                        /*
                        environmentViewModel.move(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                        environmentOtherPlayerViewModel.move(-2400f + 2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f),
                                -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), 25);
                                */
                        lookAt(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));
                    }

                    cupLook = false;
                }
            }
        }


    }

    public int getActCnt() {
        return actCnt;
    }

    public void setTurn(int turn) {
        this.turn = turn;

        // ターンを報告
        environmentViewModel.setTurn(turn == 0);
        environmentOtherPlayerViewModel.setTurn(turn == 1);
        statusViewModel.setTurn(turn);

    }

    /**
     * カメラを移動します。
     * @return
     */
    public void lookAt(float lx, float ly) {
        environmentViewModel.move(lx, ly, 25);
        environmentOtherPlayerViewModel.move(lx + 2400f, ly, 25);

    }

    public int getTurn() {
        return turn;
    }
}
