package com.xwarner.eml.nodes.variables;

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

}
