package com.xwarner.eml.nodes.objects;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.functions.InvocationNode;

public class ObjectCreationNode extends Node {

	public String toString() {
		return "object creation";
	}

	public Object invoke(Bundle bundle) {
		InvocationNode node = (InvocationNode) getChildren().get(0);
		EClass cls = bundle.context.getClass((ReferenceNode) node.getChildren().get(0), 0);
		EObject obj = new EObject(cls);
		obj.instantiate(bundle);
		return obj;
	}
}
