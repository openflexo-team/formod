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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;
import org.openflexo.pamela.ModelProperty;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.expression.BBooleanSetExpression;
import org.openflexo.ta.b.model.expression.BIdentifierExpression;
import org.openflexo.ta.b.model.expression.BIntSetExpression;
import org.openflexo.ta.b.model.expression.BNatSetExpression;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.parser.nodes.BComponentNode;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.predicate.BLessPredicate;
import org.openflexo.ta.b.model.predicate.BMemberPredicate;
import org.openflexo.ta.b.model.predicate.BSubsetPredicate;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.toolbox.StringUtils;

public class TestBPrettyPrint extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestBPrettyPrint.class.getPackage().getName());

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

	private boolean considerPropertyForEquality(ModelProperty<?> property) {
		if (property.getPropertyIdentifier().contentEquals("prettyPrintDelegate")) {
			return false;
		}
		return true;
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
			assertTrue("Objects are not equals after pretty-print",
					component.equalsObject(reparsedComponent, (property) -> considerPropertyForEquality(property)));
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
			assertTrue("Objects are not equals after normalized pretty-print",
					component.equalsObject(reparsedComponent, (property) -> considerPropertyForEquality(property)));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testMachineNoComments() {

		BComponent component = getBComponent("AtelierB/Example1/MachineNoComments.mch");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BMachine);
		assertEquals("Full.Qualified.Machine", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		testPrettyPrint(component);

		component.setName("A.New.Name");
		testPrettyPrint(component);

		component.setName("Full.Qualified.Machine");
		testPrettyPrint(component);
	}

	@Test
	public void testMachine() {

		BComponent component = getBComponent("AtelierB/Example1/Machine.mch");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BMachine);
		assertEquals("Full.Qualified.Machine", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		testPrettyPrint(component);

		component.setName("A.New.Name");
		testPrettyPrint(component);

		component.setName("Full.Qualified.Machine");
		testPrettyPrint(component);
	}

	@Test
	public void testSystem() {

		BComponent component = getBComponent("AtelierB/Example1/System1.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System1", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		testPrettyPrint(component);

		component.setName("System2");
		testPrettyPrint(component);

		component.setName("System1");
		testPrettyPrint(component);

	}

	@Test
	public void testRefinement() {

		BComponent component = getBComponent("AtelierB/Example1/System1_r.ref");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BRefinement);
		assertEquals("System1_r", component.getName());
		assertEquals("System1", ((BRefinement) component).getRefinesComponent().getComponentName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		testPrettyPrint(component);

		component.setName("System2_r");
		testPrettyPrint(component);

		component.setName("System1_r");
		testPrettyPrint(component);

	}

	@Test
	public void testImplementation() {

		BComponent component = getBComponent("AtelierB/Example1/System1_i.imp");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BImplementation);
		assertEquals("System1_i", component.getName());
		assertEquals("System1_r", ((BImplementation) component).getRefinesComponent().getComponentName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		testPrettyPrint(component);

		component.setName("System2_i");
		testPrettyPrint(component);

		component.setName("System1_i");
		testPrettyPrint(component);
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

		testPrettyPrint(component);

		// Retrieve a B resource
		BResource system1R = component.getResource().getFactory().getResourceWithName("System1_r");
		assertNotNull(system1R);

		// Modify an import
		log("Modify IMPORTS clause");
		component.getImportsClauses().get(0).setReferencedComponent(system1R);
		testPrettyPrint(component);

		// Add an extends
		log("Add EXTENDS clause");
		component.addToExtendsClauses(component.getResource().getFactory().makeExtendsClause(system1R));
		testPrettyPrint(component);

		// Removes last includes
		log("Remove last INCLUDES");
		component.removeFromIncludesClauses(component.getIncludesClauses().get(1));
		testPrettyPrint(component);

		// Removes first uses
		log("Remove first USES");
		component.removeFromUsesClauses(component.getUsesClauses().get(0));
		testPrettyPrint(component);

	}

	@Test
	public void testSets() {

		BComponent component = getBComponent("AtelierB/Example1/TestSets.sys");
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

	@Test
	public void testVariablesAndConstants() {

		BComponent component = getBComponent("AtelierB/Example1/TestVariablesAndConstants.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System4", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertEquals(2, component.getAbstractConstants().size());
		assertEquals("MaxNumber", component.getAbstractConstants().get(0).getName());
		assertEquals("MinNumber", component.getAbstractConstants().get(1).getName());
		assertEquals(1, component.getConcreteConstants().size());
		assertEquals("PosMin", component.getConcreteConstants().get(0).getName());
		assertEquals(3, component.getAbstractVariables().size());
		assertEquals("Abscisse", component.getAbstractVariables().get(0).getName());
		assertEquals("Ordinate0", component.getAbstractVariables().get(1).getName());
		assertEquals("Ordinate1", component.getAbstractVariables().get(2).getName());
		assertEquals(1, component.getConcreteVariables().size());
		assertEquals("Position", component.getConcreteVariables().get(0).getName());

		testPrettyPrint(component);

	}

	@Test
	public void testProperties() {

		BComponent component = getBComponent("AtelierB/Example1/TestProperties.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System5", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertNotNull(component.getProperties());
		assertTrue(component.getProperties() instanceof BConjunctPredicate);
		BConjunctPredicate p1 = (BConjunctPredicate) component.getProperties();
		assertTrue(p1.getLeft() instanceof BMemberPredicate);
		assertTrue(p1.getRight() instanceof BMemberPredicate);
		BMemberPredicate p2 = (BMemberPredicate) p1.getLeft();
		BMemberPredicate p3 = (BMemberPredicate) p1.getRight();
		assertTrue(p2.getLeft() instanceof BIdentifierExpression);
		assertTrue(p2.getRight() instanceof BIntSetExpression);
		assertTrue(p3.getLeft() instanceof BIdentifierExpression);
		assertTrue(p3.getRight() instanceof BNatSetExpression);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());
		debug(rootNode, 0);

		testPrettyPrint(component);
	}

	@Test
	public void testProperties2() {

		BComponent component = getBComponent("AtelierB/Example1/TestProperties2.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System8", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertNotNull(component.getProperties());
		assertTrue(component.getProperties() instanceof BConjunctPredicate);
		BConjunctPredicate p1 = (BConjunctPredicate) component.getProperties();
		assertTrue(p1.getLeft() instanceof BConjunctPredicate);
		assertTrue(p1.getRight() instanceof BSubsetPredicate);

		testPrettyPrint(component);

	}

	@Test
	public void testInvariantAndAssertions() {

		BComponent component = getBComponent("AtelierB/Example1/TestInvariantAndAssertions.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System9", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertNotNull(component.getInvariant());
		assertTrue(component.getInvariant() instanceof BMemberPredicate);
		BMemberPredicate p1 = (BMemberPredicate) component.getInvariant();
		assertTrue(p1.getLeft() instanceof BIdentifierExpression);
		assertTrue(p1.getRight() instanceof BBooleanSetExpression);

		assertEquals(2, component.getAssertions().size());
		assertTrue(component.getAssertions().get(0) instanceof BLessPredicate);
		assertTrue(component.getAssertions().get(1) instanceof BConjunctPredicate);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());
		debug(rootNode, 0);

		testPrettyPrint(component);

	}

	@Test
	public void testSysteme() {

		BComponent component = getBComponent("AtelierB/Example1/TestSysteme.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("UnSysteme", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());
		debug(rootNode, 0);

		testPrettyPrint(component);

	}

	@Test
	public void testR1() {

		BComponent component = getBComponent("FM/R1.mch");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("R1", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());
		debug(rootNode, 0);

		testPrettyPrint(component);

	}

}
