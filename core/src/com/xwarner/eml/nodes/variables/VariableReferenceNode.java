package com.xwarner.eml.nodes.variables;

import org.json.JSONObject;

import com.xwarner.eml.nodes.Node;

public class VariableReferenceNode extends Node {

	public String name;

	public VariableReferenceNode(String name) {
		super();
		this.name = name;
	}

	public VariableReferenceNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "variable reference - name: " + name;
	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("a", getClass().getSimpleName());
		obj.put("name", name);
		for (Node n : getChildren()) {
			obj.accumulate("z", n.toJSON());
		}
		return obj;
	}

}
