package com.example.tbichan.syaroescape.maingame.model.ai;

import android.util.Log;

import com.example.tbichan.syaroescape.maingame.model.Environment;

import java.util.Random;

/**
 * Created by tbichan on 2018/01/04.
 */

public class NormalAI extends AI {

    public NormalAI(Random r) {
        super(r);
    }

    /**
     * 更新処理です。
     */
    @Override
    public int calc(Environment env) {

        // プレイヤーインデックス
        int playerIndex = env.getPlayerMapPlayerIndex();
        int playerX = playerIndex % Environment.MAP_SIZE;
        int playerY = playerIndex / Environment.MAP_SIZE;

        // 最善手
        int bestIndex = -1;
        int bestCost = 0;
        int bestMoveNum = -1;

        // (y,x)
        final int[][] vecs = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

        // ウサギから離れるように移動
        for (final int[] vec: vecs) {

            final int nextX = playerX + vec[1];
            final int nextY = playerY + vec[0];

            // クリッピング
            if (nextX < 0 || nextX >= Environment.MAP_SIZE) continue;
            if (nextY < 0 || nextY >= Environment.MAP_SIZE) continue;

            // Envに移動できるか確認
            if (!env.isPlayerMove(playerX, playerY, nextX, nextY)) continue;

            // 環境をコピー
            Environment envClone = env.clone();

            // 試しに移動
            final int nextIndex = nextY * Environment.MAP_SIZE + nextX;

            String query = "move:" + playerIndex + "," + nextIndex;
            envClone.notify(query);

            // 移動後のウサギコストを取得
            int cost = envClone.getNearRabbitCost(nextIndex)[1];

            if (bestCost < cost) {
                bestCost = cost;
                bestIndex = nextIndex;
                bestMoveNum = envClone.getPlayerMoveList().size();
            } else if (bestCost == cost) {
                // 同じなら動ける個数が多いほうを採用
                final int moveNum = envClone.getPlayerMoveList().size();

                if (bestMoveNum < moveNum) {
                    bestCost = cost;
                    bestIndex = nextIndex;
                    bestMoveNum = moveNum;
                }

            }

        }

        // コストがrand以上ならコーヒーカップを取りに行く
        final int rand = 3 + randNextInt(2);
        if (bestCost >= 3) {

            Log.d("ai", "cup mode");

            // カップとのコストが１ならカップ取得
            final int[] indexCost = env.getNearCupCost(playerIndex);
            if (indexCost[1] == 1) {

                // 移動可能ならカップを取りに行く
                final int nextIndex = indexCost[0];
                final int nextX = nextIndex % Environment.MAP_SIZE;
                final int nextY = nextIndex / Environment.MAP_SIZE;

                if (env.isPlayerMove(playerX, playerY, nextX, nextY)) return nextIndex;
            } else {

                // 最善手
                bestCost = Integer.MAX_VALUE;

                for (final int[] vec : vecs) {

                    final int nextX = playerX + vec[1];
                    final int nextY = playerY + vec[0];

                    // クリッピング
                    if (nextX < 0 || nextX >= Environment.MAP_SIZE) continue;
                    if (nextY < 0 || nextY >= Environment.MAP_SIZE) continue;

                    // Envに移動できるか確認
                    if (!env.isPlayerMove(playerX, playerY, nextX, nextY)) continue;

                    // 環境をコピー
                    Environment envClone = env.clone();

                    // 試しに移動
                    final int nextIndex = nextY * Environment.MAP_SIZE + nextX;

                    String query = "move:" + playerIndex + "," + nextIndex;
                    envClone.notify(query);

                    // 移動後のカップコストを取得
                    int cost = envClone.getNearCupCost(nextIndex)[1];

                    Log.d("ai", "cup mode:" + cost);

                    if (bestCost > cost) {
                        bestCost = cost;
                        bestIndex = nextIndex;
                    }

                }
            }
        } else {
            Log.d("ai", "escape mode");
        }

        return bestIndex;
    }
}
