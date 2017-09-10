package model;

public class Point {
	private int node;
	private int span;

	public Point(int node, int span) {
		this.node = node;
		this.span = span;
	}
	
	public int getNode() {
		return node;
	}
	
	public int getSpan() {
		return span;
	}

}