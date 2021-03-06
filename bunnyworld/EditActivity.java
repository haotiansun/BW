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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
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
    private int whichShape;
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
        Shape.context = this;

        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE, null);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

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
        setTitle(R.string.app_name);
        setTitle(Editor.curPage.getName());
        setContentView(R.layout.activity_edit);
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
                                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                                            final ArrayList<Page> pages = Editor.getPages();
                                            for (Page temp : pages) {
                                                arrayAdapter.add(temp.getName());
                                            }
                                            //arrayAdapter.add("Create a new game");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setTitle("Choose a page");
                                            builder.setCancelable(true);
                                            int checkedItem = 0; //this will checked the item when user open the dialog
                                            builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                                    whichPage = which;
                                                }
                                            });

                                            builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //dialog.cancel();
                                                    dialog.dismiss();
                                                    scriptContent += " " + arrayAdapter.getItem(whichPage) + ";";
                                                    // need a function to get the current clicked shape
                                                    Shape clickedShape = Editor.getSelectedShape();
                                                    if (clickedShape != null) {
                                                        clickedShape.setScript(scriptContent);
                                                        scriptContent = "";
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
                                                    scriptContent += " " + sounds.get(whichSound) + ";";
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
                                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                                            final ArrayList<Page> pages = Editor.getPages();
                                            final ArrayList<String> shapeList = new ArrayList<String>();
                                            for (Page temp : pages) {
                                                for (Shape tempShape : temp.getshapes()) {
                                                    arrayAdapter.add("Page: " + temp.getName() + ", Shape: " + tempShape.getName());
                                                    shapeList.add(tempShape.getName());
                                                }
                                            }

                                            //arrayAdapter.add("Create a new game");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setTitle("Choose a shape");
                                            builder.setCancelable(true);
                                            int checkedItem = 0; //this will checked the item when user open the dialog
                                            builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                                    whichPage = which;
                                                }
                                            });

                                            builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //dialog.cancel();
                                                    dialog.dismiss();
                                                    scriptContent += " " + shapeList.get(whichShape) + ";";
                                                    // need a function to get the current clicked shape
                                                    Shape clickedShape = Editor.getSelectedShape();
                                                    if (clickedShape != null) {
                                                        clickedShape.setScript(scriptContent);
                                                        scriptContent = "";
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

                                        } else {
                                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                                            final ArrayList<Page> pages = Editor.getPages();
                                            for (Page temp : pages) {
                                                for (Shape tempShape : temp.getshapes()) {
                                                    arrayAdapter.add("Page: " + temp.getName() + ", Shape: " + tempShape.getName());
                                                }
                                            }

                                            //arrayAdapter.add("Create a new game");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setTitle("Choose a shape");
                                            builder.setCancelable(true);
                                            int checkedItem = 0; //this will checked the item when user open the dialog
                                            builder.setSingleChoiceItems(arrayAdapter, checkedItem, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(EditActivity.this, "Position: " + which + " Value: " + arrayAdapter.getItem(which), Toast.LENGTH_LONG).show();
                                                    whichPage = which;
                                                }
                                            });

                                            builder.setPositiveButton("Choose", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //dialog.cancel();
                                                    dialog.dismiss();
                                                    scriptContent += " " + arrayAdapter.getItem(whichShape) + ";";
                                                    // need a function to get the current clicked shape
                                                    Shape clickedShape = Editor.getSelectedShape();
                                                    if (clickedShape != null) {
                                                        clickedShape.setScript(scriptContent);
                                                        scriptContent = "";
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
                        Shape selectedShape = Editor.getSelectedShape();
                        final EditText editTextX = dialogView.findViewById(R.id.editX);
                        editTextX.setText(Float.toString(selectedShape.getX()));
//                        builder.setView(editTextX);
                        Log.d("debug1", editTextX.getText().toString());
                        final EditText editTextY = dialogView.findViewById(R.id.editY);
                        editTextY.setText(Float.toString(selectedShape.getY()));

                        final EditText editTextWidth = dialogView.findViewById(R.id.editWidth);
                        editTextWidth.setText(Float.toString(selectedShape.getWidth()));

                        final EditText editTextHeight = dialogView.findViewById(R.id.editHeight);
                        editTextHeight.setText(Float.toString(selectedShape.getHeight()));

                        final Switch moveSwitch = dialogView.findViewById(R.id.movable);
                        final Switch hideSwitch = dialogView.findViewById(R.id.visible);

                        final EditText editTextName = dialogView.findViewById(R.id.currentShape);
//set the current state of a Switch

                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.cancel();

                                Shape selectedShape = Editor.getSelectedShape();
                                selectedShape.setX(Float.parseFloat(editTextX.getText().toString()));
                                selectedShape.setY(Float.parseFloat(editTextY.getText().toString()));
                                selectedShape.setWidth(Float.parseFloat(editTextWidth.getText().toString()));
                                selectedShape.setHeight(Float.parseFloat(editTextHeight.getText().toString()));
                                selectedShape.setMovable(moveSwitch.isChecked());
                                Log.d("debug1", Boolean.toString(moveSwitch.isChecked()));
                                selectedShape.setVisible(hideSwitch.isChecked());
                                selectedShape.setName(editTextName.getText().toString());



                                // 需要一个namelist of pages
                                //newEditor.gotoPage(editTextLeft.toString());
                                dialog.cancel();
                                setContentView(R.layout.activity_edit);
                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return true;

                    case R.id.itemCopy:

                        return true;
                    case R.id.itemDelete:
                        Shape clickedShape = Editor.getSelectedShape();
                        //clickedShape = Editor.getSelectedShape();
                        Editor.noSelect();
                        Editor.curPage.removeShape(clickedShape);
                        setContentView(R.layout.activity_edit);
                        return true;

                    case R.id.itemPaste:
                        return true;
                    case R.id.itemText:
                        final Shape clickedShapeText = Editor.getSelectedShape();
                        //clickedShapeText = Editor.getSelectedShape();
                        if (clickedShapeText.getType() == 3) {

                            AlertDialog.Builder builderText = new AlertDialog.Builder(EditActivity.this);
                            builderText.setTitle("Enter the name of the text: ");

                            final EditText newEditText = new EditText(EditActivity.this);
                            builderText.setView(newEditText);

                            builderText.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.cancel();

                                    clickedShapeText.setText(newEditText.getText().toString());
                                    dialog.cancel();
                                    setContentView(R.layout.activity_edit);
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


                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditActivity.this, android.R.layout.select_dialog_singlechoice);
                        ArrayList<Page> pages = Editor.getPages();
                        for (Page tempPage : pages) {
                            arrayAdapter.add(tempPage.getName())
;                        }


                        final int checkedItem = pages.indexOf(Editor.curPage);
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
                                Log.d("debug1", Editor.curPage.getName());
                                Log.d("debug1", arrayAdapter.getItem(whichPage));
                                Editor.gotoPage(arrayAdapter.getItem(whichPage));
                                setTitle(Editor.curPage.getName());
                                setContentView(R.layout.activity_edit);
                                dialog.cancel();

                            }

                        });
                        builder.setNegativeButton("Cancel", null);

                        AlertDialog dialogTransfer = builder.create();
                        dialogTransfer.show();
                        return true;
                    case R.id.setPage:
                        Editor.startingPage = Editor.curPage;
                    default:
                        return false;
                }
            }
        });

        popup.inflate(R.menu.popup_menu_page);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save the game ?");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Editor.saveGame(db);
                dialog.cancel();
                Toast.makeText(EditActivity.this, Editor.gameName + " is saved!", Toast.LENGTH_LONG).show();
            }

        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
