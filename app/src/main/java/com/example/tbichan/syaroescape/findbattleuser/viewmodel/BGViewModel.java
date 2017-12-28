package com.example.tbichan.syaroescape.findbattleuser.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.ui.UIListener;

public class BGViewModel extends GlViewModel {
	
	GlModel bgStrErr;

	public BGViewModel(GlView glView, SceneBase sceneBase, String name) {
		super(glView, sceneBase, name);

	}

	@Override
	public void awake() {

		// 表示する文字
		GlModel bgStr = new GlModel(this, "bgStr_wait") {
			@Override
			public void start() {

			}

			@Override
			public void update() {

			}
		};

		bgStr.setTexture(R.drawable.tushin);
		bgStr.setSize(1500, 300);

		// ���S�ɏo��
		bgStr.setPosition(GlView.VIEW_WIDTH*0.5f-750, GlView.VIEW_HEIGHT*0.5f-150);

		addModel(bgStr);

		// �ڑ��G���[�̕���
		bgStrErr = new GlModel(this, "bgStr_err") {
			@Override
			public void start() {

			}

			@Override
			public void update() {

			}
		};

		bgStrErr.setTexture(R.drawable.tushin_err);
		bgStrErr.setSize(1500, 300);

		// ���S�ɏo��
		bgStrErr.setPosition(GlView.VIEW_WIDTH*0.5f-750, GlView.VIEW_HEIGHT*0.5f-450);
		bgStrErr.setVisible(false);

		addModel(bgStrErr);
	}

	@Override
	public void start() {

		MyHttp myHttp = new MyHttp("err" + NetWorkManager.DOMAIN) {

            @Override
            public void success() {
                // 表示
                try {
                    Log.d("net", result());
                } catch (Exception e) {

                }
            }

            @Override
            public void fail(Exception e) {
            	bgStrErr.setVisible(true);
                //Log.d("net", "接続エラー:" + e.toString());
            	new Thread(new Runnable(){

					@Override
					public void run() {

						MainActivity.showOKDialog( "エラー", "ネットワークエラー",
								new UIListener() {
							@Override
							public void onClick(View view) {
								SceneManager.getInstance().setNextScene(new MenuScene());
							}
						});


					}
            		
            	}).start();

            }

        }.setSecondUrl("err" + NetWorkManager.DOMAIN_SECOND);
        
        myHttp.connect();
	}

}
