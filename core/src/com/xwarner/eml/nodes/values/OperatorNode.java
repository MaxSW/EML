package com.xwarner.eml.nodes.values;

import java.util.HashMap;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.evaluator.operators.EqualityOperator;
import com.xwarner.eml.interpreter.evaluator.operators.InequalityOperator;
import com.xwarner.eml.interpreter.evaluator.operators.LeftBracketOperator;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;
import com.xwarner.eml.interpreter.evaluator.operators.RightBracketOperator;
import com.xwarner.eml.interpreter.evaluator.operators.bool.AndOperator;
import com.xwarner.eml.interpreter.evaluator.operators.bool.OrOperator;
import com.xwarner.eml.interpreter.evaluator.operators.boolean_numeric.GreaterThanEqualOperator;
import com.xwarner.eml.interpreter.evaluator.operators.boolean_numeric.GreaterThanOperator;
import com.xwarner.eml.interpreter.evaluator.operators.boolean_numeric.LessThanEqualOperator;
import com.xwarner.eml.interpreter.evaluator.operators.boolean_numeric.LessThanOperator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.AddOperator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.DivideOperator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.MultiplyOperator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.PowerOperator;
import com.xwarner.eml.interpreter.evaluator.operators.numeric.SubtractOperator;
import com.xwarner.eml.nodes.Node;

public class OperatorNode extends Node {

	public String operator;

	private Operator cache;

	private static HashMap<String, Operator> operators = new HashMap<String, Operator>();

	static {
		operators.put("+", new AddOperator());
		operators.put("-", new SubtractOperator());
		operators.put("/", new DivideOperator());
		operators.put("*", new MultiplyOperator());
		operators.put("^", new PowerOperator());
		operators.put("(", new LeftBracketOperator());
		operators.put(")", new RightBracketOperator());

		operators.put(">", new GreaterThanOperator());
		operators.put("<", new LessThanOperator());
		operators.put(">=", new GreaterThanEqualOperator());
		operators.put("<=", new LessThanEqualOperator());

		// code has to distinguish between the two types
		operators.put("==", new EqualityOperator());
		operators.put("!=", new InequalityOperator());

		operators.put("||", new OrOperator());
		operators.put("&&", new AndOperator());

		// TODO
	}

	public static Operator get(String str) {
		return operators.get(str);
	}
	
	public OperatorNode(String operator) {
		super();
		this.operator = operator;
	}

	public OperatorNode() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "operator - value: " + operator;
	}

	public Object invoke2(Bundle bundle) {
		if (cache == null)
			cache = operators.get(operator);
		return cache;
	}

	public String toSaveString() {
		return "14 " + operator;
	}
	
	public void fromSaveString(String[] split, String str) {
		operator = split[2];
	}

}
