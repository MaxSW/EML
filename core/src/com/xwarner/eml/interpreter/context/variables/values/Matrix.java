package com.xwarner.eml.interpreter.context.variables.values;

import java.math.BigDecimal;

public class Matrix {

	public int h, w;
	public BigDecimal[][] vals;

	public Matrix(int h, int w) {
		this.h = h;
		this.w = w;

		vals = new BigDecimal[h][w];
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				vals[y][x] = BigDecimal.ZERO;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				sb.append(vals[i][j]);
				if (j != w - 1)
					sb.append(", ");
			}
			if (i != h - 1)
				sb.append(" | ");
		}
		sb.append("]");
		return sb.toString();
	}

}
