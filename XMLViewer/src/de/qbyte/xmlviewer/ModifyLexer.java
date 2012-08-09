package de.qbyte.xmlviewer;

import java.util.Stack;

public class ModifyLexer {

	/* ***** CONSTANTS ***** */

	private static final int	EOF	= -1;

	/* ***** PROPERTIES ***** */

	private String				range;
	private int					position;
	private int					rangeEnd;
	private int					tokenStart;
	private int					c;
	private Stack<Context>		context;

	/* ***** CONSTRUCTORS ***** */

	public ModifyLexer() {
		this.context = new Stack<Context>();
		this.context.push(Context.DOCUMENT);
	}

	/* ***** METHODS ***** */

	public void setRange(String range) {
		this.range = range;
		this.position = 0;
		this.rangeEnd = this.range.length() - 1;
	}

	public Token nextToken() {
		this.tokenStart = this.position;
		read(1);

		// EOF
		if (this.c == EOF) {
			return Token.EOF;
		}

		// ATTRIBUTE_EQUAL_SIGN
		if (this.c == '=' && (this.context.peek() == Context.TAG_OPEN || this.context.peek() == Context.PROCESSING_INSTRUCTION)) {
			return Token.ATTRIBUTE_EQUAL_SIGNS;
		}

		// ATTRIBUTE_NAMES
		if (Character.isLetter(this.c)
				&& (this.context.peek() == Context.TAG_OPEN || this.context.peek() == Context.PROCESSING_INSTRUCTION)) {
			for (int i = 1; i <= this.rangeEnd; i++) {
				if (next(i) == '=') {
					read(i - 1);
					return Token.ATTRIBUTE_NAMES;
				}
			}
		}

		// ATTRIBUTE_VALUES
		if (this.c == '"' && (this.context.peek() == Context.TAG_OPEN || this.context.peek() == Context.PROCESSING_INSTRUCTION)) {
			for (;;) {
				read(1);
				if (this.c == '"' || this.c == EOF)
					return Token.ATTRIBUTE_VALUES;
			}
		}

		// CDATA_CONTENT
		if (this.context.peek() == Context.CONTENT && this.context.contains(Context.CDATA)) {
			for (;;) {
				if (next(1) == EOF || findNext("]]>")) {
					this.context.pop();
					return Token.CDATA_CONTENT;
				}
				read(1);
			}
		}

		// CDATA_DELIMITERS
		if (this.c == '<' && (this.context.peek() == Context.DOCUMENT || this.context.peek() == Context.CONTENT)) {
			if (findNext("![CDATA[")) {
				read(8);
				this.context.push(Context.CDATA);
				this.context.push(Context.CONTENT);
				return Token.CDATA_DELIMITERS;
			}
		}
		if (this.c == ']' && this.context.peek() == Context.CDATA) {
			if (findNext("]>")) {
				read(2);
				this.context.pop();
				return Token.CDATA_DELIMITERS;
			}
		}

		// COMMENT_CONTENT
		if (this.context.peek() == Context.CONTENT && this.context.contains(Context.COMMENT)) {
			for (;;) {
				if (next(1) == EOF || findNext("-->")) {
					this.context.pop();
					return Token.COMMENT_CONTENT;
				}
				read(1);
			}
		}

		// COMMENT_DELIMITERS
		if (this.c == '<' && (this.context.peek() == Context.DOCUMENT || this.context.peek() == Context.CONTENT)) {
			if (findNext("!--")) {
				read(3);
				this.context.push(Context.COMMENT);
				this.context.push(Context.CONTENT);
				return Token.COMMENT_DELIMITERS;
			}
		}
		if (this.c == '-' && this.context.peek() == Context.COMMENT) {
			if (findNext("->")) {
				read(2);
				this.context.pop();
				return Token.COMMENT_DELIMITERS;
			}
		}

		// CONTENT
		if (this.c == 'x') {
		}

		// DECLARATION_DELIMITERS
		if (this.c == '<' && this.context.peek() == Context.DOCUMENT) {
			if (findNext("!") && !findNext("!--") && !findNext("![CDATA[")) {
				read(1);
				this.context.push(Context.DECLARATION);
				this.context.push(Context.NAME);
				return Token.DECLARATION_DELIMITERS;
			}
		}
		if (this.c == '>' && this.context.peek() == Context.DECLARATION) {
			this.context.pop();
			return Token.DECLARATION_DELIMITERS;
		}

		// DOCTYPE_NAME
		if (this.c == 'x') {
		}

		// DOCTYPE_PUBLIC_REFERENCE
		if (this.c == 'x') {
		}

		// DOCTYPE_SYSTEM_REFERENCE
		if (this.c == 'x') {
		}

		// DOCTYPE_KEYWORD
		if (this.c == 'x') {
		}

		// ENTITY_REFERENCES
		if (this.c == '&' && this.context.peek() == Context.CONTENT) {
			for (;;) {
				read(1);
				if (this.c == ';' || this.c == EOF)
					return Token.ENTITY_REFERENCES;
			}
		}

		// PROCESSING_INSTRUCTION_CONTENT
		if (this.c == 'x') {
		}

		// PROCESSING_INSTRUCTION_DELIMITERS
		if (this.c == '<' && this.context.peek() == Context.DOCUMENT) {
			if (findNext("?")) {
				read(1);
				this.context.push(Context.PROCESSING_INSTRUCTION);
				this.context.push(Context.NAME);
				return Token.PROCESSING_INSTRUCTION_DELIMITERS;
			}
		}
		if (this.c == '?' && this.context.peek() == Context.PROCESSING_INSTRUCTION) {
			if (findNext(">")) {
				read(1);
				this.context.pop();
				return Token.PROCESSING_INSTRUCTION_DELIMITERS;
			}
		}

		// TAG_DELIMITERS
		if (this.c == '<' && (this.context.peek() == Context.DOCUMENT || this.context.peek() == Context.CONTENT)) {
			if (findNext("/")) {
				read(1);
				this.context.push(Context.TAG_CLOSE);
				this.context.push(Context.NAME);
				return Token.TAG_DELIMITERS;
			}
			if (Character.isLetter(next(1))) {
				this.context.push(Context.TAG_OPEN);
				this.context.push(Context.NAME);
				return Token.TAG_DELIMITERS;
			}
		}
		if (this.c == '/' && this.context.peek() == Context.TAG_OPEN) {
			if (findNext(">")) {
				read(1);
				this.context.pop();
				return Token.TAG_DELIMITERS;
			}
		}
		if (this.c == '>' && (this.context.peek() == Context.TAG_OPEN || this.context.peek() == Context.TAG_CLOSE)) {
			if (this.context.peek() == Context.TAG_OPEN) {
				this.context.pop();
				this.context.push(Context.CONTENT);
			}
			if (this.context.peek() == Context.TAG_CLOSE)
				this.context.pop();
			return Token.TAG_DELIMITERS;
		}

		// TAG_NAMES
		if (Character.isLetter(this.c)
				&& this.context.peek() == Context.NAME
				&& (this.context.contains(Context.TAG_OPEN) || this.context.contains(Context.TAG_CLOSE)
						|| this.context.contains(Context.PROCESSING_INSTRUCTION) || this.context.contains(Context.DECLARATION))) {
			for (int i = 1; i <= this.rangeEnd; i++) {
				if (next(i) == EOF || Character.isWhitespace(next(i)) || next(i) == '>') {
					this.context.pop();
					read(i - 1);
					return Token.TAG_NAMES;
				}
			}
		}

		// WHITESPACE
		if (Character.isWhitespace(this.c)) {
			do {
				read(1);
			} while (Character.isWhitespace(this.c));
			unread();
			return Token.WHITESPACE;
		}

		// OTHER
		return Token.OTHER;
	}

	public int getTokenStart() {
		return this.tokenStart;
	}

	public int getTokenLength() {
		return this.position - this.tokenStart;
	}

	/* ***** HELPER ***** */

	private void read(int count) {
		count--;
		if (this.position + count <= this.rangeEnd) {
			this.c = this.range.charAt(this.position + count);
			this.position += count + 1;
		} else
			this.c = EOF;
	}

	private void unread() {
		if (this.position <= this.rangeEnd) {
			this.c = this.range.charAt(this.position-- - 2);
		} else
			this.c = EOF;
	}

	private int next(int count) {
		count--;
		if (this.position + count <= this.rangeEnd)
			return this.range.charAt(this.position + count);

		return EOF;
	}

	private boolean findNext(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != this.range.charAt(this.position + i))
				return false;
		}
		return true;
	}
}
