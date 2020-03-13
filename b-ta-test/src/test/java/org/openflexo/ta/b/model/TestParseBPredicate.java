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
import org.openflexo.ta.b.model.predicate.BEqualPredicate;
import org.openflexo.ta.b.parser.lexer.CustomLexer.EntryPointKind;

public class TestParseBPredicate {
	protected static final Logger logger = Logger.getLogger(TestParseBPredicate.class.getPackage().getName());

	public void testPredicate(String toParse) throws ParseException, IOException, ModelDefinitionException {
		BObject object = BParser.parse(toParse, new BModelFactory(null, null), EntryPointKind.Predicate);
		assertTrue(object instanceof BPredicate);
		System.out.println("object=" + object);
		System.out.println("normalized: [" + object.getNormalizedBRepresentation() + "]");
		System.out.println("pretty-print: [" + object.getBPrettyPrint() + "]");
	}

	@Test
	public void testPredicate1() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AA <  BB");
	}

	@Test
	public void testPredicate2() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AA <  BB+1");
	}

	@Test
	public void testPredicate3() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AA :  BB");
	}

	/*@Test
	public void testPredicate4() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("in_l := i0 ||\nin_r := i0 ||\nout := o0");
	}*/

	@Test
	public void testPredicate5() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("in_l : T_IN &\nin_r : T_IN &\nin_l = in &\nAA={I0} &\nSE:0..5");

		BEqualPredicate newExpression = (BEqualPredicate) BParser.parse("A={I0}", new BModelFactory(null, null), EntryPointKind.Predicate);

		assertTrue(newExpression instanceof BPredicate);
		System.out.println("newExpression=" + newExpression);
		System.out.println("normalized: [" + newExpression.getNormalizedBRepresentation() + "] ("
				+ newExpression.getRight().getNormalizedBRepresentation() + ")");
		System.out.println("pretty-print: [" + newExpression.getBPrettyPrint() + "]");
	}

	@Test
	public void testPredicate6() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS:CO1>->>CO2");
	}

	@Test
	public void testPredicate7() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS:CO1>->CO2");
	}

	@Test
	public void testPredicate8() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS:CO1>+>CO2");
	}

	@Test
	public void testPredicate9_0() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("!xx.(xx:RA)");
	}

	@Test
	public void testPredicate9_1() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("xx:RA => xx:RB");
	}

	@Test
	public void testPredicate9_2() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("card(TT)=2");
	}

	@Test
	public void testPredicate9_3() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AA=BB~");
	}

	@Test
	public void testPredicate9_4() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("card(RA[{xx}])=2");
	}

	@Test
	public void testPredicate9_5() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("card(RA~[{xx}])=2");
	}

	@Test
	public void testPredicate9() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("!xx.(xx:RA=>card(RA~[{xx}])=2)");
	}

	@Test
	public void testPredicate10() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("!xx.(xx:RA=>card(RA[{xx}])=2)");
	}

	@Test
	public void testPredicate11() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS:CO1<->CO2");
	}

	@Test
	public void testPredicate12() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS=(AS1;AS2)");
	}

	@Test
	public void testPredicate13() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS=AS1/\\AS2");
	}

	@Test
	public void testPredicate14() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS=id(AS1)");
	}

	@Test
	public void testPredicate15() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS={}");
	}

	@Test
	public void testPredicate16() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("as={1|->2}");
	}

	@Test
	public void testPredicate17() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("AS=dom(AS1)");
	}

	@Test
	public void testPredicate18() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("y=f(x)");
	}

	@Test
	public void testPredicate19() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("Y=f[X]");
	}

	@Test
	public void testPredicate20() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("Y={xx}");
	}
	
	@Test
	public void testPredicate21() throws ParseException, IOException, ModelDefinitionException {
		testPredicate("X>-3");
	}

	@Test
	public void testNotPredicate() throws IOException, ModelDefinitionException {
		try {
			// This is an expression, not a predicate
			testPredicate("BB+1");
			fail();
		} catch (ParseException e) {
			System.out.println("Thats OK !");
		}
	}
}
