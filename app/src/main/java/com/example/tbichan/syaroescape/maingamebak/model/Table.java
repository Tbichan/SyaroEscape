package com.example.tbichan.syaroescape.maingamebak.model;

import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;

/**
 * Created by tbichan on 2017/10/17.
 */

public class Table extends LMoveSprite {

    public Table(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    // 読み込み
    @Override
    public void awake() {
        setTag("Table");
        setTextureId(R.drawable.desk);

        setId(StageModel.TABLE);

        // テーブルはけれない
        setKickTable(false);
        setThroughTable(false);
    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    // 更新
    @Override
    public void update() {
        // プレイヤーが動いたとき
        //move(LMoveSprite.Status.DOWN);

    }
}
