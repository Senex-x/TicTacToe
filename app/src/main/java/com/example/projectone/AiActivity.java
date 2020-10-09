package com.example.projectone;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AiActivity extends AppCompatActivity implements View.OnClickListener {

    int[][] field = new int[15][15];
    static double inf = 1000000007;
    static int sense_depth = 3;
    static int winTile = 4;
    boolean isFinished = false;
    static double e = 2.7182818284590452353602874713527;
    static double clear_cost = 1.3;

    void winCheck () {
        line = 0; winLine = winTile;
        for (int i = -(winLine - 1); i < winLine; i++) { // вертикаль
            if (field[x][y] == field[x + i][y]) {
                addCords(x + i, y);
                line++;
                if (line == winLine) {
                    endGame();
                    break;
                }
            } else {
                line = 0;
                clearCords();
            }
        }
        for (int i = -(winLine - 1); i < winLine; i++) { // горизонталь
            if (field[x][y] == field[x][y + i]) {
                addCords(x , y + i);
                line++;
                if (line == winLine) {
                    endGame();
                    break;
                }
            } else {
                line = 0;
                clearCords();
            }
        }
        for (int i = -(winLine - 1); i < winLine; i++) { // левая диагональ [\]
            if (field[x][y] == field[x + i][y + i]) {
                addCords(x + i , y + i);
                line++;
                if (line == winLine) {
                    endGame();
                    break;
                }
            } else {
                line = 0;
                clearCords();
            }
        }
        for (int i = -(winLine - 1); i < winLine; i++) { // правая диагональ [/]
            if (field[x][y] == field[x + i][y - i]) {
                addCords(x + i, y - i);
                line++;
                if (line == winLine) {
                    endGame();
                    break;
                }
            } else {
                line = 0;
                clearCords();
            }
        }
        if (!turn.getText().equals("X wins!") && !turn.getText().equals("O wins!")) {
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).getText().equals("S")) {
                    break;
                }
                if (i == tiles.size() - 1) {
                    turn.setText("DRAW");
                    turn.setTextColor(getResources().getColor(R.color.gray));
                    restart.setVisibility(View.VISIBLE);
                    for (int j = 0; j < tiles.size(); j++) {
                        tiles.get(j).setTextColor(getResources().getColor(R.color.gray));
                    }
                    change = !change;
                }
            }
        }
    }

    double s(int side) {
        if (side == 1) {
            return 1;
        } else {
            return -1;
        }
    }

    public class node {
        int[][] map = new int[15][15];
        int n, side;

        public int[][] getField() {
            return map;
        }

        int get(int x, int y) {
            return map[3 + x][3 + y];
        }

        void add(int x, int y, int val) {
            map[3 + x][3 + y] = val;
            return;
        }

        double cost(int tp) {
            double cnt = 0;
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (j > 0 && get(i, j - 1) == tp) {
                        continue;
                    }
                    if (get(i, j) != tp) {
                        continue;
                    }
                    int len = 1;
                    while (j + len < n && get(i, j + len) == tp) {
                        ++len;
                    }
                    if (len >= winTile) {
                        return inf;
                    }
                    int a = 0;
                    while (j - a - 1 >= 0 && get(i, j - a - 1) == 0) {
                        ++a;
                    }
                    int b = 0;
                    while (j + len + b < n && get(i, j + len + b) == 0) {
                        ++b;
                    }
                    if (a + len + b < winTile) {
                        continue;
                    }
                    cnt += (clear_cost / n) * (double)Math.min(a, b);
                    double x = Math.pow(e, len);
                    double bad = x / 3;
                    if (a + len < winTile) {
                        x -= bad;
                    }
                    if (b + len < winTile) {
                        x -= bad;
                    }
                    cnt += x;
                }
            }
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i > 0 && get(i - 1, j) == tp) {
                        continue;
                    }
                    if (get(i, j) != tp) {
                        continue;
                    }
                    int len = 1;
                    while (i + len < n && get(i + len, j) == tp) {
                        ++len;
                    }
                    if (len >= winTile) {
                        return inf;
                    }
                    int a = 0;
                    while (i - a - 1 >= 0 && get(i - a - 1, j) == 0) {
                        ++a;
                    }
                    int b = 0;
                    while (i + len + b < n && get(i + len + b, j) == 0) {
                        ++b;
                    }
                    if (a + len + b < winTile) {
                        continue;
                    }
                    cnt += (clear_cost / n) * (double)Math.min(a, b);
                    double x = Math.pow(e, len);
                    double bad = x / 3;
                    if (a + len < winTile) {
                        x -= bad;
                    }
                    if (b + len < winTile) {
                        x -= bad;
                    }
                    cnt += x;
                }
            }
            //^^^^here horizontal+vertical, after diagonal12>>>>>>>>>>>
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i > 0 && j > 0 && get(i - 1, j - 1) == tp) {
                        continue;
                    }
                    if (get(i, j) != tp) {
                        continue;
                    }
                    int len = 1;
                    while (i + len < n && j + len < n && get(i + len, j + len) == tp) {
                        ++len;
                    }
                    if (len >= winTile) {
                        return inf;
                    }
                    int a = 0;
                    while (i - a - 1 >= 0 && j - a - 1 >= 0 && get(i - a - 1, j - a - 1) == 0) {
                        ++a;
                    }
                    int b = 0;
                    while (i + len + b < n && j + len + b < n && get(i + len + b, j + len + b) == 0) {
                        ++b;
                    }
                    if (a + len + b < winTile) {
                        continue;
                    }
                    cnt += (clear_cost / n) * (double)Math.min(a, b);
                    double x = Math.pow(e, len);
                    double bad = x / 3;
                    if (a + len < winTile) {
                        x -= bad;
                    }
                    if (b + len < winTile) {
                        x -= bad;
                    }
                    cnt += x;
                }

            }
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i > 0 && j + 1 < n && get(i - 1, j + 1) == tp) {
                        continue;
                    }
                    if (get(i, j) != tp) {
                        continue;
                    }
                    int len = 1;
                    while (i + len < n && j - len >= 0 && get(i + len, j - len) == tp) {
                        ++len;
                    }
                    if (len >= winTile) {
                        return inf;
                    }
                    int a = 0;
                    while (i + a + 1 < n && j - a - 1 >= 0 && get(i + a + 1, j - a - 1) == 0) {
                        ++a;
                    }
                    int b = 0;
                    while (i + len + b < n && j - len - b >= 0 && get(i + len + b, j - len - b) == 0) {
                        ++b;
                    }
                    if (a + len + b < winTile) {
                        continue;
                    }
                    cnt += (clear_cost / n) * (double)Math.min(a, b);
                    double x = Math.pow(e, len);
                    double bad = x / 3;
                    if (a + len < winTile) {
                        x -= bad;
                    }
                    if (b + len < winTile) {
                        x -= bad;
                    }
                    cnt += x;
                }
            }
            return cnt;
        }

        double cost() {
            //return (int)(Math.random()*1000);
            return s(side) * (cost(side) - cost(((side - 1) ^ 1) + 1));
        }
    }


    public node skopirovatt(node ex) {
        node n = new node(); // = ex;
        n.n = ex.n;
        n.side = ex.side;
        n.map = new int[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                n.map[i][j] = ex.map[i][j];
            }
        }
        //n.getField();
        return n;
    }

    public double alphabeta(node f, int dep, double alpha, double beta, int side) {
        if (dep == 0 || Math.abs(f.cost()) > inf / 2) {
            return f.cost();
        }
        if (s(side) == 1) {
            double res = -inf;
            int did = 0;
            for (int i = 0; i < f.n; ++i) {
                for (int j = 0; j < f.n; ++j) {
                    if (did == 1) {
                        break;
                    }
                    if (f.get(i, j) == 0) {
                        node to = skopirovatt(f);
                        to.add(i, j, side);
                        to.side = ((side - 1) ^ 1) + 1;
                        res = Math.max(res, alphabeta(to, dep - 1, alpha, beta, ((side - 1) ^ 1) + 1));
                        alpha = Math.max(alpha, res);
                        if (alpha >= beta) {
                            did = 1;
                            break;
                        }
                    }
                }
            }
            return res;
        } else {
            double res = inf;
            int did = 0;
            for (int i = 0; i < f.n; ++i) {
                for (int j = 0; j < f.n; ++j) {
                    if (did == 1) {
                        break;
                    }
                    if (f.get(i, j) == 0) {
                        node to = skopirovatt(f);
                        to.add(i, j, side);
                        to.side = ((side - 1) ^ 1) + 1;
                        res = Math.min(res, alphabeta(to, dep - 1, alpha, beta, ((side - 1) ^ 1) + 1));
                        beta = Math.min(alpha, res);
                        if (alpha >= beta) {
                            did = 1;
                            break;
                        }
                    }
                }
            }
            return res;
        }
    }

    int get_type() {
        return 1;
    } // возвращает сторону, за которую играет компьютер

    int[] solve() {
        int side = get_type();
        node st = new node();
        st.side = get_type();
        st.map = new int[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                st.map[i][j] = field[i][j];
            }
        }

        st = skopirovatt(st);
        rightP.setText("");
        rightP.setTextSize(20);
        for (int k = 3; k < h+3; k++) {
            for (int f = 3; f < h+3; f++) {
                rightP.setText(rightP.getText() + Integer.toString(st.map[k][f]));
            }
            rightP.setText(rightP.getText() + " ");
        }

        st.n = w;
        P1winCount.setText(Integer.toString((int)st.cost()));
        int[] res = new int[2];
        res[0] = -1; res[1] = -1;
        double ans = -inf;
        for (int i = 0; i < st.n; ++i) {
            for (int j = 0; j < st.n; ++j) {
                if (st.get(i, j) != 0) {
                    continue;
                }
                node to = skopirovatt(st);
                to.add(i, j, side);
                to.side = ((side - 1) ^ 1) + 1;
                double x = s(side) * alphabeta(to, sense_depth, -inf, inf, ((side - 1) ^ 1) + 1);
                if (ans < x) {
                    ans = x;
                    res[0] = i; res[1] = j;
                }
            }
        }
        return res;
    }

    TableLayout tableLayout;
    int h, w, counter = 0, screenW, screenH, newId = 1, x = 0, y = 0, line = 0, winLine, P1wins = 0, P2wins = 0;
    ArrayList<TableRow> tableRows = new ArrayList<>();
    TableRow.LayoutParams tileParams = new TableRow.LayoutParams();
    ArrayList<Button> tiles = new ArrayList<>();
    TextView turn, P1winCount, P2winCount, leftP, rightP;
    Button restart;
    ArrayList winLineCords = new ArrayList();
    // хранит все текущие позиции крестиков - ноликов
    boolean check, change = false;
    static boolean flexx = false;

    public int id() {
        return newId++;
    }

    public void endGame() {
        int id, X, Y;
        Button winTile;
        for (int j = 0; j < tiles.size(); j++) {
            tiles.get(j).setEnabled(false);
        }
        if (field[x][y] == 1) {
            turn.setText("X wins!");
            turn.setTextColor(getResources().getColor(R.color.redCross));
            if(!change) P1wins++;
            else P2wins++;
        } else {
            turn.setText("O wins!");
            turn.setTextColor(getResources().getColor(R.color.blueZero));
            if(!change) P2wins++;
            else P1wins++;
        }
        for (int i = 0; i < tiles.size(); i++) {
            if(!tiles.get(i).getText().equals("S")) tiles.get(i).setTextColor(getResources().getColor(R.color.gray));
        }
        for (int i = 0; i < winLineCords.size(); i+=2) {
            X = (int)winLineCords.get(i) - 2;
            Y = (int)winLineCords.get(i+1) - 2;
            id = X * w - w + Y;
            winTile = findViewById(id);
            if (field[x][y] == 1) winTile.setTextColor(getResources().getColor(R.color.redCross));
            else winTile.setTextColor(getResources().getColor(R.color.blueZero));

        }
        restart.setVisibility(View.VISIBLE);
        change = !change;
        P1winCount.setText(Integer.toString(P1wins));
        P2winCount.setText(Integer.toString(P2wins));
        isFinished = true;
    }

    public void addCords(int x, int y) {
        winLineCords.add(x);
        winLineCords.add(y);
    }

    public void clearCords() {
        winLineCords.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai);
        Intent intent = getIntent();

        restart = findViewById(R.id.restart);
        turn = findViewById(R.id.turn);
        P1winCount = findViewById(R.id.xDisplay);
        P2winCount = findViewById(R.id.yDisplay);
        leftP = findViewById(R.id.leftPlayer);
        rightP = findViewById(R.id.rightPlayer);

        P1winCount.setTextColor(getResources().getColor(R.color.redCross));
        P2winCount.setTextColor(getResources().getColor(R.color.blueZero));
        P1winCount.setText(Integer.toString(P1wins));
        P2winCount.setText(Integer.toString(P2wins));

        tableLayout = findViewById(R.id.tableLayout);
        h = intent.getIntExtra("height", h);
        w = intent.getIntExtra("width", w);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenW = size.x;
        screenH = size.y;
        // пытаемся сделать нормальный размер ячеек
        // ели ширина больше высоты, ориентация по ширине и наоборот
        tileParams.width = screenW / w;
        if (w < h && h >= 6) { // horizontal - vertical
            tileParams.height = (screenH - 500) / h;
            tileParams.width = (screenH - 500) / h;
            check = true;
        } else {
            tileParams.height = (screenW - 75) / w;
            tileParams.width = (screenW - 75) / w;
            check = false;
        }
        tileParams.leftMargin = 2;
        tileParams.rightMargin = 2;
        tileParams.topMargin = 2;
        tileParams.bottomMargin = 2;

        turn.setText("O turn");
        turn.setTextColor(getResources().getColor(R.color.blueZero));
/*
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                field[i][j] = 0;
            }
        }
*/
        for (int j = 0; j < h; j++) { // создание филда тайлов
            tableRows.add(new TableRow(this));
            tableRows.get(tableRows.size() - 1).setId(j);
            tableLayout.addView(tableRows.get(tableRows.size() - 1)); // создание ячейки
            for (int i = 0; i < w; i++) {
                tiles.add(new Button(this));
                tiles.get(tiles.size() - 1).setId(id());
                tiles.get(tiles.size() - 1).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                tiles.get(tiles.size() - 1).setTextColor(getResources().getColor(R.color.white));
                tiles.get(tiles.size() - 1).setPadding(0, 0, 0, 0);
                //tiles.get(tiles.size() - 1).setGravity(77);
                tiles.get(tiles.size() - 1).setText("S");
                if ((!check && w >= 9) || (check && h >= 9)) {
                    tiles.get(tiles.size() - 1).setTextSize(30);
                    winTile = 4;
                } else if ((!check && w >= 7) || (check && h >= 7)) {
                    tiles.get(tiles.size() - 1).setTextSize(35);
                    winTile = 4;
                } else if ((!check && w >= 5) || (check && h >= 5)) {
                    tiles.get(tiles.size() - 1).setTextSize(50);
                    winTile = 4;
                } else if ((!check && w >= 4) || (check && h >= 4)) {
                    tiles.get(tiles.size() - 1).setTextSize(65);
                    winTile = 3;
                } else if ((!check && w >= 3) || (check && h >= 3)) {
                    tiles.get(tiles.size() - 1).setTextSize(80);
                    winTile = 3;
                }
                tiles.get(tiles.size() - 1).setOnClickListener(this);
                tableRows.get(tableRows.size() - 1).addView(tiles.get(tiles.size() - 1), tileParams);
            }
        }

        Toast.makeText(getApplicationContext(), "Условие победы: " + Integer.toString(winTile) + " в ряд", Toast.LENGTH_LONG).show();

        Button current;
        int[] a;
        a = solve();
        x = a[0] + 3;
        y = a[1] + 3;
        field[x][y] = 1;
        current = findViewById((x-3)*w+y-2);
        current.setText("X");
        current.setTextColor(getResources().getColor(R.color.redCross));
        turn.setText("O turn");
        turn.setTextColor(getResources().getColor(R.color.blueZero));
        if(!isFinished) winCheck();


        current.setEnabled(false);
    }


    /*
    вызвать solve()
    вернет координаты хода с нумерацией от 0, 0 (верхний левый угол)
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        x = (id - 1) / h + 3;
        y = (id - 1) % w + 3;
        Button current = findViewById(id);
        /*
            current.setText("X");
            //current
            current.setTextColor(getResources().getColor(R.color.redCross));
            turn.setText("0 turn");
            turn.setTextColor(getResources().getColor(R.color.blueZero));
            field[x][y] = 1;
*/

            current.setText("O");
            current.setTextColor(getResources().getColor(R.color.blueZero));
            turn.setText("X turn");
            turn.setTextColor(getResources().getColor(R.color.redCross));
            field[x][y] = 2;


        // проверка текущего хода на победу
        current.setEnabled(false);
        winCheck();

        if(!isFinished) {
            int[] a;
            a = solve();
            x = a[0] + 3;
            y = a[1] + 3;
            field[x][y] = 1;
            current = findViewById((x - 3) * w + y - 2);
            current.setText("X");
            current.setTextColor(getResources().getColor(R.color.redCross));
            turn.setText("O turn");
            turn.setTextColor(getResources().getColor(R.color.blueZero));
            winCheck();
            current.setEnabled(false);
        }
        //counter+=2;
    }

    public void restartGame(View view) {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setText("S");
            tiles.get(i).setTextColor(getResources().getColor(R.color.white)); // красим в белый
            tiles.get(i).setEnabled(true);
        }
        for (int i = 3; i < h + 3; i++) {
            for (int j = 3; j < w + 3; j++) {
                field[i][j] = 0;
            }
        }
        turn.setText("X turn");
        turn.setTextColor(getResources().getColor(R.color.redCross));
        restart.setVisibility(View.INVISIBLE);
        if(change) {
            leftP.setTextColor(getResources().getColor(R.color.blueZero));
            P1winCount.setTextColor(getResources().getColor(R.color.blueZero));
            rightP.setTextColor(getResources().getColor(R.color.redCross));
            P2winCount.setTextColor(getResources().getColor(R.color.redCross));

        }
        else {
            leftP.setTextColor(getResources().getColor(R.color.redCross));
            P1winCount.setTextColor(getResources().getColor(R.color.redCross));
            rightP.setTextColor(getResources().getColor(R.color.blueZero));
            P2winCount.setTextColor(getResources().getColor(R.color.blueZero));
        }
        counter = 1;

    }
}