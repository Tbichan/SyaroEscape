package com.example.tbichan.syaroescape.maingame.model.level;

import android.util.Log;

import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.maingame.model.Environment;

/**
 * Created by tbichan on 2018/01/06.
 */

public class LevelLoader {

    // プレイヤーマップ
    private int[][] playerMap = new int[Environment.MAP_SIZE][Environment.MAP_SIZE];

    // カップマップ
    private int[][] cupMap = new int[Environment.MAP_SIZE][Environment.MAP_SIZE];

    public void loadLevel(String levelname) {

        // ファイル読み込み
        String levelStr = MainActivity.loadFile(levelname);
        String[] levelLines = levelStr.split("\n");

        Log.d("level", levelname);

        // プレイヤーマップ部分
        for (int j = 0; j < Environment.MAP_SIZE; j ++) {
            String[] levelStrs = levelLines[j].split(",");
            for (int i = 0; i < Environment.MAP_SIZE; i++) {
                String levelChar = levelStrs[i];

                if (levelChar.equals("")) levelChar = "0";
                Log.d("level " + j + " " + i, levelChar);
                playerMap[j][i] = Integer.parseInt(levelChar);


            }
        }

        // カップマップ部分
        for (int j = Environment.MAP_SIZE; j < 2 * Environment.MAP_SIZE; j ++) {
            String[] levelStrs = levelLines[j].split(",");
            for (int i = 0; i < Environment.MAP_SIZE; i++) {
                String levelChar = levelStrs[i];
                if (levelChar.equals("")) levelChar = "0";
                Log.d("level " + j + " " + i, levelChar);
                cupMap[j - Environment.MAP_SIZE][i] = Integer.parseInt(levelChar);


            }
        }
    }

    public int[][] getPlayerMap() {
        return playerMap;
    }

    public int[][] getCupMap() {
        return cupMap;
    }
}
