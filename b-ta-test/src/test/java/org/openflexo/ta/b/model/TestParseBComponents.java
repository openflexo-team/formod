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

import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.expression.BBooleanSetExpression;
import org.openflexo.ta.b.model.expression.BIdentifierExpression;
import org.openflexo.ta.b.model.expression.BIntSetExpression;
import org.openflexo.ta.b.model.expression.BNatSetExpression;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.predicate.BLessPredicate;
import org.openflexo.ta.b.model.predicate.BMemberPredicate;
import org.openflexo.ta.b.model.predicate.BSubsetPredicate;

public class TestParseBComponents extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestParseBComponents.class.getPackage().getName());

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	@Test
	public void testMachine() {

		BComponent component = getBComponent("AtelierB/Example1/Machine.mch");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BMachine);
		assertEquals("Full.Qualified.Machine", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		// testPrettyPrint(component);
	}

	@Test
	public void testSystem() {

		BComponent component = getBComponent("AtelierB/Example1/System1.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System1", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));
	}

	@Test
	public void testRefinement() {

		BComponent component = getBComponent("AtelierB/Example1/System1_r.ref");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BRefinement);
		assertEquals("System1_r", component.getName());
		assertEquals("System1", ((BRefinement) component).getRefinesComponent().getComponentName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));
	}

	@Test
	public void testImplementation() {

		BComponent component = getBComponent("AtelierB/Example1/System1_i.imp");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BImplementation);
		assertEquals("System1_i", component.getName());
		assertEquals("System1_r", ((BImplementation) component).getRefinesComponent().getComponentName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));
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
	}

	@Test
	public void testSets() {

		BComponent component = getBComponent("AtelierB/Example1/TestSets.sys");
		System.out.println(component.getResource().getName() + ": " + component);

		assertTrue(component instanceof BSystem);
		assertEquals("System3", component.getName());

		System.out.println(component.getResource().getFactory().stringRepresentation(component));

		assertEquals(3, component.getSets().size());
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

	}

}
