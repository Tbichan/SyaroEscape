package com.example.tbichan.syaroescape.title.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;

/**
 * Created by tbichan on 2017/10/16.
 */

public class TestModel extends GlModel {

    private float vx = 1.0f;

    public void setVx(float vx) {
        this.vx = vx;
    }

    public TestModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {
        setSize(320, 320);

        // UV
        setU1(0.0f);
        setU2(1.0f/8.0f);
        setV1(7.0f/8.0f);
        setV2(8.0f/8.0f);
    }

    // 更新
    @Override
    public void update() {
        //x += vx;

        int num = getCnt() % 60;

        int tx = num % 8;
        int ty = num / 8;

        // UV
        setU1(tx/8.0f);
        setU2((tx+1)/8.0f);
        setV1(1.0f-(ty+1)/8.0f);
        setV2(1.0f-ty/8.0f);

        if (num == 59){
            // 自身を削除
            delete();
        }

        /*
        if (getCnt() == 150) {
            GlViewModel glViewModel = getGlViewModel();
            // モデル読み込み
            TestModel testModel = new TestModel(glViewModel, "TestModel2");
            testModel.setTexture(R.drawable.particle);
            testModel.setVx(0.5f);
            // サイズ変更
            //testModel.setSize(glViewModel.getGlView().getWidth(), glViewModel.getGlView().getHeight());

            // 追加
            glViewModel.addModel(testModel);

            // 自身を削除
            //delete();
        }*/

    }
}
