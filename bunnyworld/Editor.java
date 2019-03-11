package com.example.bunnyworld;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Editor {
    static ArrayList<Page> pages;
    static Inventory possession;
    static Page curPage;
    static Page startingPage;
    static Shape cutShape;
    static Shape copyShape;
    static String gameName;


    public static void main() {
        pages = new ArrayList<>();
        possession = new Inventory();
        curPage = new Page("page1");
        startingPage = curPage;
        pages.add(curPage);
        Shape bunny1 = new Shape(200, 1000, 100,100);
        bunny1.setName("bunny1");
        bunny1.setImageName("death");
        Shape bunny2 = new Shape(400, 1000,100,100);
        bunny2.setName("bunny2");
        bunny2.setImageName("mystic");
        Shape carrot1 = new Shape(600, 1000, 100,100);
        carrot1.setName("carrot1");
        carrot1.setImageName("carrot");
        Shape carrot2 = new Shape(800, 1000, 100,100);
        carrot2.setName("carrot2");
        carrot2.setImageName("carrot2");
        Shape trap = new Shape(1000, 1000, 100,100);
        trap.setName("trap");
        trap.setImageName("fire");
        Shape duck = new Shape(1200, 1000, 100,100);
        duck.setName("duck");
        duck.setImageName("duck");
        Shape rect = new Shape(1400, 1000, 100,100);
        Shape text = new Shape(1600, 1000, 100,100);
        text.setText("Text");
        possession.addShape(bunny1);
        possession.addShape(bunny2);
        possession.addShape(carrot1);
        possession.addShape(carrot2);
        possession.addShape(trap);
        possession.addShape(duck);
        possession.addShape(rect);
        possession.addShape(text);
    }

    static Page findPage(String name){
        Log.d("debugdebug", name);
        for (Page page:pages) {
            Log.d("debugdebug", page.getName());
            if (page.getName().equals(name)) {
                return page;
            }
        }
        return null;
    }

    static boolean setGameName(String name){
        if (!name.equals("gameList")) {
            gameName = name;
            return true;
        } else{
            return false;
        }
    }

    static void saveGame(SQLiteDatabase db){
        Database.saveGame(db, gameName, startingPage.getName(), pages);
    }

    static void loadGame(SQLiteDatabase db, String gameName){
        for (Page page:pages){
            pages.remove(page);
        }
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
        curPage = findPage(name);
        for (Shape shape:curPage.getshapes()) {
            shape.entered();
        }
        return;
    }


    static void addPage(String name){
        if (name.equals("")){
            Integer length = pages.size() + 1;
            Page page = new Page("page"+Integer.toString(length));
            pages.add(page);
        }
        else if (findPage(name) == null) {
            Page page = new Page(name);
            pages.add(page);
        }
    }

    static ArrayList<Page> getPages(){
        return pages;
    }

    static void deletePage(String name) {
        if (name.equals(startingPage.getName())){
            System.out.println("Can not delete starting page.");
            return;
        }
        Page page = findPage(name);
        pages.remove(page);
    }

    static void setStartingPage(String name){
        Page page = findPage(name);
        if (page!=null){
            startingPage = page;
        }
    }

    static void setPageName(String name, String newName){
        Page page = findPage(name);
        page.setName(newName);
    }


    static ArrayList<Shape> getShapesOnCurPage() {
        return curPage.getshapes();
    }

    static ArrayList<Shape> getShapesOnPos(){
        return possession.getShapes();
    }

    public static void hideShape(String shapeName) {
        for(Page page:pages) {
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

    public static void select(Shape shape){
        curPage.selectedShape = shape;
    }

    public static Shape getSelectedShape() {
        return curPage.selectedShape;
    }

    public static void noSelect(){
        curPage.selectedShape = null;
    }

    public static void cutShape(){
        curPage.removeShape(curPage.selectedShape);
        cutShape = curPage.selectedShape;
        curPage.selectedShape = null;
        copyShape = null;
    }

    public static void copyShape(){
         copyShape = new Shape(curPage.selectedShape);
    }

    public static void pasteShape(){
        if (cutShape!=null){
            curPage.addShape(cutShape);
            cutShape.setX(100);
            cutShape.setY(100);
            curPage.selectedShape = cutShape;
            cutShape = null;
        }
        if (copyShape!=null) {
            curPage.addShape(copyShape);
            copyShape.setX(100);
            copyShape.setY(100);
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
                Shape s = new Shape(shape);
                curPage.addShape(s);
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
