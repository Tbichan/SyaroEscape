package com.example.tbichan.syaroescape.maingame.model.ai;

import com.example.tbichan.syaroescape.maingame.model.Environment;

import java.util.Random;

/**
 * AI用クラスです。
 * Created by tbichan on 2018/01/04.
 */

public abstract class AI {

    // プレイヤーマップ
    private int[][] playerMap;
    private int[][] cupMap;

    private Random r = null;

    public AI(Random r) {
        // シード値を設定
        this.r = r;
    }

    public void setPlayerMap(int[][] playerMap) {
        this.playerMap = playerMap;
    }

    public void setCupMap(int[][] cupMap) {
        this.cupMap = cupMap;
    }

    protected int[][] getPlayerMap() {
        return playerMap;
    }

    protected int[][] getCupMap() {
        return cupMap;
    }

    public abstract int calc(Environment env);

    public int randNextInt(int sup) {
        return r.nextInt(sup);
    }
}
