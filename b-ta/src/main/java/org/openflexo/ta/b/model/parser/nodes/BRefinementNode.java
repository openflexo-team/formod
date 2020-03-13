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

package org.openflexo.ta.b.model.parser.nodes;

import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.ta.b.model.BRefinement;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.parser.node.AMachineHeader;
import org.openflexo.ta.b.parser.node.ARefinementMachineParseUnit;
import org.openflexo.ta.b.rm.BResource;

/**
 * @author sylvain
 * 
 */
public class BRefinementNode extends BComponentNode<ARefinementMachineParseUnit, BRefinement> {

	public BRefinementNode(ARefinementMachineParseUnit astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BRefinementNode(BRefinement concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BRefinement buildModelObjectFromAST(ARefinementMachineParseUnit astNode) {
		BRefinement returned = getModelFactory().makeRefinement();
		returned.setName(getComponentName(astNode.getHeader()));
		BResource refinedResource = getModelFactory().getResourceWithName(astNode.getRefMachine().getText());
		returned.setRefinesComponent(refinedResource);
		return returned;
	}

	@Override
	protected void performPrettyPrintHeader(boolean hasParsedVersion) {
		appendStaticContents("", getKeyword(), LINE_SEPARATOR, null);// TODO: match fragment
		appendDynamicContents(INDENTATION, () -> getModelObject().getName(), getComponentNameFragment());
		appendStaticContents(LINE_SEPARATOR, "REFINES", null);// TODO: match fragment
		appendDynamicContents(" ", () -> getModelObject().getRefinesComponentName(), getRefinesComponentNameFragment());
	}

	@Override
	protected String getKeyword() {
		return "REFINEMENT";
	}

	@Override
	protected RawSourceFragment getComponentNameFragment() {
		if (getASTNode() != null) {
			return getFragment(((AMachineHeader) getASTNode().getHeader()).getName());
		}
		return null;
	}

	protected RawSourceFragment getRefinesComponentNameFragment() {
		if (getASTNode() != null) {
			return getFragment(getASTNode().getRefMachine());
		}
		return null;
	}

}
