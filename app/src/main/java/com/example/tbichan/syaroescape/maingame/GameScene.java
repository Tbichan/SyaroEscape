package com.example.tbichan.syaroescape.maingame;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.viewmodel.FadeViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.NowLoadViewModel;
import com.example.tbichan.syaroescape.common.viewmodel.ParticleViewModel;
import com.example.tbichan.syaroescape.experience.ExperienceScene;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.viewmodel.BGViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentOtherPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.ResultViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StatusViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.ActButtonUIViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.StringViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.WazaUIViewModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.GlStringBitmap;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.scene.timer.SceneTimer;
import com.example.tbichan.syaroescape.sound.BGMManager;
import com.example.tbichan.syaroescape.sound.MyBGM;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.util.Random;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class GameScene extends SceneBase implements GlObservable {

    // パーティクル
    private ParticleViewModel particleViewModel;

    // 環境VM
    private EnvironmentViewModel environmentViewModel;
    private EnvironmentViewModel environmentOtherPlayerViewModel;

    // ステータス
    private StatusViewModel statusViewModel;

    // 文字VM
    private StringViewModel stringViewModel;

    // 結果表示用VM
    private ResultViewModel resultViewModel;

    // 行動総回数
    private int actCnt = 0;

    // タップ座標
    private  float downX, downY;

    // 現在のプレイヤー(0...左、1...右)
    private int turn = 0;

    // カップに視点を置いているかどうか
    private boolean cupLook = false;

    // 共通シード値
    private int globalSeed;

    // レベル
    private int level = 0;

    // 決着がついたかどうか
    private boolean endFlg = false;


    // ぶつかったときのカウンタ
    private int hitCnt = -1;
    private EnvironmentViewModel collisonEnvVM;


    // シーンのロード
    @Override
    public void load(GlView glView) {
        System.out.println("game");

        // 音を指定
        addSE(R.raw.button_click);
        addSE(R.raw.player_click);
        addSE(R.raw.hitension);
        addSE(R.raw.rabbit);
        addSE(R.raw.hit);

        addBitmap(R.drawable.particle);

        addBitmap(R.drawable.load_bg);
        addBitmap(R.drawable.load_str);
        addBitmap(R.drawable.bar_frame);
        addBitmap(R.drawable.bar);
        addBitmap(R.drawable.menu_bg);

        addBitmap(R.drawable.tile_0);
        addBitmap(R.drawable.syaro_menu);
        addBitmap(R.drawable.syaro_icon);
        addBitmap(R.drawable.syaro_icon_com);
        addBitmap(R.drawable.anko);
        addBitmap(R.drawable.enable);
        addBitmap(R.drawable.desk);
        addBitmap(R.drawable.powerup);
        addBitmap(R.drawable.cup);
        addBitmap(R.drawable.cups);
        addBitmap(R.drawable.arrow);
        addBitmap(R.drawable.move_button);
        addBitmap(R.drawable.waza_button);
        addBitmap(R.drawable.turnend_button);
        addBitmap(R.drawable.fast_button);
        addBitmap(R.drawable.cancel_button);
        addBitmap(R.drawable.your_turn);
        addBitmap(R.drawable.com_turn);
        addBitmap(R.drawable.end_game);
        addBitmap(R.drawable.carrot);
        addBitmap(R.drawable.maingame_bar);
        addBitmap(R.drawable.number);
        addBitmap(R.drawable.win);
        addBitmap(R.drawable.lose);
        addBitmap(R.drawable.gas);

        // プレイヤー名をよみこみ
        String playerName = "";
        try {
            playerName = DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
        } catch (Exception e) {

        }

        addBitmap(new GlStringBitmap(playerName)
        .setColor(Color.WHITE));
        addBitmap(new GlStringBitmap("ＣＯＭ")
                .setColor(Color.WHITE));

        addBitmap(new GlStringBitmap("あと３かい")
                .setColor(Color.WHITE));
        addBitmap(new GlStringBitmap("あと２かい")
                .setColor(Color.WHITE));
        addBitmap(new GlStringBitmap("あと１かい")
                .setColor(Color.WHITE));

        // 共通シードの決定
        setGlobalSeed(createGlobalSeed());

        // レベルの決定
        level = createLevel();

        // vmの追加
        NowLoadViewModel nowLoadViewModel = new NowLoadViewModel(glView, this, "NowLoadViewModel");
        nowLoadViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(nowLoadViewModel);

        particleViewModel = new ParticleViewModel(glView, this, "ParticleModel");
        particleViewModel.setSceneImgLoadedDraw(false);
        glView.addViewModel(particleViewModel);



    }

    // シーンの更新
    @Override
    public void update(){

        if (hitCnt != -1) {
            if (getCnt() - hitCnt == 60) {
                // ガス表示
                collisonEnvVM.showGas();

                SEManager.getInstance().playSE(R.raw.hit);
            }
        }
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




        environmentViewModel = createPlayerViewModel(glView);


        // 環境を作成33146
        environmentOtherPlayerViewModel = createOtherViewModel(glView);

        environmentOtherPlayerViewModel.setX(2400);
        //environmentOtherPlayerViewModel.setDefaultX(2400);


        // UI作成
        ActButtonUIViewModel uiViewModel = new ActButtonUIViewModel(glView, this, "ActButtonUIViewModel");
        WazaUIViewModel wazaUIViewModel = new WazaUIViewModel(glView, this, "WazaUIViewModel");
        uiViewModel.addEnvironmentViewModel(environmentViewModel);
        uiViewModel.addEnvironmentViewModel(environmentOtherPlayerViewModel);
        uiViewModel.setWazaUIViewModel(wazaUIViewModel);


        environmentViewModel.setUiViewModel(uiViewModel);
        environmentViewModel.setWazaUIViewModel(wazaUIViewModel);


        // ステータス
        statusViewModel = new StatusViewModel(glView, this, "StatusViewModel");

        // 文字VM
        stringViewModel = new StringViewModel(glView, this, "StringViewModel");


        // 通知に追加
        environmentViewModel.addGlObserver(statusViewModel);
        environmentViewModel.addEnvGlObserver(stringViewModel);
        environmentOtherPlayerViewModel.addGlObserver(statusViewModel);
        environmentOtherPlayerViewModel.addEnvGlObserver(stringViewModel);

        // 結果用
        resultViewModel = new ResultViewModel(glView, this, "ResultViewModel");
        resultViewModel.setVisible(false);

        // fadein
        FadeViewModel fadeViewModel = new FadeViewModel(glView, this, "FadeViewModel"){
            @Override
            public void endFadeOut() {
                // メニューに戻る。
                SceneManager.getInstance().setNextScene(new MenuScene());
            }
        };
        fadeViewModel.setInSpeed(1.2f);
        fadeViewModel.startFadeIn();

        resultViewModel.setFadeViewModel(fadeViewModel);

        // 背景
        glView.addViewModel(new BGViewModel(glView, this, "BG"));
        glView.addViewModel(environmentViewModel);
        glView.addViewModel(environmentOtherPlayerViewModel);
        glView.addViewModel(uiViewModel);
        glView.addViewModel(wazaUIViewModel);
        glView.addViewModel(stringViewModel);
        glView.addViewModel(statusViewModel);
        glView.addViewModel(resultViewModel);
        glView.addViewModel(fadeViewModel);

        // パーティクルを最前面に
        glView.moveFrontViewModel(particleViewModel);

        // 自分を移動可能に
        setTurn(initTurn());

        // 音設定
        MediaPlayer player = MediaPlayer.create(MainActivity.getContext(),R.raw.bgm_maingame);
        MyBGM myBGM = new MyBGM(player);
        myBGM.setLoop(true);
        BGMManager.getInstance().addSE(myBGM);

    }

    /**
     * 共通シード作成
     */
    public int createGlobalSeed() {
        return (int)(Math.random() * 25535);
    }

    /**
     * レベルを設定します。
     */
    public int createLevel() {
        Random rGlobal = new Random(globalSeed);

        //return rGlobal.nextInt(19) + 1;
        return 1;
    }

    public int getLevel() {
        return level;
    }

    /**
     * Player用VM作成
     */
    public EnvironmentViewModel createPlayerViewModel(GlView glView) {
        // 環境を作成26656
        return new EnvironmentViewModel(glView, this, "Env_0", 33146, level);
    }

    /**
     * 相手用VM作成
     */
    public EnvironmentViewModel createOtherViewModel(GlView glView) {
        // 環境を作成26656
        return new EnvironmentOtherPlayerViewModel(glView, this, "Env_1", 25565, level);
    }

    /**
     * 最初のターンを設定します。
     */
    public int initTurn() {
        if (getPlayerViewModel().getId() >= getOtherPlayerViewModel().getId())
            return 0;
        return 1;
    }

    // 報告を受けます。
    public void notify(Object o, String... params) {

        // パラメータ確認
        if (params != null && params.length > 0) {

            // ターン終了
            if (params[0].equals("turnend")) {

                // 衝突がなければ
                if (!environmentViewModel.isHit() && !environmentOtherPlayerViewModel.isHit()) {
                    // プレイヤー交代
                    setTurn(1 - turn);
                }
            } else if (params[0].startsWith(Environment.PARAM_ADD_CUP)) {

                /*
                // カップに視点を合わせる
                int cupIndex = Integer.parseInt(params[0].replace(Environment.PARAM_ADD_CUP + ":", "").split(",")[1]);
                lookAtCup(cupIndex);
                */
            }
            else if (params[0].startsWith("playerLook")) {

                if (!environmentViewModel.isHit() && !environmentOtherPlayerViewModel.isHit()) {
                    // プレイヤーに視点を合わせる。
                    EnvironmentViewModel envVM = environmentViewModel;
                    if (turn == 1) envVM = environmentOtherPlayerViewModel;

                    lookAtPlayer(envVM.getId(), 0);
                }

                //Log.d("hoge", (!environmentViewModel.isHit() && !environmentOtherPlayerViewModel.isHit())+"");

            } else if (params[0].startsWith("hit")) {
                // 衝突
                hit((EnvironmentViewModel) o);
            } else if (params[0].startsWith("add rabbit")) {

                if (!environmentViewModel.isHit() && !environmentOtherPlayerViewModel.isHit()) {
                    // 相手VMにウサギ追加
                    EnvironmentViewModel envVM = environmentViewModel;
                    if (envVM == o) envVM = environmentOtherPlayerViewModel;

                    // ウサギ作成
                    final int rabbitIndex = envVM.addEnvRabbit();

                    lookAt(envVM, rabbitIndex);
                }
            }

            Log.d("params", params[0]);
        }

        if (o instanceof EnvironmentViewModel) {

            // パラメータがないとき
            if (params == null || params.length == 0) {

                // 自分の環境から
                if (o == environmentViewModel) {

                    /*
                    if (environmentViewModel.getMoveCnt() % 2 == 0) {
                        Log.d("交代", environmentViewModel.getMoveCnt() + "");

                        // VM移動

                        // プレイヤー位置
                        float playerX = environmentOtherPlayerViewModel.getPlayer().getX();
                        float playerY = environmentOtherPlayerViewModel.getPlayer().getY();


                        lookAt(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                        // 相手を移動可能に
                        setTurn(1);
                        cupLook = false;

                    }
                    */
                    actCnt++;

                }

                // 相手の環境から
                else if (o == environmentOtherPlayerViewModel) {

                    /*
                    if (environmentOtherPlayerViewModel.getMoveCnt() % 2 == 0) {
                        Log.d("交代", environmentOtherPlayerViewModel.getMoveCnt() + "");

                        // プレイヤー位置
                        float playerX = environmentViewModel.getPlayer().getX();
                        float playerY = environmentViewModel.getPlayer().getY();

                        lookAt(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                        // 自分を移動可能に
                        setTurn(0);
                        cupLook = false;

                    }*/

                    actCnt++;

                }
            }

            // パラメータがあるとき
            else if (params != null && params.length > 0) {

                // カップ追加時
                /*
                if (params[0].startsWith("cup:")) {

                    // 獲得したカップの位置を取得
                    int cupIndex = Integer.parseInt(params[0].replace("cup:", ""));
                    // プレイヤー位置
                    final float cupX = EnvSprite.parseX(cupIndex);
                    final float cupY = EnvSprite.parseY(cupIndex);

                    // 移動量
                    float moveParam = -2400f;
                    if (o == environmentViewModel) moveParam = 0f;

                    lookAt(moveParam - (cupX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(cupY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));

                    cupLook = true;

                }

                // プレイヤーを見る
                if (cupLook && params[0].startsWith("playerLook")) {

                    if (o == environmentViewModel) {
                        // プレイヤー位置
                        float playerX = environmentViewModel.getPlayer().getX();
                        float playerY = environmentViewModel.getPlayer().getY();

                        lookAt(-(playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));
                    }

                    else if (o == environmentOtherPlayerViewModel) {

                        // プレイヤー位置
                        float playerX = environmentOtherPlayerViewModel.getPlayer().getX();
                        float playerY = environmentOtherPlayerViewModel.getPlayer().getY();

                        lookAt(-2400f - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));
                    }

                    cupLook = false;
                }
                */
            }
        }


    }

    public void hit(EnvironmentViewModel envVM) {

        if (isEnd()) return;
        // フラグをtrueに
        setEnd(true);

        // ガス表示
        setHitCnt(getCnt());
        setCollisonEnvVM(envVM);

        // 環境VM停止
        //environmentViewModel.setPause(true);
        //environmentOtherPlayerViewModel.setPause(true);


        // 自分の環境から
        if (envVM == environmentViewModel) {

            /*
            // 負け
            MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "/sql/detail/add_result.py?id=" + environmentViewModel.getId() + "&res=0") {

                // 接続成功時
                @Override
                public void success() {

                    String res = "";
                    // 表示
                    try {
                        res = result().replace("\n", "");
                        Log.d("net", res);



                    } catch (Exception e) {

                    }
                }

                // 接続失敗時
                @Override
                public void fail(Exception e) {
                    Log.d("net", "接続エラー:" + e.toString());
                }

            }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "/sql/detail/add_result.py?id=" + environmentViewModel.getId() + "&res=0");

            myHttp.connect();
            */

            // ダイアログ表示
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e){

                    }

                    Log.d("result", "lose");

                    resultViewModel.showResult(false);

                    /*
                    MainActivity.showOKDialog( "決着", "あなたの負け",
                            new UIListener() {
                                @Override
                                public void onClick(View view) {
                                    SceneManager.getInstance().setNextScene(new MenuScene());
                                }
                            });
                            */
                }
            }).start();


        }
        // 相手から
        else {

            // ダイアログ表示
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5000);
                    }
                    catch(InterruptedException e){

                    }

                    resultViewModel.showResult(true);

                    /*
                    Log.d("result", "win");
                    MainActivity.showOKDialog( "決着", "あなたの勝ち！",
                            new UIListener() {
                                @Override
                                public void onClick(View view) {
                                    SceneManager.getInstance().setNextScene(new MenuScene());
                                }
                            });*/
                }
            }).start();


        }


    }

    public int getActCnt() {
        return actCnt;
    }

    public void setTurn(final int turn) {

        EnvironmentViewModel envVM = environmentViewModel;
        if (turn == 1) envVM = environmentOtherPlayerViewModel;

        // 次のプレイヤーに視点を合わせる
        lookAtPlayer(envVM.getId(), 180);

        this.turn = turn;

        // ターンを報告
        environmentViewModel.setTurn(turn == 0);
        environmentOtherPlayerViewModel.setTurn(turn == 1);
        stringViewModel.setTurn(turn);

    }

    ///////////////////////// 敵出現とプレイヤー交代時にまれにおかしくなる？

    /**
     * カメラを移動します。
     * @return
     */
    public void lookAt(float lx, float ly) {
        environmentViewModel.move(lx, ly, 25);
        environmentOtherPlayerViewModel.move(lx + 2400f, ly, 25);

    }

    /**
     * カメラを移動します。
     * @return
     */
    public void lookAt(final float lx, final float ly, final int timerInterval) {

        if (timerInterval == 0) {
            lookAt(lx, ly);
            return;
        }
        startTimer(new SceneTimer() {
            @Override
            public void endTimer(Object o) {
                environmentViewModel.move(lx, ly, 25);
                environmentOtherPlayerViewModel.move(lx + 2400f, ly, 25);

            }
        }, null, timerInterval);

    }

    /**
     * カメラを移動します。
     */
    public void lookAt(EnvironmentViewModel environmentViewModel, final int index) {

        float lx = 0f; float ly = 0f;

        if (environmentViewModel == this.environmentViewModel) {

            lx = -(EnvSprite.parseX(index) - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f);

        } else {
            lx = -2400f - (EnvSprite.parseX(index) - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f);
        }

        ly = -(EnvSprite.parseY(index) - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f);

        this.environmentViewModel.move(lx, ly, 25);
        environmentOtherPlayerViewModel.move(lx + 2400f, ly, 25);

        Log.d("look", "lookat");
    }

    /**
     * プレイヤーにカメラを合わせます。
     */
    public void lookAtPlayer(final int id, int time) {
        // プレイヤー位置
        EnvironmentViewModel envVM = environmentViewModel;
        if (id == environmentOtherPlayerViewModel.getId()) envVM = environmentOtherPlayerViewModel;
        float playerX = envVM.getPlayer().getX();
        float playerY = envVM.getPlayer().getY();

        // カメラ移動
        float tmp = 0f;
        if (id == environmentOtherPlayerViewModel.getId()) tmp = -2400f;
        lookAt(tmp - (playerX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(playerY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f), time);
    }

    /**
     * 新しくできたカップにカメラを合わせます。
     * @return
     */
    public void lookAtCup(final int cupIndex) {

        // プレイヤー位置
        final float cupX = EnvSprite.parseX(cupIndex);
        final float cupY = EnvSprite.parseY(cupIndex);

        // 移動量
        float moveParam = 0f;
        if (turn == 1) moveParam = -2400f;

        lookAt(moveParam - (cupX - (GlView.VIEW_WIDTH - Environment.MAP_SIZE) * 0.5f), -(cupY - (GlView.VIEW_HEIGHT - Environment.MAP_SIZE) * 0.5f));
    }

    public int getTurn() {
        return turn;
    }

    /**
     * 自分を取得します。
     */
    public EnvironmentViewModel getPlayerViewModel() {
        return environmentViewModel;
    }

    public ResultViewModel getResultViewModel() {
        return resultViewModel;
    }

    /**
     * 相手を取得します。
     */
    public EnvironmentViewModel getOtherPlayerViewModel() {
        return environmentOtherPlayerViewModel;
    }

    /**
     * 相手のIDを取得します。
     */
    public int getOtherId(EnvironmentViewModel envVM) {
        if (envVM == environmentViewModel) {
            return environmentOtherPlayerViewModel.getId();
        } else {
            return environmentViewModel.getId();
        }
    }

    public int getGlobalSeed() {
        return globalSeed;
    }

    public void setGlobalSeed(int globalSeed) {
        this.globalSeed = globalSeed;
    }

    public boolean isEnd() {
        return endFlg;
    }

    public void setEnd(boolean endFlg) {
        this.endFlg = endFlg;
    }

    public void setHitCnt(int hitCnt) {
        this.hitCnt = hitCnt;
    }

    public void setCollisonEnvVM(EnvironmentViewModel collisonEnvVM) {
        this.collisonEnvVM = collisonEnvVM;
    }
}
