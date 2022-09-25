package com.xwarner.eml.parser.tokens;

import java.util.ArrayList;

import com.xwarner.eml.parser.TokenStream;

/**
 * Simple regex for tokens. Not to be confused with TokenMatcher, which matches strings for Tokens
 * 
 * TODO implement!
 * 
 * TODO think about how this interacts with the pulling from the streams
 * 
 * @author Max Warner
 *
 */

public class TokenRule {

	private ArrayList<TokenRuleData> rules;

	public TokenRule() {
		rules = new ArrayList<TokenRuleData>();
	}

	public TokenRule require(int type) {
		rules.add(new TokenRuleData(type, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	public TokenRule optional(int type) {
		rules.add(new TokenRuleData(type, TokenRuleData.MODE_OPTIONAL));
		return this;
	}

	public TokenRule either(int type1, int type2) {
		rules.add(new TokenRuleData(new int[] { type1, type2 }, TokenRuleData.MODE_EITHER));
		return this;
	}

	public boolean matches(TokenStream stream) {
		for (TokenRuleData rule : rules) {
			// TODO reject or not for each rule
		}
		return false;
	}

	private class TokenRuleData {
		public static final int MODE_REQUIRE = 1, MODE_OPTIONAL = 2, MODE_EITHER = 3;

		public int type;
		public int[] types;
		public int mode;

		public TokenRuleData(int type, int mode) {
			this.type = type;
			this.mode = mode;
		}

		public TokenRuleData(int[] types, int mode) {
			this.types = types;
			this.mode = mode;
		}

	}

}
