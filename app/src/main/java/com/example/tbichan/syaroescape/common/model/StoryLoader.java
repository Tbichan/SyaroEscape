package com.example.tbichan.syaroescape.common.model;

import com.example.tbichan.syaroescape.activity.MainActivity;

import java.util.HashSet;

/**
 * ストーリーを読み込みます。
 * Created by tbichan on 2018/01/11.
 */

public class StoryLoader {

    // ファイル名
    private String file;

    // 読み込む必要のある文字列リスト
    private HashSet<String> loadNeedStringList = new HashSet<>();

    public StoryLoader(String file) {
        this.file = file;
    }

    public void load() {

        // ファイル読み込み
        String[] storyStrs = MainActivity.loadFile(file).split("\n");

        for (String line: storyStrs) {

            String name = line.split(":")[0];
            loadNeedStringList.add(name);

            String[] texts = line.split(":")[1].split(",");
            for (String text: texts) {
                loadNeedStringList.add(text);
            }
        }
    }

    // 読み込む必要のある文字列リストを取得
    public final HashSet<String> getLoadNeedStringList() {
        return loadNeedStringList;
    }
}
