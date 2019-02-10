package com.xwarner.eml.interpreter.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.interpreter.context.variables.ArrayVariable;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.VectorVariable;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.ArrayMemberNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;
import com.xwarner.eml.util.ErrorHandler;

public class SubContext {
	private HashMap<String, Variable> vars;
	private HashMap<String, Function> funcs;
	private HashMap<String, EClass> classes;

	public SubContext parent;
	public HashMap<Variable, SubContext> children;

	public SubContext(SubContext parent) {
		this.parent = parent;
		vars = new HashMap<String, Variable>();
		funcs = new HashMap<String, Function>();
		classes = new HashMap<String, EClass>();
		children = new HashMap<Variable, SubContext>();
	}

	/**
	 * Variables should only be created once, by the declaration node. Subsequent
	 * assignment should edit the existing variable object
	 * 
	 * @param name
	 * @param object
	 */
	public void createVariable(String name, Object object) {
		if (object instanceof Variable) {
			vars.put(name, (Variable) object);
		}

		else if (object instanceof BigDecimal)
			vars.put(name, new NumericVariable((BigDecimal) object));
		else if (object instanceof String)
			vars.put(name, new StringVariable((String) object));
		else if (object instanceof Boolean)
			vars.put(name, new BooleanVariable((Boolean) object));
	}

	public void createVariable(ReferenceNode ref, Object object, int level, Bundle bundle) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			createVariable(vrn.name, object);
		} else {
			if (vars.containsKey(vrn.name)) {
				Variable var2 = vars.get(vrn.name);
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					children.get(obj).createVariable(ref, object, level + 1, bundle);
					return;
				} else if (var2 instanceof ArrayVariable) {
					ArrayVariable var3 = (ArrayVariable) var2;
					var3.setVariable(ref, level + 1, bundle, object);
					return;
				}
			}
			ErrorHandler.error("unknown variable reference " + vrn.name);
		}
	}

	public Variable getVariable(String name, ReferenceNode ref) {
		if (vars.containsKey(name)) {
			Variable var = vars.get(name);
			var.setReference(ref);
			return var;
		} else if (parent != null)
			return parent.getVariable(name, ref);
		else
			ErrorHandler.error("unknown variable " + name);
		return null;

	}

	public Variable getVariable(ReferenceNode ref, int level, Bundle bundle) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			return getVariable(vrn.name, ref);
		} else {
			if (vars.containsKey(vrn.name)) {
				Variable var2 = vars.get(vrn.name);
				Variable var = null;
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					var = children.get(obj).getVariable(ref, level + 1, bundle);
					var.setReference(ref);
				} else if (var2 instanceof ArrayVariable) {
					ArrayVariable var3 = (ArrayVariable) var2;
					var = var3.getVariable(ref, level + 1, bundle);
					var.setReference(ref);
				} else if (var2 instanceof VectorVariable) {
					VectorVariable vec = (VectorVariable) var2;
					ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level + 1);
					int i = ((BigDecimal) node.invoke2(bundle)).intValue();
					Vector vector = (Vector) vec.getValue(bundle);
					var = new NumericVariable(vector.vals[i]);
					var.setReference(ref);
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable mat = (MatrixVariable) var2;
					ArrayMemberNode node = (ArrayMemberNode) ref.getChildren().get(level + 1);
					int i = ((BigDecimal) node.invoke2(bundle)).intValue();
					node = (ArrayMemberNode) ref.getChildren().get(level + 2);
					int j = ((BigDecimal) node.invoke2(bundle)).intValue();
					Matrix matrix = (Matrix) mat.getValue(bundle);
					var = new NumericVariable(matrix.vals[i][j]);
					var.setReference(ref);
				} else {
					var = var2.getVariable(ref, level + 1, bundle);
					if (var == null)
						ErrorHandler.error("unknown variable of " + vrn.name);
					else
						var.setReference(ref);
				}
				return var;
			}
			ErrorHandler.error("unknown variable reference " + vrn.name);
			return null;
		}
	}

	public void setFunction(String name, Function function) {
		funcs.put(name, function);
	}

	public Object runFunction(String name, ArrayList<Object> args, Bundle bundle) {
		if (funcs.containsKey(name))
			return funcs.get(name).run(args, bundle);
		else if (parent != null)
			return parent.runFunction(name, args, bundle);
		else
			ErrorHandler.error("unknown function " + name);
		return null;
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			return runFunction(vrn.name, args, bundle);
		} else {
			if (vars.containsKey(vrn.name)) {
				Variable var2 = vars.get(vrn.name);
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					return children.get(obj).runFunction(ref, args, bundle, level + 1);
				} else if (var2 instanceof Variable) {
					return ((Variable) var2).runFunction(ref, args, bundle, level + 1);
				}
			}
			ErrorHandler.error("unknown variable reference");
			return null;
		}
	}

	public void setClass(String name, EClass cls) {
		classes.put(name, cls);
	}

	public EClass getClass(String name) {
		return classes.get(name);
	}

	public EClass getClass(ReferenceNode ref, int level) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			return getClass(vrn.name);
		} else {
			if (vars.containsKey(vrn.name)) {
				Variable var2 = vars.get(vrn.name);
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					return children.get(obj).getClass(ref, level + 1);
				}
			}
			ErrorHandler.error("unknown variable reference");
			return null;
		}
	}

}
