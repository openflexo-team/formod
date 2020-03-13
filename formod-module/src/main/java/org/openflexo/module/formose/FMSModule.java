/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

package org.openflexo.module.formose;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.task.Progress;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.Module;
import org.openflexo.module.formose.controller.FMSController;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.view.controller.FlexoController;

/**
 * A module dedicated to {@link FormoseProject} edition
 * 
 * @author sylvain
 *
 */
public class FMSModule extends FlexoModule<FMSModule> {

	public static final String FMS_MODULE_SHORT_NAME = "FMS";
	public static final String FMS_MODULE_NAME = "Formod";
	private static final Logger logger = Logger.getLogger(FMSModule.class.getPackage().getName());

	public FMSModule(ApplicationContext applicationContext) {
		super(applicationContext);
		Progress.progress(FlexoLocalization.getMainLocalizer().localizedForKey("build_editor"));
	}

	@Override
	public Module<FMSModule> getModule() {
		return FormoseEditor.INSTANCE;
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/Formose";
	}

	@Override
	public void initModule() {
		super.initModule();
		TechnologyAdapterService taService = getApplicationContext().getTechnologyAdapterService();
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLRTTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(DiagramTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(DocXTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(OWLTechnologyAdapter.class), true);
		Progress.progress(getLocales().localizedForKey("load_formose_viewpoint"));
		initFormoseViewpoint();
	}

	/**
	 * Create a binded carto editor controller.
	 *
	 * @return a freshly created CEController.
	 */
	@Override
	protected FlexoController createControllerForModule() {
		return new FMSController(this);
	}

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		}
		else {
			return getFlexoController().reviewModifiedResources() && super.close();
		}
	}

	private void initFormoseViewpoint() {
		VirtualModelResource fmsVirtualModelResource = getApplicationContext().getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.FORMOSE_VIEWPOINT_URI);
		if (fmsVirtualModelResource == null) {
			logger.severe("Cannot find Formose viewpoint !!!!");
			System.out.println("RCs=" + getApplicationContext().getResourceCenterService().getResourceCenters());
			// JarResourceCenter.addNamedJarFromClassPath(getApplicationContext().getResourceCenterService(),
			// "fr/lacl/formose_rc/1.0/formose_rc-1.0");
		}
	}
}
