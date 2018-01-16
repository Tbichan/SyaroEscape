package com.example.tbichan.syaroescape.experience.viewmodel;

import android.view.View;

import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.activity.MainActivity;
import com.example.tbichan.syaroescape.common.model.GlButton;
import com.example.tbichan.syaroescape.experience.model.TextModel;
import com.example.tbichan.syaroescape.menu.MenuScene;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.store.StoreManager;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;
import com.example.tbichan.syaroescape.sound.SEManager;
import com.example.tbichan.syaroescape.sqlite.DataBaseHelper;
import com.example.tbichan.syaroescape.ui.UIListener;

import java.util.ArrayList;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

public class StringViewModel extends GlViewModel {

	TextModel textModel2;
	float win = 0.5f;


	public StringViewModel(GlView glView, SceneBase sceneBase, String name) {
		super(glView, sceneBase, name);

	}

	@Override
	public void awake() {

		try {

			GlModel bgModel = new GlModel(this, "BGModel") {
				@Override
				public void start() {

				}

				@Override
				public void update() {

				}
			};

			bgModel.setTextureId(R.drawable.detail_bg);
			bgModel.setSize(GlView.VIEW_WIDTH, GlView.VIEW_HEIGHT);
			bgModel.setPosition(0, 0);
			bgModel.setAlpha(0.5f);

			addModel(bgModel);

			final ArrayList<String> stringHashSet = new ArrayList<>();

			// プレイヤー名をよみこみ
			String playerName = "";
			try {
				playerName = "プレイヤーネーム：" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.PLAYER_NAME);
			} catch (Exception e) {

			}

			stringHashSet.add(playerName);

			// IDをよみこみ
			String playerIdString = "";
			try {
				playerIdString = "プレイヤーＩＤ　：" + DataBaseHelper.getDataBaseHelper().read(DataBaseHelper.NETWORK_ID);
			} catch (Exception e) {

			}

			stringHashSet.add(playerIdString);

			int battle = StoreManager.restoreInteger("battle");
			int win = StoreManager.restoreInteger("win");
			int lose = battle - win;
			int persent = (int) ((float) win / battle * 100.0f);

			stringHashSet.add("対戦数　　　　　：" + String.valueOf(battle) + "かい");
			stringHashSet.add("勝った回数　　　：" + String.valueOf(win) + "かい");
			stringHashSet.add("負けた回数　　　：" + String.valueOf(lose) + "かい");
			stringHashSet.add("勝率　　　　　　：" + String.valueOf(persent) + "％");

			for (int i = 0; i < stringHashSet.size(); i++) {

				GlModel playerNameModel = new GlModel(this, "name") {
					@Override
					public void start() {

					}

					@Override
					public void update() {

					}
				};

				playerNameModel.setTextureText(stringHashSet.get(i));
				playerNameModel.setSize(2000, 500);
				playerNameModel.setPosition(200, 800 - i * 200);

				addModel(playerNameModel);
			}

			// メニューに戻る
			GlButton endGameButton = new GlButton(this, "com_turn") {

				@Override
				public void update() {
					setAlpha(0.15f * ((float) Math.sin(getCnt() * 0.2f) + 1.0f) + 0.5f);
					setBright(0.05f * ((float) Math.sin(getCnt() * 0.2f) + 1.0f) + 0.1f);
				}

				@Override
				public void onClick() {

					// 効果音再生
					SEManager.getInstance().playSE(R.raw.button_click);

					MainActivity.showOKCancelDialog(new UIListener() {
						/**
						 * OKをクリックしたとき
						 * @param view
						 */
						@Override
						public void onClick(View view) {
							SceneManager.getInstance().setNextScene(new MenuScene());
						}
					}, new UIListener() {
						/**
						 * キャンセルを押したとき
						 * @param view
						 */
						@Override
						public void onClick(View view) {

						}
					});
					//SceneManager.getInstance().setNextScene(new MenuScene());
				}

				@Override
				public void onTex() {

				}

				@Override
				public void upTex() {

				}
			};

			endGameButton.setTextureId(R.drawable.end_game);
			endGameButton.setPosition(0f, GlView.VIEW_HEIGHT - 70);
			endGameButton.setSize(500, 70);
			addModel(endGameButton);
		} catch (Exception e) {

		}

		//MatsuokaModel matsuokaModel = new MatsuokaModel(this,"matsuoka");


		//matsuokaModel.setPosition(0, 500);
		//matsuokaModel.setText

		//addModel(matsuokaModel);
		/*
		TextModel textModel = new TextModel(this, "test");
		textModel2 = new TextModel(this, "text");
		textModel2.setPosition(0, 1000);
		textModel.setPosition(0, 500);
		textModel.setText("プレイヤー名：きやま");
		textModel2.setText("CPUとの勝率：" + win * 100 + "％");
		addModel(textModel);
		addModel(textModel2);*/

	}

	@Override
	public void start() {


	}

	@Override
	public void update(GL10 gl){
		super.update(gl);


		if (getCnt()%60==0) {
			win = (float)Math.random();
			//textModel2.setText("CPUとの勝率：" + (int)(win * 100) + "％");
		}
	}

}
