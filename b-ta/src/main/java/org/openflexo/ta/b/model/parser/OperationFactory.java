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
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.operation.BNormalOperation;
import org.openflexo.ta.b.model.operation.BRefinedOperation;
import org.openflexo.ta.b.model.parser.nodes.BOperationNode;
import org.openflexo.ta.b.model.parser.nodes.operation.BNormalOperationNode;
import org.openflexo.ta.b.model.parser.nodes.operation.BRefinedOperationNode;
import org.openflexo.ta.b.parser.node.AOperation;
import org.openflexo.ta.b.parser.node.ARefinedOperation;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.POperation;
import org.openflexo.ta.b.parser.node.PPredicate;
import org.openflexo.ta.b.parser.node.PSubstitution;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;

/**
 * Factory building {@link BOperation} from parsed POperation.<br>
 * 
 * @author sylvain
 * 
 */
public class OperationFactory {

	private BModelFactory modelFactory;
	private BSemanticsAnalyzer analyzer;

	public OperationFactory(BModelFactory modelFactory, BSemanticsAnalyzer analyzer) {
		this.modelFactory = modelFactory;
		this.analyzer = analyzer;
	}

	public BOperationNode<?, ?> makeOperationNode(POperation o) throws NotImplementedException {
		if (o instanceof AOperation) {
			return new BNormalOperationNode((AOperation) o, analyzer);
		}
		if (o instanceof ARefinedOperation) {
			return new BRefinedOperationNode((ARefinedOperation) o, analyzer);
		}
		throw new NotImplementedException("Not implemented: " + o.getClass() + " : " + o);
	}

	public BOperationNode<?, ?> makeOperationNode(BOperation o) throws NotImplementedException {
		if (o instanceof BRefinedOperation) {
			return new BRefinedOperationNode((BRefinedOperation) o, analyzer);
		}
		if (o instanceof BNormalOperation) {
			return new BNormalOperationNode((BNormalOperation) o, analyzer);
		}
		throw new NotImplementedException("Not implemented: " + o.getClass() + " : " + o);
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

	public BSubstitution makeSubstitution(PSubstitution s) throws NotImplementedException {
		return analyzer.makeSubstitution(s);
	}

}
