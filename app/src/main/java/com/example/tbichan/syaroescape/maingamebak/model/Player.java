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

public class Player extends LMoveSprite {

    public Player(GlViewModel glViewModel, String name) {
        super(glViewModel, name);


    }

    // 読み込み
    @Override
    public void awake() {
        super.awake();
        setTag("Player");
        setId(StageModel.PLAYER);
        setKickTable(true);
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
        ArrayList<GlModel> cupList = getGlViewModel().findTagAll("Cup");

        for (GlModel cup: cupList) {
            if (((Cup)cup).getLx() == getLx() && ((Cup)cup).getLy() == getLy()) {
                cup.delete();
                System.out.println("hit");

                // 相手ゾーンに敵を生成
                OpponentViewModel opponentViewModel = (OpponentViewModel)(MainActivity.getGlView().findViewModel("EnemyViewModel"));
                opponentViewModel.addEnemy();

                // 自ゾーンにカップ生成
                ((PlayerViewModel)getGlViewModel()).addCup();
            }
        }

    }

    // クリック
    public boolean rightButtonClick() {
        return move(LMoveSprite.Status.RIGHT);
    }

    // クリック
    public boolean upButtonClick() {
        return move(LMoveSprite.Status.UP);
    }

    // クリック
    public boolean leftButtonClick() {
        return move(LMoveSprite.Status.LEFT);
    }

    // クリック
    public boolean downButtonClick() {
        return move(LMoveSprite.Status.DOWN);
    }
}
