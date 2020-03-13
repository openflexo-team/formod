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

import java.util.List;

import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Finder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.atelierb.AtelierBProjectObject.AtelierBProjectObjectImpl;

/**
 * Some informations on {@link AtelierBProject}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@XMLElement(xmlTag = "project")
@ImplementationClass(AtelierBProjectObjectImpl.class)
public interface AtelierBProjectDefinition extends AtelierBProjectObject {

	@PropertyIdentifier(type = String.class)
	public static final String DECOMP_DIR_KEY = "decompDir";
	@PropertyIdentifier(type = String.class)
	public static final String OLD_DECOMP_DIR_KEY = "oldDecompDir";
	@PropertyIdentifier(type = String.class)
	public static final String TYPE_KEY = "type";
	@PropertyIdentifier(type = AtelierBProject.class)
	public static final String ATELIER_B_PROJECT_KEY = "atelierBProject";
	@PropertyIdentifier(type = AtelierBComponent.class, cardinality = Cardinality.LIST)
	public static final String COMPONENTS_KEY = "components";

	@Getter(value = DECOMP_DIR_KEY)
	@XMLAttribute(xmlTag = "decomp_dir")
	public String getDecomDir();

	@Setter(DECOMP_DIR_KEY)
	public void setDecomDir(String value);

	@Getter(value = OLD_DECOMP_DIR_KEY)
	@XMLAttribute(xmlTag = "old_decomp_dir")
	public String getOldDecomDir();

	@Setter(OLD_DECOMP_DIR_KEY)
	public void setOldDecomDir(String value);

	@Getter(value = TYPE_KEY)
	@XMLAttribute(xmlTag = "type")
	public String getType();

	@Setter(TYPE_KEY)
	public void setType(String value);

	@Override
	@Getter(value = ATELIER_B_PROJECT_KEY)
	public AtelierBProject getAtelierBProject();

	@Setter(ATELIER_B_PROJECT_KEY)
	public void setAtelierBProject(AtelierBProject project);

	@Getter(value = COMPONENTS_KEY, cardinality = Cardinality.LIST)
	@XMLElement
	public List<AtelierBComponent> getComponents();

	@Finder(attribute = AtelierBComponent.NAME_KEY, collection = COMPONENTS_KEY)
	public AtelierBComponent getComponentNamed(String componentName);

	@Adder(COMPONENTS_KEY)
	public void addToComponents(AtelierBComponent aComponent);

	@Remover(COMPONENTS_KEY)
	public void removeFromComponents(AtelierBComponent aComponent);

}
