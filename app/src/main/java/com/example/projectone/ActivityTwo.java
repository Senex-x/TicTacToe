package com.example.projectone;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityTwo extends AppCompatActivity implements View.OnClickListener {

    TableLayout tableLayout;
    int h, w, counter = 1, screenW, screenH, newId = 1, x = 0, y = 0, line = 0, winLine, P1wins = 0, P2wins = 0;
    ArrayList<TableRow> tableRows = new ArrayList<>();
    TableRow.LayoutParams tileParams = new TableRow.LayoutParams();
    ArrayList<Button> tiles = new ArrayList<>();
    TextView turn, P1winCount, P2winCount, leftP, rightP;
    Button restart;
    ArrayList winLineCords = new ArrayList();
    int[][] field = new int[15][15]; // хранит все текущие позиции крестиков - ноликов
    boolean check, change = false;

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
        setContentView(R.layout.activity_two);
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

        turn.setText("X turn");
        turn.setTextColor(getResources().getColor(R.color.redCross));

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                field[i][j] = 0;
            }
        }

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
                    winLine = 4;
                } else if ((!check && w >= 7) || (check && h >= 7)) {
                    tiles.get(tiles.size() - 1).setTextSize(35);
                    winLine = 4;
                } else if ((!check && w >= 5) || (check && h >= 5)) {
                    tiles.get(tiles.size() - 1).setTextSize(50);
                    winLine = 4;
                } else if ((!check && w >= 4) || (check && h >= 4)) {
                    tiles.get(tiles.size() - 1).setTextSize(65);
                    winLine = 3;
                } else if ((!check && w >= 3) || (check && h >= 3)) {
                    tiles.get(tiles.size() - 1).setTextSize(80);
                    winLine = 3;
                }
                tiles.get(tiles.size() - 1).setOnClickListener(this);
                tableRows.get(tableRows.size() - 1).addView(tiles.get(tiles.size() - 1), tileParams);
            }
        }

        Toast.makeText(getApplicationContext(), "Условие победы: " + Integer.toString(winLine) + " в ряд", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        x = (id - 1) / h + 3;
        y = (id - 1) % w + 3;
        Button current = findViewById(id);
        if (counter % 2 == 1) {
            current.setText("X");
            //current
            current.setTextColor(getResources().getColor(R.color.redCross));
            turn.setText("0 turn");
            turn.setTextColor(getResources().getColor(R.color.blueZero));
            field[x][y] = 1;
        }
        if (counter % 2 == 0) {
            current.setText("O");
            current.setTextColor(getResources().getColor(R.color.blueZero));
            turn.setText("X turn");
            turn.setTextColor(getResources().getColor(R.color.redCross));
            field[x][y] = 2;
        }

        // проверка текущего хода на победу
        line = 0;
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

        current.setEnabled(false);
        counter++;
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
