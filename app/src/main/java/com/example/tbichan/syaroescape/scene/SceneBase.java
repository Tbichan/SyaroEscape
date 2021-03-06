package com.example.tbichan.syaroescape.scene;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.opengl.bitmapnmanager.GlStringBitmap;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.timer.SceneTimer;

import java.util.ArrayList;
import java.util.Set;

/**
 * シーンの基底クラス
 * Created by tbichan on 2017/10/15.
 */

public abstract class SceneBase {

    //protected Context mContext = null;

    private boolean load = false;

    // カウンタ
    private int cnt = 0;

    // 読みこむ音IDリスト
    private ArrayList<Integer> seIdList = new ArrayList<>();

    // 読み込む画像IDリスト
    private ArrayList<Integer> imgIdList = new ArrayList<>();

    // 読み込む文字画像リスト
    private ArrayList<GlStringBitmap> imgList = new ArrayList<>();

    // 画像をロードし終わったかどうか
    private boolean sceneImgLoaded = false;

    // 画像をロード具合
    private float sceneImgLoadPersent = 0.0f;

    // 座標
    private float x, y;

    public final float getX() {
        return x;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final float getY() {
        return y;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public final void setPostion(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // 画像読み込み終了時の処理
    public void imgLoadEnd(GlView glView) {

        setSceneImgLoadPersent(1.0f);
    }

    public final void sceneImgLoaded() {
        sceneImgLoaded = true;
    }

    // 画像を読み込み終えたかどうかを判定します。
    public boolean isSceneImgLoaded() {
        return sceneImgLoaded;
    }

    // 画像をロード具合を取得します。
    public float getImgLoadPersent(){
        return sceneImgLoadPersent;
    }

    // 画像のロード具合を設定します。
    public void setSceneImgLoadPersent(float persent) {
        sceneImgLoadPersent = persent;
    }


    public final void setLoad(boolean load){
        this.load = load;
    }

    public final boolean isLoad() {
        return load;
    }

    // コンストラクタ
    public SceneBase() {
        cnt = 0;
    }

    // シーンのロード
    public abstract void load(GlView glView);

    public void start() {

    }

    // シーンの更新
    public abstract void update();

    // タップ処理
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    // シーンのアンロード
    public void unload(){}

    public int getCnt() {
        return cnt;
    }

    public void addCnt() {
        cnt++;
    }

    // 音をを別スレッドで読み込む用
    public void addSE(int id) {
        seIdList.add(id);
    }

    // 画像を別スレッドで読み込む用
    public void addBitmap(int id) {
        imgIdList.add(id);
    }

    // 画像を別スレッドで読み込む用
    public void addBitmap(GlStringBitmap bitmap) {
        imgList.add(bitmap);
    }

    // 音ID一覧を取得
    public ArrayList<Integer> getSeIdList() {
        return seIdList;
    }

    // 画像ID一覧を取得
    public ArrayList<Integer> getImgIdList() {
        return imgIdList;
    }

    // 画像一覧を取得
    public ArrayList<GlStringBitmap> getImgList() {
        return imgList;
    }

    // タイマーを設定(別スレッド)
    public final void startTimer(final SceneTimer st, final Object o, final int interval) {

        final int startCnt = cnt;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (getCnt() - startCnt < interval) {
                    try {
                        Thread.sleep(1);

                    } catch (InterruptedException e) {

                    }
                }
                st.endTimer(o);
            }
        }).start();
    }

}
