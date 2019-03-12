package com.example.bunnyworld;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.*;

public class Game {
	static ArrayList<Page> pages;
	static Inventory possession;
	static Page curPage;
	static Page startingPage;
	static SQLiteDatabase db;
	static String gameName = "default";
	
	public static void main() {

		pages = new ArrayList<>();
		possession = new Inventory();
		curPage = new Page("page1");
		Page Page2 = new Page("page2");
		Page Page3 = new Page("page3");
		Page Page4 = new Page("page4");
		Page Page5 = new Page("page5");
		Shape button1 = new Shape(300,700,100,100);
		button1.setScript("on click goto page2;");
		Shape button2 = new Shape(800,700,100,100);
		button2.setName("door2");
		button2.setVisible(false);
		button2.setScript("on click goto page3;");
		Shape button3 = new Shape(1300,700,100,100);
		button3.setScript("on click goto page4;");
		Shape button4 = new Shape(100,700,100,100);
		button4.setScript("on click goto page1;");
		Shape button5 = new Shape(700,700,100,100);
		button5.setScript("on click goto page2;");
		Shape button6 = new Shape(1700,700,100,100);
		button6.setVisible(false);
		button6.setName("exit");
		button6.setScript("on click goto page5;");
		Shape text1 = new Shape(800,100,200,200);
		text1.setText("Bunny World!");
		Shape text2 = new Shape(800,300,200,200);
		text2.setText("You are in a maze of twisty little passages, all alike");
		Shape text3 = new Shape(800,700,200,200);
		text3.setText("Mystic Bunny Rub my tummy for a big surprise!");
		Shape text4 = new Shape(200,700, 200,200);
		text4.setText("Eek! Fire-room. Run away!");
		Shape text5 = new Shape(600,700,200,200);
		text5.setText("You must appease the Bunny of Death!");
		Shape text6 = new Shape(200,700,200,200);
		text6.setText("You Win!");
		text6.setScript("on enter play victory;");
		Shape bunny1 = new Shape(800,300,400,400);
		//bunny1.setScaled(true);
		bunny1.setImageName("mystic");
		bunny1.setScript("on click hide carrot1 play carrot-eating; on enter show door2;");
		Shape bunny2 = new Shape(1200,200,400,400);
		//bunny2.setScaled(true);
		bunny2.setImageName("death");
		bunny2.setName("death-bunny");
		bunny2.setScript("on enter play evil-laugh; on drop carrot1 hide carrot1 play carrot-eating hide death-bunny show exit;"
				+ "on click play evil-laugh");
		Shape trap = new Shape(400,200,400,400);
		//trap.setScaled(true);
		trap.setScript("on enter play fire-sound;");
		trap.setImageName("fire");
		Shape carrot1 = new Shape(1000,1000,200,200);
		carrot1.setMovable(true);
		//carrot1.setScaled(true);
		carrot1.setName("carrot1");
		carrot1.setImageName("carrot2");
		Shape carrot2 = new Shape(200,200,300,300);
		carrot2.setImageName("carrot");
		Shape carrot3 = new Shape(500,400,300,300);
		carrot3.setImageName("carrot");
		Shape carrot4 = new Shape(900,300,300,300);
		carrot4.setImageName("carrot");
		pages.add(curPage);
		pages.add(Page2);
		pages.add(Page3);
		pages.add(Page4);
		pages.add(Page5);
		curPage.addShape(button1);
		curPage.addShape(button2);
		curPage.addShape(button3);
		curPage.addShape(text1);
		curPage.addShape(text2);
		Page2.addShape(button4);
		Page2.addShape(text3);
		Page2.addShape(bunny1);
		Page3.addShape(button5);
		Page3.addShape(text4);
		Page3.addShape(carrot1);
		Page3.addShape(trap);
		Page4.addShape(button6);
		Page4.addShape(text5);
		Page4.addShape(bunny2);
		Page5.addShape(carrot2);
		Page5.addShape(carrot3);
		Page5.addShape(carrot4);
		Page5.addShape(text6);

	}

	static Page findPage(String name){
		for (Page page:pages) {
			if (page.getName().equals(name)) {
				return page;
			}
		}
		return null;
	}

	static void loadGame(SQLiteDatabase db, String gName){
		//for (Page page:pages){
		//    pages.remove(page);
		//}
		gameName = gName;
		possession = new Inventory();
		pages = new ArrayList<Page>();
		ArrayList<Shape> shapes = Database.getGameShapes(db, gameName);

		curPage = new Page(Database.getStartPage(db, gameName));
		startingPage = curPage;
		pages.add(curPage);
		for (Shape shape:shapes){
			//String pageName = shape.getShapeName();
			String pageName = shape.getPageName();
			Page page = findPage(pageName);
			if (page == null) {
				page = new Page(pageName);
				pages.add(page);
			}
			page.addShape(shape);
		}
	}


	static void gotoPage(String name) {
		for (Page page:pages) {
			if (page.getName().equals(name)) {
				curPage = page;
				for (Shape shape:curPage.getshapes()) {
					shape.entered();
				}
				return;
			}
		}
		System.out.println("No such page.");
	}
	
	static ArrayList<Shape> getShapesOnCurPage() {
		return curPage.getshapes();
	}
	
	static ArrayList<Shape> getShapesOnPos(){
		return possession.getShapes();
	}
	
	public static void hideShape(String shapeName) {
		for(Page page:pages) {
		/*	if (page.getName().equals("page4")) {
				for (Shape shape: page.getshapes()) {
					Log.d("Debug", "Shapes on page4: " + shape.getName() + ", Image: " + shape.getImageName());
				}
			}
*/
			for (Shape shape: page.getshapes()) {
				if (shape.getName().equals(shapeName)) {
					shape.setVisible(false);
				}
			}
		}
	}
	
	public static void showShape(String shapeName) {
		for(Page page:pages) {
			for (Shape shape: page.getshapes()) {
				if (shape.getName().equals(shapeName)) {
					shape.setVisible(true);
				}
			}
		}
	}
	
	public static void putDividingBoundary(float h) {
		for (Page page:pages) {
			for (Shape shape:page.getshapes()) {
				shape.limitTopHeight(h);
			}
		}
		for (Shape shape:possession.getShapes()) {
			shape.limitBottomHeight(h);
		}
	}
	
	static void moveToCurPage(Shape shape) {
		for (Shape s:curPage.getshapes()) {
			if (s.equals(shape)) {
				curPage.removeShape(s);
				curPage.addShape(shape);
				return;
			}
		}
		for (Shape s:possession.getShapes()) {
			if (s.equals(shape)) {
				possession.removeShape(s);
				curPage.addShape(shape);
				return;
			}
		}
	}
	
	static void moveToPos(Shape shape) {
		for (Shape s:curPage.getshapes()) {
			if (s.equals(shape)) {
				curPage.removeShape(s);
				possession.addShape(shape);
				return;
			}
		}
		for (Shape s:possession.getShapes()) {
			if (s.equals(shape)) {
				possession.removeShape(s);
				possession.addShape(shape);
				return;
			}
		}
	}
	
}
