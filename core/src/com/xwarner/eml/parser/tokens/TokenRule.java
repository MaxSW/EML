package com.xwarner.eml.parser.tokens;

import java.util.ArrayList;

import com.xwarner.eml.nodes.Node;
import com.xwarner.eml.parser.TokenStream5;

/**
 * Simple regex for tokens. Not to be confused with TokenMatcher, which matches
 * strings for Tokens
 * 
 * TODO implement!
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
		rules.add(new TokenRuleData(new int[] { type }, new String[] { "" }, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	public TokenRule require(int type, String value) {
		rules.add(new TokenRuleData(new int[] { type }, new String[] { value }, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	public TokenRule require(TokenRule rule) {
		rules.add(new TokenRuleData(rule, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	public TokenRule optional(int type, String value) {
		rules.add(new TokenRuleData(new int[] { type }, new String[] { value }, TokenRuleData.MODE_OPTIONAL));
		return this;
	}

	public TokenRule either(int[] types, String[] values) {
		rules.add(new TokenRuleData(types, values, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	public TokenRule any() {
		rules.add(new TokenRuleData(new int[] { 0 }, new String[] { "" }, TokenRuleData.MODE_ANY));
		return this;
	}

	public TokenRule either(int type, String... values) {
		int[] types = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			types[i] = type;
		}
		rules.add(new TokenRuleData(types, values, TokenRuleData.MODE_REQUIRE));
		return this;
	}

	private ArrayList<Token> tokens;

	public boolean matches(TokenStream5 stream) {
		int startPos = stream.getPos();

		tokens = new ArrayList<Token>();

		for (TokenRuleData rule : rules) {

			if (!rule.chain) {
				Token token = stream.next();
				tokens.add(token);
				if (token == null) {
					// TODO check if there are later token requirements
					if (rule.mode != TokenRuleData.MODE_ANY & rule.mode != TokenRuleData.MODE_OPTIONAL) {
						stream.setPos(startPos);
						return false;
					}
				}
				if (rule.mode == TokenRuleData.MODE_ANY)
					continue;
				boolean matches = rule.matches(token);
				if (!matches & rule.mode != TokenRuleData.MODE_OPTIONAL) {
					stream.setPos(startPos);
					return false;
				}
			} else {
				// chain rules together
				if (!rule.rule.matches(stream)) {
					stream.setPos(startPos);
					return false;
				}
				tokens.addAll(rule.rule.getTokens());
			}
		}
		// if matches, we don't reset the stream position
		return true;
	}

	public Node runIfMatches(TokenStream5 stream, TokenRuleResponse response) {
		if (matches(stream))
			return response.run(tokens);
		return null;
	}

	public Node extract(TokenRuleResponse response) {
		return response.run(tokens);
	}

	public ArrayList<Token> getTokens() {
		return tokens;
	}

	private class TokenRuleData {
		public static final int MODE_REQUIRE = 1, MODE_OPTIONAL = 2, MODE_ANY = 3;

		public int[] types;
		public String[] values;
		public int mode;
		public TokenRule rule;
		public boolean chain = false;

		public TokenRuleData(int[] types, String[] values, int mode) {
			this.types = types;
			this.values = values;
			this.mode = mode;
		}

		public TokenRuleData(TokenRule rule, int mode) {
			this.rule = rule;
			this.mode = mode;
			this.chain = true;
		}

		public boolean matches(Token token) {
			for (int i = 0; i < types.length; i++) {
				if (token.type == types[i] && (values[i].equals("") | token.value.equals(values[i]))) {
					return true;
				}
			}
			return false;
		}

	}

	public interface TokenRuleResponse {
		public Node run(ArrayList<Token> tokens);
	}

}
