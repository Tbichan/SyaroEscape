package com.example.tbichan.syaroescape.opengl.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v7.view.menu.MenuView;
import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneManager;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/10/15.
 */

public class GlRenderer implements GLSurfaceView.Renderer {

    protected Context context;

    // 描画中かどうか
    private boolean draw = false;

    // zバッファ
    public float zBuffer = 0.0f;

    // viewmodelのリスト
    private ArrayList<GlViewModel> mViewmodelList = new ArrayList<>();

    public boolean isDraw(){
        return draw;
    }

    public GlRenderer(Context _context){
        context = _context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // アルファブレンドを有効にする
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // 深度バッファ有効
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        /* 1.0
        // 背景色をクリア
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //ディザを無効化
        gl.glDisable(GL10.GL_DITHER);
        //深度テストを有効化
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //テクスチャ機能ON
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //透明可能に
        gl.glEnable(GL10.GL_ALPHA_TEST);
        //ブレンド可能に
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glDepthFunc(GL10.GL_LEQUAL);

        //faceSprite.setTextureId(gl,context.getResources(), R.drawable.face);
        //faceSprite2.setTextureId(gl,context.getResources(), R.drawable.face2);
        //spriteList.add(faceSprite);
        //spriteList.add(faceSprite2);
        //face_img.setTextureId(gl,context.getResources(), R.drawable.face);
        */

        // 黒で塗りつぶし
        GLES20.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //gl.glViewport(0, 0, width, height);
        //gl.glMatrixMode(GL10.GL_PROJECTION);//プロジェクションモードに設定
        //GLU.gluOrtho2D(gl, 0.0f, width, 0.0f, height);//平行投影用のパラメータをセット

        /* 1.0
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45f,(float) width / height, 1f, 50f);
        */


    }

    @Override
    public void onDrawFrame(GL10 gl) {

        // 今のシーンをロードしたら
        if (!SceneManager.getInstance().isNowSceneLoad()) return;

        // 更新
        try {
            for (int i = 0; i < mViewmodelList.size(); i++) {

                // awakeが行われていないvmは更新しない。
                GlViewModel glViewModel = mViewmodelList.get(i);
                if (!glViewModel.isAwakeFlg()) continue;
                if (glViewModel.getCnt() == 0) glViewModel.start();
                else glViewModel.update(gl);
                glViewModel.addCnt();

            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("error", e.toString());
        } catch (NullPointerException e) {
            Log.e("error", e.toString());
        }

        //MainActivity.getGlView().waitTouch();

        draw = true;

        // 画面クリア
        GLES20.glClearColor(0.01f, 0.01f, 0.01f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // zバッファ初期化
        zBuffer = 0.0f;

        // 描画
        try {
            for (int i = 0; i < mViewmodelList.size(); i++) {

                // awakeが行われていないvmは描画しない。
                GlViewModel glViewModel = mViewmodelList.get(i);
                if (!glViewModel.isAwakeFlg()) continue;
                if (!glViewModel.isVisible()) continue;
                glViewModel.draw(this);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.e("error", e.toString());
        } catch (NullPointerException e) {
            Log.e("error", e.toString());
        }

        //Log.d("hoge", zBuffer+"");

        draw = false;
    }

    // 描画を終えるまで待ちます。（ベツスレッド）
    public void waitDraw() {
        while (draw) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
    }

    // VMの追加
    public void addViewModel(GlViewModel glViewModel) {
        mViewmodelList.add(glViewModel);
    }

    // VMの削除
    public void deleteViewModel(GlViewModel glViewModel) {
        mViewmodelList.remove(glViewModel);

    }

    // VMの削除
    public void clearViewModel() {
        mViewmodelList.clear();
    }

    public ArrayList<GlViewModel> getViewmodelList(){
        return mViewmodelList;
    }

    // zバッファ取得
    public float getZBuffer() {
        return zBuffer;
    }

    // zバッファ減少
    public void decayZBuffer() {
        zBuffer -= 0.0001f;
    }
}
