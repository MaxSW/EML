package com.xwarner.eml.interpreter.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.xwarner.eml.interpreter.tools.operators.LeftBracketOperator;
import com.xwarner.eml.interpreter.tools.operators.Operator;
import com.xwarner.eml.interpreter.tools.operators.RightBracketOperator;
import com.xwarner.eml.tools.ErrorHandler;

public class Evaluator {

	public Object evaluate(ArrayList<ExpressionEntry> list) {
		ExpressionEntry e = list.get(0);
		if (e.type == ExpressionEntry.TYPE_OP) {
			ExpressionEntry ee = new ExpressionEntry();
			ee.type = ExpressionEntry.TYPE_NUM;
			ee.value = BigDecimal.ZERO;
			list.add(0, ee);
		}
		return evaluate(rpn(list));
	}

	/*
	 * public BigDecimal evaluateNumeric(ArrayList<ExpressionEntry> list) {
	 * ExpressionEntry e = list.get(0); if (e.type == ExpressionEntry.TYPE_OP) {
	 * ExpressionEntry ee = new ExpressionEntry(); ee.type =
	 * ExpressionEntry.TYPE_NUM; ee.value = BigDecimal.ZERO; list.add(0, ee); }
	 * return evaluateNumeric(rpn(list)); }
	 * 
	 * public boolean evaluateBoolean(ArrayList<ExpressionEntry> list) { return
	 * evaluateBoolean(rpn(list)); }
	 * 
	 * public String evaluateString(ArrayList<ExpressionEntry> list) { StringBuilder
	 * sb = new StringBuilder(); boolean add = true; for (ExpressionEntry exp :
	 * list) { if (exp.type != ExpressionEntry.TYPE_OP && add) { if (exp.type ==
	 * ExpressionEntry.TYPE_STRING) sb.append(exp.stringValue); else if (exp.type ==
	 * ExpressionEntry.TYPE_BOOL) sb.append(exp.booleanValue); else
	 * sb.append(exp.value); } if (exp.type == ExpressionEntry.TYPE_OP &&
	 * exp.operator.operator.equals("+")) add = true; else add = false; } return
	 * sb.toString(); }
	 * 
	 * public Object evaluateLinearAlgebra(ArrayList<ExpressionEntry> list) {
	 * System.out.println("evaluating linear algebra"); return
	 * evaluateLinearAlgebra(rpn(list)); }
	 */

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
		if (e.type == ExpressionEntry.TYPE_VEC)
			return e.vector;
		else if (e.type == ExpressionEntry.TYPE_STRING)
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
			} else if (b.type == ExpressionEntry.TYPE_VEC) {
				e.type = ExpressionEntry.TYPE_VEC;
				e.vector = op.evluateVector(a.value, b.vector);
			} else if (b.type == ExpressionEntry.TYPE_MAT) {
				e.type = ExpressionEntry.TYPE_MAT;
				e.matrix = op.evaluateMatrix(a.value, b.matrix);
			} else if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.operator.equals("+")) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.value.toString() + b.stringValue;
				} else {
					ErrorHandler.error("unexpected operator");
				}
			} else {
				ErrorHandler.error("unexpected variable type in operation");
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
					ErrorHandler.error("unexpected operator");
				}
			} else {
				ErrorHandler.error("unexpected variable type in operation");
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
				} else if (b.type == ExpressionEntry.TYPE_VEC) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.vector;
				} else if (b.type == ExpressionEntry.TYPE_BOOL) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.booleanValue;
				} else if (b.type == ExpressionEntry.TYPE_MAT) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.stringValue + b.matrix;
				}
			} else {
				ErrorHandler.error("unexpected operator");
			}
		} else if (a.type == ExpressionEntry.TYPE_VEC) {
			if (b.type == ExpressionEntry.TYPE_NUM) {

			} else if (b.type == ExpressionEntry.TYPE_VEC) {
				if (op.type == Operator.TYPE_NUMERIC) {
					e.type = ExpressionEntry.TYPE_VEC;
					e.vector = op.evaluateVector(a.vector, b.vector);
				} else if (op.type == Operator.TYPE_BOOLEAN) {
					e.type = ExpressionEntry.TYPE_BOOL;
					e.booleanValue = op.evaluateVectorBoolean(a.vector, b.vector);
				}
			} else if (b.type == ExpressionEntry.TYPE_MAT) {

			} else if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.operator.equals("+")) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.vector + b.stringValue;
				} else {
					ErrorHandler.error("unexpected operator");
				}
			} else {
				ErrorHandler.error("unexpected variable type in operation");
			}
		} else if (a.type == ExpressionEntry.TYPE_MAT) {
			if (b.type == ExpressionEntry.TYPE_NUM) {

			} else if (b.type == ExpressionEntry.TYPE_VEC) {

			} else if (b.type == ExpressionEntry.TYPE_MAT) {
				if (op.type == Operator.TYPE_NUMERIC) {
					e.type = ExpressionEntry.TYPE_MAT;
					e.matrix = op.evaluateMatrix(a.matrix, b.matrix);
				} else if (op.type == Operator.TYPE_BOOLEAN) {
					e.type = ExpressionEntry.TYPE_BOOL;
					e.booleanValue = op.evaluateMatrixBoolean(a.matrix, b.matrix);
				}
			} else if (b.type == ExpressionEntry.TYPE_STRING) {
				if (op.operator.equals("+")) {
					e.type = ExpressionEntry.TYPE_STRING;
					e.stringValue = a.matrix + b.stringValue;
				} else {
					ErrorHandler.error("unexpected operator");
				}
			} else {
				ErrorHandler.error("unexpected variable type in operation");
			}
		}
		return e;
	}

	/*
	 * private BigDecimal evaluateNumeric(LinkedList<ExpressionEntry> list) {
	 * Stack<ExpressionEntry> stack = new Stack<ExpressionEntry>(); for
	 * (ExpressionEntry token : list) { if (token.type == ExpressionEntry.TYPE_OP) {
	 * BigDecimal operand2 = stack.pop().value; BigDecimal operand1 =
	 * stack.pop().value; ExpressionEntry e = new ExpressionEntry(); e.type =
	 * ExpressionEntry.TYPE_NUM; e.value = token.operator.evaluateNumeric(operand1,
	 * operand2); stack.push(e); } else { stack.push(token); } } return
	 * stack.pop().value; }
	 * 
	 * private Object evaluateLinearAlgebra(LinkedList<ExpressionEntry> list) {
	 * Stack<ExpressionEntry> stack = new Stack<ExpressionEntry>(); for
	 * (ExpressionEntry token : list) { if (token.type == ExpressionEntry.TYPE_OP) {
	 * ExpressionEntry operand2 = stack.pop(); ExpressionEntry operand1 =
	 * stack.pop();
	 * 
	 * ExpressionEntry e = new ExpressionEntry();
	 * 
	 * if (operand1.type == ExpressionEntry.TYPE_VEC && operand2.type ==
	 * ExpressionEntry.TYPE_VEC) { if (token.operator.operator.equals("*")) { e.type
	 * = ExpressionEntry.TYPE_NUM; e.value =
	 * token.operator.evaluateVector2(operand1.vector, operand2.vector); } else {
	 * e.type = ExpressionEntry.TYPE_VEC; e.vector =
	 * token.operator.evaluateVector(operand1.vector, operand2.vector); } } else if
	 * (operand1.type == ExpressionEntry.TYPE_NUM && operand2.type ==
	 * ExpressionEntry.TYPE_VEC) { e.type = ExpressionEntry.TYPE_VEC; e.vector =
	 * token.operator.evluateVector(operand1.value, operand2.vector); } else if
	 * (operand1.type == ExpressionEntry.TYPE_MAT && operand2.type ==
	 * ExpressionEntry.TYPE_MAT) { e.type = ExpressionEntry.TYPE_MAT; e.matrix =
	 * token.operator.evaluateMatrix(operand1.matrix, operand2.matrix); } else if
	 * (operand1.type == ExpressionEntry.TYPE_NUM && operand2.type ==
	 * ExpressionEntry.TYPE_MAT) { e.type = ExpressionEntry.TYPE_MAT; e.matrix =
	 * token.operator.evaluateMatrix(operand1.value, operand2.matrix); }
	 * stack.push(e); } else { stack.push(token); } } ExpressionEntry e =
	 * stack.pop(); if (e.type == ExpressionEntry.TYPE_VEC) return e.vector; else if
	 * (e.type == ExpressionEntry.TYPE_NUM) return e.value; else if (e.type ==
	 * ExpressionEntry.TYPE_MAT) { return e.matrix; }
	 * 
	 * throw new Error("unexpected result of evaluation"); }
	 * 
	 * private boolean evaluateBoolean(LinkedList<ExpressionEntry> list) {
	 * Stack<ExpressionEntry> stack = new Stack<ExpressionEntry>(); for
	 * (ExpressionEntry token : list) { if (token.type == ExpressionEntry.TYPE_OP) {
	 * ExpressionEntry operand2 = stack.pop(); ExpressionEntry operand1 =
	 * stack.pop(); if (operand1.type != operand2.type) throw new
	 * Error("mismatched variable types in boolean argument"); Operator op =
	 * token.operator; if (op.type == Operator.TYPE_NUMERIC) { if (operand1.type !=
	 * ExpressionEntry.TYPE_NUM || operand2.type != ExpressionEntry.TYPE_NUM) throw
	 * new Error("only numbers can be used with this operator"); ExpressionEntry e =
	 * new ExpressionEntry(); e.type = ExpressionEntry.TYPE_NUM; e.value =
	 * op.evaluateNumeric(operand1.value, operand2.value); stack.push(e); } else if
	 * (op.type == Operator.TYPE_ALL || op.type == Operator.TYPE_BOOLEAN_NUMERIC) {
	 * if (op.operator.equals("==") || op.operator.equals("!=")) { if (operand1.type
	 * == ExpressionEntry.TYPE_BOOL || operand2.type == ExpressionEntry.TYPE_BOOL) {
	 * ExpressionEntry e = new ExpressionEntry(); e.type =
	 * ExpressionEntry.TYPE_BOOL; if (op.operator.equals("==")) { e.booleanValue =
	 * op.evaluateBoolean(operand1.booleanValue, operand2.booleanValue); } else if
	 * (op.operator.equals("!=")) { e.booleanValue =
	 * op.evaluateBoolean(operand1.booleanValue, operand2.booleanValue); }
	 * stack.push(e); } else if (operand1.type == ExpressionEntry.TYPE_STRING &&
	 * operand2.type == ExpressionEntry.TYPE_STRING) { ExpressionEntry e = new
	 * ExpressionEntry(); e.type = ExpressionEntry.TYPE_BOOL; e.booleanValue =
	 * op.evaluateStringBoolean(operand1.stringValue, operand2.stringValue);
	 * stack.push(e); } else { ExpressionEntry e = new ExpressionEntry(); e.type =
	 * ExpressionEntry.TYPE_BOOL; e.booleanValue =
	 * op.evaluateNumericBoolean(operand1.value, operand2.value); stack.push(e); } }
	 * else if (operand1.type != ExpressionEntry.TYPE_NUM || operand2.type !=
	 * ExpressionEntry.TYPE_NUM) throw new
	 * Error("only numbers can be used with this operator"); else { ExpressionEntry
	 * e = new ExpressionEntry(); e.type = ExpressionEntry.TYPE_BOOL; e.booleanValue
	 * = op.evaluateNumericBoolean(operand1.value, operand2.value); stack.push(e); }
	 * } else if (op.type == Operator.TYPE_BOOLEAN) { if (operand1.type !=
	 * ExpressionEntry.TYPE_BOOL || operand2.type != ExpressionEntry.TYPE_BOOL)
	 * throw new Error("only booleans can be used with this operator");
	 * ExpressionEntry e = new ExpressionEntry(); e.type =
	 * ExpressionEntry.TYPE_BOOL; e.booleanValue =
	 * op.evaluateBoolean(operand1.booleanValue, operand2.booleanValue);
	 * stack.push(e); }
	 * 
	 * } else { stack.push(token); } } return stack.pop().booleanValue;
	 * 
	 * }
	 */
}
