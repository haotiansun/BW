package com.example.bunnyworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        Shape.context = this;
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);



        String gameName;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                gameName = null;
            } else {
                gameName = extras.getString("STRING_I_NEED");
            }
        } else {
            gameName = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        Log.d("debugdebug", gameName);
        /*
        if (gameName.equals("Create a new game")) {
            Log.d("debugdebug", "create a new game");
            Editor.main();
        } else {
            Editor.loadGame(db, gameName);
        }
        */
        Game.loadGame(db, gameName);

    }
}
