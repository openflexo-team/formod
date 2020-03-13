/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

import java.io.IOException;

import org.openflexo.foundation.DefaultPamelaResourceModelFactory;
import org.openflexo.foundation.resource.DirectoryBasedIODelegate;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.pamela.ModelContextLibrary;
import org.openflexo.pamela.converter.RelativePathResourceConverter;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BComponent.BComponentType;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.BResource;

/**
 * Factory for AtelierB<br>
 * 
 * @author sylvain
 * 
 */
public class AtelierBProjectFactory extends DefaultPamelaResourceModelFactory<AtelierBProjectResource> {

	private RelativePathResourceConverter relativePathResourceConverter;

	public AtelierBProjectFactory(AtelierBProjectResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(resource, ModelContextLibrary.getModelContext(AtelierBProject.class));
		setEditingContext(editingContext);
		addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
		if (resource != null && resource.getIODelegate() != null && resource.getIODelegate().getSerializationArtefactAsResource() != null) {
			relativePathResourceConverter
					.setContainerResource(resource.getIODelegate().getSerializationArtefactAsResource().getContainer());
		}
	}

	public AtelierBProject makeNewAtelierBProject() {
		return newInstance(AtelierBProject.class);
	}

	private AtelierBComponent makeNewComponent(String name, String suffix, BComponentType componentType)
			throws SaveResourceException, ModelDefinitionException, IOException {
		AtelierBComponent returned = newInstance(AtelierBComponent.class);
		returned.setName(name);
		returned.setUser("openflexo");
		returned.setSuffix(suffix);
		AtelierBComponentInfo info = newInstance(AtelierBComponentInfo.class);
		info.setName(name);
		switch (componentType) {
			case System:
				info.setType("system");
				break;
			case Refinement:
				info.setType("refinement");
				break;
			case Machine:
				info.setType("machine");
				break;
			case Implementation:
				info.setType("implementation");
				break;
			default:
				break;
		}
		returned.setComponentInfo(info);
		if (getResource().getIODelegate() instanceof DirectoryBasedIODelegate) {
			DirectoryBasedIODelegate ioDelegate = (DirectoryBasedIODelegate) getResource().getIODelegate();
			// File newComponentFile = new File(ioDelegate.getDirectory(), name + "." + suffix);
			BResource newBResource = getResource().getTechnologyAdapter().getAtelierBProjectResourceFactory().getBResourceFactory()
					.makeInnerBResource(name + "." + suffix, getResource(), componentType, true);
			BComponent newComponent = newBResource.getLoadedResourceData();
			newComponent.setName(name);
			newBResource.save();
			returned.setComponentResource(newBResource);
			System.out.println("created resource " + newBResource);
			returned.setPath(ioDelegate.getDirectory().getCanonicalPath());

		}

		return returned;
	}

	public AtelierBComponent makeNewSystem(String name) throws SaveResourceException, ModelDefinitionException, IOException {
		return makeNewComponent(name, "sys", BComponentType.System);
	}

	public AtelierBComponent makeNewRefinement(String name) throws SaveResourceException, ModelDefinitionException, IOException {
		return makeNewComponent(name, "ref", BComponentType.Refinement);
	}

	public AtelierBComponent makeNewMachine(String name) throws SaveResourceException, ModelDefinitionException, IOException {
		return makeNewComponent(name, "mch", BComponentType.Machine);
	}

	public AtelierBComponent makeNewImplementation(String name) throws SaveResourceException, ModelDefinitionException, IOException {
		return makeNewComponent(name, "imp", BComponentType.Implementation);
	}

}
