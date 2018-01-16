package com.example.tbichan.syaroescape.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.sound.BGMManager;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.ui.EditAlertListenerManager;

public class OptionActivity extends Activity {

    private AudioManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 音用
        manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        DialogFragment dialogFragment = new TestDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(), "test");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        } else {
            // 戻るキーなら終了させない
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
        //MainActivity.getGlView().onTouchEvent(event);
        return false;
    }

    public static class TestDialogFragment extends DialogFragment implements View.OnTouchListener {

        Activity activity;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            activity = getActivity();

            Dialog dialog = new Dialog(activity);

            // タイトル非表示
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.volume_layout);

            // 背景を透明にする
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // OKボタンのリスナ
            Button button = (Button)dialog.findViewById(R.id.positive_button);
            //button.setTypeface(Typeface.createFromAsset(activity.getAssets(), "uzura.ttf"));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // クリック伝達
                    if (EditAlertListenerManager.getPositiveListener() != null) {
                        EditAlertListenerManager.getPositiveListener().onClick(null);
                    }

                    dismiss();
                    activity.finish();
                }
            });

            int bgm = 50;
            try {
                bgm = Integer.parseInt(DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.BGM));
            } catch (Exception e) {

            }
            // SeekBar
            SeekBar seekBarBGM = (SeekBar)dialog.findViewById(R.id.seekBarBGM);
            // 初期値
            seekBarBGM.setProgress(bgm);
            // 最大値
            seekBarBGM.setMax(100);
            seekBarBGM.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        //ツマミがドラッグされると呼ばれる
                        @Override
                        public void onProgressChanged(
                                SeekBar seekBar, int progress, boolean fromUser) {

                            int volume = (int)(progress / 10.0f);

                            ((OptionActivity)activity).manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

                            // networkidをsqliteに保存
                            DataBaseHelper.getDataBaseHelper().write(DataBaseHelper.BGM, String.valueOf(volume * 10));
                        }

                        // ツマミがタッチされた時に呼ばれる
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        // ツマミがリリースされた時に呼ばれる
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }

                    });

            int se = 50;
            try {
                se = (int)(100.0f * Float.parseFloat(DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.SE)));
            } catch (Exception e) {

            }
            // SeekBar
            SeekBar seekBarSE = (SeekBar)dialog.findViewById(R.id.seekBarSE);
            // 初期値
            seekBarSE.setProgress(se);
            // 最大値
            seekBarSE.setMax(100);
            seekBarSE.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        //ツマミがドラッグされると呼ばれる
                        @Override
                        public void onProgressChanged(
                                SeekBar seekBar, int progress, boolean fromUser) {

                            float volume = (progress / 100.0f);

                            SEManager.getInstance().setVolume(volume);

                            // networkidをsqliteに保存
                            DataBaseHelper.getDataBaseHelper().write(DataBaseHelper.SE, String.valueOf(volume));
                        }

                        // ツマミがタッチされた時に呼ばれる
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        // ツマミがリリースされた時に呼ばれる
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }

                    });

            return dialog;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //MainActivity.getGlView().onTouchEvent(event);
            return false;
        }

        @Override
        public void onResume(){
            super.onResume();
            BGMManager.getInstance().resume();
        }

        @Override
        public void onPause(){
            super.onPause();
            BGMManager.getInstance().pause();
        }
    }
}
