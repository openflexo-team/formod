package org.openflexo.ta.b.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

public class TestIHM {

	public static final String display = "/* System1_i\n" + "* Author: \n" + "* Creation date: 12/12/2018\n" + "*/\n" + "\n"
			+ "IMPLEMENTATION System1_i\n" + "REFINES System1_r\n" + "\n" + " END\n";

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		JFrame frame = new JFrame();

		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/b", "org.openflexo.ta.b.view.BTokenMaker");

		Class<SyntaxConstants> clazz = SyntaxConstants.class;

		/*JTabbedPane tabbedPane = new JTabbedPane();
		
		for (Field field : clazz.getFields()) {
			System.out.println("> " + field.getName() + " value: " + field.get(clazz));
			RSyntaxTextArea rTA = new RSyntaxTextArea(display);
			String style = (String) field.get(clazz);
			rTA.setSyntaxEditingStyle(style);
			tabbedPane.add(style, rTA);
		}
		
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);*/

		RSyntaxTextArea rTA = new RSyntaxTextArea(display);
		rTA.setSyntaxEditingStyle("text/b");
		frame.getContentPane().add(rTA, BorderLayout.CENTER);

		frame.validate();
		frame.pack();
		frame.show();
	}

}
