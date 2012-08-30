package de.qbyte.xmlviewer.lexer;

import java.util.Stack;

import de.qbyte.xmlviewer.util.EContext;
import de.qbyte.xmlviewer.util.EToken;

public class ModifyLexer {

	/* ***** CONSTANTS ***** */

	private static final int	EOL	= -1;
	private static final int	EOF	= -2;

	/* ***** PROPERTIES ***** */

	private String				range;
	private int					position;
	private int					rangeEnd;
	private int					tokenStart;
	private int					c;
	private Stack<EContext>		context;

	/* ***** CONSTRUCTORS ***** */

	public ModifyLexer() {
		this.context = new Stack<EContext>();
		this.context.push(EContext.DOCUMENT);
	}

	/* ***** METHODS ***** */

	public void setRange(String range) {
		this.range = range;
		this.position = 0;
		this.rangeEnd = this.range.length() - 1;
	}

	public EToken nextToken() {
		this.tokenStart = this.position;
		read(1);

		// EOF
		if (this.c == EOF) {
			return EToken.EOF;
		}

		// ATTRIBUTE_EQUAL_SIGN
		if (this.c == '=' && (this.context.peek() == EContext.TAG_OPEN || this.context.peek() == EContext.PROCESSING_INSTRUCTION)) {
			return EToken.ATTRIBUTE_EQUAL_SIGNS;
		}

		// ATTRIBUTE_NAMES
		if (Character.isLetter(this.c)
				&& (this.context.peek() == EContext.TAG_OPEN || this.context.peek() == EContext.PROCESSING_INSTRUCTION)) {
			for (int i = 1; i <= this.rangeEnd; i++) {
				if (next(i) == '=') {
					read(i - 1);
					return EToken.ATTRIBUTE_NAMES;
				}
			}
		}

		// ATTRIBUTE_VALUES
		if (this.c == '"' && (this.context.peek() == EContext.TAG_OPEN || this.context.peek() == EContext.PROCESSING_INSTRUCTION)) {
			for (;;) {
				read(1);
				if (this.c == EOF || this.c == '"')
					return EToken.ATTRIBUTE_VALUES;
			}
		}

		// CDATA_CONTENT
		if (this.context.peek() == EContext.CONTENT && this.context.contains(EContext.CDATA)) {
			for (;;) {
				if (next(1) == EOF || findNext("]]>")) {
					this.context.pop();
					return EToken.CDATA_CONTENT;
				}
				read(1);
			}
		}

		// CDATA_DELIMITERS
		if (this.c == '<' && (this.context.peek() == EContext.DOCUMENT || this.context.peek() == EContext.CONTENT)) {
			if (findNext("![CDATA[")) {
				read(8);
				this.context.push(EContext.CDATA);
				this.context.push(EContext.CONTENT);
				return EToken.CDATA_DELIMITERS;
			}
		}
		if (this.c == ']' && this.context.peek() == EContext.CDATA) {
			if (findNext("]>")) {
				read(2);
				this.context.pop();
				return EToken.CDATA_DELIMITERS;
			}
		}

		// COMMENT_CONTENT
		if (this.context.peek() == EContext.CONTENT && this.context.contains(EContext.COMMENT)) {
			for (;;) {
				if (next(1) == EOF || findNext("-->")) {
					this.context.pop();
					return EToken.COMMENT_CONTENT;
				}
				read(1);
			}
		}

		// COMMENT_DELIMITERS
		if (this.c == '<' && (this.context.peek() == EContext.DOCUMENT || this.context.peek() == EContext.CONTENT)) {
			if (findNext("!--")) {
				read(3);
				this.context.push(EContext.COMMENT);
				this.context.push(EContext.CONTENT);
				return EToken.COMMENT_DELIMITERS;
			}
		}
		if (this.c == '-' && this.context.peek() == EContext.COMMENT) {
			if (findNext("->")) {
				read(2);
				this.context.pop();
				return EToken.COMMENT_DELIMITERS;
			}
		}

		// CONTENT
		if (this.c == 'x') {
		}

		// DECLARATION_DELIMITERS
		if (this.c == '<' && this.context.peek() == EContext.DOCUMENT) {
			if (findNext("!") && !findNext("!--") && !findNext("![CDATA[")) {
				read(1);
				this.context.push(EContext.DECLARATION);
				this.context.push(EContext.NAME);
				return EToken.DECLARATION_DELIMITERS;
			}
		}
		if (this.c == '>' && this.context.peek() == EContext.DECLARATION) {
			this.context.pop();
			return EToken.DECLARATION_DELIMITERS;
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
		if (this.c == '&' && this.context.peek() == EContext.CONTENT) {
			for (;;) {
				read(1);
				if (this.c == ';' || this.c == EOF)
					return EToken.ENTITY_REFERENCES;
			}
		}

		// PROCESSING_INSTRUCTION_CONTENT
		if (this.c == 'x') {
		}

		// PROCESSING_INSTRUCTION_DELIMITERS
		if (this.c == '<' && this.context.peek() == EContext.DOCUMENT) {
			if (findNext("?")) {
				read(1);
				this.context.push(EContext.PROCESSING_INSTRUCTION);
				this.context.push(EContext.NAME);
				return EToken.PROCESSING_INSTRUCTION_DELIMITERS;
			}
		}
		if (this.c == '?' && this.context.peek() == EContext.PROCESSING_INSTRUCTION) {
			if (findNext(">")) {
				read(1);
				this.context.pop();
				return EToken.PROCESSING_INSTRUCTION_DELIMITERS;
			}
		}

		// TAG_DELIMITERS
		if (this.c == '<' && (this.context.peek() == EContext.DOCUMENT || this.context.peek() == EContext.CONTENT)) {
			if (findNext("/")) {
				read(1);
				this.context.push(EContext.TAG_CLOSE);
				this.context.push(EContext.NAME);
				return EToken.TAG_DELIMITERS;
			}
			if (Character.isLetter(next(1))) {
				this.context.push(EContext.TAG_OPEN);
				this.context.push(EContext.NAME);
				return EToken.TAG_DELIMITERS;
			}
		}
		if (this.c == '/' && this.context.peek() == EContext.TAG_OPEN) {
			if (findNext(">")) {
				read(1);
				this.context.pop();
				return EToken.TAG_DELIMITERS;
			}
		}
		if (this.c == '>' && (this.context.peek() == EContext.TAG_OPEN || this.context.peek() == EContext.TAG_CLOSE)) {
			if (this.context.peek() == EContext.TAG_OPEN) {
				this.context.pop();
				this.context.push(EContext.CONTENT);
			}
			if (this.context.peek() == EContext.TAG_CLOSE)
				this.context.pop();
			return EToken.TAG_DELIMITERS;
		}

		// TAG_NAMES
		if (Character.isLetter(this.c)
				&& this.context.peek() == EContext.NAME
				&& (this.context.contains(EContext.TAG_OPEN) || this.context.contains(EContext.TAG_CLOSE)
						|| this.context.contains(EContext.PROCESSING_INSTRUCTION) || this.context.contains(EContext.DECLARATION))) {
			for (int i = 1; i <= this.rangeEnd; i++) {
				if (next(i) == EOF || Character.isWhitespace(next(i)) || next(i) == '>') {
					this.context.pop();
					read(i - 1);
					return EToken.TAG_NAMES;
				}
			}
		}

		// WHITESPACE
		if (Character.isWhitespace(this.c)) {
			do {
				read(1);
			} while (Character.isWhitespace(this.c));
			unread();
			return EToken.WHITESPACE;
		}

		// OTHER
		return EToken.OTHER;
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
