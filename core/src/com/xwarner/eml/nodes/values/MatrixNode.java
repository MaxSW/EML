package com.xwarner.eml.nodes.values;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.xwarner.eml.interpreter.bundle.Bundle;
import com.xwarner.eml.interpreter.context.variables.MatrixVariable;
import com.xwarner.eml.interpreter.context.variables.values.Matrix;
import com.xwarner.eml.nodes.Node;

public class MatrixNode extends Node {

	public String toString() {
		return "matrix";
	}

	public Object invoke(Bundle bundle) {
		ArrayList<Node> rows = getChildren();

		Matrix m = new Matrix(rows.size(), rows.get(0).getChildren().size());

		for (int i = 0; i < m.h; i++) {
			for (int j = 0; j < m.w; j++) {
				m.vals[i][j] = (BigDecimal) rows.get(i).getChildren().get(j).invoke(bundle);
			}
		}
		MatrixVariable var = new MatrixVariable(m);
		return var;
	}

}
