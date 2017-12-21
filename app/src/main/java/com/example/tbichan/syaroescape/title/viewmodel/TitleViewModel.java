package com.example.tbichan.syaroescape.title.viewmodel;

import android.content.Context;

import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.title.model.TestModel;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tbichan on 2017/10/16.
 */

public class TitleViewModel extends GlViewModel {

    public TitleViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);


    }

    // 読み込み
    @Override
    public void awake() {

        // モデル読み込み
        TestModel testModel = new TestModel(this, "TestModel");
        testModel.setTexture(R.drawable.particle);
        testModel.setPosition(640,0);
        testModel.setVx(1.0f);
        // 追加
        addModel(testModel);

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    @Override
    public void update(GL10 gl){
        super.update(gl);
    }

}


