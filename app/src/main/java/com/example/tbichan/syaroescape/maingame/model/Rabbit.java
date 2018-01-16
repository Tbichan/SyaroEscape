package com.example.tbichan.syaroescape.maingame.model;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sound.SEManager;

/**
 * Created by tbichan on 2017/12/24.
 */

public class Rabbit extends EnvSprite {


    public Rabbit(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setTextureId(R.drawable.anko);
    }

    @Override
    public void endMove() {
        super.endMove();

        // 衝突時
        // たらい音鳴らす
        if (((GameScene) SceneManager.getInstance().getNowScene()).isEnd()) {
            //SEManager.getInstance().playSE(R.raw.hit);
        }
    }
}
