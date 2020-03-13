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

package org.openflexo.ta.b.model.parser.nodes.operation;

import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.operation.BNormalOperation;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.BOperationNode;
import org.openflexo.ta.b.model.parser.nodes.BSubstitutionNode;
import org.openflexo.ta.b.parser.node.AOperation;
import org.openflexo.ta.b.parser.node.PExpression;

/**
 * @author sylvain
 * 
 */
public class BNormalOperationNode extends BOperationNode<AOperation, BNormalOperation> {

	public BNormalOperationNode(AOperation astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BNormalOperationNode(BNormalOperation concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BNormalOperation buildModelObjectFromAST(AOperation astNode) {
		getAnalyser().push(this);
		BNormalOperation returned = getModelFactory().makeOperation(BNormalOperation.class, getAnalyser().getBComponent());
		returned.setName(getText(astNode.getOpName()));
		for (PExpression returnValue : astNode.getReturnValues()) {
			BExpressionNode<?, ?> returnValueNode = getExpressionFactory().makeExpressionNode(returnValue);
			returned.addToReturnValues(returnValueNode.getModelObject());
		}
		for (PExpression parameter : astNode.getParameters()) {
			BExpressionNode<?, ?> parameterNode = getExpressionFactory().makeExpressionNode(parameter);
			returned.addToParameters(parameterNode.getModelObject());
		}
		BSubstitutionNode<?, ?> substitutionNode = getSubstitutionFactory().makeSubstitutionNode(astNode.getOperationBody());
		returned.setOperationBody(substitutionNode.getModelObject());
		getAnalyser().pop();
		return returned;
	}

	@Override
	public final void preparePrettyPrint(boolean hasParsedVersion) {

		super.preparePrettyPrint(hasParsedVersion);

		appendToChildrenPrettyPrintContents("", ",", () -> getModelObject().getReturnValues(), "", " <-- ", BExpression.class);
		appendDynamicContents(() -> getModelObject().getName(), getOperationNameFragment());
		appendToChildrenPrettyPrintContents("(", ", ", () -> getModelObject().getParameters(), "", ")", BExpression.class);
		appendToChildPrettyPrintContents(" = ", () -> getModelObject().getOperationBody(), "", Indentation.DoNotIndent);
	}

	protected RawSourceFragment getOperationNameFragment() {
		if (getASTNode() != null) {
			return getFragment(getASTNode().getOpName());
		}
		return null;
	}

}
