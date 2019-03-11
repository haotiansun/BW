package com.example.bunnyworld;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Editor {
    static ArrayList<Page> pages;
    static Inventory possession;
    static Page curPage;
    static Page startingPage;
    static Shape copyShape;
    static String gameName;


    public static void main() {
        pages = new ArrayList<>();
        possession = new Inventory();
        curPage = new Page("page1");
        startingPage = curPage;
        pages.add(curPage);
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

    static void deletePage(String name) {
        if (name.equals("page1")){
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

    public static void copyShape(){
         copyShape = curPage.selectedShape;
    }

    public static void pasteShape(){
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
