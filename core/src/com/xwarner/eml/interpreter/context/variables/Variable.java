package com.xwarner.eml.interpreter.context.variables;

import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.definitions.Definition;
import com.xwarner.eml.nodes.ReferenceNode;

/**
 * Variables are not used internally for calculations!
 * 
 * @author max
 *
 */

public class Variable {

	/**
	 * Whether this variable is an equation or a constant. Equation variables will
	 * be automatically changed when their constituent variables are changed
	 */
	private boolean equation;
	protected Definition definition;

	/**
	 * This is only to be used in special cases and will not always be defined.
	 * Variable objects are designed to be independent of their location and for the
	 * context classes to handle that
	 */
	private ReferenceNode reference;

	private ArrayList<VariableListener> listeners;

	public Variable() {
		listeners = new ArrayList<VariableListener>();
	}

	public Object getValue(Bundle bundle) {
		return null;
	}

	public void setValue(Object obj) {
		if (listeners != null)
			for (VariableListener listener : listeners)
				listener.onChange(obj);
		// switching back to standard mode
		if (equation)
			equation = false;

	}

	public boolean isEquation() {
		return equation;
	}

	public void setEquation(boolean equation) {
		this.equation = equation;
	}

	public ReferenceNode getReference() {
		return reference;
	}

	public void setReference(ReferenceNode reference) {
		this.reference = reference;
	}

	public void addListener(VariableListener listener) {
		if (listeners == null)
			listeners = new ArrayList<VariableListener>();
		listeners.add(listener);
	}

	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

	public Variable getVariable(ReferenceNode ref, int level, Bundle bundle) {
		return null;
	}

	public void setVariable(ReferenceNode ref, int level, Bundle bundle, Object object) {
	}

	public Object runFunction(ReferenceNode ref, ArrayList<Object> args, Bundle bundle, int level) {
		return null;
	}

}
