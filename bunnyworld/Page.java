package com.example.bunnyworld;
import java.util.*;

public class Page {
	private String name;
	private ArrayList<Shape> shapes;
	//static
	

	Page(String name){
		this.name = name;
		shapes = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}


	public ArrayList<Shape> getshapes() {
		return shapes;
	}
		
	public void clearPage() {
		for (Shape shape:shapes) {
			shapes.remove(shape);
		}
	}
	
	public void addShape(Shape shape) {
		if(!shapes.contains(shape)) {
			shape.setPageName(this.name);
			shapes.add(shape);
		}
	}
	

	public void removeShape(Shape shape) {
		for (Shape s:shapes) {
			if (shape.equals(s)) {
				s.setPageName("");
				shapes.remove(s);
				return;
			}
		}
		System.out.println("No such shape.");
	}

}


