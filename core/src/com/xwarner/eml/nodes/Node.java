package com.xwarner.eml.nodes;

import java.util.ArrayList;

import org.json.JSONObject;

import com.xwarner.eml.interpreter.bundle.Bundle;

public class Node {

	private ArrayList<Node> children;
	public Node parent;

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

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("a", getClass().getSimpleName());
		for (Node n : children) {
			obj.accumulate("z", n.toJSON());
		}
		return obj;
	}

	public void optimise(Bundle bundle) {

	}

}
