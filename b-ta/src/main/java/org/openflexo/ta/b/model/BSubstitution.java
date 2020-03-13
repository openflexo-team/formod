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

package org.openflexo.ta.b.model;

import java.util.logging.Logger;

import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.ta.b.model.substitution.BAnySubstitution;
import org.openflexo.ta.b.model.substitution.BAssignSubstitution;
import org.openflexo.ta.b.model.substitution.BBecomeElementOfSubstitution;
import org.openflexo.ta.b.model.substitution.BBecomeSuchSubstitution;
import org.openflexo.ta.b.model.substitution.BBlockSubstitution;
import org.openflexo.ta.b.model.substitution.BParallelSubstitution;
import org.openflexo.ta.b.model.substitution.BPreconditionSubstitution;
import org.openflexo.ta.b.model.substitution.BSelectSubstitution;
import org.openflexo.ta.b.model.substitution.BSkipSubstitution;

/**
 * A B substitution
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BSkipSubstitution.class), @Import(BSelectSubstitution.class), @Import(BBecomeSuchSubstitution.class),
		@Import(BAnySubstitution.class), @Import(BAssignSubstitution.class), @Import(BBlockSubstitution.class),
		@Import(BBecomeElementOfSubstitution.class), @Import(BParallelSubstitution.class), @Import(BPreconditionSubstitution.class) })
public interface BSubstitution extends BObject, InnerBComponent {

	/**
	 * Default base implementation for {@link BSubstitution}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class BSubstitutionImpl extends BObjectImpl implements BSubstitution {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BObjectImpl.class.getPackage().getName());

		@Override
		public String getSerializationIdentifier() {
			// Cannot serialize id for substitution
			return null;
		}
	}

}
