package com.xwarner.eml.nodes.variables;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.context.objects.BlankObject;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.nodes.ExpressionNode;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.functions.InvocationNode;
import com.xwarner.eml.nodes.objects.ObjectCreationNode;

public class DeclarationNode extends Node {

	public String varType;

	public DeclarationNode(String varType) {
		super();
		this.varType = varType;
	}

	public DeclarationNode() {
	}

	public String toString() {
		return "declaration - type: " + varType;
	}

	public Object invoke() {
		ArrayList<Node> children = getChildren();
		ReferenceNode ref = (ReferenceNode) children.get(0);
		if (ref.flag) {
			if (children.size() == 2) {
				ExpressionNode exp = (ExpressionNode) children.get(1);
				Definition def = new Definition(exp);
				if (varType.equals("var"))
					Core.context.createVariable(ref, new NumericVariable(def), 0);
				else if (varType.equals("str"))
					Core.context.createVariable(ref, new StringVariable(def), 0);
				else if (varType.equals("bool"))
					Core.context.createVariable(ref, new BooleanVariable(def), 0);
				else if (varType.equals("mat") || varType.equals("vec"))
					Core.context.createVariable(ref, new MatrixVariable(def), 0);
			}
		} else {
			if (children.size() == 2) {
				if (children.get(1) instanceof ExpressionNode) {
					ExpressionNode exp = (ExpressionNode) children.get(1);
					if (varType.equals("bool")) {
						Core.context.createVariable(ref, new BooleanVariable((boolean) exp.invoke()), 0);
					} else if (varType.equals("str")) {
						Core.context.createVariable(ref, new StringVariable((String) exp.invoke()), 0);
					} else if (varType.equals("var")) {
						NumericVariable var;
						// if (varType.equals("def"))
						// var = new DefVariable(exp, (BigDecimal) exp.invoke(bundle));
						// else
						var = new NumericVariable((BigDecimal) exp.invoke());
						Core.context.createVariable(ref, var, 0);
					} else if (varType.equals("vec") || varType.equals("mat") || varType.equals("arr")) {
						Core.context.createVariable(ref, new MatrixVariable((Matrix) exp.invoke()), 0);
					}
				} else if (children.get(1) instanceof ObjectCreationNode) {
					EObject obj = (EObject) children.get(1).invoke();
					Core.context.createVariable(ref, obj, 0);

				} else if (children.get(1) instanceof InvocationNode) {
					EObject obj = (EObject) children.get(1).invoke();
					Core.context.createVariable(ref, obj, 0);
				}
				/*
				 * else if (varType.equals("arr")) { DeclarationNode n = (DeclarationNode)
				 * children.get(1); bundle.context.createVariable(ref, new
				 * ArrayVariable(n.varType), 0, bundle); }
				 */
			} else {
				if (varType.equals("obj") && children.size() == 1) {
					EObject obj = new BlankObject();
					obj.instantiate();
					Core.context.createVariable(ref, obj, 0);
				} else if (varType.equals("arr")) {
					Core.context.createVariable(ref, new MatrixVariable(0, 0), 0);
				}
			}

		}
		return null;
	}
}
