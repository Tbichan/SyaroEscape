package com.example.tbichan.syaroescape.opengl.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.bitmapnmanager.BitMapManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.renderer.GlRenderer;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/10/16.
 */

public abstract class GlViewModel {

    // 幅、高さ
    private float width, height;

    // モデルのリスト
    private HashMap<String, ArrayList<GlModel>> modelMap = new HashMap<String, ArrayList<GlModel>>();

    // 生成モデルのリスト
    private ArrayList<GlModel> createModelList = new ArrayList<>();

    // 削除モデルのリスト
    private ArrayList<GlModel> removeModelList = new ArrayList<>();

    //private Context context;

    // ビュー
    private GlView glView;

    // シーン
    private SceneBase scene;

    // ビューのどこに描画するか
    private float x, y;

    private int cnt = 0;

    // awakeが終了しているか
    private boolean awakeFlg = false;

    // 名前
    private String name = "";

    // 表示するか
    private boolean visible = true;

    // シーンの画像がロードし終わったら描画するかどうかのフラグ
    private boolean sceneImgLoadedDraw = true;

    public void setSceneImgLoadedDraw(boolean sceneImgLoadedDraw) {
        this.sceneImgLoadedDraw = sceneImgLoadedDraw;
    }

    public String getName() {
        return name;
    }

    public GlView getGlView(){
        return glView;
    }

    public SceneBase getScene() {
        return scene;
    }

    public abstract void awake();

    public abstract void start();

    public GlViewModel(GlView glView, SceneBase scene, String name) {
        this.glView = glView;
        this.scene = scene;
        this.name = name;
        x = y = 0;
    }


    public void update(GL10 gl){

        boolean texFlg = true;

        // 更新
        try {
            for (String key : modelMap.keySet()) {
                ArrayList<GlModel> modelList = modelMap.get(key);

                for (int i = 0; i < modelList.size(); i++) {
                    GlModel glModel = modelList.get(i);
                    if (glModel.getCnt() == 0) {
                        glModel.startBefore();
                        glModel.start();
                    }

                    // テクスチャロード
                    if (!glModel.isLoadTex()) {
                        glModel.loadTexture(gl);
                        texFlg = false;
                    }

                    // 更新
                    glModel.updateBefore();
                    glModel.update();
                    // カウントアップ
                    glModel.addCnt();
                }
            }
        } catch (ConcurrentModificationException e) {

        }

        // テクスチャアンロード
        //if (!texFlg) BitMapManager.recycleAll();

        // 削除
        if (removeModelList.size() != 0) {
            for (GlModel glModel : removeModelList) {
                if (glModel.getTag().equals("cup")) {
                    Log.d("hoges", modelMap.get(glModel.getTag()).size() + "");
                }
                modelMap.get(glModel.getTag()).remove(glModel);
            }
            removeModelList.clear();
        }
    }


    public void draw(final GlRenderer glRenderer){

        if (sceneImgLoadedDraw && !SceneManager.getInstance().getNowScene().isSceneImgLoaded()) return;

        // 描画
        try {
            for (String key : modelMap.keySet()) {
                ArrayList<GlModel> modelList = modelMap.get(key);

                for (int i = 0; i < modelList.size(); i++) {
                    GlModel glModel = modelList.get(i);

                    if (glModel.isVisible()) {
                        glModel.setDrawFlg(true);
                        glModel.draw(glRenderer);
                        // zバッファ変更
                        glRenderer.decayZBuffer();
                        glModel.setDrawFlg(false);
                    }
                }
            }
        }
        catch (ConcurrentModificationException e) {

        }

    }

    // タップイベント
    public boolean onTouchEvent(MotionEvent ev) {
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (int i = 0; i < modelList.size(); i++) {
                GlModel glModel = modelList.get(i);
                glModel.onTouchEvent(ev);
            }
        }
        return true;
    }

    // タップしたところのX座標を取得
    public float getTouchX(MotionEvent ev) {
        // タッチした座標を取得
        float tx = ev.getX() * glView.getViewWidth() / glView.getWidth();

        return tx;
    }

    // タップしたところのX座標を取得
    public float getTouchY(MotionEvent ev) {
        // タッチした座標を取得
        float ty = ev.getY() * glView.getViewHeight() / glView.getHeight();

        // y軸だけ座標系が違う
        ty = MainActivity.getGlView().getViewHeight() - ty;

        return ty;
    }

    // テクスチャの一斉ロード
    public void loadTexAll() {
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (GlModel glModel : modelList) {
                glModel.unLoadTex();
            }
        }
    }

    // モデル追加
    public void addModel(GlModel glModel) {
        //createModelList.add(glModel);

        // タグ判別
        String tag = glModel.getTag();

        // 新規タグなら追加
        if (!modelMap.containsKey(tag)) {
            modelMap.put(tag, new ArrayList<GlModel>());
        }

        modelMap.get(tag).add(glModel);

        // モデル最初のロード
        glModel.awake();

    }

    // モデル削除
    public void deleteModel(GlModel model) {
        removeModelList.add(model);
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public final void setX(float x){
        this.x = x;
    }

    public final void setY(float y){
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public final void setAwakeFlg(boolean awakeFlg) {
        this.awakeFlg = awakeFlg;
    }

    public final boolean isAwakeFlg() {
        return awakeFlg;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    // ViewModelのロードが終わるのを待ちます。
    public void waitAwake() {
        while (!awakeFlg) {

        }
    }

    // Modelの検索を行います。
    public GlModel findModel(String name) {
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (GlModel glModel : modelList) {
                if (glModel.getName().equals(name)) return glModel;
            }
        }
        return null;
    }

    // Modelの検索を行います。
    public ArrayList<GlModel> findModelAll() {
        ArrayList<GlModel> hogeList = new ArrayList<GlModel>();
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (GlModel glModel : modelList) {
                hogeList.add(glModel);
            }
        }
        return hogeList;
    }

    // Modelの検索を行います。
    public ArrayList<GlModel> findModelAll(String name) {
        ArrayList<GlModel> hogeList = new ArrayList<GlModel>();
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (GlModel glModel : modelList) {
                if (glModel.getName().equals(name)) hogeList.add(glModel);
            }
        }
        return hogeList;
    }

    // Modelの一部検索を行います。
    public ArrayList<GlModel> containModelAll(String name) {
        ArrayList<GlModel> hogeList = new ArrayList<GlModel>();
        for (String key: modelMap.keySet()) {
            ArrayList<GlModel> modelList = modelMap.get(key);

            for (GlModel glModel : modelList) {
                if (glModel.getName().contains(name)) hogeList.add(glModel);
            }
        }
        return hogeList;
    }

    // Modelのタグ検索を行います。
    public ArrayList<GlModel> findTagAll(String tag) {
        return modelMap.get(tag);
    }

    public final void addCnt() {
        cnt++;
    }

    public final int getCnt() {
        return cnt;
    }

    // タグの変更の通知用
    public final void changeTag(GlModel model, String preTag, String newTag) {

        // 前タグからは削除
        if (modelMap.containsKey(preTag)) {
            modelMap.get(preTag).remove(model);
        }

        // 新規タグなら追加
        if (!modelMap.containsKey(newTag)) {
            modelMap.put(newTag, new ArrayList<GlModel>());
        }

        modelMap.get(newTag).add(model);
    }

    public void frontModel(GlModel glModel) {
        String tag = glModel.getTag();

        ArrayList<GlModel> glModelArrayList = modelMap.get(tag);
        glModelArrayList.remove(glModel);
        glModelArrayList.add(glModel);
    }
}
