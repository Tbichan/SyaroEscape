package com.example.tbichan.syaroescape.maingame.viewmodel;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;


import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Cup;
import com.example.tbichan.syaroescape.maingame.model.EnableFloor;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.Player;
import com.example.tbichan.syaroescape.maingame.model.Rabbit;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.scene.SceneManager;

import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_DESK;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_RABBIT;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_ADD_CUP;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_END;

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

    // ウサギリスト
    private ArrayList<Rabbit> rabbitList = new ArrayList<>();
    
    // 環境
    Environment env;

    // 自分のID
    private int id = -1;

    // 自分のターンになった時のカウンタ
    private int turnCnt = -1;

    // プレイヤー選択中かどうか
    private boolean playerActive = false;

    // uiVM
    private ActButtonUIViewModel uiViewModel;

    // デフォルト位置
    private float defaultX = 0f;

    // 初期化済みかどうか
    private boolean initFlg = false;

    // 自分のターンになった時の行動総回数
    private int preActCnt = 0;

    // 行動総回数(move)
    private int actCnt = 0;

    // 交代間隔
    private int actInterval = 2;

    // カップに視点を合わせているか
    private boolean cupLook = false;

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

        // タイル作成
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

        // ステージをロード
        loadMap();

        // 初期化
        env.init(20171226);

    }

    // 蛻晄悄蜃ｦ逅�蛻･繧､繝ｳ繧ｹ繧ｿ繝ｳ繧ｹ逋ｻ骭ｲ)
    @Override
    public void start() {
    	

    	
    }
    
    // 環境の変更時に実行されます。
    public void changeEnvironment(String... params) {

        // 行動回数追加
        actCnt++;

        // ステージデータを確認
        int deskCnt = 0;
        int cupCnt = 0;
        int rabbitCnt = 0;
        // プレイヤーモデルをロード
        for (int y = 0; y < Environment.MAP_SIZE; y++) {
            for (int x = 0; x < Environment.MAP_SIZE; x++) {

                // id取得
                int id = env.getPlayerMapID(y, x);

                switch (id) {
                    case Environment.PLAYER_ID:
                        // 移動
                        player.move(y, x, 10);
                        break;
                    /*
                    case Environment.RABBIT_ID:
                        // ウサギ位置変更
                        rabbitList.get(rabbitCnt).move(y, x, 10);
                        rabbitCnt++;
                        break;*/
                }

                // カップ版id取得
                id = env.getCupMapID(y, x);

                /*
                switch (id) {

                    case Environment.CUP_ID:
                        // カップ位置変更
                        cupList.get(cupCnt).move(y, x);
                        //cupList.get(cupCnt).hide(60);
                        cupCnt++;
                        break;
                }*/
            }
        }


        // カップ取得回数取得
        int getCupCnt = env.getGetCupCnt();

        // 机の移動を確認
        String param = contains(params, MOVE_DESK);
        if (param != null) {

            // 移動元と移動先の机を取得
            String[] hoge = param.replace(MOVE_DESK + ":", "").split(",");
            int oldIndex = Integer.parseInt(hoge[0]);
            int nextIndex = Integer.parseInt(hoge[1]);

            // 机を取得
            EnvSprite desk = getDesk(oldIndex);

            // 移動
            desk.move(nextIndex / Environment.MAP_SIZE, nextIndex % Environment.MAP_SIZE, 10);

        }

        for (int i = 0;; i++) {
            // i番目のウサギの移動を確認
            param = contains(params, MOVE_RABBIT, i);
            if (param == null) break;

            // 移動元と移動先のウサギを取得
            String[] hoge = param.replace(MOVE_RABBIT + ":", "").split(",");
            int oldIndex = Integer.parseInt(hoge[0]);
            int nextIndex = Integer.parseInt(hoge[1]);

            // ウサギを取得
            Rabbit rabbit = getRabbit(oldIndex);

            Log.d("hoge", oldIndex+"," + param);

            // 移動
            rabbit.move(nextIndex / Environment.MAP_SIZE, nextIndex % Environment.MAP_SIZE, 10);
        }

        // カップの取得を確認
        param = contains(params, PARAM_ADD_CUP);
        if (param != null) {

            // 移動元と移動先のカップを取得
            String[] hoge = param.replace(PARAM_ADD_CUP + ":", "").split(",");
            int oldIndex = Integer.parseInt(hoge[0]);
            int nextIndex = Integer.parseInt(hoge[1]);

            // カップを取得
            Cup cup = getCup(oldIndex);

            // 移動
            cup.move(nextIndex / Environment.MAP_SIZE, nextIndex % Environment.MAP_SIZE, 10);
            cup.hide(60);
        }


        // シーンに報告
        GameScene nowScene = (GameScene) getScene();

        if (actCnt - preActCnt >= actInterval) {

            // ターン交代を通知
            nowScene.notify(this, "turnend");

        } else {

            boolean flg = true;

            param = contains(params, PARAM_END);

            if (param != null) {

                // ターン交代を通知
                nowScene.notify(this, "turnend");
                flg = false;
            }
            if (flg) {

                nowScene.notify(this);
            }
        }

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
                        // 机作成
                        EnvSprite desk = new EnvSprite(this, "desk");
                        desk.move(y, x);
                        desk.setTexture(R.drawable.desk);

                        // リストに追加
                        deskList.add(desk);

                        // VMに追加
                        addModel(desk);
                        break;

                    case Environment.RABBIT_ID:
                        // ウサギ作成
                        Rabbit rabbit = new Rabbit(this, "rabbit");
                        rabbit.move(y, x);

                        // リストに追加
                        rabbitList.add(rabbit);

                        // VMに追加
                        addModel(rabbit);
                        break;
                }

                // カップ版id取得
                id = env.getCupMapID(y, x);

                switch (id) {

                    case Environment.CUP_ID:
                        // カップ作成
                        Cup cup = new Cup(this, "cup_" + cupCnt);
                        cup.move(y, x);
                        cup.setTexture(R.drawable.cups);

                        // リストに追加
                        cupList.add(cup);

                        // VMに追加
                        addModel(cup);

                        cupCnt++;

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
                String param = contains(params, PARAM_ADD_CUP);
                if (param != null) {

                    // カップ追加時

                    // カップに視点を合わせる。
                    setCupLook(true);
                    // sceneに報告
                    ((GameScene) getScene()).notify(this, new String[]{param});
                }

                changeEnvironment(params);
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
    public final void queryEnv(String query, boolean network) {
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


    public void setUiViewModel(ActButtonUIViewModel uiViewModel) {
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

    // 移動しないで終了します。
    public void endTurn() {

        String query = "end";

        queryEnv(query);

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
            preActCnt = actCnt;
        } else {
            turnCnt = -1;
        }
    }

    public int getTurnCnt() {
        return turnCnt;
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

    public boolean isCupLook() {
        return cupLook;
    }

    public void setCupLook(boolean cupLook) {
        this.cupLook = cupLook;
    }

    /**
     * インデックスに対応する机を取得します。
     */
    public EnvSprite getDesk(int index) {
        for (EnvSprite desk: deskList) {
            if (index == desk.getMapIndex()) return desk;
        }
        return null;
    }

    /**
     * インデックスに対応するカップを取得します。
     */
    public Cup getCup(int index) {
        for (Cup cup: cupList) {
            if (index == cup.getMapIndex()) return cup;
        }
        return null;
    }

    /**
     * インデックスに対応するウサギを取得します。
     */
    public Rabbit getRabbit(int index) {
        for (Rabbit rabbit: rabbitList) {
            if (index == rabbit.getMapIndex()) return rabbit;
        }
        return null;
    }

    /**
     * 移動後の処理です。
     */
    @Override
    public void endMove() {
        if (isTurn() && isCupLook()) {
            ((GameScene)getScene()).notify(this, "playerLook");
            setCupLook(false);
        }
    }

    /**
     * パラメータの中に含まれているかを取得します。
     */
    public String contains(final String[] params, final String word) {
        return contains(params, word, 0);
    }

    /**
     * パラメータの中に含まれているかを取得します。
     */
    public String contains(final String[] params, final String word, final int index) {
        if (params == null) return null;

        int num = 0;

        for (String param: params) {
            if (param.contains(word)) {
                if (num == index) return param;
                num++;
            }
        }
        return null;
    }
}
