package com.example.tbichan.syaroescape.maingame;

import android.graphics.Color;
import android.util.Log;

import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentNetworkOtherPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentNetworkPlayerViewModel;
import com.example.tbichan.syaroescape.maingame.viewmodel.EnvironmentViewModel;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.GlStringBitmap;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;

import java.util.Random;

/**
 * タイトルシーン
 * Created by tbichan on 2017/10/15.
 */

public class NetworkGameScene extends GameScene implements GlObservable {

    // 読み込むファイル名
    private String fileName = "";

    // シーンのロード
    @Override
    public void load(GlView glView) {
        super.load(glView);

        // 相手プレイヤー名をよみこみ
        String otherPlayerName = StoreManager.restoreString("other_name");
        addBitmap(new GlStringBitmap(otherPlayerName).setColor(Color.WHITE));



        // サーバに送る
        /*
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
        */
    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {
        super.imgLoadEnd(glView);

        int id = getPlayerViewModel().getId();
        int otherId = getOtherPlayerViewModel().getId();

        if (id < otherId) {
            fileName = id + "-" + otherId + "-" + getGlobalSeed() + ".replay";
        } else {
            fileName = otherId + "-" + id + "-" + getGlobalSeed() + ".replay";
        }
        Log.d("hoge", fileName);

    }

    /**
     * 共通シード作成
     */
    @Override
    public int createGlobalSeed() {
        return StoreManager.restoreInteger("globalSeed");
    }

    /**
     * レベルを設定します。
     */
    public int createLevel() {
        Random rGlobal = new Random(getGlobalSeed());

        return rGlobal.nextInt(19) + 1;
    }

    /**
     * Player用VM作成
     */
    @Override
    public EnvironmentViewModel createPlayerViewModel(GlView glView) {

        int playerId = -1;

        // 待ちリストに登録
        /*
        if (StoreManager.containsKey("player_id")) {

            // プレイヤー名をよみこみ
            playerId = StoreManager.restoreInteger("player_id");
        }*/

        // 待ちリストに登録
        // ID、プレイヤー名をよみこみ
        String playerIdString = "";
        try {
            playerIdString = DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.NETWORK_ID);
        } catch (Exception e) {

        }
        playerId = Integer.parseInt(playerIdString);

        Log.d("playerid", playerId + "");

        // 環境を作成26656
        return new EnvironmentNetworkPlayerViewModel(glView, this, "Env_0", playerId, getLevel());
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

        Log.d("playerid", otherId + "");

        // 環境を作成26656
        return new EnvironmentNetworkOtherPlayerViewModel(glView, this, "Env_1", otherId,  getLevel());
    }

    public String getFileName() {
        return fileName;
    }
}
