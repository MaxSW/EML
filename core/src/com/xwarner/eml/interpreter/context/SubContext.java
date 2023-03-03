package com.xwarner.eml.interpreter.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.functions.Function;
import com.xwarner.eml.interpreter.context.objects.EClass;
import com.xwarner.eml.interpreter.context.objects.EObject;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.ReferenceNode;
import com.xwarner.eml.nodes.variables.VariableReferenceNode;
import com.xwarner.eml.util.ErrorHandler;

public class SubContext {
	private HashMap<String, Function> funcs;
	private HashMap<String, EClass> classes;

	private HashMap<String, Integer> map;

	public SubContext parent;
//	public HashMap<Variable, SubContext> children;

	public DataStore store;

	public SubContext(SubContext parent, DataStore store) {
		this.parent = parent;
		this.store = store;
		funcs = new HashMap<String, Function>();
		classes = new HashMap<String, EClass>();
		map = new HashMap<String, Integer>();
//		children = new HashMap<Variable, SubContext>();
	}

	/**
	 * Variables should only be created once, by the declaration node. Subsequent
	 * assignment should edit the existing variable object
	 * 
	 * @param name
	 * @param object
	 */
	public void createVariable(String name, Object object, Bundle bundle) {
		if (object instanceof Variable)
			map.put(name, store.put((Variable) object));
		else
			map.put(name, store.put(bundle.vars.generateVariable(object)));
	}

	public void createVariable(ReferenceNode ref, Object object, int level, Bundle bundle) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			createVariable(vrn.name, object, bundle);
		} else {
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					obj.context.createVariable(ref, object, level + 1, bundle);
					return;
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable var3 = (MatrixVariable) var2;
					var3.setVariable(ref, level + 1, bundle, object);
					return;
				}
			}
			ErrorHandler.error("unknown variable reference " + vrn.name);
		}
	}

	public Variable getVariable(String name, ReferenceNode ref) {
		if (map.containsKey(name)) {
			Variable var = store.get(map.get(name));
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
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				Variable var = null;
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					var = obj.context.getVariable(ref, level + 1, bundle);
					var.setReference(ref);
					/*
					 * } else if (var2 instanceof ArrayVariable) { ArrayVariable var3 =
					 * (ArrayVariable) var2; var = var3.getVariable(ref, level + 1, bundle);
					 * var.setReference(ref); } else if (var2 instanceof VectorVariable) {
					 * VectorVariable vec = (VectorVariable) var2; ArrayMemberNode node =
					 * (ArrayMemberNode) ref.getChildren().get(level + 1); int i = ((BigDecimal)
					 * node.invoke(bundle)).intValue(); Vector vector = (Vector)
					 * vec.getValue(bundle); var = new NumericVariable(vector.vals[i]);
					 * var.setReference(ref); }
					 */
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable mat = (MatrixVariable) var2;
					Node node = ref.getChildren().get(level + 1);
					int i = ((BigDecimal) node.invoke(bundle)).intValue();
					node = ref.getChildren().get(level + 2);
					int j = ((BigDecimal) node.invoke(bundle)).intValue();
					Matrix matrix = (Matrix) mat.getValue(bundle);
					var = new NumericVariable(matrix.vals[j][i]);
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
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					return obj.context.runFunction(ref, args, bundle, level + 1);
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable var3 = (MatrixVariable) var2;
					EObject obj = (EObject) var3.getVariable(ref, level + 1, bundle);
					return obj.context.runFunction(ref, args, bundle, level + 2);
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
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					return obj.context.getClass(ref, level + 1);
				}
			}
			ErrorHandler.error("unknown variable reference");
			return null;
		}
	}
}
