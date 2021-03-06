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
import org.openflexo.ta.b.model.BMachine;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.parser.node.AAbstractMachineParseUnit;
import org.openflexo.ta.b.parser.node.AMachineHeader;

/**
 * @author sylvain
 * 
 */
public class BMachineNode extends BComponentNode<AAbstractMachineParseUnit, BMachine> {

	public BMachineNode(AAbstractMachineParseUnit astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BMachineNode(BMachine concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BMachine buildModelObjectFromAST(AAbstractMachineParseUnit astNode) {
		BMachine returned = getModelFactory().makeMachine();
		returned.setName(getComponentName(astNode.getHeader()));
		return returned;
	}

	@Override
	protected String getKeyword() {
		return "MACHINE";
	}

	@Override
	protected RawSourceFragment getComponentNameFragment() {
		if (getASTNode() != null) {
			// System.out.println("Frament qui contient le nom: " + getFragment(((AMachineHeader) getASTNode().getHeader()).getName()));
			return getFragment(((AMachineHeader) getASTNode().getHeader()).getName());
		}
		return null;
	}

}
