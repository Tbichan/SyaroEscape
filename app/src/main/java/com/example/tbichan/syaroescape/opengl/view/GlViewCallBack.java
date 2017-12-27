package com.example.tbichan.syaroescape.opengl.view;

/**
 * Created by tbichan on 2017/10/16.
 */
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

public class GlViewCallBack implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder holder = null;
    private Thread thread = null;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        Log.d("surface", "change");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ

        Log.d("surface", "create");
        this.holder = holder;
        if (thread == null) {
            thread = new Thread(this);
            thread.start(); //スレッドを開始
        } else {
            // テクスチャリロード
            //MainActivity.getGlView().loadTexAll();
            //Log.d("surface", "reloadTex");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO 自動生成されたメソッド・スタブ
        //thread = null; //スレッドを終了
        Log.d("surface", "destroy");
    }

    @Override
    public void run() {
        // 初期シーン
        SceneBase firstScene = MainActivity.getFirstScene();

        // シーンマネージャーにセット
        SceneManager.getInstance().setFirstScene(firstScene);
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.sceneMain(thread);
    }

    public void destroyGame() {
        thread = null;
    }
}
