package com.xwarner.eml.nodes.variables;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.objects.BlankObject;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.interpreter.context.variables.ArrayVariable;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.VectorVariable;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
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

	public Object invoke2(Bundle bundle) {
		ArrayList<Node> children = getChildren();
		ReferenceNode ref = (ReferenceNode) children.get(0);
		if (ref.flag) {
			if (children.size() == 2) {
				ExpressionNode exp = (ExpressionNode) children.get(1);
				Definition def = new Definition(exp, bundle);
				if (varType.equals("var"))
					bundle.context.createVariable(ref, new NumericVariable(def), 0, bundle);
				else if (varType.equals("str"))
					bundle.context.createVariable(ref, new StringVariable(def), 0, bundle);
				else if (varType.equals("bool"))
					bundle.context.createVariable(ref, new BooleanVariable(def), 0, bundle);
				else if (varType.equals("vec"))
					bundle.context.createVariable(ref, new VectorVariable(def), 0, bundle);
				else if (varType.equals("mat"))
					bundle.context.createVariable(ref, new MatrixVariable(def), 0, bundle);
			}
		} else {
			if (children.size() == 2) {
				if (children.get(1) instanceof ExpressionNode) {
					ExpressionNode exp = (ExpressionNode) children.get(1);
					if (varType.equals("bool")) {
						bundle.context.createVariable(ref, new BooleanVariable((boolean) exp.invoke2(bundle)), 0,
								bundle);
					} else if (varType.equals("str")) {
						bundle.context.createVariable(ref, new StringVariable((String) exp.invoke2(bundle)), 0, bundle);
					} else if (varType.equals("var")) {
						NumericVariable var;
						// if (varType.equals("def"))
						// var = new DefVariable(exp, (BigDecimal) exp.invoke2(bundle));
						// else
						var = new NumericVariable((BigDecimal) exp.invoke2(bundle));
						bundle.context.createVariable(ref, var, 0, bundle);
					} else if (varType.equals("vec")) {
						bundle.context.createVariable(ref, new VectorVariable((Vector) exp.invoke2(bundle)), 0, bundle);
					} else if (varType.equals("mat")) {
						bundle.context.createVariable(ref, new MatrixVariable((Matrix) exp.invoke2(bundle)), 0, bundle);
					}
				} else if (children.get(1) instanceof ObjectCreationNode) {
					EObject obj = (EObject) children.get(1).invoke2(bundle);
					bundle.context.createVariable(ref, obj, 0, bundle);

				} else if (children.get(1) instanceof InvocationNode) {
					EObject obj = (EObject) children.get(1).invoke2(bundle);
					bundle.context.createVariable(ref, obj, 0, bundle);
				}
			} else {
				if (varType.equals("obj") && children.size() == 1) {
					EObject obj = new BlankObject();
					obj.instantiate(bundle);
					bundle.context.createVariable(ref, obj, 0, bundle);
				} else if (varType.equals("arr")) {
					bundle.context.createVariable(ref, new ArrayVariable(), 0, bundle);
				}
			}

		}
		return null;
	}

	public String toSaveString() {
		return "19 " + varType;
	}

	public void fromSaveString(String[] split, String str) {
		varType = split[2];
	}

}
