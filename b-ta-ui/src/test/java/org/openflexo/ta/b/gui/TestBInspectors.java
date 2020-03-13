/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.ta.b.gui;

import org.junit.Test;
import org.openflexo.gina.test.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

/**
 * Used to test all inspectors defined in this technology adapter<br>
 * 
 * To use that class, first execute main method to generate all tests in the console, then copy-paste all the tests in this source file
 * 
 * 
 * @author sylvain
 *
 */
public class TestBInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/B")).getFile(),
				"Inspectors/B/"));
	}

	@Test
	public void testBObjectInspector() {
		validateFIB("Inspectors/B/BObject.inspector");
	}

	@Test
	public void testBComponentInspector() {
		validateFIB("Inspectors/B/BComponent.inspector");
	}

	@Test
	public void testAddBMemberInspector() {
		validateFIB("Inspectors/B/EditionAction/AddBMember.inspector");
	}

	@Test
	public void testAddBSetValueInspector() {
		validateFIB("Inspectors/B/EditionAction/AddBSetValue.inspector");
	}

	@Test
	public void testCreateBExpressionFromStringInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBExpressionFromString.inspector");
	}

	@Test
	public void testRemoveBPredicateInBPredicateInspector() {
		validateFIB("Inspectors/B/EditionAction/RemoveBPredicateInBPredicate.inspector");
	}

	@Test
	public void testAddBOperationInspector() {
		validateFIB("Inspectors/B/EditionAction/AddBOperation.inspector");
	}

	@Test
	public void testCreateBBinaryExpressionInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBBinaryExpression.inspector");
	}

	@Test
	public void testCreateBParallelSubstitutionInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBParallelSubstitution.inspector");
	}

	@Test
	public void testString2IntInspector() {
		validateFIB("Inspectors/B/EditionAction/String2Int.inspector");
	}

	@Test
	public void testAddBRefinedOperationInspector() {
		validateFIB("Inspectors/B/EditionAction/AddBRefinedOperation.inspector");
	}

	@Test
	public void testCreateBBinaryExpressionPredicateInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBBinaryExpressionPredicate.inspector");
	}

	@Test
	public void testCreateBPredicateFromStringInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBPredicateFromString.inspector");
	}

	@Test
	public void testAddBSetInspector() {
		validateFIB("Inspectors/B/EditionAction/AddBSet.inspector");
	}

	@Test
	public void testCreateBBinaryPredicatePredicateInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBBinaryPredicatePredicate.inspector");
	}

	@Test
	public void testCreateBSubstitutionInspector() {
		validateFIB("Inspectors/B/EditionAction/CreateBSubstitution.inspector");
	}
}
