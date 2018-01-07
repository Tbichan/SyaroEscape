package com.example.tbichan.syaroescape.common.viewmodel;

import android.view.MotionEvent;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.common.model.ParticleModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * Created by tbichan on 2017/11/28.
 */

public class ParticleViewModel extends GlViewModel {

    // パーティクルモデル
    private ParticleModel particleModel = null;

    public ParticleViewModel(GlView glView, SceneBase sceneBase, String name){
        super(glView, sceneBase, name);
    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);


        GlView glView = MainActivity.getGlView();

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // タッチした座標を取得
            float x = ev.getX() * glView.getViewWidth() / glView.getWidth();
            float y = ev.getY() * glView.getViewHeight() / glView.getHeight();
            //SceneManager.getInstance().setNextScene(new GameScene(getContext()));
            // y軸だけ座標系が違う
            y = MainActivity.getGlView().getViewHeight() - y;
            // モデル読み込み

            if (particleModel == null) {
                particleModel = new ParticleModel(this, "particle");
                particleModel.setTextureId(R.drawable.particle);

                // 追加
                addModel(particleModel);
            }

            particleModel.setPosition(x - 160, y - 160);

            // アニメーションリセット
            particleModel.resetAnim();



        }

        return true;
    }
}
