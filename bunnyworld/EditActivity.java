package com.example.bunnyworld;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.microedition.khronos.egl.EGLDisplay;

import static java.security.AccessController.getContext;

public class EditActivity extends AppCompatActivity{

    private int whichSound;
    private int whichTrigger;
    private int whichScript;
    private String scriptContent;
    private int whichPage;
    //Editor newEditor = new Editor();
    private ArrayList<String> triggers = new ArrayList<String>();
    private ArrayList<String> scripts = new ArrayList<String>();
    private ArrayList<String> sounds = new ArrayList<String>();
    private static final String ONCLICK = "on click";
    private static final String ONENTER = "on enter";
    private static final String ONDROP = "on drop";


    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        /*
        String[] shapeArray = {"Script", "Create Script", "Set Property", "Edit Text",
                "Copy Shape", "Paste Shape", "Delete Shape",};
        Spinner spinner = findViewById(R.id.shapeMenu);
        SpinnerAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shapeArray);
        spinner.setAdapter(adapter);
        */
        triggers.add("on click");
        triggers.add("on enter");
        triggers.add("on drop");
        scripts.add("goto");
        scripts.add("play");
        scripts.add("hide");
        scripts.add("show");
        sounds.add("carror-eating");
        sounds.add("evil-laugh");
        sounds.add("fire-sound");
        sounds.add("victory");
        sounds.add("munch");
        sounds.add("munching");
        sounds.add("woof");


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

        if (gameName.equals("Create a new game")) {
            Log.d("debugdebug", "create a new game");
            Editor.main();
        } else {
            Editor.loadGame(db, gameName);
        }
        //Editor.main();
        //ArrayList<Shape> newShapes = new ArrayList<Shape>();
        //Editor.loadGame(newShapes);
        //Editor.loadGame();
    }

    public void scriptMenuPop(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //popup.setOnMenuItemClickListener(this);//通过按钮打开菜单

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                switch (item.getItemId()) {
                    case R.id.itemCreate:

                        // Select a trigger
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                        arrayAdapter.add("On Click");

                        arrayAdapter.add("On Enter");

                        arrayAdapter.add("On Drop");

                        builder.setTitle("Select a trigger");
                        builder.setCancelable(true);
                        final int checkedItem = 0; //this will checked the item when user open the dialog
                        builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                // get the trigger
                                whichTrigger = which;
                            }
                        });

                        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();
                                dialog.dismiss();

                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                                arrayAdapter.add("goto");

                                arrayAdapter.add("play");
                                arrayAdapter.add("hide");
                                arrayAdapter.add("show");

                                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                builder.setTitle("Choose Script");
                                builder.setCancelable(true);
                                final int checkedItem = 0; //this will checked the item when user open the dialog

                                builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                        // get the script
                                        whichScript = which;

                                    }
                                });

                                builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        // goto
                                        scriptContent = "";
                                        scriptContent += triggers.get(whichTrigger);
                                        scriptContent += " " + scripts.get(whichScript);
                                        System.out.println(which);
                                        if (whichScript == 0) {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setTitle("Enter the name of the page you want to transfer to: ");

                                            final EditText editText = new EditText(EditActivity.this);
                                            builder.setView(editText);

                                            builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    scriptContent += " " + editText.getText().toString();
                                                    // need a function to get the current clicked shape
                                                    Shape clickedShape = Editor.getSelectedShape();
                                                    if (clickedShape != null) {
                                                        clickedShape.setScript(scriptContent);
                                                        scriptContent="";
                                                    }

                                                    dialog.cancel();
                                                }
                                            });
                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    scriptContent="";
                                                    dialog.cancel();
                                                }
                                            });

                                            builder.show();
                                        }
                                        // play sound
                                        if (whichScript == 1) {
                                            final ArrayAdapter<String> soundarrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                                            soundarrayAdapter.add("carrot");

                                            soundarrayAdapter.add("evillaugh");
                                            soundarrayAdapter.add("fire");
                                            soundarrayAdapter.add("hooray");
                                            soundarrayAdapter.add("munch");
                                            soundarrayAdapter.add("munching");
                                            soundarrayAdapter.add("woof");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setTitle("Choose a sound");
                                            builder.setCancelable(true);
                                            int checkedItem = 0; //this will checked the item when user open the dialog
                                            builder.setSingleChoiceItems(soundarrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + soundarrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                                    MediaPlayer mp;
                                                    if (which  == 0) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.carrotcarrotcarrot);
                                                    } else if (which == 1) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.evillaugh);

                                                    } else if (which == 2) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.fire);

                                                    } else if (which == 3) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.hooray);

                                                    } else if (which == 4) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.munch);

                                                    } else if (which == 5) {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.munching);

                                                    } else {
                                                        mp = MediaPlayer.create(EditActivity.this,R.raw.woof);
                                                    }
                                                    whichSound = which;
                                                    mp.start();
                                                }
                                            });

                                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //dialog.cancel();
                                                    scriptContent += " " + sounds.get(whichSound);
                                                    // need a function to get the current clicked shape
                                                    Shape clickedShape = Editor.getSelectedShape();
                                                    if (clickedShape != null) {
                                                        clickedShape.setScript(scriptContent);
                                                        scriptContent="";
                                                    }

                                                    dialog.dismiss();

                                                }

                                            });

                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });

                                            builder.show();
                                        }
                                        // hide & show

                                        if (whichScript == 2) {
                                            Shape clickedShape = Editor.getSelectedShape();
                                            if (clickedShape != null) {
                                                clickedShape.setVisible(false);
                                                clickedShape.setMovable(false);
                                            }

                                        } else {
                                            Shape clickedShape = Editor.getSelectedShape();
                                            if (clickedShape != null) {
                                                clickedShape.setVisible(true);
                                                clickedShape.setMovable(true);
                                            }


                                        }
                                    }

                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        return true;

                    // show script here.
                    case R.id.itemShow:

                        builder.setTitle("Here is the script: ");

                        final EditText editText = new EditText(EditActivity.this);
                        Shape clickedShape = Editor.getSelectedShape();
                        editText.setText(clickedShape.getScript());
                        builder.setView(editText);

                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();
                                // 是否自己输入string
                                Shape clickedShape = Editor.getSelectedShape();
                                if (clickedShape != null) {
                                    clickedShape.setScript(editText.getText().toString());
                                }

                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();



                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.inflate(R.menu.popup_menu_script);
        popup.show();
    }



    public void shapeMenuPop(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //popup.setOnMenuItemClickListener(this);//通过按钮打开菜单

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemSet:
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setTitle("Set Property: ");

                        final EditText editText = new EditText(EditActivity.this);

                        builder.setView(editText);



                        LinearLayout layout = new LinearLayout(EditActivity.this);
                        layout.setOrientation(LinearLayout.HORIZONTAL);

                        // Add a TextView here for the "Title" label, as noted in the comments
                        final EditText titleBox = new EditText(EditActivity.this);
                        titleBox.setHint("Left");
                        layout.addView(titleBox); // Notice this is an add method

                        // Add another TextView here for the "Description" label
                        final EditText descriptionBox = new EditText(EditActivity.this);
                        descriptionBox.setHint("Top ");
                        layout.addView(descriptionBox); // Another add method

                        builder.setView(layout);

                        builder.setPositiveButton("Done", null);
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        */

                        //move this part to the right place
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setTitle("Properties: ");

                        //LayoutInflater inflater = this.getLayoutInflater();
                        LayoutInflater inflater = LayoutInflater.from(EditActivity.this);
                        View dialogView = inflater.inflate(R.layout.properties, null);
                        builder.setView(dialogView);

                        final EditText editTextLeft = findViewById(R.id.editLeft);
                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();

                                // 需要一个namelist of pages
                                //newEditor.gotoPage(editTextLeft.toString());
                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;

                    case R.id.itemCopy:

                        return true;
                    case R.id.itemDelete:
                        return true;

                    case R.id.itemPaste:
                        return true;
                    case R.id.itemText:
                        final Shape clickedShape = Editor.getSelectedShape();
                        if (clickedShape.getType() == 3) {

                            AlertDialog.Builder builderText = new AlertDialog.Builder(EditActivity.this);
                            builderText.setTitle("Enter the name of the page you want to delete: ");

                            final EditText newEditText = new EditText(EditActivity.this);
                            builderText.setView(newEditText);

                            builderText.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.cancel();

                                    clickedShape.setText(newEditText.getText().toString());
                                    dialog.cancel();
                                }

                            });
                            builderText.setNegativeButton("Cancel", null);

                            AlertDialog dialogText = builderText.create();
                            dialogText.show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });

        popup.inflate(R.menu.popup_menu_shape);
        popup.show();
    }


public void pageMenuPop(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //popup.setOnMenuItemClickListener(this);//通过按钮打开菜单

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                switch (item.getItemId()) {
                    case R.id.createPage:
                        //AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setTitle("Enter the name of the new page: ");

                        final EditText editTextCreate = new EditText(EditActivity.this);
                        builder.setView(editTextCreate);


                        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();
                                // 是否自己输入string
                                Editor.addPage(editTextCreate.getText().toString());

                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;

                    case R.id.renamePage:
                        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Enter the new name of the page: ");

                        final EditText editTextRename = new EditText(EditActivity.this);
                        builder.setView(editTextRename);

                        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();
                                // 是否自己输入string
                                Editor.setPageName(Editor.curPage.getName(), editTextRename.getText().toString());
                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialogRename = builder.create();
                        dialogRename.show();
                        return true;
                    case R.id.deletePage:
                        builder.setTitle("Enter the name of the page you want to delete: ");

                        final EditText editTextDelete = new EditText(EditActivity.this);
                        builder.setView(editTextDelete);

                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();

                                Editor.deletePage(editTextDelete.getText().toString());
                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialogDelete = builder.create();
                        dialogDelete.show();
                        return true;

                    case R.id.transferPage:
                        builder.setTitle("Choose the page you want to transfer to: ");

                        final EditText editText = new EditText(EditActivity.this);
                        builder.setView(editText);
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                        ArrayList<String> pages;
                        arrayAdapter.add("goto");

                        arrayAdapter.add("play");
                        arrayAdapter.add("hide");
                        arrayAdapter.add("show");

                        final int checkedItem = 0;
                        builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                // get the script
                                whichPage = which;

                            }
                        });
                        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();

                                // 需要一个namelist of pages
                                dialog.dismiss();

                                Editor.gotoPage(arrayAdapter.getItem(whichPage));
                                dialog.cancel();
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialogTransfer = builder.create();
                        dialogTransfer.show();
                        return true;

                    default:
                        return false;
                }
            }
        });

        popup.inflate(R.menu.popup_menu_shape);
        popup.show();
    }


/*
    public void createPage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the new page: ");

        final EditText editText = new EditText(this);
        builder.setView(editText);


        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                // 是否自己输入string
                Editor.addPage(editText.getText().toString());
                
                dialog.cancel();
            }

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void transferPage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the page you want to transfer to: ");

        final EditText editText = new EditText(this);
        builder.setView(editText);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();

                // 需要一个namelist of pages
                Editor.gotoPage(editText.getText().toString());
                dialog.cancel();
            }

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deletePage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the page you want to delete: ");

        final EditText editText = new EditText(this);
        builder.setView(editText);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();

                Editor.deletePage(editText.getText().toString());
                dialog.cancel();
            }

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void renamePage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the new name of the page: ");

        final EditText editText = new EditText(this);
        builder.setView(editText);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                // 是否自己输入string
                Editor.setPageName(Editor.curPage.getName(), editText.getText().toString());
                dialog.cancel();
            }

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
*/
    public void saveGame(View view) {
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);
        Editor.saveGame(db);
    }

}
