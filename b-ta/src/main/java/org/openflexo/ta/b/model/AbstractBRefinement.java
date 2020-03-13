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

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.ta.b.rm.BResource;

/**
 * Represents an abstract B refinement (REFINEMENT or IMPLEMENTATION)
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface AbstractBRefinement extends BComponent {

	@PropertyIdentifier(type = BResource.class)
	public static final String REFINES_COMPONENT_KEY = "refinesComponent";

	/**
	 * Return {@link BResource} this component refines
	 * 
	 * @return
	 */
	@Getter(value = REFINES_COMPONENT_KEY)
	public BResource getRefinesComponent();

	/**
	 * Sets {@link BResource} this component refines
	 * 
	 * @return
	 */
	@Setter(REFINES_COMPONENT_KEY)
	public void setRefinesComponent(BResource aBResource);

	@Getter(value = "refinesComponentName")
	@XMLAttribute
	public String getRefinesComponentName();

	/**
	 * Default base implementation for {@link AbstractBRefinement}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class AbstractBRefinementImpl extends BComponentImpl implements AbstractBRefinement {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(AbstractBRefinement.class.getPackage().getName());

		@Override
		public String getRefinesComponentName() {
			if (getRefinesComponent() != null) {
				return getRefinesComponent().getComponentName();
			}
			return null;
		}

		@Override
		protected String getRefinesClause() {
			return "REFINES\n" + "    " + getRefinesComponentName() + "\n";
		}

	}
}
