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

package org.openflexo.ta.b.model.parser.nodes.predicate;

import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.BPredicateNode;
import org.openflexo.ta.b.model.predicate.BForAllPredicate;
import org.openflexo.ta.b.parser.node.AForallPredicate;
import org.openflexo.ta.b.parser.node.PExpression;

/**
 * @author sylvain
 * 
 */
public class BForAllPredicateNode extends BPredicateNode<AForallPredicate, BForAllPredicate> {

	public BForAllPredicateNode(AForallPredicate astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BForAllPredicateNode(BForAllPredicate concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BForAllPredicate buildModelObjectFromAST(AForallPredicate astNode) {
		getAnalyser().push(this);
		BForAllPredicate returned = getModelFactory().makePredicate(BForAllPredicate.class, getAnalyser().getBComponent());
		for (PExpression identifier : astNode.getIdentifiers()) {
			BExpressionNode<?, ?> identifierNode = getExpressionFactory().makeExpressionNode(identifier);
			returned.addToExpressions(identifierNode.getModelObject());
		}
		BPredicateNode<?, ?> implicationNode = getPredicateFactory().makePredicateNode(astNode.getImplication());
		returned.setImplication(implicationNode.getModelObject());
		getAnalyser().pop();
		return returned;
	}

	@Override
	public final void preparePrettyPrint(boolean hasParsedVersion) {

		super.preparePrettyPrint(hasParsedVersion);

		appendToChildrenPrettyPrintContents("! ", ",", () -> getModelObject().getExpressions(), "", "", BExpression.class);

		appendToChildPrettyPrintContents(" . (", () -> getModelObject().getImplication(), " )", Indentation.DoNotIndent);

	}
}
