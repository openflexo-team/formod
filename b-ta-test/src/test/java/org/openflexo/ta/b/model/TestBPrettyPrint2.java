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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.parser.nodes.BComponentNode;
import org.openflexo.ta.b.model.parser.nodes.BSetNode;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.StringUtils;

@RunWith(OrderedRunner.class)
public class TestBPrettyPrint2 extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestBPrettyPrint2.class.getPackage().getName());

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	private void debug(P2PPNode<?, ?> node, int indentLevel) {
		System.out.println(StringUtils.buildWhiteSpaceIndentation(indentLevel * 2) + "> " + node.getClass().getSimpleName() + " "
				+ node.getLastParsedFragment() + " prelude=" + node.getPrelude() + " postlude=" + node.getPostlude() + " contents="
				+ node.getRegisteredForContents());
		for (P2PPNode<?, ?> child : node.getChildren()) {
			debug(child, indentLevel + 1);
		}
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
			assertTrue("Objects are not equals after pretty-print", component.equalsObject(reparsedComponent));
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
			assertTrue("Objects are not equals after normalized pretty-print", component.equalsObject(reparsedComponent));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private static BComponent component;

	@Test
	@TestOrder(1)
	public void loadComponent() {

		log("loadComponent()");

		component = getBComponent("AtelierB/Example1/TestSets.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System3", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertEquals(3, component.getSets().size());

		testPrettyPrint(component);

		BSet newSet = component.getResource().getFactory().makeSet("TEST_SET");
		BSetValue value1 = component.getResource().getFactory().makeSetValue("Value1");
		BSetValue value2 = component.getResource().getFactory().makeSetValue("Value2");
		newSet.addToEnumeratedValues(value1);
		newSet.addToEnumeratedValues(value2);
		component.addToSets(newSet);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		testPrettyPrint(component);

		BSetValue value3 = component.getResource().getFactory().makeSetValue("Value3");
		component.getSets().get(2).addToEnumeratedValues(value3);
		testPrettyPrint(component);

		component.getSets().get(2).getEnumeratedValues().get(1).setName("anOtherValue");
		testPrettyPrint(component);

		BSetValue value4 = component.getResource().getFactory().makeSetValue("Value4");
		component.getSets().get(0).addToEnumeratedValues(value4);
		testPrettyPrint(component);

		BSetValue value5 = component.getResource().getFactory().makeSetValue("Value5");
		component.getSets().get(0).addToEnumeratedValues(value5);
		testPrettyPrint(component);

		BSetValue value6 = component.getResource().getFactory().makeSetValue("Value6");
		component.getSets().get(0).addToEnumeratedValues(value6);
		component.getSets().get(0).moveEnumeratedValueToIndex(value6, 0);
		testPrettyPrint(component);

		BSetValue value7 = component.getResource().getFactory().makeSetValue("Value7");
		component.getSets().get(0).addToEnumeratedValues(value7);
		component.getSets().get(0).moveEnumeratedValueToIndex(value7, 2);
		testPrettyPrint(component);

		component.getSets().get(0).removeFromEnumeratedValues(value7);
		testPrettyPrint(component);

		component.getSets().get(2).removeFromEnumeratedValues(component.getSets().get(2).getEnumeratedValues().get(0));
		testPrettyPrint(component);

		component.getSets().get(2).removeFromEnumeratedValues(component.getSets().get(2).getEnumeratedValues().get(1));
		testPrettyPrint(component);

		component.getSets().get(2).removeFromEnumeratedValues(component.getSets().get(2).getEnumeratedValues().get(0));
		testPrettyPrint(component);

		component.removeFromSets(component.getSets().get(2));
		testPrettyPrint(component);

		component.removeFromSets(component.getSets().get(0));
		testPrettyPrint(component);
	}

	private static BSet set1;
	private static BSet set2;
	private static BSet set3;

	@Test
	@TestOrder(2)
	public void changeTextualValue() throws ParseException {

		log("changeTextualValue()");

		String newValue = "SYSTEM" + "\n" + "System3" + "\n" + "SETS" + "\n" + "LOCATION; // Comments for the first set" + "\n"
				+ "TEST_SET = {Value1, Value2} // Comments for the set2" + "\n" + "END";
		component.getResource().updateWithRawSource(newValue);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		testPrettyPrint(component);

		set1 = component.getSets().get(0);
		set2 = component.getSets().get(1);

		assertTrue(rootNode.getChildren().get(0) instanceof BSetNode);
		assertTrue(rootNode.getChildren().get(1) instanceof BSetNode);

		assertSame(set1, rootNode.getChildren().get(0).getModelObject());
		assertSame(set2, rootNode.getChildren().get(1).getModelObject());

		assertEquals(2, component.getSets().size());
		assertEquals("LOCATION", set1.getName());
		assertEquals("TEST_SET", set2.getName());
		assertEquals(2, set2.getEnumeratedValues().size());

	}

	@Test
	@TestOrder(3)
	public void programmaticallyEditB() throws ParseException {

		log("programmaticallyEditB()");

		set3 = component.getFactory().makeSet("AThirdSet");
		component.addToSets(set3);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		testPrettyPrint(component);

		assertEquals(3, component.getSets().size());
		assertEquals("LOCATION", set1.getName());
		assertEquals("TEST_SET", set2.getName());
		assertEquals(2, set2.getEnumeratedValues().size());
		assertEquals("AThirdSet", set3.getName());

	}

	@Test
	@TestOrder(4)
	public void updateSetName() throws ParseException {

		log("updateSetName()");

		String newValue = "SYSTEM" + "\n" + "System3" + "\n" + "SETS" + "\n" + "RENAMED_LOCATION; // Comments for the first set" + "\n"
				+ "TEST_SET = {Value1, Value2}; // Comments for the set2" + "\n" + "AThirdSet" + "\n" + "END";
		component.getResource().updateWithRawSource(newValue);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		testPrettyPrint(component);

		BSet newSet1 = component.getSets().get(0);
		BSet newSet2 = component.getSets().get(1);
		BSet newSet3 = component.getSets().get(2);

		assertSame(set1, rootNode.getChildren().get(0).getModelObject());
		assertSame(set2, rootNode.getChildren().get(1).getModelObject());
		assertSame(set3, rootNode.getChildren().get(2).getModelObject());

		assertSame(set1, newSet1);
		assertSame(set2, newSet2);
		assertSame(set3, newSet3);

		assertEquals("RENAMED_LOCATION", set1.getName());

	}

	@Test
	@TestOrder(5)
	public void addSetValues() throws ParseException {

		log("updateSetName()");

		String newValue = "SYSTEM" + "\n" + "System3" + "\n" + "SETS" + "\n" + "RENAMED_LOCATION; // Comments for the first set" + "\n"
				+ "TEST_SET = {Value1, Value2, Value3}; // Comments for the set2" + "\n" + "AThirdSet" + "\n" + "END";
		component.getResource().updateWithRawSource(newValue);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());

		debug(rootNode, 0);

		testPrettyPrint(component);

		BSet newSet2 = component.getSets().get(1);
		assertSame(set2, rootNode.getChildren().get(1).getModelObject());
		assertSame(set2, newSet2);
		assertEquals(3, set2.getEnumeratedValues().size());

	}

}
