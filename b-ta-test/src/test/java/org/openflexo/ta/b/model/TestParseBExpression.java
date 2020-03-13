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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.parser.lexer.CustomLexer.EntryPointKind;

public class TestParseBExpression {
	protected static final Logger logger = Logger.getLogger(TestParseBExpression.class.getPackage().getName());

	public void testExpression(String toParse) throws ParseException, IOException, ModelDefinitionException {
		BObject object = BParser.parse(toParse, new BModelFactory(null, null), EntryPointKind.Expression);
		assertTrue(object instanceof BExpression);
		System.out.println("object=" + object);
		System.out.println("normalized: [" + object.getNormalizedBRepresentation() + "]");
		System.out.println("pretty-print: [" + object.getBPrettyPrint() + "]");
	}

	@Test
	public void testExpression1() throws ParseException, IOException, ModelDefinitionException {
		testExpression("BB+1");
	}

	@Test
	public void testExpression2() throws ParseException, IOException, ModelDefinitionException {
		testExpression("BB-AA");
	}

	@Test
	public void testExpression3() throws ParseException, IOException, ModelDefinitionException {
		testExpression("NAT");
	}

	@Test
	public void testExpression4() throws ParseException, IOException, ModelDefinitionException {
		testExpression("TRUE");
	}

	@Test
	public void testExpression5() throws ParseException, IOException, ModelDefinitionException {
		testExpression("ran(  AA)");
	}
	
	@Test
	public void testExpression6() throws ParseException, IOException, ModelDefinitionException {
		testExpression("-1+2");
	}
	
	@Test
	public void testExpression7() throws ParseException, IOException, ModelDefinitionException {
		testExpression("1.5");
	}
	

	@Test
	public void testNotExpression() throws IOException, ModelDefinitionException {
		try {
			testExpression("AA < BB");
			fail();
		} catch (ParseException e) {
			System.out.println("Thats OK !");
		}
	}
}
