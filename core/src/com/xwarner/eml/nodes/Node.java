package com.xwarner.eml.nodes;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;

public class Node {

	private ArrayList<Node> children;
	public Node parent;

	public int type;

	public Node() {
		children = new ArrayList<Node>();
	}

	public void addChild(Node n) {
		if (n != null)
			n.parent = this;
		children.add(n);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		for (Node n : children)
			n.parent = this;
		this.children = children;
	}

	public void clearChildren() {
		children.clear();
	}

	/**
	 * The first of the two functions called by the interpreter. This is used to set
	 * up any code that will be used later, for example the declaration (but not the
	 * execution) of functions and classes
	 * 
	 * @param bundle
	 * @return
	 */
	public Object invoke1(Bundle bundle) {
		return null;
	}

	/**
	 * The second of the two functions called by the interpreter. This is used to
	 * actually evaluate the given node, and if necessary, return its value. Each
	 * node evaluates itself, using the bundle
	 * 
	 * @param bundle
	 * @return
	 */
	public Object invoke2(Bundle bundle) {
		return null;
	}

	/**
	 * The node should return a string starting with its unique id and then followed
	 * by space separated values. All of its data should be included
	 * 
	 * @return
	 */
	public String toSaveString() {
		return "";
	}

	/**
	 * The node should re-populate its values from the string generated by the
	 * toSaveString function
	 * 
	 * @param split
	 * @param str
	 */
	public void fromSaveString(String[] split, String str) {

	}

}
