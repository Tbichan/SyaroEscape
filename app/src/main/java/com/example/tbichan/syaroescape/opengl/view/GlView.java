package com.example.tbichan.syaroescape.opengl.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Created by tbichan on 2017/10/15.
 */

public class GlView extends GLSurfaceView {

    public static final int TILE_SIZE = 64;

    private static final int OPENGL_ES_VERSION = 2;

    // 幅、高さ
    public static final int VIEW_WIDTH = 1280 * 2;
    public static final int VIEW_HEIGHT = 727 * 2;

    // レンダラー
    protected GlRenderer mRenderer;


    // コールバック
    private GlViewCallBack cb;

    // 直前に検索されたGlModel
    private GlModel preGlModel = null;

    // ビューの大きさ（指定した大きさが画面いっぱいになる）
    private int vw, vh;

    // タップイベント中かどうか
    private boolean touchEvent = false;

    public final boolean isTouchEvent() {
        return touchEvent;
    }

    public GlView(Context context) {
        super(context);
        mRenderer = new GlRenderer(context);

        // バージョンを指定
        setEGLContextClientVersion(OPENGL_ES_VERSION);
        // 作成したレンダラーをセット
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_CONTINUOUSLY);

        SurfaceHolder holder = getHolder();
        cb = new GlViewCallBack();
        holder.addCallback(cb);

        vw = VIEW_WIDTH;
        vh = VIEW_HEIGHT;
    }

    // ゲーム終了
    public void destroyGame() {
        cb.destroyGame();
    }

    // VMの追加
    public void addViewModel(GlViewModel glViewModel) {
        mRenderer.addViewModel(glViewModel);
        glViewModel.awake();
        glViewModel.setAwakeFlg(true);
    }

    // VMの削除
    public void deleteViewModel(GlViewModel glViewModel) {
        mRenderer.deleteViewModel(glViewModel);
    }

    // 最背面にVMを移動
    public void moveFrontViewModel(GlViewModel glViewModel) {
        mRenderer.deleteViewModel(glViewModel);
        mRenderer.addViewModel(glViewModel);
    }

    // VMの削除
    public void clearViewModel() {
        mRenderer.clearViewModel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        // 描画が終わるまでまつ
        mRenderer.waitDraw();

        // タップイベント中
        touchEvent = true;

        int x, y;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // タッチした座標を取得
            x = (int)(ev.getX());
            y = (int)(ev.getY());
            System.out.println("x:" + x + ", y:" + y);
        }

        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        SceneBase nowScene = SceneManager.getInstance().getNowScene();

        nowScene.onTouchEvent(ev);

        try {
            // vmにタップイベントを渡す
            for (GlViewModel glViewModel : viewmodelList) {
                glViewModel.onTouchEvent(ev);
            }
        } catch (ConcurrentModificationException e) {

        }
        // タップイベント終了
        touchEvent = false;

        return true;
    }

    public void waitTouch() {
        while(touchEvent) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {

            }
        }
    }

    // ViewModelの検索を行います。
    public GlViewModel findViewModel(String name) {

        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        // vmにタップイベントを渡す
        for (GlViewModel glViewModel: viewmodelList) {
            if (glViewModel.getName().equals(name)) return glViewModel;
        }
        return null;
    }

    // Modelの検索を行います。
    public GlModel findModel(String name) {

        // 直前の検索かどうか
        if (preGlModel != null) {
            if (preGlModel.getName().equals(name)) {
                return preGlModel;
            }
        }

        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        // vmにタップイベントを渡す
        for (GlViewModel glViewModel: viewmodelList) {
            GlModel glModel = glViewModel.findModel(name);
            if (glModel != null) {
                preGlModel = glModel;
                return glModel;
            }
        }

        return null;
    }

    // Modelのタグ検索を行います。
    public ArrayList<GlModel> findTagAll(String tag) {
        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        ArrayList<GlModel> glModelList = new ArrayList<>();

        // vmに渡す
        for (GlViewModel glViewModel: viewmodelList) {
            ArrayList<GlModel> hogeList = glViewModel.findTagAll(tag);
            glModelList.addAll(hogeList);

        }

        return glModelList;
    }

    // Modelの一部検索を行います。
    public ArrayList<GlModel> containModelAll(String name) {

        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        ArrayList<GlModel> glModelList = new ArrayList<>();

        // vmに渡す
        for (GlViewModel glViewModel: viewmodelList) {
            ArrayList<GlModel> hogeList = glViewModel.containModelAll(name);
            glModelList.addAll(hogeList);

        }

        return glModelList;
    }

    public int getViewWidth() {
        return vw;
    }

    public int getViewHeight(){
        return vh;
    }

    public boolean isDraw() {
        return mRenderer.isDraw();
    }

    // テクスチャの一斉ロード
    public void loadTexAll() {

        // viewmodelのリスト
        ArrayList<GlViewModel> viewmodelList = mRenderer.getViewmodelList();

        try {
            // vmにタップイベントを渡す
            for (GlViewModel glViewModel : viewmodelList) {
                glViewModel.loadTexAll();
            }
        } catch (ConcurrentModificationException e) {

        }
    }
}
