package de.qbyte.xmlviewer.styler;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.qbyte.xmlviewer.lexer.LineLexer;
import de.qbyte.xmlviewer.util.EToken;

public class LineStyler implements LineStyleListener {

	/* ***** PROPERTIES ***** */

	private final LineLexer	lexer	= new LineLexer();

	/* ***** METHODS ***** */

	@Override
	public void lineGetStyle(LineStyleEvent event) {

		Vector<StyleRange> styles = new Vector<StyleRange>();
		this.lexer.setRange(event.lineText);
		EToken token = this.lexer.nextToken();

		while (token != EToken.EOF) {
			if (token == EToken.OTHER || token == EToken.WHITESPACE) {
				// do nothing
			} else {
				StyleRange style = new StyleRange();
				style.start = this.lexer.getTokenStart() + event.lineOffset;
				style.length = this.lexer.getTokenLength();

				switch (token) {
					case ATTRIBUTE_EQUAL_SIGNS:
						style.foreground = new Color(Display.getCurrent(), 0, 0, 0);
						break;
					case ATTRIBUTE_NAMES:
						style.foreground = new Color(Display.getCurrent(), 127, 0, 127);
						break;
					case ATTRIBUTE_VALUES:
						style.foreground = new Color(Display.getCurrent(), 42, 0, 255);
						style.fontStyle = SWT.ITALIC;
						break;
					case CDATA_CONTENT:
						style.foreground = new Color(Display.getCurrent(), 0, 0, 0);
						break;
					case CDATA_DELIMITERS:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case COMMENT_CONTENT:
						style.foreground = new Color(Display.getCurrent(), 63, 95, 191);
						break;
					case COMMENT_DELIMITERS:
						style.foreground = new Color(Display.getCurrent(), 63, 95, 191);
						break;
					case CONTENT:
						style.foreground = new Color(Display.getCurrent(), 0, 0, 0);
						break;
					case DECLARATION_DELIMITERS:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case DOCTYPE_NAME:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case DOCTYPE_PUBLIC_REFERENCE:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case DOCTYPE_SYSTEM_REFERENCE:
						style.foreground = new Color(Display.getCurrent(), 63, 127, 95);
						break;
					case DOCTYPE_KEYWORD:
						style.foreground = new Color(Display.getCurrent(), 128, 128, 128);
						break;
					case ENTITY_REFERENCES:
						style.foreground = new Color(Display.getCurrent(), 42, 0, 255);
						break;
					case PROCESSING_INSTRUCTION_CONTENT:
						style.foreground = new Color(Display.getCurrent(), 0, 0, 0);
						break;
					case PROCESSING_INSTRUCTION_DELIMITERS:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case TAG_DELIMITERS:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					case TAG_NAMES:
						style.foreground = new Color(Display.getCurrent(), 0, 128, 128);
						break;
					default:
						break;
				}

				styles.addElement(style);
			}
			token = this.lexer.nextToken();
		}
		event.styles = new StyleRange[styles.size()];
		styles.copyInto(event.styles);
	}

	// private void highlight1(StyleRange style) {
	// style.foreground = new Color(Display.getCurrent(), 255, 0, 0);
	// style.background = new Color(Display.getCurrent(), 222, 222, 222);
	// style.fontStyle = SWT.BOLD;
	// }
	//
	// private void highlight2(StyleRange style) {
	// style.foreground = new Color(Display.getCurrent(), 0, 255, 0);
	// style.background = new Color(Display.getCurrent(), 222, 222, 222);
	// style.fontStyle = SWT.BOLD;
	// }
	//
	// private void highlight3(StyleRange style) {
	// style.foreground = new Color(Display.getCurrent(), 0, 0, 255);
	// style.background = new Color(Display.getCurrent(), 222, 222, 222);
	// style.fontStyle = SWT.BOLD;
	// }
	//
	// private void highlight4(StyleRange style) {
	// style.background = new Color(Display.getCurrent(), 200, 200, 255);
	// style.fontStyle = SWT.BOLD;
	// }

}
