package com.xwarner.eml.interpreter.context.variables.values;

import java.math.BigDecimal;

public class Vector {

	public BigDecimal[] vals;
	public int size;

	public Vector(int size) {
		this.size = size;
		vals = new BigDecimal[size];
		for (int i = 0; i < size; i++)
			vals[i] = BigDecimal.ZERO;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			sb.append(vals[i]);
			if (i != size - 1)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

}
