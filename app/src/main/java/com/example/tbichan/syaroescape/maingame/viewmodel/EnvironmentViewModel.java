package com.example.tbichan.syaroescape.maingame.viewmodel;

import java.util.ArrayList;
import java.util.HashSet;

import android.util.Log;


import com.example.tbichan.syaroescape.R;
import com.example.tbichan.syaroescape.maingame.GameScene;
import com.example.tbichan.syaroescape.maingame.model.Carrot;
import com.example.tbichan.syaroescape.maingame.model.Cup;
import com.example.tbichan.syaroescape.maingame.model.EnableFloor;
import com.example.tbichan.syaroescape.maingame.model.EnvSprite;
import com.example.tbichan.syaroescape.maingame.model.Environment;
import com.example.tbichan.syaroescape.maingame.model.GasEffect;
import com.example.tbichan.syaroescape.maingame.model.Player;
import com.example.tbichan.syaroescape.maingame.model.PowerEffect;
import com.example.tbichan.syaroescape.maingame.model.Rabbit;
import com.example.tbichan.syaroescape.network.MyHttp;
import com.example.tbichan.syaroescape.network.NetWorkManager;
import com.example.tbichan.syaroescape.opengl.GlObservable;
import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.scene.SceneBase;
import com.example.tbichan.syaroescape.sound.SEManager;

import static com.example.tbichan.syaroescape.maingame.model.Environment.CREATE_RABBIT;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MAP_SIZE;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_DESK;
import static com.example.tbichan.syaroescape.maingame.model.Environment.MOVE_RABBIT;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_ADD_CUP;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_END;
import static com.example.tbichan.syaroescape.maingame.model.Environment.PARAM_HIT;

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

    // ニンジンリスト
    private ArrayList<Carrot> carrotList = new ArrayList<>();

    // エフェクトモデル
    private PowerEffect powerEffect;

    // エフェクトモデル
    private GasEffect gasEffect;
    
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
    private WazaUIViewModel wazaUIViewModel;

    // デフォルト位置
    private float defaultX = 0f;

    // 自分のターンになった時の行動総回数
    private int preActCnt = 0;

    // 行動総回数(move)
    private int actCnt = 0;

    // 交代間隔
    private int actInterval = 2;

    // カップに視点を合わせているか
    private boolean cupLook = false;

    // Observserable
    private ArrayList<GlObservable> glObservableList = new ArrayList<>();

    public EnvironmentViewModel(GlView glView, SceneBase sceneBase, String name, int id, int level) {
        super(glView, sceneBase, name);

        this.id = id;



        // 共通シード値
        int globalSeed = ((GameScene)sceneBase).getGlobalSeed();

        // シード値
        int seed = Math.abs(globalSeed - id);

        // 環境を作成
        env = new Environment("env_1", seed, level);
        queryEnv("seed:" + seed, true);

        // VMを追加
        env.addGlObservable(this);

    }

    @Override
    public void awake() {

        // タイル作成
        GlModel tile = new GlModel(this, "tile") {

            @Override
            public void start() {

            }

            // 譖ｴ譁ｰ
            @Override
            public void update() {

            }

        };
        tile.setTextureId(R.drawable.tile_0);
        tile.setSize(GlView.VIEW_WIDTH / 16f * 12f, GlView.VIEW_WIDTH / 16f * 12f);
        tile.setY(INIT_Y);
        tile.setUV(0.0f, 0.0f, 3.0f, 3.0f);

        // 霑ｽ蜉�
        addModel(tile);

        // player
        player = createPlayer();
        //player.setTextureId(R.drawable.syaro_icon);
        // vm追加
        player.setEnviromentViewModel(this);
        addModel(player);



        // ステージをロード
        loadMap();

        // エフェクト
        powerEffect = new PowerEffect(this, "Eff");
        powerEffect.setPosition(player.getX(), player.getY());
        addModel(powerEffect);

        gasEffect = new GasEffect(this, "Eff");
        gasEffect.setPosition(player.getX(), player.getY());
        addModel(gasEffect);

        // シード値設定
        //queryEnv("seed:20171228");

    }

    /**
     * プレイヤーを作成します。
     */
    public Player createPlayer() {
        Player player = new Player(this, "player");
        player.setTextureId(R.drawable.syaro_icon);

        return player;
    }
    
    // 環境の変更時に実行されます。
    public void changeEnvironment(String... params) {

        // プレイヤーの移動を確認
        String param = contains(params, "move");
        if (param != null) {

            // プレイヤーモデルをロード
            for (int y = 0; y < Environment.MAP_SIZE; y++) {
                for (int x = 0; x < Environment.MAP_SIZE; x++) {

                    // id取得
                    int id = env.getPlayerMapID(y, x);

                    if (env.isMapID(y, x, Environment.PLAYER_ID)) {
                        // 移動
                        player.move(y, x, 10);
                    }

                }
            }

            // 行動回数追加
            actCnt++;
            Log.d("hoge", actCnt+"");
        }

        // 机の移動を確認
        param = contains(params, MOVE_DESK);
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
            final Rabbit rabbit = getRabbit(oldIndex);

            Log.d("rabbit", oldIndex+"");

            if (rabbit != null) {

                // 時間差で移動
                rabbit.move(oldIndex / Environment.MAP_SIZE, oldIndex % Environment.MAP_SIZE, 20);
                rabbit.move(nextIndex / Environment.MAP_SIZE, nextIndex % Environment.MAP_SIZE, 10);
            }
        }

        for (int i = 0;; i++) {
            // ウサギの追加
            param = contains(params, CREATE_RABBIT, i);
            if (param == null) break;

            int rabbitIndex = Integer.parseInt(param.replace(CREATE_RABBIT + ":", ""));

            // ウサギ作成
            final Rabbit rabbit = new Rabbit(this, "rabbit");

            // 30カウント非表示
            rabbit.hide(30, new GlObservable() {
                @Override
                public void notify(Object o, String... param) {
                    // エフェクト出現
                    EnvironmentViewModel.this.frontModel(powerEffect);
                    powerEffect.setPosition(rabbit.getX(), rabbit.getY() - 40f);
                    powerEffect.appear();

                    // 効果音再生
                    SEManager.getInstance().playSE(R.raw.rabbit);
                }
            });
            rabbit.move(rabbitIndex / MAP_SIZE, rabbitIndex % MAP_SIZE);

            // リストに追加
            rabbitList.add(rabbit);

            // VMに追加
            addModel(rabbit);

        }

        Log.d("hoge", getName() + " " + actCnt);

        // シーンに報告
        GameScene nowScene = (GameScene)getScene();

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
            cup.hide(45);

            // シーンに報告
            nowScene.notify(this, "add rabbit");
        }

        if (actCnt - preActCnt >= actInterval) {

            Log.d("hoge", getName() + " " + "endddddddddd");

            // ターン交代を通知
            nowScene.notify(this, "turnend");

        } else {

            boolean flg = true;

            param = contains(params, PARAM_END);

            if (param != null) {

                Log.d("hoge", "endddddddddd2");

                // ターン交代を通知
                nowScene.notify(this, "turnend");
                flg = false;
            }
            if (flg) {

                nowScene.notify(this);
            }
        }

        // Observerに通知
        for (GlObservable glObservable: glObservableList) {
            glObservable.notify(this);
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
        int playerIndex = env.getPlayerMapPlayerIndex();
    	
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
			enableFloor.setTextureId(R.drawable.enable);
			
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
        	if (!env.isPlayerMove(spriteX, spriteY, moveX, moveY)) continue;
        	
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
                        desk.setTextureId(R.drawable.desk);

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
                        cup.setTextureId(R.drawable.cups);

                        // リストに追加
                        cupList.add(cup);

                        // VMに追加
                        addModel(cup);

                        cupCnt++;

                        break;

                    case Environment.CARROT_ID:
                        // ニンジン作成
                        Carrot carrot = new Carrot(this, "carrot_" + 0);
                        carrot.move(y, x);

                        // リストに追加
                        carrotList.add(carrot);

                        // VMに追加
                        addModel(carrot);
                }
    		}
    	}
    }

    // 通知を受け取ります。
    @Override
    public void notify(Object o, String... params) {

        // プレイヤークリック時
        if (o instanceof Player) {

            if (uiViewModel != null && wazaUIViewModel != null && !((GameScene)getScene()).isEnd()) {

                // 移動可能床クリア
                clearPlayerEnableMoveList();

                uiViewModel.tapPlayer();
                wazaUIViewModel.tapPlayer();

                // 効果音再生
                SEManager.getInstance().playSE(R.raw.player_click);
            }
        }

        // 移動可能床クリック時
        else if (o instanceof EnableFloor) {
            final EnableFloor enableFloor = (EnableFloor)o;

            String query = "move:" + player.getMapIndex() + "," + enableFloor.getMapIndex();
            queryEnv(query, true);

            if (uiViewModel != null) uiViewModel.movedPlayer();

        } else if (o instanceof Environment) {

            // パラメータ確認
            String param = contains(params, PARAM_ADD_CUP);
            if (param != null) {

                // カップ追加時

                // カップに視点を合わせる。
                setCupLook(true);
                // sceneに報告
                ((GameScene) getScene()).notify(this, new String[]{param});
            }

            // パラメータ確認
            param = contains(params, PARAM_HIT);
            if (param != null) {

                // 衝突

                // sceneに報告
                ((GameScene) getScene()).notify(this, new String[]{param});
            }

            changeEnvironment(params);

        }

    }
    // 環境にクエリとして送ります。
    public void queryEnv(String query, boolean net) {
        env.notify(query);

        if (query.contains("move")) {
            // 効果音再生
            SEManager.getInstance().playSE(R.raw.player_click);
        }
    }


    public void setUiViewModel(ActButtonUIViewModel uiViewModel) {
        this.uiViewModel = uiViewModel;
    }
    public void setWazaUIViewModel(WazaUIViewModel wazaUIViewModel) {
        this.wazaUIViewModel = wazaUIViewModel;
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
    public void endTurn(boolean net) {

        String query = "end";

        queryEnv(query, true);

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
            // 移動回数を2かいに戻す
            setActInterval(2, false);

        } else {
            turnCnt = -1;
            preActCnt = actCnt;


        }
    }

    public int getTurnCnt() {
        return turnCnt;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * 環境VMにObserverを追加します。
     */
    public void addGlObserver(GlObservable glObservable) {
        glObservableList.add(glObservable);
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
     * 環境からカフェインパワーを取得します。
     */
    public int getCaffeinePower() {
        return env.getCupCnt() * 1;
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

    /**
     * ウサギを環境に追加します。
     */
    public int addEnvRabbit() {

        // パラメータ
        ArrayList<String> params = new ArrayList<>();

        int nextRabbit = env.createRabbit(4, params);

        // 環境更新
        changeEnvironment((String[])params.toArray(new String[params.size()]));

        return nextRabbit;

    }

    /**
     * 3回動けるようにする。
     */
    public void threeMove(boolean flg) {
        setActInterval(3, flg);

        // エフェクト出現
        powerEffect.setPosition(player.getX(), player.getY() - 40f);
        powerEffect.appear();
    }

    /**
     * 交代間隔を設定します。
     */
    protected void setActInterval(int interval) {
        this.actInterval = interval;
    }

    /**
     * 交代間隔を設定します。
     */
    protected void setActInterval(int interval, boolean flg) {
        setActInterval(interval);

        // クエリ送信
        queryEnv("fast:" + interval, flg);
    }

    /**
     * 残り回数取得
     */
    public int lastCount() {
        return actInterval - (actCnt - preActCnt);
    }

    /**
     * 衝突したかどうか
     */
    public boolean isHit() {
        return env.isHit();
    }

    /**
     * 3回移動モードかどうか
     */
    public boolean isThreeMode() {
        return actInterval == 3;
    }

    /**
     * 煙を表示させる
     */
    public void showGas() {
        // エフェクト出現
        frontModel(gasEffect);
        gasEffect.setPosition(player.getX(), player.getY());
        gasEffect.appear();
    }

}
