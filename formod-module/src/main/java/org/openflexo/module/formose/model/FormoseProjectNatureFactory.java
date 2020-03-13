/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either version 1.1 of the License, or any later version ),
 * which is available at https://joinup.ec.europa.eu/software/page/eupl/licence-eupl and the GNU General Public License (GPL, either version
 * 3 of the License, or any later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you must include the following additional permission.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with software containing parts covered by the terms of EPL
 * 1.0, the licensors of this Program grant you additional permission to convey the resulting work. *
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org) or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.module.formose.model;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.nature.DefaultProjectNatureFactoryImpl;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.module.formose.model.action.GivesFormoseNature;

/**
 * This class is used to interpret a {@link FlexoProject} as a {@link EAProject}<br>
 * 
 * A {@link FlexoProject} has the {@link EAProject} nature if it contains at least a view conform to formose viewpoint<br>
 * The first found view will be considered as the Formose View
 * 
 * @author sylvain
 */
public class FormoseProjectNatureFactory extends DefaultProjectNatureFactoryImpl<FormoseProjectNature> {

	static final Logger logger = FlexoLogger.getLogger(FormoseProjectNatureFactory.class.getPackage().getName());

	public FormoseProjectNatureFactory() {
		super(FormoseProjectNature.class);
	}

	@Override
	public FormoseProjectNature givesNature(FlexoProject<?> project, FlexoEditor editor) {
		GivesFormoseNature givesEANature = GivesFormoseNature.actionType.makeNewAction(project, null, editor);
		givesEANature.doAction();

		return givesEANature.getNewNature();
	}

}

/*public class FormoseProjectNatureFactory implements ProjectNature<FormoseProjectNatureFactory, FormoseProject> {

	private static final Logger LOGGER = Logger.getLogger(FormoseProjectNatureFactory.class.getPackage().getName());

	private static final Resource ERROR_DIALOG = ResourceLocator.locateResource("Fib/Dialog/ErrorProjectNatureDialog.fib");

	private ProjectNatureService projectNatureService;

	private final Map<FlexoProject, FormoseProject> projectsMap;

	public FormoseProjectNatureFactory() {
		projectsMap = new HashMap<FlexoProject, FormoseProject>();
	}

	public boolean authorizeInit(FlexoProject project) {
		// if no VP in ResourceCenters, add VP resources in project.
		if (getFormoseViewPoint(project.getServiceManager()) == null) {
			// Do dialog
			FIBComponent fibComponent = ApplicationFIBLibraryImpl.instance().retrieveFIBComponent(ERROR_DIALOG);
			JFIBDialog.instanciateAndShowDialog(fibComponent, null, FlexoFrame.getActiveFrame(), true,
					new FlexoFIBController(fibComponent, SwingViewFactory.INSTANCE, FlexoFrame.getActiveFrame().getController()));
			return false;
		}
		return true;
	}

	@Override
	public void givesNature(final FlexoProject project, final FlexoEditor editor) {
		System.out.println("On donne donc la nature");
		ConvertToFormoseProject action = ConvertToFormoseProject.ACTION_TYPE.makeNewAction(project, null, editor);
		action.doAction();
		System.out.println("Et hop c'est fait");
	}

	@Override
	public FormoseProject getProjectWrapper(final FlexoProject files) {
		return getFormoseProject(files);
	}

	@Override
	public ProjectNatureService getProjectNatureService() {
		return this.projectNatureService;
	}

	@Override
	public void setProjectNatureService(ProjectNatureService projectNatureService) {
		this.projectNatureService = projectNatureService;
	}

	@Override
	public boolean hasNature(final FlexoProject<?> project) {
		if (project == null || project.getVirtualModelInstanceRepository().getAllResources().size() == 0) {
			return false;
		}

		VirtualModel formoseVirtualModel = getFormoseViewPoint(project.getServiceManager());
		if (formoseVirtualModel == null) {
			return false;
		}

		for (FMLRTVirtualModelInstanceResource viewResource : project.getVirtualModelInstanceRepository().getAllResources()) {
			if (viewResource.getVirtualModelResource() != null
					&& viewResource.getVirtualModelResource() == formoseVirtualModel.getResource()) {
				return true;
			}
		}

		return false;
	}

	public VirtualModel getFormoseViewPoint(FlexoServiceManager serviceManager) {
		final List<FlexoResourceCenter<?>> lst = serviceManager.getResourceCenterService().getResourceCenters();
		VirtualModel ceVirtualModel = null;
		for (FlexoResourceCenter<?> resourceCenter : lst) {
			try {
				ceVirtualModel = resourceCenter.getServiceManager().getVirtualModelLibrary()
						.getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
			} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (ceVirtualModel != null) {
				break;
			}
		}
		return ceVirtualModel;
	}

	public FormoseProject getFormoseProject(FlexoProject<?> project) {
		FormoseProject returned = projectsMap.get(project);
		if (returned == null) {
			try {
				ModelFactory factory = new ModelFactory(FormoseProject.class);
				returned = factory.newInstance(FormoseProject.class);
				returned.init(project, this);
				projectsMap.put(project, returned);
			} catch (ModelDefinitionException e) {
				LOGGER.log(Level.SEVERE, "Error while initializing new FormoseProject", e);
			}
		}
		return returned;
	}

	public FMLRTVirtualModelInstance getFormoseView(final FlexoProject<?> project) {
		if (project != null && project.hasNature(this)) {
			VirtualModel formoseVirtualModel = getFormoseViewPoint(project.getServiceManager());
			if (formoseVirtualModel == null) {
				return null;
			}
			for (FMLRTVirtualModelInstanceResource viewResource : project.getVirtualModelInstanceRepository().getAllResources()) {
				if (viewResource.getVirtualModelResource() != null
						&& viewResource.getVirtualModelResource() == formoseVirtualModel.getResource()) {
					try {
						return viewResource.getResourceData();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ResourceLoadingCancelledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FlexoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}*/
