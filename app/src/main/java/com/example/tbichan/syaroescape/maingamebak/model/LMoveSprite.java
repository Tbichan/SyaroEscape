package com.example.tbichan.syaroescape.maingamebak.model;

import android.view.MotionEvent;

import com.example.tbichan.syaroescape.opengl.model.GlModel;
import com.example.tbichan.syaroescape.opengl.view.GlView;
import com.example.tbichan.syaroescape.opengl.viewmodel.GlViewModel;

import static com.example.tbichan.syaroescape.activity.MainActivity.getGlView;

/**
 * Created by tbichan on 2017/10/17.
 */

public abstract class LMoveSprite extends Sprite {

    // 状態
    enum Status {
        WAIT,
        RIGHT,
        UP,
        LEFT,
        DOWN;
    }

    // 動いているか
    private Status status = Status.WAIT;

    // 現在の格子点
    private int lx, ly;

    // 向かう予定の格子点
    private int nextLx, nextLy;

    // 直前の静止状態時の座標
    private float preX, preY;

    // ステージモデル
    private StageModel stageModel;

    // id
    private int id;

    // 机を蹴飛ばせるかどうか
    private boolean kickTable = true;

    // 机に貫通するかどうか
    private boolean throughTable = false;

    // 動いた回数
    private int moveCnt = 0;

    public void setKickTable(boolean kickTable) {
        this.kickTable = kickTable;
    }

    public boolean isKickTable() {
        return kickTable;
    }

    public void setThroughTable(boolean throughTable) {
        this.throughTable = throughTable;
    }

    public boolean isThroughTable() {
        return throughTable;
    }

    public int getLx() {
        return lx;
    }

    public int getLy() {
        return ly;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LMoveSprite(GlViewModel glViewModel, String name) {
        super(glViewModel, name);
        setLPosition(0, 0);

    }

    // 読み込み
    @Override
    public void awake() {

    }

    // 初期処理(別インスタンス登録)
    @Override
    public void startBefore() {
        // モデル読み込み
        stageModel = ((StageModel) getGlView().findModel("stagemodel"));
    }

    // 事前更新
    @Override
    public final void updateBefore() {
        float mx, my;

        switch (status) {
            case RIGHT:
                // 右に移動
                mx = getX() + 3.0f;
                if (getX() - preX > GlView.TILE_SIZE) {
                    mx = preX + GlView.TILE_SIZE;
                    status = Status.WAIT;
                    lx = nextLx;

                    // マップ登録
                    stageModel.setMapID(StageModel.EMPTY, getGlViewModel(), lx - 1, ly);
                    stageModel.setMapID(id, getGlViewModel(), lx, ly);
                    moveCnt++;
                }
                setPosition(mx, getY());
                break;
            case UP:
                // 上に移動
                my = getY() + 3.0f;
                if (getY() - preY > GlView.TILE_SIZE) {
                    my = preY + GlView.TILE_SIZE;
                    status = Status.WAIT;
                    ly = nextLy;

                    // マップ登録
                    stageModel.setMapID(StageModel.EMPTY, getGlViewModel(), lx, ly + 1);
                    stageModel.setMapID(id, getGlViewModel(), lx, ly);
                    moveCnt++;
                }
                setPosition(getX(), my);
                break;
            case LEFT:
                // 左に移動
                mx = getX() - 3.0f;
                if (preX - getX() > GlView.TILE_SIZE) {
                    mx = preX - GlView.TILE_SIZE;
                    status = Status.WAIT;
                    lx = nextLx;

                    // マップ登録
                    stageModel.setMapID(StageModel.EMPTY, getGlViewModel(), lx + 1, ly);
                    stageModel.setMapID(id, getGlViewModel(), lx, ly);
                    moveCnt++;
                }
                setPosition(mx, getY());
                break;
            case DOWN:
                // 下に移動
                my = getY() - 3.0f;
                if (preY - getY() > GlView.TILE_SIZE) {
                    my = preY - GlView.TILE_SIZE;
                    status = Status.WAIT;
                    ly = nextLy;

                    // マップ登録
                    stageModel.setMapID(StageModel.EMPTY, getGlViewModel(), lx, ly - 1);
                    stageModel.setMapID(id, getGlViewModel(), lx, ly);
                    moveCnt++;
                }
                setPosition(getX(), my);
                break;
        }
    }

    // タップイベント
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    // 格子点に移動
    public void setLPosition(int lx, int ly) {
        this.lx = lx;
        this.ly = ly;
        setPosition(lx * GlView.TILE_SIZE, (15 - ly) * GlView.TILE_SIZE);
    }

    // 移動
    public boolean move(Status s){

        switch (s) {
            case RIGHT:
                if (status == Status.WAIT && lx < 14) {
                    //if (stageModel.getMap(getGlViewModel(), lx + 1, ly) != 1) {
                    if (stageModel.isPass(this, Status.RIGHT, lx, ly)) {
                        // 右に移動
                        status = Status.RIGHT;
                        nextLx = lx + 1;
                        preX = getX();
                        preY = getY();

                        // 右に机があるかどうか
                        GlModel table = stageModel.getMap(getGlViewModel(), lx + 1, ly);
                        if (table != null && table instanceof Table) {
                            ((Table) table).move(Status.RIGHT);
                        }
                        return true;
                    }
                    //}
                }
                break;
            case UP:
                if (status == Status.WAIT && ly > 1) {
                    if (stageModel.isPass(this, Status.UP, lx, ly)) {
                        // 上に移動
                        status = Status.UP;
                        nextLy = ly - 1;
                        preX = getX();
                        preY = getY();

                        // 上に机があるかどうか
                        GlModel table = stageModel.getMap(getGlViewModel(), lx, ly - 1);
                        if (table != null && table instanceof Table) {
                            ((Table) table).move(Status.UP);
                        }
                        return true;
                    }
                }
                break;
            case LEFT:
                if (status == Status.WAIT && lx > 1) {
                    if (stageModel.isPass(this, Status.LEFT, lx, ly)) {
                        // 左に移動
                        status = Status.LEFT;
                        nextLx = lx - 1;
                        preX = getX();
                        preY = getY();

                        // 左に机があるかどうか
                        GlModel table = stageModel.getMap(getGlViewModel(), lx - 1, ly);
                        if (table != null && table instanceof Table) {
                            ((Table) table).move(Status.LEFT);
                        }
                        return true;
                    }
                }
                break;
            case DOWN:
                if (status == Status.WAIT && ly < 14) {
                    if (stageModel.isPass(this, Status.DOWN, lx, ly)) {
                        // 下に移動
                        status = Status.DOWN;
                        nextLy = ly + 1;
                        preX = getX();
                        preY = getY();

                        // 下に机があるかどうか
                        GlModel table = stageModel.getMap(getGlViewModel(), lx, ly + 1);
                        if (table != null && table instanceof Table) {
                            ((Table) table).move(Status.DOWN);
                        }
                        return true;
                    }
                }
                break;
        }

        // 移動失敗
        return false;
    }

    public int getMoveCnt() {
        return moveCnt;
    }
}
