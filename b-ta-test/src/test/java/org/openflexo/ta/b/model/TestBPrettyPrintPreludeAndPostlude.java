package org.openflexo.ta.b.model;
/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.parser.nodes.BComponentNode;
import org.openflexo.ta.b.model.parser.nodes.BExtendsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BImportsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BIncludesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BSeesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BUsesClauseNode;
import org.openflexo.toolbox.StringUtils;

public class TestBPrettyPrintPreludeAndPostlude extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestBPrettyPrintPreludeAndPostlude.class.getPackage().getName());

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	private void testPrettyPrint(BComponent component) {
		System.out.println("testPrettyPrint with " + component);

		// Test syntax-preserving pretty-print
		try {
			String prettyPrint = component.getBPrettyPrint();
			System.out.println("prettyPrint=\n" + prettyPrint);
			BComponent reparsedComponent = BParser.parse(prettyPrint, component.getResource().getFactory());
			reparsedComponent.setResource(component.getResource());
			// System.out.println("component=" + component);
			System.out.println("reparsedComponent=" + reparsedComponent);
			assertTrue(component.equalsObject(reparsedComponent));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Test normalized pretty-print
		try {
			String normalizedB = component.getNormalizedBRepresentation();
			System.out.println("normalizedB=\n" + normalizedB);
			BComponent reparsedComponent = BParser.parse(normalizedB, component.getResource().getFactory());
			reparsedComponent.setResource(component.getResource());
			// System.out.println("component=" + component);
			System.out.println("reparsedComponent=" + reparsedComponent);
			assertTrue(component.equalsObject(reparsedComponent));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void debug(P2PPNode<?, ?> node, int indentLevel) {
		System.out.println(StringUtils.buildWhiteSpaceIndentation(indentLevel * 2) + "> " + node.getClass().getSimpleName() + " "
				+ node.getLastParsedFragment() + " prelude=" + node.getPrelude() + " postlude=" + node.getPostlude());
		for (P2PPNode<?, ?> child : node.getChildren()) {
			debug(child, indentLevel + 1);
		}
	}

	@Test
	public void testVisibilityClauses() {

		BComponent component = getBComponent("AtelierB/Example1/TestVisibility.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System2", component.getName());

		assertEquals(2, component.getIncludesClauses().size());
		assertEquals("System1_i", component.getIncludesClauses().get(0).getReferencedComponentName());
		assertEquals("Full.Qualified.Machine", component.getIncludesClauses().get(1).getReferencedComponentName());

		assertEquals(1, component.getImportsClauses().size());
		assertEquals("System1", component.getImportsClauses().get(0).getReferencedComponentName());

		assertEquals(2, component.getSeesClauses().size());
		assertEquals("System1", component.getSeesClauses().get(0).getReferencedComponentName());
		assertEquals("System1_r", component.getSeesClauses().get(1).getReferencedComponentName());

		assertEquals(1, component.getExtendsClauses().size());
		assertEquals("System1", component.getExtendsClauses().get(0).getReferencedComponentName());

		assertEquals(2, component.getUsesClauses().size());
		assertEquals("System1", component.getUsesClauses().get(0).getReferencedComponentName());
		assertEquals("Full.Qualified.Machine", component.getUsesClauses().get(1).getReferencedComponentName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		// testPrettyPrint(component);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		assertEquals(rawSource.makeFragment(1, 0, 17, 3), rootNode.getLastParsedFragment());

		BIncludesClauseNode includes1 = (BIncludesClauseNode) rootNode.getChildren().get(0);
		assertEquals(rawSource.makeFragment(7, 1, 7, 10), includes1.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(5, 11, 7, 1), includes1.getPrelude());
		assertEquals(null, includes1.getPostlude());

		BIncludesClauseNode includes2 = (BIncludesClauseNode) rootNode.getChildren().get(1);
		assertEquals(rawSource.makeFragment(7, 12, 7, 34), includes2.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(7, 10, 7, 11), includes2.getPrelude());
		assertEquals(null, includes2.getPostlude());

		BSeesClauseNode sees1 = (BSeesClauseNode) rootNode.getChildren().get(2);
		assertEquals(rawSource.makeFragment(9, 1, 9, 8), sees1.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(7, 34, 9, 1), sees1.getPrelude());
		assertEquals(null, sees1.getPostlude());

		BSeesClauseNode sees2 = (BSeesClauseNode) rootNode.getChildren().get(3);
		assertEquals(rawSource.makeFragment(9, 9, 9, 18), sees2.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(9, 8, 9, 9), sees2.getPrelude());
		assertEquals(null, sees2.getPostlude());

		BImportsClauseNode import1 = (BImportsClauseNode) rootNode.getChildren().get(4);
		assertEquals(rawSource.makeFragment(11, 1, 11, 8), import1.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(9, 18, 11, 1), import1.getPrelude());
		assertEquals(null, import1.getPostlude());

		BExtendsClauseNode extends1 = (BExtendsClauseNode) rootNode.getChildren().get(5);
		assertEquals(rawSource.makeFragment(13, 1, 13, 8), extends1.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(11, 8, 13, 1), extends1.getPrelude());
		assertEquals(null, extends1.getPostlude());

		BUsesClauseNode uses1 = (BUsesClauseNode) rootNode.getChildren().get(6);
		assertEquals(rawSource.makeFragment(15, 1, 15, 8), uses1.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(13, 8, 15, 1), uses1.getPrelude());
		assertEquals(null, uses1.getPostlude());

		BUsesClauseNode uses2 = (BUsesClauseNode) rootNode.getChildren().get(7);
		assertEquals(rawSource.makeFragment(15, 9, 15, 31), uses2.getLastParsedFragment());
		assertEquals(rawSource.makeFragment(15, 8, 15, 9), uses2.getPrelude());
		assertEquals(null, uses2.getPostlude());
	}

}
