package com.xwarner.eml.interpreter.context.objects;

import java.util.ArrayList;

import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.functions.FunctionArgumentNode;

public class EClass {

	private BodyNode body;
	private ArrayList<FunctionArgumentNode> args;

	public EClass() {
		args = new ArrayList<FunctionArgumentNode>();
	}

	public BodyNode getBody() {
		return body;
	}

	public void setBody(BodyNode body) {
		this.body = body;
	}

	public ArrayList<FunctionArgumentNode> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<FunctionArgumentNode> args) {
		this.args = args;
	}

	public void addArg(FunctionArgumentNode node) {
		args.add(node);
	}

}
