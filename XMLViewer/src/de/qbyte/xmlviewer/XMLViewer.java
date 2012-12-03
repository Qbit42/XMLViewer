package de.qbyte.xmlviewer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.qbyte.xmlviewer.styler.LineStyler;

public class XMLViewer {

	/* ***** PROPERTIES ***** */

	private final Display		display		= new Display();
	private final Shell			shell;
	private final StyledText	textEditor;
	private final StringBuffer	text;

	/* ***** CONSTRUCTORS ***** */

	private XMLViewer() {

		// initialize shell
		this.shell = new Shell(this.display);
		this.shell.setSize(800, 600);
		this.shell.setText("XML Viewer");
		this.shell.setLayout(new FillLayout());

		// get text from file
		this.text = new StringBuffer();
		try {
			String path = System.getProperty("user.dir") + "/files/";
			String filename = "example01.xml";
			BufferedReader in = new BufferedReader(new FileReader(path + filename));
			String str;
			while ((str = in.readLine()) != null) {
				this.text.append(str).append("\n");
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create text editor
		this.textEditor = new StyledText(this.shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		this.textEditor.setFont(new Font(this.display, "Courier New", 9, SWT.NORMAL));
//		this.textEditor.addModifyListener(new ModifyStyler());
		this.textEditor.setText(this.text.toString());
		this.textEditor.addLineStyleListener(new LineStyler());

		// application loop
		this.shell.open();
		while (!this.shell.isDisposed())
			if (!this.display.readAndDispatch())
				this.display.sleep();
		this.display.dispose();
	}

	/* ***** MAIN ***** */

	public static void main(String[] args) {
		new XMLViewer();
	}

}
