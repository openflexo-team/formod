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
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.ta.b.rm.BResource;

/**
 * Visibility clause, such as INCLUDES, IMPORTS, SEES, EXTENDS, USES
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BIncludesClause.class), @Import(BImportsClause.class), @Import(BSeesClause.class), @Import(BExtendsClause.class),
		@Import(BUsesClause.class) })
public interface BVisibilityClause extends BObject, InnerBComponent {

	@PropertyIdentifier(type = BResource.class)
	public static final String REFERENCED_COMPONENT_KEY = "referencedComponent";

	/**
	 * Return {@link BResource} this clause references
	 * 
	 * @return
	 */
	@Getter(value = REFERENCED_COMPONENT_KEY)
	public BResource getReferencedComponent();

	/**
	 * Sets {@link BResource} this clause references
	 * 
	 * @return
	 */
	@Setter(REFERENCED_COMPONENT_KEY)
	public void setReferencedComponent(BResource aBResource);

	@Getter(value = "referencedComponentName")
	@XMLAttribute
	public String getReferencedComponentName();

	/**
	 * Default base implementation for {@link BVisibilityClause}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class BVisibilityClauseImpl extends BObjectImpl implements BVisibilityClause {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BVisibilityClauseImpl.class.getPackage().getName());

		@Override
		public String getReferencedComponentName() {
			if (getReferencedComponent() != null) {
				return getReferencedComponent().getComponentName();
			}
			return null;
		}

		@Override
		public String getSerializationIdentifier() {
			// Cannot serialize id for visibility clause
			return null;
		}

	}

}
