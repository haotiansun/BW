package com.example.bunnyworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    SQLiteDatabase db;
    private int whichGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);
        initDB();

    }
    protected void initDB() {
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='gameList';", null);
        if (tablesCursor.getCount() == 0) { // ... then we need to setup the table }
            String setupStr = "CREATE TABLE gameList ("
                    + "game TEXT, startPage TEXT, _id INTEGER PRIMARY KEY AUTOINCREMENT);";
            db.execSQL(setupStr);
        }
    }



    public void playGame(View view) {

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
        final ArrayList<String> gameNames = Database.getGames(db);
        for (String temp : gameNames) {
            arrayAdapter.add(temp);
        }
        //arrayAdapter.add("Create a new game");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose a game to play");
        builder.setCancelable(true);
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                whichGame = which;
            }
        });

        builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                dialog.dismiss();



                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                String strName = arrayAdapter.getItem(whichGame);
                intent.putExtra("STRING_I_NEED", strName);
                startActivity(intent);



            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void editGame(View view) {







        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
        final ArrayList<String> gameNames = Database.getGames(db);
        for (String temp : gameNames) {
            arrayAdapter.add(temp);
        }
        arrayAdapter.add("Create a new game");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose a game to edit");
        builder.setCancelable(true);
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                whichGame = which;
            }
        });

        builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                dialog.dismiss();

                if (whichGame == arrayAdapter.getCount() - 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Enter the name of the new game: ");

                    final EditText editText = new EditText(MainActivity.this);
                    builder.setView(editText);

                    builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.cancel();
                            if (gameNames.contains(editText.getText().toString()) || editText.getText().toString().equals("gameList")) {
                                new  AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Warning" )
                                        .setMessage("The game name has already existed" )
                                        .setPositiveButton("OK" ,  null )
                                        .show();
                            } else {
                                Editor.setGameName(editText.getText().toString());
                                //Editor.saveGame(db);
                                dialog.cancel();
                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                String strName = arrayAdapter.getItem(whichGame);
                                intent.putExtra("STRING_I_NEED", strName);
                                startActivity(intent);
                            }
                            // Editor.setGameName


                        }

                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialogNew = builder.create();
                    dialogNew.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    String strName = arrayAdapter.getItem(whichGame);
                    intent.putExtra("STRING_I_NEED", strName);
                    startActivity(intent);
                }


            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();



        //Intent intent = new Intent(this,EditActivity.class);
        //startActivity(intent);
    }

    public void quitGame(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
