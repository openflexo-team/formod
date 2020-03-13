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
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.parser.nodes.BSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BAnySubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BAssignSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BBecomeElementOfSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BBecomeSuchSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BBlockSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BParallelSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BPreconditionSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BSelectSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.substitution.BSkipSubstitutionNode;
import org.openflexo.ta.b.model.substitution.BAnySubstitution;
import org.openflexo.ta.b.model.substitution.BAssignSubstitution;
import org.openflexo.ta.b.model.substitution.BBecomeElementOfSubstitution;
import org.openflexo.ta.b.model.substitution.BBecomeSuchSubstitution;
import org.openflexo.ta.b.model.substitution.BBlockSubstitution;
import org.openflexo.ta.b.model.substitution.BParallelSubstitution;
import org.openflexo.ta.b.model.substitution.BPreconditionSubstitution;
import org.openflexo.ta.b.model.substitution.BSelectSubstitution;
import org.openflexo.ta.b.model.substitution.BSkipSubstitution;
import org.openflexo.ta.b.parser.node.AAnySubstitution;
import org.openflexo.ta.b.parser.node.AAssignSubstitution;
import org.openflexo.ta.b.parser.node.ABecomesElementOfSubstitution;
import org.openflexo.ta.b.parser.node.ABecomesSuchSubstitution;
import org.openflexo.ta.b.parser.node.ABlockSubstitution;
import org.openflexo.ta.b.parser.node.AParallelSubstitution;
import org.openflexo.ta.b.parser.node.APreconditionSubstitution;
import org.openflexo.ta.b.parser.node.ASelectSubstitution;
import org.openflexo.ta.b.parser.node.ASkipSubstitution;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.PPredicate;
import org.openflexo.ta.b.parser.node.PSubstitution;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;

/**
 * Factory building {@link BSubstitution} from parsed PSubstitution.<br>
 * 
 * @author sylvain
 * 
 */
public class SubstitutionFactory {

	private BModelFactory modelFactory;
	private BSemanticsAnalyzer analyzer;

	public SubstitutionFactory(BModelFactory modelFactory, BSemanticsAnalyzer analyzer) {
		this.modelFactory = modelFactory;
		this.analyzer = analyzer;
	}

	public BSubstitutionNode<?, ?> makeSubstitutionNode(PSubstitution s) throws NotImplementedException {
		if (s instanceof ASkipSubstitution) {
			return new BSkipSubstitutionNode((ASkipSubstitution) s, analyzer);
		}
		if (s instanceof ASelectSubstitution) {
			return new BSelectSubstitutionNode((ASelectSubstitution) s, analyzer);
		}
		if (s instanceof ABecomesSuchSubstitution) {
			return new BBecomeSuchSubstitutionNode((ABecomesSuchSubstitution) s, analyzer);
		}
		if (s instanceof APreconditionSubstitution) {
			return new BPreconditionSubstitutionNode((APreconditionSubstitution) s, analyzer);
		}
		if (s instanceof AAnySubstitution) {
			return new BAnySubstitutionNode((AAnySubstitution) s, analyzer);
		}
		if (s instanceof AAssignSubstitution) {
			return new BAssignSubstitutionNode((AAssignSubstitution) s, analyzer);
		}
		if (s instanceof ABlockSubstitution) {
			return new BBlockSubstitutionNode((ABlockSubstitution) s, analyzer);
		}
		if (s instanceof ABecomesElementOfSubstitution) {
			return new BBecomeElementOfSubstitutionNode((ABecomesElementOfSubstitution) s, analyzer);
		}
		if (s instanceof AParallelSubstitution) {
			return new BParallelSubstitutionNode((AParallelSubstitution) s, analyzer);
		}
		if (s == null) {
			return null;
		}
		throw new NotImplementedException("Not implemented: " + s.getClass() + " : " + s);
	}

	public BSubstitutionNode<?, ?> makeSubstitutionNode(BSubstitution s) throws NotImplementedException {
		if (s instanceof BSkipSubstitution) {
			return new BSkipSubstitutionNode((BSkipSubstitution) s, analyzer);
		}
		if (s instanceof BSelectSubstitution) {
			return new BSelectSubstitutionNode((BSelectSubstitution) s, analyzer);
		}
		if (s instanceof BBecomeSuchSubstitution) {
			return new BBecomeSuchSubstitutionNode((BBecomeSuchSubstitution) s, analyzer);
		}
		if (s instanceof BPreconditionSubstitution) {
			return new BPreconditionSubstitutionNode((BPreconditionSubstitution) s, analyzer);
		}
		if (s instanceof BAnySubstitution) {
			return new BAnySubstitutionNode((BAnySubstitution) s, analyzer);
		}
		if (s instanceof BAssignSubstitution) {
			return new BAssignSubstitutionNode((BAssignSubstitution) s, analyzer);
		}
		if (s instanceof BBlockSubstitution) {
			return new BBlockSubstitutionNode((BBlockSubstitution) s, analyzer);
		}
		if (s instanceof BBecomeElementOfSubstitution) {
			return new BBecomeElementOfSubstitutionNode((BBecomeElementOfSubstitution) s, analyzer);
		}
		if (s instanceof BParallelSubstitution) {
			return new BParallelSubstitutionNode((BParallelSubstitution) s, analyzer);
		}
		if (s == null) {
			return null;
		}
		throw new NotImplementedException("Not implemented: " + s.getClass() + " : " + s);
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

	public BPredicate makePredicate(PPredicate p) throws NotImplementedException {
		return analyzer.makePredicate(p);
	}

}
