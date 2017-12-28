package com.example.tbichan.syaroescape.scene;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * シーン管理クラス
 * Created by tbichan on 2017/10/15.
 */

public class SceneManager {

    // インスタンス
    private static SceneManager instance = new SceneManager();

    private SceneManager(){

    }

    public static SceneManager getInstance() {
        return instance;
    }

    // 前のシーン
    private SceneBase mPreScene = null;

    // 今のシーン
    private SceneBase mNowScene = null;

    // シーン保持用
    private SceneBase mTmpScene = null;

    // 初期シーンをセット
    public void setFirstScene(SceneBase firstScene) {
        mNowScene = firstScene;
    }

    // ロードがおわったかどうか
    private boolean load = true;

    // シーンメイン
    public void sceneMain(Thread thread){

        // 最初のシーンをロード
        mNowScene.load(MainActivity.getGlView());

        // 画像を別スレッドから読み込み
        loadTextureAll();

        mNowScene.setLoad(true);
        //loadNextScene();

        // ループ
        while (thread != null) {
            long oldTime = System.currentTimeMillis();
            // 今のシーンを更新
            mNowScene.update();
            mNowScene.addCnt();

            if (mTmpScene != null) loadNextScene();

            long newTime = System.currentTimeMillis();
            long sleepTime = 16 - (newTime - oldTime); // 休止できる時間
            if(sleepTime < 2) sleepTime = 2; // 最低でも2msは休止
            try {
                Thread.sleep(sleepTime); // 休止
            } catch (InterruptedException e) {

            }
        }

    }

    // 今のシーンを取得
    public SceneBase getNowScene() {
        return mNowScene;
    }

    // 新しいシーンを設定
    public void setNextScene(SceneBase scene) {
        mTmpScene = scene;
    }

    // 新しいシーンにする。
    private void loadNextScene() {

        // タップイベント中はロードしない
        while (MainActivity.getGlView().isTouchEvent() || MainActivity.getGlView().isDraw()) {

        }

        load = false;

        // アンロード
        if (mPreScene != null) mPreScene.unload();

        mPreScene = mNowScene;
        mNowScene = mTmpScene;

        GlView glView = MainActivity.getGlView();
        // ビューモデル削除
        glView.clearViewModel();

        // 画像をアンロード
        BitMapManager.recycleAll();

        // 読み込み
        mNowScene.load(glView);

        // 画像を別スレッドから読み込み
        loadTextureAll();

        mNowScene.setLoad(true);
        mTmpScene = null;

        load = true;
    }

    public boolean isNowSceneLoad() {
        return load;
    }

    // 別スレッドで画像を読み込みます。
    public void loadTextureAll() {
        // テクスチャロード(別スレッド)
        ImgLoadThread imgLoad = new ImgLoadThread(mNowScene);

        // テクスチャ登録
        imgLoad.setImgIdList(mNowScene.getImgIdList());

        Thread thread = new Thread(imgLoad);
        thread.start();
    }

    // 別スレッド
    public static class ImgLoadThread implements Runnable {

        private ArrayList<Integer> imgIdList;

        // 読み込んでいるシーン
        private SceneBase nowScene;

        public ImgLoadThread(SceneBase sceneBase) {
            this.nowScene = sceneBase;
        }

        @Override
        public void run() {

            // 画像数
            int len = imgIdList.size();

            int i = 0;

            for (int id: imgIdList) {
                Log.d("bitmap", id + "を読み込み");

                Resources res = MainActivity.getContext().getResources();
                InputStream is = res.openRawResource(id);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // マネージャに登録
                    BitMapManager.addBitMap(id, bitmap);
                } catch (Exception e) {
                    Log.d("ImageError", "画像の読み込みに失敗");

                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }

                try{
                    Thread.sleep(5);
                } catch (InterruptedException e){

                }

                i++;

                // ロード進行を報告
                nowScene.setSceneImgLoadPersent(((float)i / len));
            }

            // シーンに終了を通知
            nowScene.imgLoadEnd(MainActivity.getGlView());
            nowScene.sceneImgLoaded();

        }

        public void setImgIdList(ArrayList<Integer> imgIdList) {
            this.imgIdList = imgIdList;
        }
    }
}

