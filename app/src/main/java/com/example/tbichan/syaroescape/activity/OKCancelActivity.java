package com.example.tbichan.syaroescape.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.ui.EditAlertListenerManager;

public class OKCancelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

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

    public static class TestDialogFragment extends DialogFragment {

        Activity activity;
        EditText editText;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            activity = getActivity();

            Dialog dialog = new Dialog(activity);

            // タイトル非表示
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.custom_okcancel);

            // 背景を透明にする
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // OKボタンのリスナ
            Button button = (Button)dialog.findViewById(R.id.positive_button);
            //button.setTypeface(Typeface.createFromAsset(activity.getAssets(), "uzura.ttf"));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // クリック伝達
                    EditAlertListenerManager.getPositiveListener().onClick(null);

                    dismiss();
                    activity.finish();
                }
            });
            // closeボタンのリスナ
            dialog.findViewById(R.id.negative_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    activity.finish();
                }
            });

            return dialog;
        }
    }
}
