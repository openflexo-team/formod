/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.b.fml.editionaction;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.BAbstractVariable;
import org.openflexo.ta.b.model.BComponent;

@ModelEntity
@ImplementationClass(AddBAbstractVariable.AddBAbstractVariableImpl.class)
@XMLElement
@FML("AddBAbstractVariable")
public interface AddBAbstractVariable extends AddBMember<BAbstractVariable> {

	public static abstract class AddBAbstractVariableImpl extends AddBMemberImpl<BAbstractVariable> implements AddBAbstractVariable {

		@Override
		public Class<BAbstractVariable> getAssignableType() {
			return BAbstractVariable.class;
		}

		@Override
		protected BAbstractVariable makeMember(String name, BComponent component) {
			return component.getFactory().makeAbstractVariable(name);
		}

		@Override
		protected void addToMembers(BAbstractVariable newVariable, BComponent component) {
			component.addToAbstractVariables(newVariable);
		}

	}
}
