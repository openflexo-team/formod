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

package org.openflexo.ta.b;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactories;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.ta.b.fml.binding.BBindingFactory;
import org.openflexo.ta.b.rm.AtelierBProjectRepository;
import org.openflexo.ta.b.rm.AtelierBProjectResourceFactory;
import org.openflexo.ta.b.rm.BResourceFactory;
import org.openflexo.ta.b.rm.BResourceRepository;

/**
 * B technology adapter built over a sablecc grammar
 * 
 * We offer the connexion to B resources (system, machine, refinement, implementation)
 * 
 * @author sylvain
 * 
 */
@DeclareModelSlots({ AtelierBProjectModelSlot.class, BModelSlot.class })
@DeclareResourceFactories({ AtelierBProjectResourceFactory.class, BResourceFactory.class })
public class BTechnologyAdapter extends TechnologyAdapter<BTechnologyAdapter> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BTechnologyAdapter.class.getPackage().getName());

	private static final BBindingFactory BINDING_FACTORY = new BBindingFactory();

	@Override
	public String getName() {
		return "B technology adapter";
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/BTechnologyAdapter";
	}

	@Override
	public void ensureAllRepositoriesAreCreated(FlexoResourceCenter<?> rc) {
		super.ensureAllRepositoriesAreCreated(rc);
		getAtelierBProjectRepository(rc);
		getBResourceRepository(rc);

	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {

		// We ignore all systems found in a AtelierB project
		if (getBResourceFactory().isValidArtefact(contents, resourceCenter)) {
			I potentialProject = resourceCenter.getContainer(contents);
			if (getAtelierBProjectResourceFactory().isValidArtefact(potentialProject, resourceCenter)) {
				return true;
			}
			if (resourceCenter.retrieveName(potentialProject).equals("src")) {
				return true;
			}
			if (resourceCenter.retrieveName(potentialProject).equals("expand_src")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public String getIdentifier() {
		return "B";
	}

	public BResourceFactory getBResourceFactory() {
		return getResourceFactory(BResourceFactory.class);
	}

	public AtelierBProjectResourceFactory getAtelierBProjectResourceFactory() {
		return getResourceFactory(AtelierBProjectResourceFactory.class);
	}

	@SuppressWarnings("unchecked")
	public <I> BResourceRepository<I> getBResourceRepository(FlexoResourceCenter<I> resourceCenter) {
		BResourceRepository<I> returned = resourceCenter.retrieveRepository(BResourceRepository.class, this);
		if (returned == null) {
			returned = BResourceRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, BResourceRepository.class, this);
		}
		return returned;
	}

	@SuppressWarnings("unchecked")
	public <I> AtelierBProjectRepository<I> getAtelierBProjectRepository(FlexoResourceCenter<I> resourceCenter) {
		AtelierBProjectRepository<I> returned = resourceCenter.retrieveRepository(AtelierBProjectRepository.class, this);
		if (returned == null) {
			returned = AtelierBProjectRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, AtelierBProjectRepository.class, this);
		}
		return returned;
	}

}
