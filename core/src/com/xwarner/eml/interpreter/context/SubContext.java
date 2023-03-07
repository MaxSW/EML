package com.xwarner.eml.interpreter.context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.xwarner.eml.core.Core;
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
	public void createVariable(String name, Object object) {
		if (object instanceof Variable)
			map.put(name, store.put((Variable) object));
		else
			map.put(name, store.put(Core.vars.generateVariable(object)));
	}

	public void createVariable(ReferenceNode ref, Object object, int level) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			createVariable(vrn.name, object);
		} else {
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					obj.context.createVariable(ref, object, level + 1);
					return;
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable var3 = (MatrixVariable) var2;
					var3.setVariable(ref, level + 1, object);
					return;
				}
			}
			Core.error.error("unknown variable reference " + vrn.name);
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
			Core.error.error("unknown variable " + name);
		return null;

	}

	public Variable getVariable(ReferenceNode ref, int level) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			return getVariable(vrn.name, ref);
		} else {
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				Variable var = null;
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					var = obj.context.getVariable(ref, level + 1);
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
					int i = ((BigDecimal) node.invoke()).intValue();
					node = ref.getChildren().get(level + 2);
					int j = ((BigDecimal) node.invoke()).intValue();
					Matrix matrix = (Matrix) mat.getValue();
					var = new NumericVariable(matrix.vals[j][i]);
					var.setReference(ref);
				} else {
					var = var2.getVariable(ref, level + 1);
					if (var == null)
						Core.error.error("unknown variable of " + vrn.name);
					else
						var.setReference(ref);
				}
				return var;
			}
			Core.error.error("unknown variable reference " + vrn.name);
			return null;
		}

	}

	public void setFunction(String name, Function function) {
		funcs.put(name, function);
	}

	public Object runFunction(String name, ArrayList<Object> args) {
		if (funcs.containsKey(name))
			return funcs.get(name).run(args);
		else if (parent != null)
			return parent.runFunction(name, args);
		else
			Core.error.error("unknown function " + name);
		return null;
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, int level) {
		VariableReferenceNode vrn = (VariableReferenceNode) ref.getChildren().get(level);
		if (ref.getChildren().size() == level + 1) {
			return runFunction(vrn.name, args);
		} else {
			if (map.containsKey(vrn.name)) {
				Variable var2 = store.get(map.get(vrn.name));
				if (var2 instanceof EObject) {
					EObject obj = (EObject) var2;
					return obj.context.runFunction(ref, args, level + 1);
				} else if (var2 instanceof MatrixVariable) {
					MatrixVariable var3 = (MatrixVariable) var2;
					EObject obj = (EObject) var3.getVariable(ref, level + 1);
					return obj.context.runFunction(ref, args, level + 2);
				} else if (var2 instanceof Variable) {
					return ((Variable) var2).runFunction(ref, args, level + 1);
				}
			}
			Core.error.error("unknown variable reference");
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
			Core.error.error("unknown variable reference");
			return null;
		}
	}
}
