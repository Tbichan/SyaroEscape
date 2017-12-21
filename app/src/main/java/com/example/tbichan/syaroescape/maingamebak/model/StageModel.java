package com.example.tbichan.syaroescape.maingamebak.model;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.OpponentViewModel;
import com.example.tbichan.syaroescape.maingamebak.viewmodel.PlayerViewModel;

import java.util.ArrayList;

/**
 * ステージマップを管理する
 * Created by tbichan on 2017/10/18.
 */

public class StageModel extends Sprite {

    public static final int EMPTY = 0;
    public static final int TABLE = 1;
    public static final int CUP = 2;
    public static final int PLAYER = 3;
    public static final int ENEMY = 4;

    // プレイヤーマップ
    private int[][] playerMap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 4, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    // 相手のマップ
    private int[][] comMap = new int[16][16];

    // カップマップ
    private int[][] playerCupMap = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    private int[][] comCupMap = new int[16][16];

    public StageModel(GlViewModel glViewModel, String name) {
        super(glViewModel, name);


    }

    @Override
    public void awake() {
        setTag("stage");
        for (int j = 0; j < playerMap.length; j++) {
            for (int i = 0; i < playerMap[j].length; i++) {
                comMap[j][i] = playerMap[j][i];
                comCupMap[j][i] = playerCupMap[j][i];
            }
        }


    }

    // 初期処理(別インスタンス登録)
    @Override
    public void start() {

    }

    // 更新
    @Override
    public void update() {
    }

    // マップID取得
    public int getMapID(GlViewModel glViewModel, int lx, int ly) {
        if (glViewModel instanceof PlayerViewModel) return playerMap[ly][lx];
        if (glViewModel instanceof OpponentViewModel) return comMap[ly][lx];
        return -1;
    }

    // カップがあるかどうか
    public boolean isCup(GlViewModel glViewModel, int lx, int ly) {
        if (glViewModel instanceof PlayerViewModel) return playerCupMap[ly][lx] == CUP;
        if (glViewModel instanceof OpponentViewModel) return comCupMap[ly][lx] == CUP;
        return false;
    }

    // マップ取得
    public LMoveSprite getMap(GlViewModel glViewModel, int lx, int ly) {
        // モデル取得
        ArrayList<GlModel> glModelList = glViewModel.findModelAll();

        for (GlModel glModel : glModelList) {
            if (glModel instanceof LMoveSprite) {

                // 格子点が同じかどうか
                if (((LMoveSprite) glModel).getLx() == lx && ((LMoveSprite) glModel).getLy() == ly && !(glModel instanceof Cup)) {

                    return (LMoveSprite)glModel;
                }
            }
        }

        return null;
    }

    // マップID更新
    public void setMapID(int id, GlViewModel glViewModel, int lx, int ly) {
        if (glViewModel instanceof PlayerViewModel) playerMap[ly][lx] = id;
        if (glViewModel instanceof OpponentViewModel) comMap[ly][lx] = id;
        //System.out.println("-----------------------------------");
        for (int j = 0; j < 16; j++) {
            for (int i = 0; i < 16; i++) {
                //System.out.print(playerMap[j][i] + ", ");
            }
            //System.out.println();
        }
    }

    // 通れるかどうか
    public boolean isPass(LMoveSprite lMoveSprite, LMoveSprite.Status status, int lx, int ly) {

        // ビューモデル取得
        GlViewModel glViewModel = lMoveSprite.getGlViewModel();

        int[][] map = null;

        // マップ選択
        if (glViewModel instanceof PlayerViewModel) map = playerMap;
        else if (glViewModel instanceof OpponentViewModel) map = comMap;

        switch (status) {
            case RIGHT:

                // テーブルが蹴れるか
                if (lMoveSprite.isKickTable()) {
                    // 通れない
                    LMoveSprite tmp1 = getMap(glViewModel, lx + 1, ly);
                    LMoveSprite tmp2 = getMap(glViewModel, lx + 2, ly);
                    if (tmp1 != null && tmp2 != null) {

                        //if (!tmp1.isThroughTable() && !tmp2.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp1 instanceof Table && !tmp2.isThroughTable()) {
                            return false;
                        }
                    }
                } else {

                    // 通れない
                    LMoveSprite tmp = getMap(glViewModel, lx + 1, ly);
                    if (tmp != null) {

                        //if (!tmp.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp instanceof Table) {
                            return false;
                        }
                    }
                }
                break;
            case UP:

                // テーブルが蹴れるか
                if (lMoveSprite.isKickTable()) {
                    // 通れない
                    LMoveSprite tmp1 = getMap(glViewModel, lx, ly - 1);
                    LMoveSprite tmp2 = getMap(glViewModel, lx, ly - 2);
                    if (tmp1 != null && tmp2 != null) {

                        //if (!tmp1.isThroughTable() && !tmp2.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp1 instanceof Table && !tmp2.isThroughTable()) {
                            return false;
                        }
                    }
                } else {

                    // 通れない
                    LMoveSprite tmp = getMap(glViewModel, lx, ly - 1);
                    if (tmp != null) {

                        //if (!tmp.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp instanceof Table) {
                            return false;
                        }
                    }
                }
                break;
            case LEFT:

                // テーブルが蹴れるか
                if (lMoveSprite.isKickTable()) {
                    // 通れない
                    LMoveSprite tmp1 = getMap(glViewModel, lx - 1, ly);
                    LMoveSprite tmp2 = getMap(glViewModel, lx - 2, ly);
                    if (tmp1 != null && tmp2 != null) {

                        //if (!tmp1.isThroughTable() && !tmp2.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp1 instanceof Table && !tmp2.isThroughTable()) {
                            return false;
                        }
                    }
                } else {

                    // 通れない
                    LMoveSprite tmp = getMap(glViewModel, lx - 1, ly);
                    if (tmp != null) {

                        //if (!tmp.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp instanceof Table) {
                            return false;
                        }
                    }
                }
                break;
            case DOWN:

                // テーブルが蹴れるか
                if (lMoveSprite.isKickTable()) {
                    // 通れない
                    LMoveSprite tmp1 = getMap(glViewModel, lx, ly + 1);
                    LMoveSprite tmp2 = getMap(glViewModel, lx, ly + 2);
                    if (tmp1 != null && tmp2 != null) {

                        //if (!tmp1.isThroughTable() && !tmp2.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp1 instanceof Table && !tmp2.isThroughTable()) {
                            return false;
                        }
                    }
                } else {

                    // 通れない
                    LMoveSprite tmp = getMap(glViewModel, lx, ly + 1);
                    if (tmp != null) {

                        //if (!tmp.isThroughTable()) {
                        //    return false;
                        //}
                        if (tmp instanceof Table) {
                            return false;
                        }
                    }
                }
                break;
        }


        return true;
    }

}
