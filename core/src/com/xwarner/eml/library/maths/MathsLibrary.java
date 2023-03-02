package com.xwarner.eml.library.maths;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathsLibrary {

	public static BigDecimal round(BigDecimal value, BigDecimal places) {
		if (places == null)
			return value.setScale(0, RoundingMode.HALF_EVEN);
		return value.setScale(places.intValue(), RoundingMode.HALF_EVEN);
	}

	public static BigDecimal floor(BigDecimal value, BigDecimal places) {
		if (places == null)
			return value.setScale(0, RoundingMode.FLOOR);
		return value.setScale(places.intValue(), RoundingMode.FLOOR);
	}

	public static BigDecimal ceil(BigDecimal value, BigDecimal places) {
		if (places == null)
			return value.setScale(0, RoundingMode.CEILING);
		return value.setScale(places.intValue(), RoundingMode.CEILING);
	}

	public static BigDecimal abs(BigDecimal value) {
		return value.abs();
	}

	public static BigDecimal log(BigDecimal value) {
		return BigDecimal.valueOf(Math.log(value.doubleValue()));
	}

	public static BigDecimal log10(BigDecimal value) {
		return BigDecimal.valueOf(Math.log10(value.doubleValue()));
	}

	public static BigDecimal modulus(BigDecimal value1, BigDecimal value2) {
		return value1.remainder(value2);
	}

	public static BigDecimal sin(BigDecimal value) {
		return new BigDecimal(Math.sin(value.doubleValue()));
	}

	public static BigDecimal cos(BigDecimal value) {
		return new BigDecimal(Math.cos(value.doubleValue()));
	}

	public static BigDecimal tan(BigDecimal value) {
		return new BigDecimal(Math.tan(value.doubleValue()));
	}

}
