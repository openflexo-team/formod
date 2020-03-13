/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.ta.b.rm;

import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BComponent.BComponentType;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;

/**
 * Implementation of PamelaResourceFactory for {@link BResource} inside a {@link AtelierBProjectResource}
 * 
 * @author sylvain
 *
 */
public class InnerAtelierBProjectResourceFactory extends BResourceFactory {

	private static final Logger logger = Logger.getLogger(InnerAtelierBProjectResourceFactory.class.getPackage().getName());

	public InnerAtelierBProjectResourceFactory() throws ModelDefinitionException {
		super();
	}

	public <I> BResource retrieveInnerBResource(I serializationArtefact, AtelierBProjectResource projectResource)
			throws ModelDefinitionException, IOException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) projectResource.getResourceCenter();
		String name = resourceCenter.retrieveName(serializationArtefact);

		BResource returned = initResourceForRetrieving(serializationArtefact, resourceCenter);
		returned.setURI(projectResource.getURI() + "/" + name);

		projectResource.addToContents(returned);
		projectResource.notifyContentsAdded(returned);

		registerBResource(returned, projectResource);
		registerResource(returned, resourceCenter);

		return returned;
	}

	private void registerBResource(BResource returned, AtelierBProjectResource projectResource) {
		String name = returned.getName().substring(0, returned.getName().lastIndexOf("."));
		AtelierBComponent component = projectResource.getAtelierBProject().getAtelierBProjectDefinition().getComponentNamed(name);
		if (component != null) {
			component.setComponentResource(returned);
		}
		else {
			logger.warning("No component definition for name:" + name);
		}
	}

	public <I> BResource makeInnerBResource(String name, AtelierBProjectResource projectResource, BComponentType componentType,
			boolean createEmptyContents) throws SaveResourceException, ModelDefinitionException {

		FlexoResourceCenter<I> resourceCenter = (FlexoResourceCenter<I>) projectResource.getResourceCenter();
		I dbArtefact = resourceCenter.getContainer((I) projectResource.getIODelegate().getSerializationArtefact());
		I containerArtefact = resourceCenter.getContainer(dbArtefact);

		I serializationArtefact = resourceCenter.createEntry(name, containerArtefact);

		BResource returned = initResourceForCreation(serializationArtefact, resourceCenter, name, projectResource.getURI() + "/" + name);

		projectResource.addToContents(returned);
		projectResource.notifyContentsAdded(returned);

		registerBResource(returned, projectResource);
		registerResource(returned, resourceCenter);

		if (createEmptyContents) {
			BComponent resourceData = makeEmptyResourceData(returned, componentType);
			resourceData.setResource(returned);
			returned.setResourceData(resourceData);
			returned.setModified(true);
			returned.save();
		}

		return returned;
	}

}
