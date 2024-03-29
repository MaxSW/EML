package com.xwarner.eml.interpreter.evaluator.operators;

import java.math.BigDecimal;

import com.xwarner.eml.interpreter.context.variables.values.Matrix;

public class Operator {

	public String operator;
	public int precedence;
	public boolean leftAssociativity;

	public int type;

	public static final int TYPE_NUMERIC = 1, TYPE_BOOLEAN = 2, TYPE_OTHER = 3;

	public Operator(String operator, int precedence, boolean leftAssociativity, int type) {
		this.operator = operator;
		this.precedence = precedence;
		this.leftAssociativity = leftAssociativity;
		this.type = type;
	}

	// TODO default should be to through an error saying this operator isn't defined
	// for this variable type - then only implement things that actually work for
	// specific operators

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

	public Matrix evaluateMatrix(Matrix a, Matrix b) {
		return null;
	}

	public Matrix evaluateNumericMatrix(BigDecimal a, Matrix b) {
		return null;
	}

	public String evaluateString(String a, String b) {
		return null;
	}

	public boolean evaluateMatrixBoolean(Matrix a, Matrix b) {
		return false;
	}

}
