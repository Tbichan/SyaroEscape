package com.example.tbichan.syaroescape.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tbichan on 2017/10/22.
 */

public class MyHttpRunnable implements Runnable {

    // URL
    private String url = "";
    private String secondUrl = "";
    URLConnection conn = null;

    // パラメータ
    private String[] params;

    // 結果
    String resultStr = "";

    private MyHttp myHttp;

    // エラー検出用
    boolean err = false;

    public MyHttpRunnable(MyHttp myHttp, String url, String... params) {
        this.myHttp = myHttp;
        this.url = url;
        this.params = params;
    }

    @Override
    public void run(){

        String res = "";
        BufferedReader in = null;

        try {
            // URL生成
            String urlInParams = url;

            // パラメータ確認
            if (params != null && params.length > 0){
                urlInParams+="?";

                for (String param: params) {
                    urlInParams+=param + "&";
                }
            }

            // URLオブジェクト生成
            URL realUrl = new URL(urlInParams);
            conn = realUrl.openConnection();

            conn.setReadTimeout(0);

            // 接続
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                res += "\n" + line;
            }

            resultStr = res;

            myHttp.success();

        } catch (Exception e1) {

            // セカンダリ接続
            try {
                // URL生成
                String urlInParams = secondUrl;

                // パラメータ確認
                if (params != null && params.length > 0){
                    urlInParams+="?";

                    for (String param: params) {
                        urlInParams+=param + "&";
                    }
                }

                // URLオブジェクト生成
                URL realUrl = new URL(urlInParams);
                conn = realUrl.openConnection();

                conn.setReadTimeout(0);

                // 接続
                conn.connect();

                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    res += "\n" + line;
                }

                resultStr = res;

                myHttp.success();

            }catch (Exception e2) {
                err = true;
                resultStr = "";

                myHttp.fail(e2);
            }
        } finally {

        }

    }

    /**
     * セカンダリURLを指定します。
     */
    public void setSecondUrl(String secondUrl) {
        this.secondUrl = secondUrl;
    }

    /**
     * 結果がかえって来たかを取得します。
     */
    public boolean isResult() throws Exception {
        if (err) throw new Exception();
        return resultStr != null && resultStr.length() != 0;
    }

    /**
     * 結果を出力します。
     */
    public String result() throws Exception {

        if (err) throw new Exception();

        return resultStr;
    }
}
