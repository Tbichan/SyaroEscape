package com.example.tbichan.syaroescape.maingame.viewmodel;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.NetworkGameScene;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by 5515012o on 2017/12/19.
 */

public class StatusViewModel extends GlViewModel implements GlObservable {

    // カップ数
    private int cupNum = 0;

    // プレイヤー表示用
    private GlModel playerNameModel;

    // 相手プレイヤー表示用
    private GlModel comNameModel;

    // バー
    private GlModel lastModel, comLastmodel;

    // カフェイン表示用文字
    private GlModel[] caffineModels = new GlModel[3];
    private GlModel[] comCaffineModels = new GlModel[3];

    // 残り回数
    private int last = 2;
    private int comLast = 2;

    public StatusViewModel(GlView glView, SceneBase sceneBase, String name) {
        super(glView, sceneBase, name);

    }

    @Override
    public void awake() {

        // バー
        GlModel bar1 = new GlModel(this, "bar") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        bar1.setTextureId(R.drawable.maingame_bar);
        bar1.setSize(GlView.VIEW_WIDTH*0.5f, 300);
        bar1.setAlpha(0.75f);
        addModel(bar1);

        GlModel bar2 = new GlModel(this, "bar") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        bar2.setTextureId(R.drawable.maingame_bar);
        bar2.setSize(GlView.VIEW_WIDTH*0.5f, 300);
        bar2.setX(GlView.VIEW_WIDTH*0.5f);
        bar2.setAlpha(0.75f);
        addModel(bar2);

        // ステータス
        playerNameModel = new GlModel(this, "sta") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        addModel(playerNameModel);

        // プレイヤー名をよみこみ
        String playerName = "";
        try {
            playerName = DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
        } catch (Exception e) {

        }
        playerNameModel.setTextureText(playerName);
        playerNameModel.setX(50f);
        playerNameModel.setSize(2048, 600);
        playerNameModel.setY(-320f);

        // ステータス
        comNameModel = new GlModel(this, "sta_com") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        addModel(comNameModel);
        String otherPlayerName = "ＣＯＭ";

        // ネットワークの時のみ
        if (getScene() instanceof NetworkGameScene) {

            otherPlayerName = StoreManager.restoreString("other_name");
        }

        comNameModel.setTextureText(otherPlayerName);
        comNameModel.setX(GlView.VIEW_WIDTH*0.5f+50f);
        comNameModel.setSize(2048, 600);
        comNameModel.setX(GlView.VIEW_WIDTH*0.5f);
        comNameModel.setY(-320f);

        // 残り回数表示用
        lastModel = new GlModel(this, "last") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        addModel(lastModel);

        lastModel.setTextureText("あと２かい");
        lastModel.setSize(2048, 600);
        lastModel.setX(600f);
        lastModel.setY(-320f);

        // 残り回数表示用
        comLastmodel = new GlModel(this, "last_com") {
            @Override
            public void start() {

            }

            @Override
            public void update() {

            }

        };

        addModel(comLastmodel);

        comLastmodel.setTextureText("あと２かい");
        comLastmodel.setSize(2048, 600);
        comLastmodel.setX(600f + GlView.VIEW_WIDTH*0.5f);
        comLastmodel.setY(-320f);

        // カフェイン数字表示用
        for (int i = 0; i < 2; i++) {
            GlModel cup = new GlModel(this, "cup_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                }
            };
            cup.setTextureId(R.drawable.cup);
            cup.setSize(100, 100);
            cup.setPosition(50 + GlView.VIEW_WIDTH*0.5f*i, 50);

            addModel(cup);
        }


        // プレイヤー
        caffineModels[0] = new GlModel(this, "caffine_0") {
            @Override
            public void start() {

            }

            @Override
            public void update() {
            }
        };
        caffineModels[0].setTextureId(R.drawable.number);
        caffineModels[0].setU1(10f / 11f);
        caffineModels[0].setSize(100, 100);
        caffineModels[0].setPosition(150, 50);
        addModel(caffineModels[0]);

        for (int i = 1;i < 3; i++) {
            caffineModels[i] = new GlModel(this, "caffine_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                    //int t = (getCnt() / 30) % 11;
                    //setU1(t / 11f);
                    //setU2((t+1) / 11f);

                }
            };
            caffineModels[i].setTextureId(R.drawable.number);
            caffineModels[i].setU2(1 / 9f);
            caffineModels[i].setSize(100, 100);
            caffineModels[i].setPosition(160 + i * 70, 50);
            addModel(caffineModels[i]);
        }

        // 相手
        comCaffineModels[0] = new GlModel(this, "caffine_0") {
            @Override
            public void start() {

            }

            @Override
            public void update() {
            }
        };
        comCaffineModels[0].setTextureId(R.drawable.number);
        comCaffineModels[0].setU1(10f / 11f);
        comCaffineModels[0].setSize(100, 100);
        comCaffineModels[0].setPosition(150 + GlView.VIEW_WIDTH*0.5f, 50);
        addModel(comCaffineModels[0]);

        for (int i = 1;i < 3; i++) {
            comCaffineModels[i] = new GlModel(this, "caffine_" + i) {
                @Override
                public void start() {

                }

                @Override
                public void update() {

                    //int t = (getCnt() / 30) % 11;
                    //setU1(t / 11f);
                    //setU2((t+1) / 11f);

                }
            };
            comCaffineModels[i].setTextureId(R.drawable.number);
            comCaffineModels[i].setU2(1 / 9f);
            comCaffineModels[i].setSize(100, 100);
            comCaffineModels[i].setPosition(160 + i * 70 + GlView.VIEW_WIDTH*0.5f, 50);
            addModel(comCaffineModels[i]);
        }

    }

    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl) {
        super.update(gl);

        EnvironmentViewModel environmentViewModel = ((GameScene) getScene()).getPlayerViewModel();
        if (environmentViewModel.lastCount() != last && ((GameScene) getScene()).getTurn() == 0) {
            last = environmentViewModel.lastCount();
            Log.d("last", last + "a");
            final String[] tmps = {"０", "１", "２", "３"};

            lastModel.setTextureText("あと" + tmps[last] + "かい");
            lastModel.setVisible(true);
        }

        if (((GameScene) getScene()).getTurn() == 1) {
            lastModel.setVisible(false);
            comLastmodel.setVisible(true);
        }

        environmentViewModel = ((GameScene) getScene()).getOtherPlayerViewModel();
        if (environmentViewModel.lastCount() != comLast && ((GameScene) getScene()).getTurn() == 1) {
            comLast = environmentViewModel.lastCount();
            Log.d("lastcom", comLast + "a");
            final String[] tmps = {"０", "１", "２", "３"};
            comLastmodel.setTextureText("あと" + tmps[comLast] + "かい");
            comLastmodel.setVisible(true);
        }

        if (((GameScene) getScene()).getTurn() == 0) {
            lastModel.setVisible(true);
            comLastmodel.setVisible(false);
        }

        // カフェイン数

        environmentViewModel = ((GameScene) getScene()).getPlayerViewModel();
        // プレイヤーのとき

        // ステータス

        // 獲得カップ数
        int caffeinePower = environmentViewModel.getCaffeinePower();

        //if (caffeinePower != cupNum) {
        //cupNum = caffeinePower;

        // 数字変換
        changeCaffeStr(caffeinePower, caffineModels);

        //}

        environmentViewModel = ((GameScene) getScene()).getOtherPlayerViewModel();

        // ステータス

        // 獲得カップ数
        caffeinePower = environmentViewModel.getCaffeinePower();

        //if (caffeinePower != cupNum) {
        //cupNum = caffeinePower;

        // 数字変換
        changeCaffeStr(caffeinePower, comCaffineModels);

        //}
    }

    @Override
    public void notify(Object o, String... params) {

        Log.d("notify", o.getClass().toString());

        // 環境VMからの通知
        if (o instanceof EnvironmentViewModel) {

            /*
            EnvironmentViewModel environmentViewModel = (EnvironmentViewModel)o;
            // プレイヤーのとき
            if (environmentViewModel == ((GameScene)getScene()).getPlayerViewModel()) {
                // ステータス

                // 獲得カップ数
                final int caffeinePower = environmentViewModel.getCaffeinePower();

                //if (caffeinePower != cupNum) {
                cupNum = caffeinePower;

                // 数字変換
                changeCaffeStr(cupNum, caffineModels);

                //}
            } else if (environmentViewModel == ((GameScene)getScene()).getOtherPlayerViewModel()) {
                // ステータス

                // 獲得カップ数
                final int caffeinePower = environmentViewModel.getCaffeinePower();

                //if (caffeinePower != cupNum) {
                cupNum = caffeinePower;

                // 数字変換
                changeCaffeStr(cupNum, comCaffineModels);

                //}
            }
            */
        }
    }

    public void changeCaffeStr(int num, GlModel[] caffineModels) {
        if (num >= 100) num = 99;

        // 1桁目
        int b = num % 10;
        caffineModels[2].setUV(b / 11f, 0f, (b+1) / 11f, 1.0f);

        // 2桁目
        int a = (num / 10) % 10;
        caffineModels[1].setUV(a / 11f, 0f, (a+1) / 11f, 1.0f);
    }

}
