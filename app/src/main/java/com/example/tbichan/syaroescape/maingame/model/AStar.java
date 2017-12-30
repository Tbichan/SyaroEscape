package com.example.tbichan.syaroescape.maingame.model;

import java.util.ArrayList;

/**
 * Created by tbichan on 2017/12/22.
 */

public class AStar {

    private int startId = 1;
    private int goalId = 2;
    private int spaseId = 0;

    private int[][] map;

    private int size;

    Node start, goal;

    Node[][] nodes;

    public AStar(final int[][] map) {
        this.map = map;
        size = map.length;
        nodes = new Node[map.length][map[0].length];
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setSpaseId(int spaseId) {
        this.spaseId = spaseId;
    }

    /**
     * マップを読み込みます。
     */
    public void loadMap() {

        // Node配列に格納
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                // スタート
                if (map[y][x] == startId) {
                    nodes[y][x] = new Node(x, y);
                    start = nodes[y][x];
                } else if (map[y][x] == goalId) {
                    nodes[y][x] = new Node(x, y);
                    goal = nodes[y][x];
                } else if (map[y][x] == spaseId) {
                    nodes[y][x] = new Node(x, y);
                }
            }
        }
    }

    /**
     * 最短経路を探索します。
     */
    public void calc(){


        // ゴールのx, y座標
        int tx = goal.getX();
        int ty = goal.getY();

        // スタートをOpenにする。
        start.setStatus(Node.OPEN);

        // 実コストを求める。
        start.setCost(0);

        // 推定こすと、スコアを求める。
        start.calcEstimatedCost(tx, ty);
        start.calcScore();

        openAround(start);
    }

    /**
     * 周りをOpenにします。
     */
    public void openAround(Node sNode) {
        // 移動用(y,x)
        final int[][] vecs = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};

        // 新たにOpenしたNodeのリスト
        ArrayList<Node> nodeList = new ArrayList<Node>();

        for (int[] vec: vecs) {

            int tmpY = sNode.getY() + vec[0];
            int tmpX = sNode.getX() + vec[1];

            // クリッピング
            if (tmpY < 0 || tmpY >= size) continue;
            if (tmpX < 0 || tmpX >= size) continue;

            // null(岩)出なければ、NONEでなければOpenする。
            Node node = nodes[tmpY][tmpX];
            if (node != null && node.getStatus() == Node.NONE) {

                // 親の設定
                node.setParent(sNode);

                // open
                node.setStatus(Node.OPEN);

                // ゴールのx, y座標
                int tx = goal.getX();
                int ty = goal.getY();

                // コストを１加算
                node.setCost(sNode.getCost() + 1);

                // スコアを求める。
                node.calcEstimatedCost(tx, ty);
                node.calcScore();

                // リストに追加
                nodeList.add(node);
            }
        }

        // 基準ノードをcloseする。
        sNode.setStatus(Node.CLOSE);

        while (nodeList.size() > 0) {

            // スコアが最小のノードを選ぶ
            int minScore = Integer.MAX_VALUE;
            int minCost = Integer.MAX_VALUE;
            Node minNode = null;

            for (Node node: nodeList) {

                int score = node.getScore();

                // 最小かどうか
                if (minScore < score)
                    continue;

                // スコアが同じなら実コストを比較
                if (minScore == score && node.getCost() >= minCost)
                    continue;

                // セットする。
                minNode = node;
                minScore = node.getScore();
                minCost = node.getCost();

            }

            // 最小スコアをOpenする。
            openAround(minNode);
            nodeList.remove(minNode);

        }

    }

    /**
     * コスト出力用
     */
    public void printCost() {

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (nodes[y][x] != null) {
                    System.out.print(nodes[y][x].getCost() + ", ");
                } else {
                    System.out.print("-1, ");
                }
            }
            System.out.println();
        }
    }

    /**
     * 次の経路インデックス取得
     */
    public int next() {

        Node parent = goal.getParent();

        if (parent != null) {
            return parent.getY() * size + parent.getX();

        }
        return -1;

    }

    /**
     * コスト出力
     */
    public int getCost() {
        String[][] hogeMap = new String[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (map[y][x] == startId) hogeMap[y][x] = "Ｓ";
                else if (map[y][x] == goalId) hogeMap[y][x] = "Ｇ";
                else if (map[y][x] == spaseId) hogeMap[y][x] = "□";
                else hogeMap[y][x] = "×";

            }
        }

        return print(goal, hogeMap);
    }

    /**
     * 経路出力
     */
    public void print() {

        String[][] hogeMap = new String[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (map[y][x] == startId) hogeMap[y][x] = "Ｓ";
                else if (map[y][x] == goalId) hogeMap[y][x] = "Ｇ";
                else if (map[y][x] == spaseId) hogeMap[y][x] = "□";
                else hogeMap[y][x] = "×";

            }
        }

        System.out.print("(" + goal.getY() + ", " + goal.getX() + ") -> ");

        int cost = print(goal, hogeMap);

        System.out.println("\ncost:" + cost);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(hogeMap[y][x]);
            }
            System.out.println();
        }
    }

    public int print(Node node, String[][] hogeMap) {

        Node parent = node.getParent();

        if (parent != null) {
            System.out.print("(" + parent.getY() + ", " + parent.getX() + ") -> ");

            if (!hogeMap[parent.getY()][parent.getX()].equals("Ｓ")) hogeMap[parent.getY()][parent.getX()] = "■";

            return print(parent, hogeMap) + 1;
        } else {
            return 0;
        }


    }

    private static class Node {

        public static final int NONE = 0;
        public static final int OPEN = 1;
        public static final int CLOSE = 2;

        // 座標
        private int x, y = 0;

        // ステータス
        private int status = NONE;

        // 実コスト
        private int cost = 0;

        // 推定コスト
        private int estimatedCost = 0;

        // スコア
        private int score = 0;

        // 親ノードクラス
        private Node parent = null;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getCost() {
            return cost;
        }

        /**
         * ステータスを変更します。
         */
        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        /**
         * 実コストを設定します。
         */
        public void setCost(int cost) {
            this.cost = cost;
        }

        /**
         * 推定コストを求めます。
         */
        public int calcEstimatedCost(int tx, int ty) {
            int dx = tx - x;
            int dy = ty - y;
            if (dx < 0) dx *= -1;
            if (dy < 0) dy *= -1;

            estimatedCost = dx + dy;

            return estimatedCost;
        }

        /**
         * スコアを求めます。
         */
        public int calcScore() {

            score = cost + estimatedCost;

            return score;
        }

        public int getScore() {
            return score;
        }

        public void setParent(Node node) {
            parent = node;
        }

        public Node getParent() {
            return parent;
        }

    }
}

