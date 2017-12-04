package com.droidrank.tictactoe;

import android.content.DialogInterface;
import android.os.Build;
import android.renderscript.Matrix3f;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TIC-TAC-TOE";

    Button[][] blocks;
    Button block1, block2, block3, block4, block5, block6, block7, block8, block9, restart;
    TextView result;
    Matrix3f matrix;
    boolean gameInProgress;
    Player currentPlayer = Player.one;

    enum Player {
        one, two, none;

        Player other() {
            return this == Player.one ? Player.two : Player.one;
        }

        String getChar() {
            return this == Player.one ? "O" : "X";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        block1 = (Button) findViewById(R.id.bt_block1);
        block2 = (Button) findViewById(R.id.bt_block2);
        block3 = (Button) findViewById(R.id.bt_block3);
        block4 = (Button) findViewById(R.id.bt_block4);
        block5 = (Button) findViewById(R.id.bt_block5);
        block6 = (Button) findViewById(R.id.bt_block6);
        block7 = (Button) findViewById(R.id.bt_block7);
        block8 = (Button) findViewById(R.id.bt_block8);
        block9 = (Button) findViewById(R.id.bt_block9);

        blocks = new Button[][]{
                {block1, block2, block3},
                {block4, block5, block6},
                {block7, block8, block9}
        };


        for (Button[] row : blocks) {
            for (Button block : row) {
                block.setOnClickListener(this);
            }
        }

        result = (TextView) findViewById(R.id.tv_show_result);
        restart = (Button) findViewById(R.id.bt_restart_game);

        /**
         * Restarts the game
         */
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameInProgress) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                    }
                    builder.setTitle(R.string.app_name)
                            .setMessage(R.string.restart_message)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startGame();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
                else {
                    startGame();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_block1:
                blockSelected(0, 0);
                break;
            case R.id.bt_block2:
                blockSelected(0, 1);
                break;
            case R.id.bt_block3:
                blockSelected(0, 2);
                break;
            case R.id.bt_block4:
                blockSelected(1, 0);
                break;
            case R.id.bt_block5:
                blockSelected(1, 1);
                break;
            case R.id.bt_block6:
                blockSelected(1, 2);
                break;
            case R.id.bt_block7:
                blockSelected(2, 0);
                break;
            case R.id.bt_block8:
                blockSelected(2, 1);
                break;
            case R.id.bt_block9:
                blockSelected(2, 2);
                break;
        }
    }

    private void startGame() {
        gameInProgress = true;
        currentPlayer = Player.one;
        result.setText(null);
        restart.setText(R.string.restart_button_text_in_middle_of_game);

        for (Button[] row : blocks) {
            for (Button block : row) {
                block.setText(null);
            }
        }
    }

    private void blockSelected(int row, int column) {
        if (!gameInProgress) {
            return;
        }

        Button block = blocks[row][column];
        if (block.getText() == null || block.getText().length() == 0) {
            blocks[row][column].setText(currentPlayer.getChar());
            currentPlayer = currentPlayer.other();
            checkEndGameConditions();
        }
    }

    private void checkEndGameConditions() {
        Player winner = Player.none;

        winner = getWinner(new Button[] {blocks[0][0], blocks[1][0], blocks[2][0]});

        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[0][1], blocks[1][1], blocks[2][1]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[0][2], blocks[1][2], blocks[2][2]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[0][0], blocks[0][1], blocks[0][2]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[1][0], blocks[1][1], blocks[1][2]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[2][0], blocks[2][1], blocks[2][2]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[]{blocks[0][0], blocks[1][1], blocks[2][2]});
        }
        if (winner == Player.none) {
            winner = getWinner(new Button[] {blocks[2][0], blocks[1][1], blocks[0][2]});
        }

        if (winner == Player.one) {
            result.setText(R.string.player_1_wins);
            gameInProgress = false;
        }
        else if (winner == Player.two) {
            result.setText(R.string.player_2_wins);
            gameInProgress = false;
        }
    }

    private Player getWinner(Button[] blocks) {
        CharSequence one = blocks[0].getText();
        CharSequence two = blocks[1].getText();
        CharSequence three = blocks[2].getText();
        Log.d(TAG, String.format("getWinner: %s %s %s", one, two, three));

        if (one == null || one.length() == 0 || !one.equals(two) || !one.equals(three)) {
            return Player.none;
        }
        else {
            return one.equals(Player.one.getChar()) ? Player.one : Player.two;
        }
    }
}
