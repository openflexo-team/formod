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

package org.openflexo.ta.b.model.parser.nodes.substitution;

import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BSubstitutionNode;
import org.openflexo.ta.b.model.substitution.BParallelSubstitution;
import org.openflexo.ta.b.parser.node.AParallelSubstitution;
import org.openflexo.ta.b.parser.node.PSubstitution;

/**
 * @author sylvain
 * 
 */
public class BParallelSubstitutionNode extends BSubstitutionNode<AParallelSubstitution, BParallelSubstitution> {

	public BParallelSubstitutionNode(AParallelSubstitution astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
	}

	public BParallelSubstitutionNode(BParallelSubstitution concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
	}

	@Override
	public BParallelSubstitution buildModelObjectFromAST(AParallelSubstitution astNode) {
		getAnalyser().push(this);
		BParallelSubstitution returned = getModelFactory().makeSubstitution(BParallelSubstitution.class, getAnalyser().getBComponent());
		for (PSubstitution sub : astNode.getSubstitutions()) {
			BSubstitutionNode<?, ?> substitutionNode = getSubstitutionFactory().makeSubstitutionNode(sub);
			returned.addToSubstitutions(substitutionNode.getModelObject());
		}
		getAnalyser().pop();
		return returned;
	}

	@Override
	public final void preparePrettyPrint(boolean hasParsedVersion) {

		super.preparePrettyPrint(hasParsedVersion);

		appendToChildrenPrettyPrintContents("", " || ", () -> getModelObject().getSubstitutions(), "", "", BSubstitution.class);

	}
}
