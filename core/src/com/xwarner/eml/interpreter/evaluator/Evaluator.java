package com.xwarner.eml.interpreter.evaluator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.xwarner.eml.core.Core;
import com.xwarner.eml.interpreter.evaluator.operators.LeftBracketOperator;
import com.xwarner.eml.interpreter.evaluator.operators.Operator;
import com.xwarner.eml.interpreter.evaluator.operators.RightBracketOperator;

public class Evaluator {

	public Object evaluate(ArrayList<ExpressionEntry> list) {
		ExpressionEntry e = list.get(0);
		// handle initial negative number
		if (e.type == ExpressionEntry.TYPE_OP) {
			ExpressionEntry ee = new ExpressionEntry();
			ee.type = ExpressionEntry.TYPE_NUM;
			ee.value = BigDecimal.ZERO;
			list.add(0, ee);
		}
		return evaluate(rpn(list));
	}

	private LinkedList<ExpressionEntry> rpn(ArrayList<ExpressionEntry> list) {
		LinkedList<ExpressionEntry> output = new LinkedList<ExpressionEntry>();
		Stack<ExpressionEntry> operatorStack = new Stack<ExpressionEntry>();

		for (ExpressionEntry token : list) {
			if (token.type != ExpressionEntry.TYPE_OP) {
				output.add(token);
			} else if (token.operator instanceof LeftBracketOperator) {
				operatorStack.push(token);
			} else if (token.operator instanceof RightBracketOperator) {
				while (!(operatorStack.peek().operator instanceof LeftBracketOperator)) {
					output.add(operatorStack.pop());
				}
				operatorStack.pop();
			} else {
				Operator op = token.operator;
				if (!operatorStack.isEmpty()) {
					Operator peek = operatorStack.peek().operator;
					while (!operatorStack.isEmpty() && (((peek.precedence > op.precedence)
							|| (peek.precedence == op.precedence && peek.leftAssociativity))
							&& !peek.operator.equals("("))) {
						output.add(operatorStack.pop());
						if (!operatorStack.isEmpty())
							peek = operatorStack.peek().operator;
					}
				}
				operatorStack.push(token);
			}
		}

		while (!operatorStack.isEmpty()) {
			output.add(operatorStack.pop());
		}

		return output;
	}

	private Object evaluate(LinkedList<ExpressionEntry> list) {
		Stack<ExpressionEntry> stack = new Stack<ExpressionEntry>();
		for (ExpressionEntry token : list) {
			if (token.type == ExpressionEntry.TYPE_OP) {
				ExpressionEntry operand2 = stack.pop();
				ExpressionEntry operand1 = stack.pop();
				stack.push(operate(token.operator, operand1, operand2));
			} else {
				stack.push(token);
			}
		}
		ExpressionEntry e = stack.pop();
		// if (e.type == ExpressionEntry.TYPE_VEC)
		// return e.vector;
		// else
		if (e.type == ExpressionEntry.TYPE_STRING)
			return e.stringValue;
		else if (e.type == ExpressionEntry.TYPE_BOOL)
			return e.booleanValue;
		else if (e.type == ExpressionEntry.TYPE_NUM)
			return e.value;
		else if (e.type == ExpressionEntry.TYPE_MAT) {
			return e.matrix;
		}
		throw new Error("unexpected result of evaluation");
	}

	private ExpressionEntry operate(Operator op, ExpressionEntry a, ExpressionEntry b) {
		ExpressionEntry e = new ExpressionEntry();
		if (a.type == ExpressionEntry.TYPE_NUM) {
			if (b.type == ExpressionEntry.TYPE_NUM) {
				if (op.type == Operator.TYPE_NUMERIC) {
					e.type = ExpressionEntry.TYPE_NUM;
					e.value = op.evaluateNumeric(a.value, b.value);
				} else if (op.type == Operator.TYPE_BOOLEAN) {
					e.type = ExpressionEntry.TYPE_BOOL;
					e.booleanValue = op.evaluateNumericBoolean(a.value, b.value);
				}
				/*
				 * } else if (b.type == ExpressionEntry.TYPE_VEC) { e.type =
				 * ExpressionEntry.TYPE_VEC; e.vector = op.evluateVector(a.value, b.vector);
				 */
			} else if (b.type == ExpressionEntry.TYPE_MAT) {
				e.type = ExpressionEntry.TYPE_MAT;
				e.matrix = op.evaluateNumericMatrix(a.value, b.matrix);
			} else if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.operator.equals("+")) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.value.toString() + b.stringValue;
				} else {
					Core.error.error("unexpected operator");
				}
			} else {
				Core.error.error("unexpected variable type in operation");
			}
		} else if (a.type == ExpressionEntry.TYPE_BOOL) {
			if (b.type == ExpressionEntry.TYPE_BOOL) {
				e.type = ExpressionEntry.TYPE_BOOL;
				e.booleanValue = op.evaluateBoolean(a.booleanValue, b.booleanValue);
			} else if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.operator.equals("+")) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.booleanValue + b.stringValue;
				} else {
					Core.error.error("unexpected operator");
				}
			} else {
				Core.error.error("unexpected variable type in operation");
			}
		} else if (a.type == ExpressionEntry.TYPE_STRING) {
			if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.type == Operator.TYPE_NUMERIC) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.stringValue;
				} else if (op.type == Operator.TYPE_BOOLEAN) {
					e.type = ExpressionEntry.TYPE_BOOL;
					e.booleanValue = op.evaluateStringBoolean(a.stringValue, b.stringValue);
				}
			} else if (op.operator.equals("+")) {
				if (b.type == ExpressionEntry.TYPE_NUM) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.value.toString();
				} else if (b.type == ExpressionEntry.TYPE_BOOL) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.booleanValue;
				} else if (b.type == ExpressionEntry.TYPE_MAT) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.matrix;
				}
			} else {
				Core.error.error("unexpected operator");
			}
			/*
			 * } else if (a.type == ExpressionEntry.TYPE_VEC) { if (b.type ==
			 * ExpressionEntry.TYPE_NUM) {
			 * 
			 * } else if (b.type == ExpressionEntry.TYPE_VEC) { if (op.type ==
			 * Operator.TYPE_NUMERIC) { e.type = ExpressionEntry.TYPE_VEC; e.vector =
			 * op.evaluateVector(a.vector, b.vector); } else if (op.type ==
			 * Operator.TYPE_BOOLEAN) { e.type = ExpressionEntry.TYPE_BOOL; e.booleanValue =
			 * op.evaluateVectorBoolean(a.vector, b.vector); } } else if (b.type ==
			 * ExpressionEntry.TYPE_MAT) {
			 * 
			 * } else if (b.type == ExpressionEntry.TYPE_STRING) { if
			 * (op.operator.equals("+")) { e.type = ExpressionEntry.TYPE_STRING;
			 * e.stringValue = a.vector + b.stringValue; } else {
			 * ErrorHandler.error("unexpected operator"); } } else {
			 * ErrorHandler.error("unexpected variable type in operation"); }
			 */
		} else if (a.type == ExpressionEntry.TYPE_MAT) {
			/*
			 * if (b.type == ExpressionEntry.TYPE_NUM) {
			 * 
			 * } else if (b.type == ExpressionEntry.TYPE_VEC) {
			 * 
			 * } else if (b.type == ExpressionEntry.TYPE_MAT) { if (op.type ==
			 * Operator.TYPE_NUMERIC) { e.type = ExpressionEntry.TYPE_MAT; e.matrix =
			 * op.evaluateMatrix(a.matrix, b.matrix); } else if (op.type ==
			 * Operator.TYPE_BOOLEAN) { e.type = ExpressionEntry.TYPE_BOOL; e.booleanValue =
			 * op.evaluateMatrixBoolean(a.matrix, b.matrix); } } else if (b.type ==
			 * ExpressionEntry.TYPE_STRING) { if (op.operator.equals("+")) { e.type =
			 * ExpressionEntry.TYPE_STRING; e.stringValue = a.matrix + b.stringValue; } else
			 * { ErrorHandler.error("unexpected operator"); } } else {
			 * ErrorHandler.error("unexpected variable type in operation"); }
			 */
		}
		return e;
	}

}
