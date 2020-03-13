/**
 * 
 * Copyright (c) 2019, Openflexo
 * 
 * This file is part of FML-parser, a component of the software infrastructure 
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

package org.openflexo.ta.b.model.parser.nodes.expression;

import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.ta.b.model.expression.BReverseExpression;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BExpressionNode;
import org.openflexo.ta.b.parser.node.AReverseExpression;

/**
 * @author sylvain
 * 
 */
public class BReverseExpressionNode extends BExpressionNode<AReverseExpression, BReverseExpression> {

	public BReverseExpressionNode(AReverseExpression astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BReverseExpressionNode(BReverseExpression concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BReverseExpression buildModelObjectFromAST(AReverseExpression astNode) {
		getAnalyser().push(this);
		BReverseExpression returned = getModelFactory().makeExpression(BReverseExpression.class, getAnalyser().getBComponent());
		BExpressionNode<?, ?> expressionNode = getExpressionFactory().makeExpressionNode(astNode.getExpression());
		returned.setExpression(expressionNode.getModelObject());
		getAnalyser().pop();
		return returned;
	}

	@Override
	public final void preparePrettyPrint(boolean hasParsedVersion) {

		super.preparePrettyPrint(hasParsedVersion);
		appendToChildPrettyPrintContents("(", () -> getModelObject().getExpression(), ")", Indentation.DoNotIndent);
		appendStaticContents("~", null);// TODO: match fragment
	}

}
