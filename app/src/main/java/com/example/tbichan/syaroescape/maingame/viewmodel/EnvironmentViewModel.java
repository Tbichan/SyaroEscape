package com.example.tbichan.syaroescape.maingame.viewmodel;

import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;


import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Cup;
import com.example.tbichan.syaroescape.maingame.model.EnableFloor;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.Player;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_ADD_CUP;

/**
 * Created by tbichan on 2017/12/10.
 */

public class EnvironmentViewModel extends MoveViewModel implements GlObservable {

    // Y蠎ｧ讓吶�蛻晄悄蛟､
    private final float INIT_Y = GlView.VIEW_HEIGHT - GlView.VIEW_WIDTH / 16f * 12f;

    // player
    private Player player;
    
    // 机リスト
    private ArrayList<EnvSprite> deskList = new ArrayList<>();

    // カップリスト
    private ArrayList<Cup> cupList = new ArrayList<>();
    
    // 移動可能床リスト
    private ArrayList<EnableFloor> enableFloorList = new ArrayList<>();
    
    // 環境
    Environment env;

    // 自分のID
    private int id = -1;

    // 自分のターンになった時のカウンタ
    private int turnCnt = -1;

    // プレイヤー選択中かどうか
    private boolean playerActive = false;

    // uiVM
    private UIViewModel uiViewModel;

    // デフォルト位置
    private float defaultX = 0f;

    // 移動回数
    private int moveCnt = 0;

    // 初期化済みかどうか
    private boolean initFlg = false;

    public EnvironmentViewModel(GlView glView, SceneBase sceneBase, String name, int id) {
        super(glView, sceneBase, name);

        this.id = id;

        // 環境を作成
        env = new Environment("env_1");

        // VMを追加
        env.addGlObservable(this);

    }

    // 隱ｭ縺ｿ霎ｼ縺ｿ
    @Override
    public void awake() {

        // 繝｢繝�Ν隱ｭ縺ｿ霎ｼ縺ｿ

        // 繧ｿ繧､繝ｫ
        GlModel tile = new GlModel(this, "tile") {

            // 蛻晄悄蜃ｦ逅�蛻･繧､繝ｳ繧ｹ繧ｿ繝ｳ繧ｹ逋ｻ骭ｲ)
            @Override
            public void start() {

            }

            // 譖ｴ譁ｰ
            @Override
            public void update() {

            }

        };
        tile.setTexture(R.drawable.tile_0);
        tile.setSize(GlView.VIEW_WIDTH / 16f * 12f, GlView.VIEW_WIDTH / 16f * 12f);
        tile.setY(INIT_Y);
        tile.setUV(0.0f, 0.0f, 3.0f, 3.0f);

        // 霑ｽ蜉�
        addModel(tile);

        // player
        player = new Player(this, "player");
        player.setTexture(R.drawable.syaro_icon);
        // vm追加
        player.setEnviromentViewModel(this);
        addModel(player);

    }

    // 蛻晄悄蜃ｦ逅�蛻･繧､繝ｳ繧ｹ繧ｿ繝ｳ繧ｹ逋ｻ骭ｲ)
    @Override
    public void start() {
    	
    	// ステージをロード
    	loadMap();

        // 初期化
        env.init();
    	
    }
    
    // 環境の変更時に実行されます。
    public void changeEnvironment() {
    	
    	// ステージデータを確認
    	int deskCnt = 0;
        int cupCnt = 0;
    	// プレイヤーモデルをロード
    	for (int y = 0; y < Environment.MAP_SIZE; y++) {
            for (int x = 0; x < Environment.MAP_SIZE; x++) {

                // id取得
                int id = env.getPlayerMapID(y, x);

                switch (id) {
                    case Environment.PLAYER_ID:
                        // 移動
                        player.move(y, x);
                        break;

                    case Environment.DESK_ID:
                        // 机位置変更
                        deskList.get(deskCnt).move(y, x);
                        deskCnt++;
                        break;

                }

                // カップ版id取得
                id = env.getCupMapID(y, x);

                switch (id) {

                    case Environment.CUP_ID:
                        // カップ位置変更
                        cupList.get(cupCnt).move(y, x);
                        cupCnt++;
                        break;
                }
            }
        }

        // 余ったカップを削除
        while (cupCnt < cupList.size()) {
            //Log.d("hoge", cupCnt+"");
            Cup delCup = cupList.get(cupList.size() - 1);
            delCup.delete();
            delCup.setVisible(false);
            cupList.remove(delCup);
        }

    	// 移動回数更新
        moveCnt++;

        // カップ取得回数取得
        int getCupCnt = env.getGetCupCnt();

        Log.d("cupCnt", getCupCnt + "");

        // シーンに報告
        GameScene nowScene = (GameScene)SceneManager.getInstance().getNowScene();
        nowScene.notify(this);
    	
    	// 移動可能リスト取得
    	//createPlayerEnableMoveList(1);
    }

    // プレイヤーの移動できるところ一覧を消します。
    public void clearPlayerEnableMoveList() {
        // リスト初期化
        for (EnableFloor enablefloor: enableFloorList) {
            enablefloor.delete();
        }
        enableFloorList.clear();
    }
    
    // プレイヤーの移動できるところ一覧を作成します。
    public void createPlayerEnableMoveList(int distance) {
    	
    	// リスト初期化
        clearPlayerEnableMoveList();
    	
    	// 環境からプレイヤーの場所を取得
        int playerIndex = env.getPlayerMapPlyaerIndex();
    	
    	// 一覧を取得します。
    	final HashSet<Integer> enableList = getSpriteEnableMoveList(playerIndex, distance);
    	
    	for (int index: enableList) {

            // 自身は除外
            if (index == playerIndex) continue;

    		int mapY = index / Environment.MAP_SIZE;
    		int mapX = index % Environment.MAP_SIZE;
    		// 床作成
    		EnableFloor enableFloor = new EnableFloor(this, "enable");

            // プレイヤー登録
            enableFloor.setPlayer(player);
			enableFloor.move(mapY, mapX);
			enableFloor.setTexture(R.drawable.enable);
			
			// 環境VMを追加
			enableFloor.setEnviromentViewModel(this);
			
			// リストに追加
			enableFloorList.add(enableFloor);
			
			// VMに追加
			addModel(enableFloor);
    	}
    }
    
    // プレイヤーの移動できるところ一覧を取得します。
    public HashSet<Integer> getSpriteEnableMoveList(int spriteIndex, int distance) {
    	
    	HashSet<Integer> enableList = new HashSet<>();
        
        // 自身を追加
        enableList.add(spriteIndex);
        
        int spriteY = spriteIndex / Environment.MAP_SIZE;
        int spriteX = spriteIndex % Environment.MAP_SIZE;

        // 移動ベクトル
        final int[][] vecs = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};
        
        for (int[] vec: vecs) {
        	int moveY = spriteY + vec[1];
        	int moveX = spriteX + vec[0];
        	
        	// クリッピング
        	if (moveY < 0) continue;
        	if (moveX < 0) continue;
        	
        	if (moveY >= Environment.MAP_SIZE) continue;
        	if (moveX >= Environment.MAP_SIZE) continue;
        	
        	// Envに移動できるか確認
        	if (!env.isMove(spriteX, spriteY, moveX, moveY)) continue;
        	
        	int index = moveY * Environment.MAP_SIZE + moveX;
        	
        	// 追加
        	enableList.add(index);
        	
        	// 次の移動リスト取得
        	if (distance > 1) {
        		final HashSet<Integer> enableNextList = getSpriteEnableMoveList(index, distance - 1);
        		enableList.addAll(enableNextList);
        	}
        }
    	
    	return enableList;
    	
    }

    // マップを読み込みます。
    public void loadMap() {
    	
    	// プレイヤーモデルをロード
    	for (int y = 0; y < Environment.MAP_SIZE; y++) {
    		for (int x = 0; x < Environment.MAP_SIZE; x++) {

    			// id取得
    			int id = env.getPlayerMapID(y, x);

    			switch (id) {
                    case Environment.PLAYER_ID:
                        // 移動
                        player.move(y, x);
                        break;

                    case Environment.DESK_ID:
                        // 机作成
                        EnvSprite desk = new EnvSprite(this, "desk");
                        desk.move(y, x);
                        desk.setTexture(R.drawable.desk);

                        // リストに追加
                        deskList.add(desk);

                        // VMに追加
                        addModel(desk);
                        break;
                }

                // カップ版id取得
                id = env.getCupMapID(y, x);

                switch (id) {

                    case Environment.CUP_ID:
                        // カップ作成
                        Cup cup = new Cup(this, "cup");
                        cup.move(y, x);
                        cup.setTexture(R.drawable.cups);

                        // リストに追加
                        cupList.add(cup);

                        // VMに追加
                        addModel(cup);
                        break;
                }
    		}
    	}
    }

    // 通知を受け取ります。
    @Override
    public void notify(Object o, String... params) {

        // プレイヤークリック時
        if (o instanceof Player) {

            if (uiViewModel != null) uiViewModel.tapPlayer();
            //uiViewShowButton();
        }

        // 移動可能床クリック時
        else if (o instanceof EnableFloor) {
            final EnableFloor enableFloor = (EnableFloor)o;

            String query = "move:" + player.getMapIndex() + "," + enableFloor.getMapIndex();
            queryEnv(query);

            if (uiViewModel != null) uiViewModel.movedPlayer();

        } else if (o instanceof Environment) {

            // 最初の一回は適用しない
            if (initFlg) {

                // パラメータ確認
                if (params != null) {
                    if (params.length > 0) {

                        // カップ追加時
                        if (params[0].equals(PARAM_ADD_CUP)) {

                            createCup();
                        }
                    }
                }

                changeEnvironment();
            } else {
                initFlg = true;
            }


        }

    }
    // 環境にクエリとして送ります。
    public void queryEnv(String query) {
        queryEnv(query, true);
    }

    // 環境にクエリとして送ります。
    public void queryEnv(String query, boolean network) {
        env.notify(query);

        if (network == true) {

            // クエリにIDを付加

            // サーバに送る
            MyHttp myHttp = new MyHttp(NetWorkManager.DOMAIN + "sql/send/send.py?query=" + query + "&" + "id=" + id) {

                // 接続成功時
                @Override
                public void success() {
                    // 表示
                    try {
                        Log.d("net", result());
                    } catch (Exception e) {

                    }
                }

                // 接続失敗時
                @Override
                public void fail(Exception e) {
                    Log.d("net", "接続エラーss:" + e.toString());

                }

            }.setSecondUrl(NetWorkManager.DOMAIN_SECOND + "sql/send/send.py?query=" + query + "&" + "id=" + id);

            myHttp.connect();
        }
    }


    public void setUiViewModel(UIViewModel uiViewModel) {
        this.uiViewModel = uiViewModel;
    }

    // ボタン表示
    public void uiViewShowButton() {

        if (uiViewModel != null) {
            uiViewModel.enter();
        }

        // プレイヤー選択状態に
        playerActive = true;
    }

    // 移動ボタンクリック時
    public void moveButtonClick() {

        // ボタン非表示
        if (uiViewModel != null) {
            // 移動可能リスト取得
            createPlayerEnableMoveList(1);

            uiViewModel.hide();
        }
    }

    // カメラ移動モードに
    public void cameraCanMoveMode() {

        // ボタン非表示
        if (uiViewModel != null) {
            uiViewModel.hide();

        }

        // 移動可能床クリア
        clearPlayerEnableMoveList();

        // プレイヤー非選択状態に
        playerActive = false;
    }

    /**
     * ランダムにカップを作成します。
     */
    public void createCup() {
        final int playerX = player.getMapX();
        final int playerY = player.getMapY();

        int cupX = (int) (Math.random() * Environment.MAP_SIZE);
        int cupY = (int) (Math.random() * Environment.MAP_SIZE);

        // プレイヤーと重なっているか確認
        while ((cupX == playerX && cupY == playerY) || env.getCupMapID(cupY, cupX) == Environment.CUP_ID) {
            cupX = (int) (Math.random() * Environment.MAP_SIZE);
            cupY = (int) (Math.random() * Environment.MAP_SIZE);
        }

        // クエリ作成
        String query = "cup:" + (cupY * Environment.MAP_SIZE + cupX);

        queryEnv(query);

        // sceneに報告
        ((GameScene)getScene()).notify(this, new String[]{query});
    }

    // 移動しないで終了します。
    public void endTurn() {
        // プレイヤー移動
        // 環境にクエリ通達
        final int playerIndex = player.getMapIndex();

        //String query = playerX + "," + playerY + "," + nextX + "," + nextY;
        String query = "move:" + playerIndex + "," + playerIndex;
        //String query = player.getMapX() + ", " + player.getMapY() + ", " + player.getMapX() + ", " + player.getMapY();
        queryEnv(query);

        if (moveCnt % 2 == 1) queryEnv(query);
        //Log.d("hoge", moveCnt+"");
    }

    public float getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(float defaultX) {
        this.defaultX = defaultX;
    }

    public boolean isTurn() {
        return turnCnt != -1;
    }

    public void setTurn(boolean turn) {
        if (turn) {
            turnCnt = getCnt();
        } else {
            turnCnt = -1;
        }
    }

    public int getTurnCnt() {
        return turnCnt;
    }

    public int getMoveCnt() {
        return moveCnt;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * 環境にObserverを追加します。
     */
    public void addEnvGlObserver(GlObservable glObserver) {
        env.addGlObservable(glObserver);
    }

    public Environment getEnv() {
        return env;
    }

    public int getId() {
        return id;
    }

    /**
     * 移動後の処理です。
     */
    @Override
    public void endMove() {
        ((GameScene)getScene()).notify(this, "playerLook");
    }
}
