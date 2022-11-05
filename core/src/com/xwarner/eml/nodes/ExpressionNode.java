package com.xwarner.eml.nodes;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.VectorVariable;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
import com.xwarner.eml.interpreter.evaluator.ExpressionEntry;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.MultiplyOperator;
import com.xwarner.eml.nodes.functions.InvocationNode;
import com.xwarner.eml.nodes.values.NumberNode;

public class ExpressionNode extends Node {

	public String toString() {
		return "expression";
	}

	/** Evaluate the expression **/
	public Object invoke(Bundle bundle) {
		// TODO we could pre-do a lot of this
		ArrayList<ExpressionEntry> vals = new ArrayList<ExpressionEntry>();

		Object last = null;
		for (Node n : getChildren()) {
			Object o = n.invoke(bundle);
			if (o == null)
				continue;
			if (o instanceof BigDecimal) {
				if (last != null) {
					if (last instanceof BigDecimal) {
						ExpressionEntry ee = new ExpressionEntry();
						ee.type = ExpressionEntry.TYPE_OP;
						ee.operator = new MultiplyOperator();
						vals.add(ee);
					}
				}
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_NUM;
				e.value = (BigDecimal) o;
				vals.add(e);

			} else if (o instanceof Operator) {
				if (last != null)
					if (((Operator) o).operator.equals("(") && ((Operator) last).operator.equals(")")) {
						ExpressionEntry ee = new ExpressionEntry();
						ee.type = ExpressionEntry.TYPE_OP;
						ee.operator = new MultiplyOperator();
						vals.add(ee);
					}
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_OP;
				e.operator = (Operator) o;
				vals.add(e);
			} else if (o instanceof String) {
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_STRING;
				e.stringValue = (String) o;
				vals.add(e);
			} else if (o instanceof Boolean) {
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_BOOL;
				e.booleanValue = (boolean) o;
				vals.add(e);
			} else if (o instanceof VectorVariable) {
				if (last instanceof BigDecimal) {
					ExpressionEntry ee = new ExpressionEntry();
					ee.type = ExpressionEntry.TYPE_OP;
					ee.operator = new MultiplyOperator();
					vals.add(ee);
				}
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_VEC;
				e.vector = (Vector) ((VectorVariable) o).getValue(bundle);
				vals.add(e);
			} else if (o instanceof MatrixVariable) {
				if (last instanceof BigDecimal) {
					ExpressionEntry ee = new ExpressionEntry();
					ee.type = ExpressionEntry.TYPE_OP;
					ee.operator = new MultiplyOperator();
					vals.add(ee);
				}
				ExpressionEntry e = new ExpressionEntry();
				e.type = ExpressionEntry.TYPE_MAT;
				e.matrix = (Matrix) ((MatrixVariable) o).getValue(bundle);
				vals.add(e);
			}

			else if (o instanceof Variable) {
				// for when variables are referenced directly with :
				return o;
			} else {
				throw new Error("unrecognised " + o);
			}
			last = o;
		}
		if (vals.size() == 1) {
			ExpressionEntry ee = vals.get(0);
			if (ee.type == ExpressionEntry.TYPE_BOOL)
				return ee.booleanValue;
			else if (ee.type == ExpressionEntry.TYPE_STRING)
				return ee.stringValue;
			else if (ee.type == ExpressionEntry.TYPE_VEC)
				return ee.vector;
			else if (ee.type == ExpressionEntry.TYPE_MAT)
				return ee.matrix;
			else
				return ee.value;
		}

		if (vals.isEmpty())
			return null;

		return bundle.evaluator.evaluate(vals);
	}

	public ArrayList<Variable> getVariables(Bundle bundle) {
		ArrayList<Variable> vars = new ArrayList<Variable>();
		for (Node node : getChildren())
			loopVars(vars, node, bundle);
		return vars;
	}

	private void loopVars(ArrayList<Variable> vars, Node node, Bundle bundle) {
		if (node instanceof ReferenceNode)
			vars.add(bundle.context.getVariable((ReferenceNode) node, 0, bundle));
		else {
			for (Node n : node.getChildren()) {
				loopVars(vars, n, bundle);
			}
		}
	}

	private boolean hasDependencies(Node node) {
		if (node instanceof ReferenceNode || node instanceof InvocationNode)
			return true;
		for (Node n : node.getChildren())
			if (hasDependencies(n))
				return true;
		return false;
	}

	public void optimise(Bundle bundle) {
		// simplest rule - if there are no invocations, pre-evaluate
		if (!hasDependencies(this)) {
			Object result = invoke(bundle);
			if (result instanceof Number) {
				getChildren().clear();
				getChildren().add(new NumberNode(((Number) result).floatValue()));
			}
			// TODO other types
		}
	}

}
