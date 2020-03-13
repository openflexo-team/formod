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
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.parser.nodes.BPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BConjunctPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BDisjunctPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BEqualPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BForAllPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BGreaterEqualPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BGreaterPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BImplicationPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BLessEqualPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BLessPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BMemberPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BNotEqualPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.predicate.BSubsetPredicateNode;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.predicate.BDisjunctPredicate;
import org.openflexo.ta.b.model.predicate.BEqualPredicate;
import org.openflexo.ta.b.model.predicate.BForAllPredicate;
import org.openflexo.ta.b.model.predicate.BGreaterEqualPredicate;
import org.openflexo.ta.b.model.predicate.BGreaterPredicate;
import org.openflexo.ta.b.model.predicate.BImplicationPredicate;
import org.openflexo.ta.b.model.predicate.BLessEqualPredicate;
import org.openflexo.ta.b.model.predicate.BLessPredicate;
import org.openflexo.ta.b.model.predicate.BMemberPredicate;
import org.openflexo.ta.b.model.predicate.BNotEqualPredicate;
import org.openflexo.ta.b.model.predicate.BSubsetPredicate;
import org.openflexo.ta.b.parser.node.AConjunctPredicate;
import org.openflexo.ta.b.parser.node.ADisjunctPredicate;
import org.openflexo.ta.b.parser.node.AEqualPredicate;
import org.openflexo.ta.b.parser.node.AForallPredicate;
import org.openflexo.ta.b.parser.node.AGreaterEqualPredicate;
import org.openflexo.ta.b.parser.node.AGreaterPredicate;
import org.openflexo.ta.b.parser.node.AImplicationPredicate;
import org.openflexo.ta.b.parser.node.ALessEqualPredicate;
import org.openflexo.ta.b.parser.node.ALessPredicate;
import org.openflexo.ta.b.parser.node.AMemberPredicate;
import org.openflexo.ta.b.parser.node.ANotEqualPredicate;
import org.openflexo.ta.b.parser.node.ASubsetPredicate;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.PPredicate;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;

/**
 * Factory building {@link BPredicate} from parsed PPredicate.<br>
 * 
 * @author sylvain
 * 
 */
public class PredicateFactory {

	private BModelFactory modelFactory;
	private BSemanticsAnalyzer analyzer;

	public PredicateFactory(BModelFactory modelFactory, BSemanticsAnalyzer analyzer) {
		this.modelFactory = modelFactory;
		this.analyzer = analyzer;
	}

	public BPredicateNode<?, ?> makePredicateNode(PPredicate p) throws NotImplementedException {
		// System.out.println("make predicate with " + p + " of " + p.getClass().getSimpleName());
		if (p instanceof AConjunctPredicate) {
			return new BConjunctPredicateNode((AConjunctPredicate) p, analyzer);
		}
		if (p instanceof ADisjunctPredicate) {
			return new BDisjunctPredicateNode((ADisjunctPredicate) p, analyzer);
		}
		if (p instanceof AImplicationPredicate) {
			return new BImplicationPredicateNode((AImplicationPredicate) p, analyzer);
		}
		if (p instanceof AMemberPredicate) {
			return new BMemberPredicateNode((AMemberPredicate) p, analyzer);
		}
		if (p instanceof ASubsetPredicate) {
			return new BSubsetPredicateNode((ASubsetPredicate) p, analyzer);
		}
		if (p instanceof AEqualPredicate) {
			return new BEqualPredicateNode((AEqualPredicate) p, analyzer);
		}
		if (p instanceof ANotEqualPredicate) {
			return new BNotEqualPredicateNode((ANotEqualPredicate) p, analyzer);
		}
		if (p instanceof ALessPredicate) {
			return new BLessPredicateNode((ALessPredicate) p, analyzer);
		}
		if (p instanceof ALessEqualPredicate) {
			return new BLessEqualPredicateNode((ALessEqualPredicate) p, analyzer);
		}
		if (p instanceof AGreaterPredicate) {
			return new BGreaterPredicateNode((AGreaterPredicate) p, analyzer);
		}
		if (p instanceof AGreaterEqualPredicate) {
			return new BGreaterEqualPredicateNode((AGreaterEqualPredicate) p, analyzer);
		}
		if (p instanceof AForallPredicate) {
			return new BForAllPredicateNode((AForallPredicate) p, analyzer);
		}
		throw new NotImplementedException("Not implemented: " + p.getClass() + " : " + p);
	}

	public BPredicateNode<?, ?> makePredicateNode(BPredicate p) throws NotImplementedException {
		// System.out.println("make predicate with " + p + " of " + p.getClass().getSimpleName());
		if (p instanceof BConjunctPredicate) {
			return new BConjunctPredicateNode((BConjunctPredicate) p, analyzer);
		}
		if (p instanceof BDisjunctPredicate) {
			return new BDisjunctPredicateNode((BDisjunctPredicate) p, analyzer);
		}
		if (p instanceof BImplicationPredicate) {
			return new BImplicationPredicateNode((BImplicationPredicate) p, analyzer);
		}
		if (p instanceof BMemberPredicate) {
			return new BMemberPredicateNode((BMemberPredicate) p, analyzer);
		}
		if (p instanceof BSubsetPredicate) {
			return new BSubsetPredicateNode((BSubsetPredicate) p, analyzer);
		}
		if (p instanceof BEqualPredicate) {
			return new BEqualPredicateNode((BEqualPredicate) p, analyzer);
		}
		if (p instanceof BNotEqualPredicate) {
			return new BNotEqualPredicateNode((BNotEqualPredicate) p, analyzer);
		}
		if (p instanceof BLessPredicate) {
			return new BLessPredicateNode((BLessPredicate) p, analyzer);
		}
		if (p instanceof BLessEqualPredicate) {
			return new BLessEqualPredicateNode((BLessEqualPredicate) p, analyzer);
		}
		if (p instanceof BGreaterPredicate) {
			return new BGreaterPredicateNode((BGreaterPredicate) p, analyzer);
		}
		if (p instanceof BGreaterEqualPredicate) {
			return new BGreaterEqualPredicateNode((BGreaterEqualPredicate) p, analyzer);
		}
		if (p instanceof BForAllPredicate) {
			return new BForAllPredicateNode((BForAllPredicate) p, analyzer);
		}
		throw new NotImplementedException("Not implemented: " + p.getClass() + " : " + p);
	}

	protected String getText(List<TIdentifierLiteral> idLitList) {
		return analyzer.getText(idLitList);
	}

	protected String getText(PExpression expression) {
		return analyzer.getText(expression);
	}

	public BExpression makeExpression(PExpression e) throws NotImplementedException {
		return analyzer.makeExpression(e);
	}

}
