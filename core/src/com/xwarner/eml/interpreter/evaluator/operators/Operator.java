package com.xwarner.eml.interpreter.evaluator.operators;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.interpreter.context.variables.values.Vector;

public class Operator {

	public String operator;
	public int precedence;
	public boolean leftAssociativity;

	public int type;

	public static final int TYPE_NUMERIC = 1,TYPE_BOOLEAN = 2, TYPE_OTHER = 3;

	public Operator(String operator, int precedence, boolean leftAssociativity, int type) {
		this.operator = operator;
		this.precedence = precedence;
		this.leftAssociativity = leftAssociativity;
		this.type = type;
	}

	public BigDecimal evaluateNumeric(BigDecimal a, BigDecimal b) {
		return BigDecimal.ZERO;
	}

	public boolean evaluateBoolean(boolean a, boolean b) {
		return false;
	}

	public boolean evaluateNumericBoolean(BigDecimal a, BigDecimal b) {
		return false;
	}

	public boolean evaluateStringBoolean(String a, String b) {
		return false;
	}

	public Vector evaluateVector(Vector a, Vector b) {
		return null;
	}

	public BigDecimal evaluateVector2(Vector a, Vector b) {
		return BigDecimal.ZERO;
	}

	public Vector evluateVector(BigDecimal a, Vector b) {
		return null;
	}

	public Matrix evaluateMatrix(Matrix a, Matrix b) {
		return null;
	}

	public Matrix evaluateMatrix(BigDecimal a, Matrix b) {
		return null;
	}

	public boolean evaluateMatrixBoolean(Matrix a, Matrix b) {
		return false;
	}
	
	public boolean evaluateVectorBoolean(Vector a, Vector b) {
		return false;
	}

}
