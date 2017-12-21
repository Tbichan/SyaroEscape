package com.example.tbichan.syaroescape.maingamebak.model;

import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;

/**
 * Created by tbichan on 2017/10/17.
 */

public class Cup extends LMoveSprite {

    public Cup(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    // 読み込み
    @Override
    public void awake() {
        setTag("Cup");
        setId(StageModel.CUP);
        setThroughTable(true);
    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {
        setTexture(R.drawable.cup);
    }

    // 更新
    @Override
    public void update() {

    }
}
