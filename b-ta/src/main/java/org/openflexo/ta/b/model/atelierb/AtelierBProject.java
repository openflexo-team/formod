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

package org.openflexo.ta.b.model.atelierb;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.BObject.BObjectImpl;
import org.openflexo.ta.b.model.atelierb.AtelierBProject.AtelierBProjectImpl;
import org.openflexo.ta.b.rm.AtelierBProjectResource;

/**
 * A AtelierB project
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(AtelierBProjectImpl.class)
@XMLElement(xmlTag = "db_xml")
public interface AtelierBProject extends AtelierBProjectObject, ResourceData<AtelierBProject> {

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";
	@PropertyIdentifier(type = AtelierBDefinition.class)
	public static final String ATELIER_B_DEFINITION_KEY = "atelierBDefinition";
	@PropertyIdentifier(type = AtelierBProjectDefinition.class)
	public static final String ATELIER_B_PROJECT_DEFINITION_KEY = "atelierBProjectDefinition";

	/**
	 * Return name of this object
	 * 
	 * @return
	 */
	@Getter(value = NAME_KEY)
	@XMLAttribute
	public String getName();

	/**
	 * Sets name of this object
	 * 
	 * @return
	 */
	@Setter(NAME_KEY)
	public void setName(String aName);

	@Getter(value = ATELIER_B_DEFINITION_KEY, inverse = AtelierBDefinition.ATELIER_B_PROJECT_KEY)
	@XMLElement
	public AtelierBDefinition getAtelierBDefinition();

	@Setter(ATELIER_B_DEFINITION_KEY)
	public void setAtelierBDefinition(AtelierBDefinition value);

	@Getter(value = ATELIER_B_PROJECT_DEFINITION_KEY, inverse = AtelierBProjectDefinition.ATELIER_B_PROJECT_KEY)
	@XMLElement
	public AtelierBProjectDefinition getAtelierBProjectDefinition();

	@Setter(ATELIER_B_PROJECT_DEFINITION_KEY)
	public void setAtelierBProjectDefinition(AtelierBProjectDefinition value);

	@Override
	public AtelierBProjectResource getResource();

	public AtelierBComponent getComponentNamed(String componentName);

	/**
	 * Default base implementation for {@link AtelierBProject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class AtelierBProjectImpl extends AtelierBProjectObjectImpl implements AtelierBProject {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BObjectImpl.class.getPackage().getName());

		@Override
		public AtelierBProject getAtelierBProject() {
			return this;
		}

		@Override
		public AtelierBProjectResource getResource() {
			return (AtelierBProjectResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public AtelierBComponent getComponentNamed(String componentName) {
			if (getAtelierBProjectDefinition() != null) {
				return getAtelierBProjectDefinition().getComponentNamed(componentName);
			}
			return null;
		}

	}

}
