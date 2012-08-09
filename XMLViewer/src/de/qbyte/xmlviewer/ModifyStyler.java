package de.qbyte.xmlviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class ModifyStyler implements ModifyListener {

	@Override
	public void modifyText(ModifyEvent event) {

		StyledText textEditor = ((StyledText) event.widget);
		ModifyLexer lexer = new ModifyLexer();
		lexer.setRange(textEditor.getText());
		
		Token token = lexer.nextToken();
		while (token != Token.EOF) {
			if (token == Token.OTHER || token == Token.WHITESPACE) {
				// do nothing
			} else {
				StyleRange style = new StyleRange();
				style.start = lexer.getTokenStart();
				style.length = lexer.getTokenLength();

				switch (token) {
					case ATTRIBUTE_EQUAL_SIGNS:
						style.foreground = new Color(Display.getCurrent(), 0, 0, 0);
						break;
					case ATTRIBUTE_NAMES:
						style.foreground = new Color(Display.getCurrent(), 127, 0, 127);
						style.background = new Color(Display.getCurrent(), 222, 222, 222);
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

				textEditor.setStyleRange(style);
			}
			token = lexer.nextToken();
		}
	}

}
