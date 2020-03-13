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

import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BPredicateNode;
import org.openflexo.ta.b.model.predicate.BDisjunctPredicate;
import org.openflexo.ta.b.parser.node.ADisjunctPredicate;

/**
 * @author sylvain
 * 
 */
public class BDisjunctPredicateNode extends BBinaryPredicatePredicateNode<ADisjunctPredicate, BDisjunctPredicate> {

	public BDisjunctPredicateNode(ADisjunctPredicate astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BDisjunctPredicateNode(BDisjunctPredicate concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public String getOperator() {
		return "or";
	}

	@Override
	public BDisjunctPredicate buildModelObjectFromAST(ADisjunctPredicate astNode) {
		getAnalyser().push(this);
		BDisjunctPredicate returned = getModelFactory().makePredicate(BDisjunctPredicate.class, getAnalyser().getBComponent());
		BPredicateNode<?, ?> leftPredicate = getPredicateFactory().makePredicateNode(astNode.getLeft());
		BPredicateNode<?, ?> rightPredicate = getPredicateFactory().makePredicateNode(astNode.getRight());
		returned.setLeft(leftPredicate.getModelObject());
		returned.setRight(rightPredicate.getModelObject());
		getAnalyser().pop();
		return returned;
	}

}
