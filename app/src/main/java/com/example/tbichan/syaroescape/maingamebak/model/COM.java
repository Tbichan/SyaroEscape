package com.example.tbichan.syaroescape.maingamebak.model;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.OpponentViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.PlayerViewModel;

import java.util.ArrayList;

/**
 * Created by tbichan on 2017/10/17.
 */

public class COM extends LMoveSprite {

    final Status[] StatusArrays = {Status.RIGHT, Status.UP, Status.LEFT, Status.DOWN};

    public COM(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    // 読み込み
    @Override
    public void awake() {
        setTag("COM");
        setTextureId(R.drawable.com);
    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    // 更新
    @Override
    public void update() {

        // Cupを取得
        ArrayList<GlModel> cupList = getGlViewModel().containModelAll("Cup");

        for (GlModel cup: cupList) {
            if (((Cup)cup).getLx() == getLx() && ((Cup)cup).getLy() == getLy()) {
                cup.delete();
                System.out.println("hit");

                // 相手ゾーンに敵を生成
                PlayerViewModel playerViewModel = (PlayerViewModel)(MainActivity.getGlView().findViewModel("PlayerViewModel"));
                playerViewModel.addEnemy();

                // 自ゾーンにカップ生成
                ((OpponentViewModel)getGlViewModel()).addCup();
            }
        }
    }

    // プレイヤーが動いたとき
    public void playerMove() {
        while (!move(StatusArrays[(int)(Math.random() * 4)])){

        }
    }

}
