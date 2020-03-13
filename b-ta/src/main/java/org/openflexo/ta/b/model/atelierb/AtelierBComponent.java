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

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent.AtelierBComponentImpl;
import org.openflexo.ta.b.rm.BResource;

/**
 * A {@link BComponent} definition in an AtelierB project
 * 
 * @author sylvain
 *
 */
@ModelEntity
@XMLElement(xmlTag = "component_file")
@ImplementationClass(AtelierBComponentImpl.class)
public interface AtelierBComponent extends AtelierBProjectObject {

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";
	@PropertyIdentifier(type = String.class)
	public static final String SUFFIX_KEY = "suffix";
	@PropertyIdentifier(type = String.class)
	public static final String USER_KEY = "user";
	@PropertyIdentifier(type = String.class)
	public static final String PATH_KEY = "path";
	@PropertyIdentifier(type = AtelierBComponentInfo.class)
	public static final String COMPONENT_INFO_KEY = "componentInfo";
	@PropertyIdentifier(type = AtelierBProject.class)
	public static final String ATELIER_B_PROJECT_KEY = "atelierBProject";
	@PropertyIdentifier(type = BComponent.class)
	public static final String COMPONENT_RESOURCE_KEY = "componentResource";

	@Getter(value = NAME_KEY)
	@XMLAttribute
	public String getName();

	@Setter(NAME_KEY)
	public void setName(String aName);

	@Getter(value = SUFFIX_KEY)
	@XMLAttribute
	public String getSuffix();

	@Setter(SUFFIX_KEY)
	public void setSuffix(String suffix);

	@Getter(value = USER_KEY)
	@XMLAttribute
	public String getUser();

	@Setter(USER_KEY)
	public void setUser(String user);

	@Getter(value = PATH_KEY)
	@XMLAttribute
	public String getPath();

	@Setter(PATH_KEY)
	public void setPath(String path);

	@Getter(value = COMPONENT_INFO_KEY, inverse = AtelierBComponentInfo.COMPONENT_KEY)
	@XMLElement
	public AtelierBComponentInfo getComponentInfo();

	@Setter(COMPONENT_INFO_KEY)
	public void setComponentInfo(AtelierBComponentInfo info);

	@Override
	@Getter(value = ATELIER_B_PROJECT_KEY)
	public AtelierBProject getAtelierBProject();

	@Setter(ATELIER_B_PROJECT_KEY)
	public void setAtelierBProject(AtelierBProject project);

	@Getter(value = COMPONENT_RESOURCE_KEY, ignoreType = true)
	public BResource getComponentResource();

	@Setter(COMPONENT_RESOURCE_KEY)
	public void setComponentResource(BResource componentResource);

	public String getBPrettyPrint();

	public String getNormalizedBRepresentation();

	/**
	 * Default base implementation for {@link AtelierBProjectObject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class AtelierBComponentImpl extends AtelierBProjectObjectImpl implements AtelierBComponent {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(AtelierBComponentImpl.class.getPackage().getName());

		@Override
		public String getBPrettyPrint() {
			try {
				return getComponentResource().getResourceData().getBPrettyPrint();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String getNormalizedBRepresentation() {
			try {
				return getComponentResource().getResourceData().getNormalizedBRepresentation();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
