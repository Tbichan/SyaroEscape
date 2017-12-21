package com.example.tbichan.syaroescape.ui;

import android.content.DialogInterface;

/**
 * Created by tbichan on 2017/12/09.
 */

public class EditAlertListenerManager {

    // インスタンス
    private static EditAlertListenerManager instance = new EditAlertListenerManager();

    public static void setPositiveListener(UIListener positiveListener) {
        instance.positiveListener = positiveListener;
    }

    public static UIListener getPositiveListener() {
        return instance.positiveListener;
    }

    public static void setNegativeListener(UIListener negativeListener) {
        instance.negativeListener = negativeListener;
    }

    public static UIListener getNegativeListener() {
        return instance.negativeListener;
    }

    // OKを押したとき
    private UIListener positiveListener;

    // キャンセルを押したとき
    private UIListener negativeListener;

    private EditAlertListenerManager(){

    }


}
