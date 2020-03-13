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

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BComponent.BComponentType;
import org.openflexo.ta.b.model.BModelFactory;

/**
 * Implementation of {@link FlexoResourceFactory} for {@link BResource}
 * 
 * @author sylvain
 *
 */
public class BResourceFactory extends TechnologySpecificPamelaResourceFactory<BResource, BComponent, BTechnologyAdapter, BModelFactory> {

	private static final Logger logger = Logger.getLogger(BResourceFactory.class.getPackage().getName());

	public static String MCH_FILE_EXTENSION = ".mch";
	public static String SYS_FILE_EXTENSION = ".sys";
	public static String REF_FILE_EXTENSION = ".ref";
	public static String IMP_FILE_EXTENSION = ".imp";

	public BResourceFactory() throws ModelDefinitionException {
		super(BResource.class);
	}

	@Override
	public BComponent makeEmptyResourceData(BResource resource) {
		return resource.getFactory().makeSystem();
	}

	public BComponent makeEmptyResourceData(BResource resource, BComponentType componentType) {
		return resource.getFactory().makeComponent(componentType);
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(MCH_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(SYS_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(REF_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(IMP_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	protected <I> BResource registerResource(BResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the repository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getBResourceRepository(resourceCenter));

		return resource;
	}

	@Override
	public BModelFactory makeModelFactory(BResource resource, TechnologyContextManager<BTechnologyAdapter> technologyContextManager)
			throws ModelDefinitionException {
		return new BModelFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

}
