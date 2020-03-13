/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Connie-core, a component of the software infrastructure 
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

package org.openflexo.ta.b.model.parser;

import java.util.List;

import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.expression.BAddExpression;
import org.openflexo.ta.b.model.expression.BBooleanSetExpression;
import org.openflexo.ta.b.model.expression.BCardExpression;
import org.openflexo.ta.b.model.expression.BCompositionExpression;
import org.openflexo.ta.b.model.expression.BConvertBoolExpression;
import org.openflexo.ta.b.model.expression.BCoupleExpression;
import org.openflexo.ta.b.model.expression.BDomainExpression;
import org.openflexo.ta.b.model.expression.BEmptySetExpression;
import org.openflexo.ta.b.model.expression.BFalseExpression;
import org.openflexo.ta.b.model.expression.BFunctionExpression;
import org.openflexo.ta.b.model.expression.BIdentifierExpression;
import org.openflexo.ta.b.model.expression.BIdentityExpression;
import org.openflexo.ta.b.model.expression.BImageExpression;
import org.openflexo.ta.b.model.expression.BIntSetExpression;
import org.openflexo.ta.b.model.expression.BIntegerExpression;
import org.openflexo.ta.b.model.expression.BIntersectionExpression;
import org.openflexo.ta.b.model.expression.BIntervalExpression;
import org.openflexo.ta.b.model.expression.BMaxIntExpression;
import org.openflexo.ta.b.model.expression.BMinIntExpression;
import org.openflexo.ta.b.model.expression.BMinusOrSetSubstractExpression;
import org.openflexo.ta.b.model.expression.BMultOrCartExpression;
import org.openflexo.ta.b.model.expression.BNatSetExpression;
import org.openflexo.ta.b.model.expression.BNaturalSetExpression;
import org.openflexo.ta.b.model.expression.BPartialFunctionExpression;
import org.openflexo.ta.b.model.expression.BPartialInjectionExpression;
import org.openflexo.ta.b.model.expression.BPartialSurjectionExpression;
import org.openflexo.ta.b.model.expression.BPowSubsetExpression;
import org.openflexo.ta.b.model.expression.BRangeExpression;
import org.openflexo.ta.b.model.expression.BRelationsExpression;
import org.openflexo.ta.b.model.expression.BReverseExpression;
import org.openflexo.ta.b.model.expression.BSetExtensionExpression;
import org.openflexo.ta.b.model.expression.BStrictNatSetExpression;
import org.openflexo.ta.b.model.expression.BStrictNaturalSetExpression;
import org.openflexo.ta.b.model.expression.BTotalBijectionExpression;
import org.openflexo.ta.b.model.expression.BTotalFunctionExpression;
import org.openflexo.ta.b.model.expression.BTotalInjectionExpression;
import org.openflexo.ta.b.model.expression.BTotalSurjectionExpression;
import org.openflexo.ta.b.model.expression.BTrueExpression;
import org.openflexo.ta.b.model.expression.BUnaryMinusExpression;
import org.openflexo.ta.b.model.expression.BUnionExpression;
import org.openflexo.ta.b.model.parser.nodes.BExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BAddExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BBooleanSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BCardExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BCompositionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BConvertBoolExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BCoupleExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BDomainExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BEmptySetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BFalseExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BFunctionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIdentifierExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIdentityExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BImageExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIntSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIntegerExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIntegerSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIntersectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BIntervalExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BMaxIntExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BMinIntExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BMinusOrSetSubstractExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BMultOrCartExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BNatSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BNaturalSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BPartialFunctionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BPartialInjectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BPartialSurjectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BPowSubsetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BRangeExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BRelationsExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BReverseExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BSetExtensionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BStrictNatSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BStrictNaturalSetExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BTotalBijectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BTotalFunctionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BTotalInjectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BTotalSurjectionExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BTrueExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BUnaryMinusExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.expression.BUnionExpressionNode;
import org.openflexo.ta.b.parser.node.AAddExpression;
import org.openflexo.ta.b.parser.node.ABoolSetExpression;
import org.openflexo.ta.b.parser.node.ABooleanFalseExpression;
import org.openflexo.ta.b.parser.node.ABooleanTrueExpression;
import org.openflexo.ta.b.parser.node.ACardExpression;
import org.openflexo.ta.b.parser.node.ACompositionExpression;
import org.openflexo.ta.b.parser.node.AConvertBoolExpression;
import org.openflexo.ta.b.parser.node.ACoupleExpression;
import org.openflexo.ta.b.parser.node.ADomainExpression;
import org.openflexo.ta.b.parser.node.AEmptySetExpression;
import org.openflexo.ta.b.parser.node.AFunctionExpression;
import org.openflexo.ta.b.parser.node.AIdentifierExpression;
import org.openflexo.ta.b.parser.node.AIdentityExpression;
import org.openflexo.ta.b.parser.node.AImageExpression;
import org.openflexo.ta.b.parser.node.AIntSetExpression;
import org.openflexo.ta.b.parser.node.AIntegerExpression;
import org.openflexo.ta.b.parser.node.AIntegerSetExpression;
import org.openflexo.ta.b.parser.node.AIntersectionExpression;
import org.openflexo.ta.b.parser.node.AIntervalExpression;
import org.openflexo.ta.b.parser.node.AMaxIntExpression;
import org.openflexo.ta.b.parser.node.AMinIntExpression;
import org.openflexo.ta.b.parser.node.AMinusOrSetSubtractExpression;
import org.openflexo.ta.b.parser.node.AMultOrCartExpression;
import org.openflexo.ta.b.parser.node.ANat1SetExpression;
import org.openflexo.ta.b.parser.node.ANatSetExpression;
import org.openflexo.ta.b.parser.node.ANatural1SetExpression;
import org.openflexo.ta.b.parser.node.ANaturalSetExpression;
import org.openflexo.ta.b.parser.node.APartialFunctionExpression;
import org.openflexo.ta.b.parser.node.APartialInjectionExpression;
import org.openflexo.ta.b.parser.node.APartialSurjectionExpression;
import org.openflexo.ta.b.parser.node.APowSubsetExpression;
import org.openflexo.ta.b.parser.node.ARangeExpression;
import org.openflexo.ta.b.parser.node.ARelationsExpression;
import org.openflexo.ta.b.parser.node.AReverseExpression;
import org.openflexo.ta.b.parser.node.ASetExtensionExpression;
import org.openflexo.ta.b.parser.node.ATotalBijectionExpression;
import org.openflexo.ta.b.parser.node.ATotalFunctionExpression;
import org.openflexo.ta.b.parser.node.ATotalInjectionExpression;
import org.openflexo.ta.b.parser.node.ATotalSurjectionExpression;
import org.openflexo.ta.b.parser.node.AUnaryMinusExpression;
import org.openflexo.ta.b.parser.node.AUnionExpression;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;
import org.openflexo.ta.b.parser.node.TIntegerLiteral;

/**
 * Factory building {@link BExpression} from parsed PExpression.<br>
 * 
 * @author sylvain
 * 
 */
public class ExpressionFactory {

	private BModelFactory modelFactory;
	private BSemanticsAnalyzer analyzer;

	public ExpressionFactory(BModelFactory modelFactory, BSemanticsAnalyzer analyzer) {
		this.modelFactory = modelFactory;
		this.analyzer = analyzer;
	}

	public BExpressionNode<?, ?> makeExpressionNode(PExpression e) throws NotImplementedException {

		// System.out.println("make expression with " + e + " of " + e.getClass().getSimpleName());

		// Simple expressions
		if (e instanceof AIdentifierExpression) {
			return new BIdentifierExpressionNode((AIdentifierExpression) e, analyzer);
		}
		if (e instanceof AConvertBoolExpression) {
			return new BConvertBoolExpressionNode((AConvertBoolExpression) e, analyzer);
		}
		if (e instanceof AIntSetExpression) {
			return new BIntSetExpressionNode((AIntSetExpression) e, analyzer);
		}
		if (e instanceof AIntegerSetExpression) {
			return new BIntegerSetExpressionNode((AIntegerSetExpression) e, analyzer);
		}
		if (e instanceof AMaxIntExpression) {
			return new BMaxIntExpressionNode((AMaxIntExpression) e, analyzer);
		}
		if (e instanceof AMinIntExpression) {
			return new BMinIntExpressionNode((AMinIntExpression) e, analyzer);
		}
		if (e instanceof ANaturalSetExpression) {
			return new BNaturalSetExpressionNode((ANaturalSetExpression) e, analyzer);
		}
		if (e instanceof ANatural1SetExpression) {
			return new BStrictNaturalSetExpressionNode((ANatural1SetExpression) e, analyzer);
		}
		if (e instanceof ANatSetExpression) {
			return new BNatSetExpressionNode((ANatSetExpression) e, analyzer);
		}
		if (e instanceof ANat1SetExpression) {
			return new BStrictNatSetExpressionNode((ANat1SetExpression) e, analyzer);
		}
		if (e instanceof AIntegerExpression) {
			return new BIntegerExpressionNode((AIntegerExpression) e, analyzer);
		}
		if (e instanceof ABoolSetExpression) {
			return new BBooleanSetExpressionNode((ABoolSetExpression) e, analyzer);
		}
		if (e instanceof ABooleanTrueExpression) {
			return new BTrueExpressionNode((ABooleanTrueExpression) e, analyzer);
		}
		if (e instanceof ABooleanFalseExpression) {
			return new BFalseExpressionNode((ABooleanFalseExpression) e, analyzer);
		}
		if (e instanceof AEmptySetExpression) {
			return new BEmptySetExpressionNode((AEmptySetExpression) e, analyzer);
		}

		// Unary expressions
		if (e instanceof AReverseExpression) {
			return new BReverseExpressionNode((AReverseExpression) e, analyzer);
		}
		if (e instanceof ARangeExpression) {
			return new BRangeExpressionNode((ARangeExpression) e, analyzer);
		}
		if (e instanceof ACardExpression) {
			return new BCardExpressionNode((ACardExpression) e, analyzer);
		}
		if (e instanceof AIdentityExpression) {
			return new BIdentityExpressionNode((AIdentityExpression) e, analyzer);
		}
		if (e instanceof ADomainExpression) {
			return new BDomainExpressionNode((ADomainExpression) e, analyzer);
		}
		if (e instanceof AUnaryMinusExpression) {
			return new BUnaryMinusExpressionNode((AUnaryMinusExpression) e, analyzer);
		}
		if (e instanceof APowSubsetExpression) {
			return new BPowSubsetExpressionNode((APowSubsetExpression) e, analyzer);
		}

		// Binary expressions
		if (e instanceof ATotalFunctionExpression) {
			return new BTotalFunctionExpressionNode((ATotalFunctionExpression) e, analyzer);
		}
		if (e instanceof AIntervalExpression) {
			return new BIntervalExpressionNode((AIntervalExpression) e, analyzer);
		}
		if (e instanceof APartialSurjectionExpression) {
			return new BPartialSurjectionExpressionNode((APartialSurjectionExpression) e, analyzer);
		}
		if (e instanceof ATotalBijectionExpression) {
			return new BTotalBijectionExpressionNode((ATotalBijectionExpression) e, analyzer);
		}
		if (e instanceof APartialInjectionExpression) {
			return new BPartialInjectionExpressionNode((APartialInjectionExpression) e, analyzer);
		}
		if (e instanceof ARelationsExpression) {
			return new BRelationsExpressionNode((ARelationsExpression) e, analyzer);
		}
		if (e instanceof AIntersectionExpression) {
			return new BIntersectionExpressionNode((AIntersectionExpression) e, analyzer);
		}
		if (e instanceof ATotalSurjectionExpression) {
			return new BTotalSurjectionExpressionNode((ATotalSurjectionExpression) e, analyzer);
		}
		if (e instanceof ATotalInjectionExpression) {
			return new BTotalInjectionExpressionNode((ATotalInjectionExpression) e, analyzer);
		}
		if (e instanceof ACompositionExpression) {
			return new BCompositionExpressionNode((ACompositionExpression) e, analyzer);
		}
		if (e instanceof AUnionExpression) {
			return new BUnionExpressionNode((AUnionExpression) e, analyzer);
		}
		if (e instanceof AAddExpression) {
			return new BAddExpressionNode((AAddExpression) e, analyzer);
		}
		if (e instanceof AMultOrCartExpression) {
			return new BMultOrCartExpressionNode((AMultOrCartExpression) e, analyzer);
		}
		if (e instanceof AMinusOrSetSubtractExpression) {
			return new BMinusOrSetSubstractExpressionNode((AMinusOrSetSubtractExpression) e, analyzer);
		}
		if (e instanceof APartialFunctionExpression) {
			return new BPartialFunctionExpressionNode((APartialFunctionExpression) e, analyzer);
		}
		// Other expressions
		if (e instanceof AFunctionExpression) {
			return new BFunctionExpressionNode((AFunctionExpression) e, analyzer);
		}
		if (e instanceof AImageExpression) {
			return new BImageExpressionNode((AImageExpression) e, analyzer);
		}
		if (e instanceof ACoupleExpression) {
			return new BCoupleExpressionNode((ACoupleExpression) e, analyzer);
		}
		if (e instanceof ASetExtensionExpression) {
			return new BSetExtensionExpressionNode((ASetExtensionExpression) e, analyzer);
		}

		throw new NotImplementedException("Not implemented: " + e.getClass() + " : " + e);
	}

	public BExpressionNode<?, ?> makeExpressionNode(BExpression e) throws NotImplementedException {

		// System.out.println("make expression with " + e + " of " + e.getClass().getSimpleName());

		// Simple expressions
		if (e instanceof BIdentifierExpression) {
			return new BIdentifierExpressionNode((BIdentifierExpression) e, analyzer);
		}
		if (e instanceof BConvertBoolExpression) {
			return new BConvertBoolExpressionNode((BConvertBoolExpression) e, analyzer);
		}
		if (e instanceof BIntSetExpression) {
			return new BIntSetExpressionNode((BIntSetExpression) e, analyzer);
		}
		if (e instanceof BMaxIntExpression) {
			return new BMaxIntExpressionNode((BMaxIntExpression) e, analyzer);
		}
		if (e instanceof BMinIntExpression) {
			return new BMinIntExpressionNode((BMinIntExpression) e, analyzer);
		}
		if (e instanceof BNaturalSetExpression) {
			return new BNaturalSetExpressionNode((BNaturalSetExpression) e, analyzer);
		}
		if (e instanceof BStrictNaturalSetExpression) {
			return new BStrictNaturalSetExpressionNode((BStrictNaturalSetExpression) e, analyzer);
		}
		if (e instanceof BNatSetExpression) {
			return new BNatSetExpressionNode((BNatSetExpression) e, analyzer);
		}
		if (e instanceof BStrictNatSetExpression) {
			return new BStrictNatSetExpressionNode((BStrictNatSetExpression) e, analyzer);
		}
		if (e instanceof BIntegerExpression) {
			return new BIntegerExpressionNode((BIntegerExpression) e, analyzer);
		}
		if (e instanceof BBooleanSetExpression) {
			return new BBooleanSetExpressionNode((BBooleanSetExpression) e, analyzer);
		}
		if (e instanceof BTrueExpression) {
			return new BTrueExpressionNode((BTrueExpression) e, analyzer);
		}
		if (e instanceof BFalseExpression) {
			return new BFalseExpressionNode((BFalseExpression) e, analyzer);
		}
		if (e instanceof BEmptySetExpression) {
			return new BEmptySetExpressionNode((BEmptySetExpression) e, analyzer);
		}

		// Unary expressions
		if (e instanceof BReverseExpression) {
			return new BReverseExpressionNode((BReverseExpression) e, analyzer);
		}
		if (e instanceof BRangeExpression) {
			return new BRangeExpressionNode((BRangeExpression) e, analyzer);
		}
		if (e instanceof BCardExpression) {
			return new BCardExpressionNode((BCardExpression) e, analyzer);
		}
		if (e instanceof BIdentityExpression) {
			return new BIdentityExpressionNode((BIdentityExpression) e, analyzer);
		}
		if (e instanceof BDomainExpression) {
			return new BDomainExpressionNode((BDomainExpression) e, analyzer);
		}
		if (e instanceof BUnaryMinusExpression) {
			return new BUnaryMinusExpressionNode((BUnaryMinusExpression) e, analyzer);
		}
		if (e instanceof BPowSubsetExpression) {
			return new BPowSubsetExpressionNode((BPowSubsetExpression) e, analyzer);
		}

		// Binary expressions
		if (e instanceof BIntervalExpression) {
			return new BIntervalExpressionNode((BIntervalExpression) e, analyzer);
		}
		if (e instanceof BTotalFunctionExpression) {
			return new BTotalFunctionExpressionNode((BTotalFunctionExpression) e, analyzer);
		}
		if (e instanceof BTotalSurjectionExpression) {
			return new BTotalSurjectionExpressionNode((BTotalSurjectionExpression) e, analyzer);
		}
		if (e instanceof BPartialSurjectionExpression) {
			return new BPartialSurjectionExpressionNode((BPartialSurjectionExpression) e, analyzer);
		}
		if (e instanceof BTotalBijectionExpression) {
			return new BTotalBijectionExpressionNode((BTotalBijectionExpression) e, analyzer);
		}
		if (e instanceof BPartialInjectionExpression) {
			return new BPartialInjectionExpressionNode((BPartialInjectionExpression) e, analyzer);
		}
		if (e instanceof BRelationsExpression) {
			return new BRelationsExpressionNode((BRelationsExpression) e, analyzer);
		}
		if (e instanceof BIntersectionExpression) {
			return new BIntersectionExpressionNode((BIntersectionExpression) e, analyzer);
		}
		if (e instanceof BTotalInjectionExpression) {
			return new BTotalInjectionExpressionNode((BTotalInjectionExpression) e, analyzer);
		}
		if (e instanceof BCompositionExpression) {
			return new BCompositionExpressionNode((BCompositionExpression) e, analyzer);
		}
		if (e instanceof BUnionExpression) {
			return new BUnionExpressionNode((BUnionExpression) e, analyzer);
		}
		if (e instanceof BAddExpression) {
			return new BAddExpressionNode((BAddExpression) e, analyzer);
		}
		if (e instanceof BMultOrCartExpression) {
			return new BMultOrCartExpressionNode((BMultOrCartExpression) e, analyzer);
		}
		if (e instanceof BMinusOrSetSubstractExpression) {
			return new BMinusOrSetSubstractExpressionNode((BMinusOrSetSubstractExpression) e, analyzer);
		}
		if (e instanceof BPartialFunctionExpression) {
			return new BPartialFunctionExpressionNode((BPartialFunctionExpression) e, analyzer);
		}
		// Other expressions
		if (e instanceof BFunctionExpression) {
			return new BFunctionExpressionNode((BFunctionExpression) e, analyzer);
		}
		if (e instanceof BImageExpression) {
			return new BImageExpressionNode((BImageExpression) e, analyzer);
		}
		if (e instanceof BCoupleExpression) {
			return new BCoupleExpressionNode((BCoupleExpression) e, analyzer);
		}
		if (e instanceof BSetExtensionExpression) {
			return new BSetExtensionExpressionNode((BSetExtensionExpression) e, analyzer);
		}

		throw new NotImplementedException("Not implemented: " + e.getClass() + " : " + e);
	}

	protected String getText(List<TIdentifierLiteral> idLitList) {
		return analyzer.getText(idLitList);
	}

	protected String getText(PExpression expression) {
		return analyzer.getText(expression);
	}

	protected Integer getInteger(TIntegerLiteral literal) {
		return analyzer.getInteger(literal);
	}

}
