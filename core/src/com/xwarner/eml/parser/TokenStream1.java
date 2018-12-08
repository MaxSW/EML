package com.xwarner.eml.parser;

import com.xwarner.eml.parser.tokens.Token;
import com.xwarner.eml.parser.tokens.TokenMatcher;
import com.xwarner.eml.tools.ErrorHandler;

/**
 * Converts the input stream into a stream of tokens
 * 
 * @author max
 *
 */

public class TokenStream1 extends TokenStream {

	private InputStream input;

	public ErrorHandler error;

	public TokenStream1(InputStream input) {
		this.input = input;
		this.error = input.error;
	}

	protected Token readNext() {
		// skip any whitespace
		readWhile(isWhitespace);
		// finish if empty
		if (input.done())
			return null;
		char c = input.peek();
		if (c == '/') {
			input.next();
			char c2 = input.peek();
			if (c2 == '/') {
				// inline comment
				readWhile(isntNewline);
				return readNext();
			} else if (c2 == '*') {
				// multi-line comment
				if (input.done())
					return null;
				char c3 = input.next();
				while (true) {
					if (c3 == '*') {
						if (input.done())
							return null;
						if (input.peek() == '/') {
							input.next();
							return readNext();
						} else {
							c3 = input.next();
						}
					} else {
						if (input.done())
							return null;
						c3 = input.next();
					}
				}
			} else {
				// a division operator
				return new Token(Token.OPERATOR, "/" + readWhile(isOperator), input.line());
			}
		} else if (isAssignment.match(c)) {
			input.next();
			char cc = input.peek();
			if (isAssignment.match(cc)) {
				input.next();
				return new Token(Token.OPERATOR, "==", input.line());
			} else
				return new Token(Token.ASSIGNMENT, "=", input.line());
		} else if (isDigit.match(c)) {
			return new Token(Token.NUMBER, readNumber(), input.line());
		} else if (isNameStart.match(c)) {
			return readName();
		} else if (isPunctuation.match(c)) {
			return new Token(Token.PUNCTUATION, Character.toString(input.next()), input.line());
		} else if (isOperator.match(c)) {
			return new Token(Token.OPERATOR, readWhile(isOperator), input.line());
		} else if (c == '"') {
			return readString();
		} else if (isNewline.match(c)) {
			input.next();
			// skip multiple last lines
			if (last != null && last.type != Token.NEWLINE)
				return new Token(Token.NEWLINE, "", input.line());
			else
				return readNext();
		}

		return null;
	}

	private String readWhile(TokenMatcher matcher) {
		String str = "";
		while (!input.done() && matcher.match(input.peek())) {
			str += input.next();
		}
		return str;
	}

	private String readNumber() {
		boolean decimal = false;
		String str = "";
		while (!input.done()) {
			char c = input.peek();
			if (c == '.') {
				if (decimal)
					return str;
				decimal = true;
				str += input.next();
			} else {
				if (isDigit.match(c))
					str += input.next();
				else
					return str;
			}
		}
		return str;
	}

	private Token readName() {
		String id = readWhile(isName);
		int type = isKeyword(id) ? Token.KEYWORD : Token.NAME;
		return new Token(type, id, input.line());
	}

	private Token readString() {
		boolean escaped = false;
		String str = "";
		input.next(); // consume "
		while (!input.done()) {
			char c = input.next();
			if (escaped) {
				if (c == '"')
					str += c;
				else if (c == 'n')
					str += '\n';
				else if (c == 't')
					str += '\t';
				else if (c == '\\')
					str += '\\';
				escaped = false;
			} else if (c == '\\') {
				escaped = true;
			} else if (c == '"') {
				break;
			} else {
				str += c;
			}
		}
		return new Token(Token.STRING, str, input.line());
	}

	/* Matchers */

	String[] keywords = { "var", "const", "str", "bool", "func", "true", "false", "if", "else", "while", "for",
			"return", "class", "new", "obj", "arr", "break", "continue", "vec", "mat" };

	private boolean isKeyword(String str) {
		for (int i = 0; i < keywords.length; i++) {
			if (str.equals(keywords[i]))
				return true;
		}
		return false;
	}

	private TokenMatcher isWhitespace = new TokenMatcher() {
		public boolean match(char c) {
			return " \t".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isPunctuation = new TokenMatcher() {
		public boolean match(char c) {
			return "{}(),;.[]:".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isDigit = new TokenMatcher() {
		public boolean match(char c) {
			return Character.isDigit(c);
		}
	};

	private TokenMatcher isOperator = new TokenMatcher() {
		public boolean match(char c) {
			return "+-*/^|&<>!".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isAssignment = new TokenMatcher() {
		public boolean match(char c) {
			return "=".indexOf(c) >= 0;
		}
	};

	private TokenMatcher isntNewline = new TokenMatcher() {
		public boolean match(char c) {
			return c != '\n';
		}
	};

	private TokenMatcher isName = new TokenMatcher() {
		public boolean match(char c) {
			int type = Character.getType(c);
			return (type == Character.LOWERCASE_LETTER) || (type == Character.UPPERCASE_LETTER)
					|| (type == Character.DECIMAL_DIGIT_NUMBER) || c == '_';
		}
	};

	// names must start with a lowercase letter or a _
	private TokenMatcher isNameStart = new TokenMatcher() {
		public boolean match(char c) {
			return Character.getType(c) == Character.LOWERCASE_LETTER || c == '_';
		}
	};
	private TokenMatcher isNewline = new TokenMatcher() {
		public boolean match(char c) {
			return "\n".indexOf(c) >= 0;
		}
	};

}
