package com.example.tbichan.syaroescape.maingamebak.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.R;

import java.util.ArrayList;

/**
 * Created by tbichan on 2017/10/17.
 */

public class Enemy extends LMoveSprite {

    final Status[] StatusArrays = {Status.RIGHT, Status.UP, Status.LEFT, Status.DOWN};

    private LMoveSprite player;

    public Enemy(GlViewModel glViewModel, String name) {
        super(glViewModel, name);

    }

    // 読み込み
    @Override
    public void awake() {
        super.awake();
        setId(StageModel.ENEMY);
        setKickTable(false);
        setTextureId(R.drawable.player);
    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {
        ArrayList<GlModel> playerList = getGlViewModel().findTagAll("Player");
        if (playerList != null) {
            player = (Player)playerList.get(0);

        } else {
            playerList = getGlViewModel().findTagAll("COM");
            if (playerList != null) {
                player = (COM)playerList.get(0);

            }
        }
    }

    // 更新
    @Override
    public void update() {

    }

    // プレイヤーが動いたとき
    public void playerMove() {
        if (player == null) {

            int tmp = 0;
            while (!move(StatusArrays[(int) (Math.random() * 4)]) && tmp < 20) {
                tmp++;
            }
        } else {
            // プレイヤーの座標
            boolean flg = false;
            int dx = Math.abs(getLx() - player.getLx());
            int dy = Math.abs(getLy() - player.getLy());

            if (dx > dy) {
                if (player.getLx() < getLx()) flg = move(Status.LEFT);
                if (!flg && player.getLx() >= getLx()) flg = move(Status.RIGHT);
                if (!flg && player.getLy() < getLy()) flg = move(Status.UP);
                if (!flg && player.getLy() >= getLy()) move(Status.DOWN);
                if (!flg) {
                    int tmp = 0;
                    while (!move(StatusArrays[(int) (Math.random() * 4)]) && tmp < 20) {
                        tmp++;
                    }
                }
            } else {
                if (player.getLy() < getLy()) flg = move(Status.UP);
                if (!flg && player.getLy() >= getLy()) move(Status.DOWN);
                if (!flg && player.getLx() < getLx()) flg = move(Status.LEFT);
                if (!flg && player.getLx() >= getLx()) flg = move(Status.RIGHT);
                if (!flg) {
                    int tmp = 0;
                    while (!move(StatusArrays[(int) (Math.random() * 4)]) && tmp < 20) {
                        tmp++;
                    }
                }
            }
        }
    }
}
