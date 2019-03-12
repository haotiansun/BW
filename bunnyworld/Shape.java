package com.example.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class Shape {

	private float x, y, width, height;         // x and y are the coordinates of the center of the shape
	private ArrayList<Float> x_backup = new ArrayList<Float>();
	private ArrayList<Float> y_backup = new ArrayList<Float>();
	private ArrayList<Float> width_backup = new ArrayList<Float>();
	private ArrayList<Float> height_backup = new ArrayList<Float>();

	private int type;                      // determine if the shape is rectangle, image or text
	private ArrayList<Integer> type_backup = new ArrayList<Integer>();
	private String name;
	private ArrayList<String> name_backup = new ArrayList<String>();
	private String imageName;            // Name of the associated image
	private ArrayList<String> imageName_backup = new ArrayList<String>();
	private Script script;
	private ArrayList<Script> script_backup = new ArrayList<Script>();
	private String text;
	private ArrayList<String> text_backup = new ArrayList<String>();
	private float textSize;
	private ArrayList<Float> textSize_backup = new ArrayList<Float>();
	private String pageName;    // List of pages which contain the corresponding shape
	private ArrayList<String> pageName_backup = new ArrayList<String>();
	
	private boolean movable;             // True if the shape is movable
	private ArrayList<Boolean> movable_backup = new ArrayList<Boolean>();
	private boolean visible;             // True if the shape is visible
	private ArrayList<Boolean> visible_backup = new ArrayList<Boolean>();
	private boolean useImageBounds;              // True if the user choose to use default size of the image
	private ArrayList<Boolean> useImageBounds_backup = new ArrayList<Boolean>();
	private String undoType;
	
	private static int shapeNum = 0;         // Incremented when a new shape is created
	private static final int RECTANGLE = 1;
	private static final int IMAGE = 2;
	private static final int TEXT = 3;
	private static final float DEFAULT_TEXTSIZE = 50.0f;
	private static final ArrayList<String> imageList = new ArrayList<String>(Arrays.asList("carrot", "carrot2", "death", "duck", "fire", "mystic"));

	// Trigger and Actions
	private static final String ONCLICK = "on click";
	private static final String ONENTER = "on enter";
	private static final String ONDROP = "on drop";
	private static final String GOTO = "goto";
	private static final String PLAY = "play";
	private static final String HIDE = "hide";
	private static final String SHOW = "show";

	// Name of Sounds
	private static final String CARROT = "carrot-eating";
	private static final String EVIL = "evil-laugh";
	private static final String FIRE = "fire-sound";
	private static final String HOORAY = "victory";
	private static final String MUNCH = "munch";
	private static final String MUNCHING = "munching";
	private static final String WOOF = "woof";

	// Name of undo types
	private static final String UNDO_X = "undo_x";
	private static final String UNDO_Y = "undo_y";
	private static final String UNDO_WIDTH = "undo_width";
	private static final String UNDO_HEIGHT = "undo_height";
	private static final String UNDO_NAME = "undo_name";
	private static final String UNDO_IMAGENAME = "undo_imageName";
	private static final String UNDO_SCRIPT = "undo_script";
	private static final String UNDO_TEXT  = "undo_text";
	private static final String UNDO_TEXTSIZE = "undo_textSize";
	private static final String UNDO_PAGENAME = "undo_pageName";
	private static final String UNDO_MOVABLE = "undo_movable";
	private static final String UNDO_VISIBLE = "undo_visible";
	private static final String UNDO_USEIMAGEBOUNDS = "undo_useImageBounds";

	private Paint grayFillPaint;
	private Paint blueOutlinePaint;
	private Paint textPaint;
	static Context context;
	
	public Shape(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		xBackup(); yBackup(); widthBackup(); heightBackup();

		type = RECTANGLE; typeBackup();
		shapeNum++;
		name = "shape" + shapeNum; nameBackup();
		imageName = ""; imageNameBackup();
		script = new Script(""); scriptBackup();
		text = ""; textBackup();
		pageName = ""; pageNameBackup();
		textSize = DEFAULT_TEXTSIZE;
		undoType = "";
		
		movable = true; movableBackup();
		visible = true; visibleBackup();
		useImageBounds = false; useImageBoundsBackup();

		grayFillPaint = new Paint();
		grayFillPaint.setColor(Color.rgb(211, 211, 211));
		blueOutlinePaint = new Paint();
		blueOutlinePaint.setColor(Color.BLUE);
		blueOutlinePaint.setStyle(Paint.Style.STROKE);
		blueOutlinePaint.setStrokeWidth(20.0f);

		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);

	}

	// Alternative constructor
	// Parameters:
	// shape name, page name, type, x, y, width, height,
	// image name ("" if no image), text ("" if no text), script,
	// movable, visible, use image bounds (true if you want to use the default size of the image)
	public Shape(float x, float y, float width, float height, int typ, String sName, String imName,
				 String scr, String txt, float tSize, String pName, int mov, int vis, int useImBounds) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		xBackup(); yBackup(); widthBackup(); heightBackup();

		shapeNum++;
		// Check if the shape name has already been used. If yes, use default shape name
		/*if (checkShapeName(sName)) {
			name = sName;
		} else {
			name = "shape" + shapeNum;
		}*/
        name = sName;
		nameBackup();

		// Check if the associated page exists in the game. If not, page name becomes empty string
		/*if (checkPageName(pName)) {
			pageName = pName;
		} else {
			pageName = "";
		}*/
		pageName = pName;
		pageNameBackup();

		script = new Script(scr); scriptBackup();
		if (mov == 0) {
			movable = false;
		} else {
			movable = true;
		}
		movableBackup();

		if (vis == 0) {
			visible = false;
		} else {
			visible = true;
		}
		visibleBackup();

		if (useImBounds == 0) {
			useImageBounds = false;
		} else {
			useImageBounds = true;
		}
		useImageBoundsBackup();

		if (typ != RECTANGLE && typ != IMAGE && typ != TEXT) {
			type = RECTANGLE;
		} else {
			type = typ;
		}
		typeBackup();

		if (type == IMAGE) {
			imageName = imName;
		} else {
			imageName = "";
		}
		imageNameBackup();

		if (type == TEXT) {
			text = txt;
		} else {
			text = "";
		}
		textBackup();

		textSize = tSize;
		undoType = "";

		grayFillPaint = new Paint();
		grayFillPaint.setColor(Color.rgb(211, 211, 211));
		blueOutlinePaint = new Paint();
		blueOutlinePaint.setColor(Color.BLUE);
		blueOutlinePaint.setStyle(Paint.Style.STROKE);
		blueOutlinePaint.setStrokeWidth(5.0f);

		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);

	}

	// Copy Constructor, use this constructor to copy a shape
	// Pass in a Shape object you want to copy from
	// The name of the shape will be the name of the copied shape plus "_copy"
	public Shape(Shape shape) {
		this.x = shape.x; xBackup();
		this.y = shape.y; yBackup();
		this.width = shape.width; widthBackup();
		this.height = shape.height; heightBackup();

		shapeNum++;
		name = shape.name + "_copy"; nameBackup();
		type = shape.type; typeBackup();
		imageName = shape.imageName; imageNameBackup();
		script = shape.script; scriptBackup();
		text = shape.text; textBackup();
		textSize = shape.textSize; textSizeBackup();
		pageName = shape.pageName; pageNameBackup();
		movable = shape.movable; movableBackup();
		visible = shape.visible; visibleBackup();
		useImageBounds = shape.useImageBounds; useImageBoundsBackup();
		undoType = "";

		grayFillPaint = new Paint();
		grayFillPaint.setColor(Color.rgb(211, 211, 211));
		blueOutlinePaint = new Paint();
		blueOutlinePaint.setColor(Color.BLUE);
		blueOutlinePaint.setStyle(Paint.Style.STROKE);
		blueOutlinePaint.setStrokeWidth(5.0f);

		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
	}

	//-------------------------------Set shape parameters--------------------------------------------------
	// Here setX and setY is only used for game editing, in game playing we use move()
	public void setX(float x) {
		xBackup();
		undoType = UNDO_X;
		this.x = x;
	}

	public void setY(float y) {
		yBackup();
		undoType = UNDO_Y;
		this.y = y;
	}

	public void setWidth(float width) {
		widthBackup();
		undoType = UNDO_WIDTH;
		this.width = width;
	}

	public void setHeight(float height) {
		heightBackup();
		undoType = UNDO_HEIGHT;
		this.height = height;
	}

	public void setName(String str) {
		nameBackup();
		undoType = UNDO_NAME;
		this.name = str;
	}
	
	public void setImageName(String imageName) {
		// If there's no such image, shape remains to be a rectangle
		imageNameBackup();
		typeBackup();
		undoType = UNDO_IMAGENAME;
		if (imageList.contains(imageName)) {
			this.imageName = imageName;
			// If shape contains text, text takes precedence
			if (text.equals("")) {
				type = IMAGE;
			} else {
				type = TEXT;
			}
			// Refer to the image indicated by imageName
		}
	}
	
	public void setScript(String str) {
		scriptBackup();
		undoType = UNDO_SCRIPT;
		this.script = new Script(str);
	}
	
	public void setText(String text) {
		textBackup();
		typeBackup();
		undoType = UNDO_TEXT;
		this.text = text;
		type = TEXT;
	}

	public void setTextSize(float textSize) {
		textSizeBackup();
		undoType = UNDO_TEXTSIZE;
		this.textSize = textSize;
	}
	
	public void setMovable(boolean movable) {
		movableBackup();
		undoType = UNDO_MOVABLE;
		this.movable = movable;
//		if (!visible) {
//			this.movable = false;
//		}
	}
	
	public void setVisible(boolean visible) {
		visibleBackup();
		undoType = UNDO_VISIBLE;
		this.visible = visible;
//		if (!visible) {
//			this.movable = false;
//		}
	}
	
	public void setUseImageBounds(boolean bool) {
		useImageBoundsBackup();
		undoType = UNDO_USEIMAGEBOUNDS;
		this.useImageBounds = bool;
	}

	public void setPageName(String pageName) {
		pageNameBackup();
		undoType = UNDO_PAGENAME;
		this.pageName = pageName;
	}
	
	//-------------------------------Get shape parameters--------------------------------------------------
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageName() {
		return imageName;
	}

	public String getPageName() {
		return pageName;
	}
	
	public String getScript() {
		return script.toString();
	}
	
	public String getText() {
		return text;
	}

	public float getTextSize() {
		return textSize;
	}

	//-------------------Used for data saving-------------------------
	public int getMov() {
		if (movable) {
			return 1;
		} else {
			return 0;
		}
	}

	public int getVis() {
		if (visible) {
			return 1;
		} else {
			return 0;
		}
	}

	public int getUse() {
		if (useImageBounds) {
			return 1;
		} else {
			return 0;
		}
	}
	//-------------------------------------------------------------------

	public boolean isMovable() {
		return movable;
	}

	public boolean isVisible() {
		return visible;
	}

	//-------------------------------Shape Related Functions--------------------------------------------------
	public void drawOutline(Canvas canvas) {
        canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, blueOutlinePaint);
    }



    // Draw thenselves
	// Need to be tested in Android
	public void draw(Canvas canvas, Shape shape) {
		BitmapDrawable carrotDrawable, carrot2Drawable, deathDrawable, duckDrawable,fireDrawable, mysticDrawable;
		if (visible) {
		    if (shape != null && this.droppableBy(shape)) {
                canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, blueOutlinePaint);
            }
			if (type == RECTANGLE) {
				// Draw a light gray rectangle
				canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, grayFillPaint);
			} else if (type == IMAGE) {
				// If the shape does not have an image or the image cannot be loaded, draw a light gray rectangle
				if (!imageList.contains(imageName)) {
					canvas.drawRect(x - width/2, y - height/2, x + width/2, y + height/2, grayFillPaint);
				} else {
					// Load Bitmap resource and draw it on canvas
					if (!useImageBounds) {
						if (imageName.equals("carrot")) {
							carrotDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot);
							canvas.drawBitmap(carrotDrawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						} else if (imageName.equals("carrot2")) {
							carrot2Drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot2);
							canvas.drawBitmap(carrot2Drawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						} else if (imageName.equals("death")) {
							deathDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.death);
							canvas.drawBitmap(deathDrawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						} else if (imageName.equals("duck")) {
							duckDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.duck);
							canvas.drawBitmap(duckDrawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						} else if (imageName.equals("fire")) {
							fireDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.fire);
							canvas.drawBitmap(fireDrawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						} else if (imageName.equals("mystic")) {
							mysticDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.mystic);
							canvas.drawBitmap(mysticDrawable.getBitmap(), null, new RectF(x - width/2, y - height/2, x + width/2, y + height/2), null);
						}
					} else {
						// Use default size of the image regardless of the input width and height
						if (imageName.equals("carrot")) {
							carrotDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot);
							Bitmap carrotMap = carrotDrawable.getBitmap();
							canvas.drawBitmap(carrotMap, x - carrotMap.getWidth()/2.0f, y - carrotMap.getHeight()/2.0f, null);
							width = (float)carrotMap.getWidth(); height = (float)carrotMap.getHeight();
						} else if (imageName.equals("carrot2")) {
							carrot2Drawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot2);
							Bitmap carrot2Map = carrot2Drawable.getBitmap();
							canvas.drawBitmap(carrot2Map, x - carrot2Map.getWidth()/2.0f, y - carrot2Map.getHeight()/2.0f, null);
							width = (float)carrot2Map.getWidth(); height = (float)carrot2Map.getHeight();
						} else if (imageName.equals("death")) {
							deathDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.death);
							Bitmap deathMap = deathDrawable.getBitmap();
							canvas.drawBitmap(deathMap, x - deathMap.getWidth()/2.0f, y - deathMap.getHeight()/2.0f, null);
							width = (float)deathMap.getWidth(); height = (float)deathMap.getHeight();
						} else if (imageName.equals("duck")) {
							duckDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.duck);
							Bitmap duckMap = duckDrawable.getBitmap();
							canvas.drawBitmap(duckMap, x - duckMap.getWidth()/2.0f, y - duckMap.getHeight()/2.0f, null);
							width = (float)duckMap.getWidth(); height = (float)duckMap.getHeight();
						} else if (imageName.equals("fire")) {
							fireDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.fire);
							Bitmap fireMap = fireDrawable.getBitmap();
							canvas.drawBitmap(fireMap, x - fireMap.getWidth()/2.0f, y - fireMap.getHeight()/2.0f, null);
							width = (float)fireMap.getWidth(); height = (float)fireMap.getHeight();
						} else if (imageName.equals("mystic")) {
							mysticDrawable = (BitmapDrawable) context.getResources().getDrawable(R.drawable.mystic);
							Bitmap mysticMap = mysticDrawable.getBitmap();
							canvas.drawBitmap(mysticMap, x - mysticMap.getWidth()/2.0f, y - mysticMap.getHeight()/2.0f, null);
							width = (float)mysticMap.getWidth(); height = (float)mysticMap.getHeight();
						}
					}
				}
			} else {
				// Display text
				textPaint.setTextSize(textSize);
				textPaint.setTextAlign(Paint.Align.CENTER);
				canvas.drawText(text, x, y, textPaint);
			}
		}

	}

	// Move to a certain location (x, y)
	public boolean move(float x, float y) {
		xBackup(); yBackup();
		if (movable && visible) {
			this.x = x;
			this.y = y;
			return true;
		} else {
			return false;
		}
	}

	// If more than half of the shape is above the limited height, move the shape up
	public void limitTopHeight(float y) {
		if (this.y + height/2 > y) {
			this.y = y - height / 2;

		}
	}

	// If more than half of the shape is below the limited height, move the shape down
	public void limitBottomHeight(float y) {
		if (this.y + height/2 > y) {
			this.y = y + height / 2;

		}
	}

	// Determine if a point (xval, yval) is inside the shape
	public boolean contains(float xval, float yval) {
		if (visible) {
			if (Math.abs(this.x - xval) <= width/2 && Math.abs(this.y - yval) <= height/2) {
				return true;
			}
		}
		return false;
	}

	// Trigger onClick effects
	public void clicked() {
		if (visible) {
			ArrayList<String> clickAction = script.getActions(ONCLICK, "");
			executeActions(clickAction);
		}
		// Execute actions in the ArrayList
	}

	// Trigger onDrop effects by shape s
	public void droppedBy(Shape s) {
		if (droppableBy(s)) {
			String shapeName = s.getName();
			ArrayList<String> dropAction = script.getActions(ONDROP, shapeName);
			executeActions(dropAction);
		}
	}

	public void entered() {
		if (visible) {
			ArrayList<String> enterAction = script.getActions(ONENTER, "");
			executeActions(enterAction);
		}
	}

	// Execute actions according to the ArrayList of actions
	public void executeActions(ArrayList<String> actionList) {
		MediaPlayer mp;
		for (int i=0; i<actionList.size(); i++) {
			String action = actionList.get(i);
			if (action.equals(GOTO)) {
				String pageName = actionList.get(i + 1);
				Game.gotoPage(pageName);
			} else if (action.equals(PLAY)) {
				String soundName = actionList.get(i + 1);
				if (soundName.equals(CARROT)) {
					mp = MediaPlayer.create(context, R.raw.carrotcarrotcarrot);
					mp.start();
				} else if (soundName.equals(EVIL)) {
					mp = MediaPlayer.create(context, R.raw.evillaugh);
					mp.start();
				} else if (soundName.equals(FIRE)) {
					mp = MediaPlayer.create(context, R.raw.fire);
					mp.start();
				} else if (soundName.equals(HOORAY)) {
					mp = MediaPlayer.create(context, R.raw.hooray);
					mp.start();
				} else if (soundName.equals(MUNCH)) {
					mp = MediaPlayer.create(context, R.raw.munch);
					mp.start();
				} else if (soundName.equals(MUNCHING)) {
					mp = MediaPlayer.create(context, R.raw.munching);
					mp.start();
				} else if (soundName.equals(WOOF)) {
					mp = MediaPlayer.create(context, R.raw.woof);
					mp.start();
				}
			} else if (action.equals(HIDE)) {
				String shapeName = actionList.get(i + 1);
				Game.hideShape(shapeName);
			} else if (action.equals(SHOW)) {
				String shapeName = actionList.get(i + 1);
				Game.showShape(shapeName);
			}
		}
	}

	// Determine if this shape is droppable by another shape s
	// Claimed public for future use
	public boolean droppableBy(Shape s) {
		if (visible && s.isVisible()) {
			String shapeName = s.getName();
			ArrayList<String> dropAction = script.getActions(ONDROP, shapeName);
			if (dropAction.size() != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

	//-------------------------------Error Check--------------------------------------------------
	// Check if page exists in the game
	/*public boolean checkPageName(String pageName) {
		for (Page p: Game.pages) {
			if (p.getName().equals(pageName)) {
				return true;
			}
		}
		return false;
	}*/

	/*// Check if shape name already exists in the game
	public boolean checkShapeName(String shapeName) {
		for (Page p: Game.pages) {
			for (Shape s: p.getshapes()) {
				if (s.getName().equals(shapeName)) {
					return false;
				}
			}
		}
		return true;
	}*/

	// Check if image name relates to an actual image
	public boolean checkImageName(String imageName) {
		if (imageList.contains(imageName)) {
			return true;
		}
		return false;
	}

	//-------------------------------Backup for Undo Support--------------------------------------------------
	public void xBackup() {
		x_backup.add(x);
	}

	public void yBackup() {
		y_backup.add(y);
	}

	public void widthBackup() {
		width_backup.add(width);
	}

	public void heightBackup() {
		height_backup.add(height);
	}

	public void typeBackup() {
		type_backup.add(type);
	}

	public void nameBackup() {
		name_backup.add(name);
	}

	public void imageNameBackup() {
		imageName_backup.add(imageName);
	}

	public void textBackup() {
		text_backup.add(text);
	}

	public void textSizeBackup() {
		textSize_backup.add(textSize);
	}

	public void scriptBackup() {
		script_backup.add(script);
	}

	public void pageNameBackup() {
		pageName_backup.add(pageName);
	}

	public void movableBackup() {
		movable_backup.add(movable);
	}

	public void visibleBackup() {
		visible_backup.add(visible);
	}

	public void useImageBoundsBackup() {
		useImageBounds_backup.add(useImageBounds);
	}

	public void undo() {

	}
}
