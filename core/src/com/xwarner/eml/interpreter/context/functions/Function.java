package com.xwarner.eml.interpreter.context.functions;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.Bundle;
import com.xwarner.eml.interpreter.context.variables.BooleanVariable;
import com.xwarner.eml.interpreter.context.variables.NullVariable;
import com.xwarner.eml.interpreter.context.variables.NumericVariable;
import com.xwarner.eml.interpreter.context.variables.StringVariable;
import com.xwarner.eml.interpreter.context.variables.Variable;
import com.xwarner.eml.interpreter.flags.ReturnFlag;
import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.nodes.functions.BodyNode;
import com.xwarner.eml.nodes.functions.FunctionArgumentNode;

public class Function extends Variable {

	private BodyNode body;
	private ArrayList<FunctionArgumentNode> args;

	public Function() {
		args = new ArrayList<FunctionArgumentNode>();
	}

	public Object run(ArrayList<Object> args2, Bundle bundle) {
		bundle.context.enter(this);

		if (args2 == null && args.size() != 0) {
			for (int i = 0; i < args.size(); i++) {
				FunctionArgumentNode node = args.get(i);
				bundle.context.createVariable(node.name, new NullVariable(), bundle);
			}
		} else {

			for (int i = 0; i < args.size(); i++) {
				FunctionArgumentNode node = args.get(i);

				Variable var = null;

				if (i < args2.size()) {
					Object o = args2.get(i);

					if (o instanceof BigDecimal) {
						var = new NumericVariable((BigDecimal) o);
					} else if (node.type.equals("bool")) {
						var = new BooleanVariable((Boolean) o);
					} else if (node.type.equals("str")) {
						var = new StringVariable((String) o);
					}
				} else {
					var = new NullVariable();
				}

				bundle.context.createVariable(node.name, var, bundle);
			}
		}
		ArrayList<Node> children = body.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node node = children.get(i);
			Object o = node.invoke2(bundle);
			if (o instanceof ReturnFlag) {
				bundle.context.exit();
				return ((ReturnFlag) o).obj;
			}
		}

		bundle.context.exit();
		return null;
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
