package com.example.tbichan.syaroescape.common;

import com.example.tbichan.syaroescape.scene.SceneBase;

/**
 * Created by tbichan on 2018/01/08.
 */

public abstract class VibrationScene extends SceneBase {

    // 振動フラグ
    private boolean vibrationFlg = false;

    /**
     * 更新を行います。
     */
    @Override
    public void update() {
        if (vibrationFlg) {
            if (getCnt() % 4 == 0) {
                setX(5);
            } else if (getCnt() % 4 == 2) {
                setX(-5);
            }
        }
    }

    /**
     * 振動させます。
     */
    public void setVibration(boolean vibration) {
        vibrationFlg = vibration;
        if (vibration == false) {
            setX(0);
        }
    }
}
