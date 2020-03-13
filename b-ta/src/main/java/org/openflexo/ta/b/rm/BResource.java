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

package org.openflexo.ta.b.rm;

import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.atelierb.AtelierBProject;
import org.openflexo.ta.b.model.parser.ParseException;

/**
 * A resource storing a {@link BComponent}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(BResourceImpl.class)
public interface BResource extends TechnologyAdapterResource<BComponent, BTechnologyAdapter>, PamelaResource<BComponent, BModelFactory> {

	// @PropertyIdentifier(type = String.class)
	public static final String RAW_SOURCE_KEY = "rawSource";
	@PropertyIdentifier(type = AtelierBProject.class)
	public static final String ATELIER_B_PROJECT_KEY = "atelierBProject";

	/**
	 * Return raw source for this resource
	 * 
	 * @return
	 */
	// @Getter(value = RAW_SOURCE_KEY)
	public String getRawSource();

	/**
	 * Sets raw source for this resource
	 * 
	 * @return
	 */
	/*@Setter(RAW_SOURCE_KEY)
	public void setRawSource(String rawSource);*/

	/**
	 * Convenient method to retrieve resource data
	 * 
	 * @return
	 */
	public BComponent getBComponent();

	/**
	 * Returns the component name this resource stores.<br>
	 * Stores this name in resource meta data when possible
	 * 
	 * @return
	 */
	public String getComponentName();

	/**
	 * Update resource with supplied raw source
	 * 
	 * @param rawSource
	 */
	public void updateWithRawSource(String rawSource) throws ParseException;
}
