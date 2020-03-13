/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
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

package org.openflexo.module.formose.model.action;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.action.ModuleSpecificFlexoAction;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.nature.GivesNatureAction;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FMSModule;
import org.openflexo.module.formose.FormoseEditor;
import org.openflexo.module.formose.model.FormoseInstance;
import org.openflexo.module.formose.model.FormoseProjectNature;

/**
 * This action is called to gives Formose nature to a project
 * 
 * @author sylvain
 */
public class GivesFormoseNature extends GivesNatureAction<GivesFormoseNature, FormoseProjectNature>
		implements ModuleSpecificFlexoAction<FMSModule> {

	private static final Logger logger = Logger.getLogger(GivesFormoseNature.class.getPackage().getName());

	public static FlexoActionFactory<GivesFormoseNature, FlexoProject<?>, FlexoObject> actionType = new FlexoActionFactory<GivesFormoseNature, FlexoProject<?>, FlexoObject>(
			"gives_formose_nature") {

		/**
		 * Factory method
		 */
		@Override
		public GivesFormoseNature makeNewAction(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new GivesFormoseNature(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return project != null && !project.hasNature(FormoseProjectNature.class);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(GivesFormoseNature.actionType, FlexoProject.class);
	}

	GivesFormoseNature(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FormoseEditor.class).getLoadedModuleInstance()
					.getLocales();
		}
		return super.getLocales();
	}

	@Override
	public Class<FMSModule> getFlexoModuleClass() {
		return FMSModule.class;
	}

	public VirtualModel getFormoseVirtualModel() {
		if (getServiceManager() != null) {
			try {
				return getServiceManager().getVirtualModelLibrary().getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
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
		return null;
	}

	@Override
	public FormoseProjectNature makeNewNature() {

		System.out.println("On fait une nouvelle nature");

		FormoseProjectNature nature = getFocusedObject().getModelFactory().newInstance(FormoseProjectNature.class);

		FormoseInstance formoseVMI = retrieveFormoseInstance();

		System.out.println("FormoseVMI-1 " + formoseVMI);

		if (formoseVMI == null) {
			formoseVMI = makeFormoseInstance();
		}

		System.out.println("FormoseVMI-2 " + formoseVMI);

		nature.setFormoseInstance(formoseVMI);

		System.out.println("A la fin: " + nature.getFormoseInstance());

		return nature;
	}

	private FormoseInstance retrieveFormoseInstance() {

		VirtualModel formoseVirtualModel = getFormoseVirtualModel();
		if (formoseVirtualModel == null) {
			return null;
		}
		for (FMLRTVirtualModelInstanceResource viewResource : getFocusedObject().getVirtualModelInstanceRepository().getAllResources()) {
			if (viewResource.getVirtualModelResource() != null
					&& viewResource.getVirtualModelResource() == formoseVirtualModel.getResource()) {
				try {

					FormoseInstance newFormoseVMI = getFocusedObject().getModelFactory().newInstance(FormoseInstance.class);
					newFormoseVMI.setAccessedVirtualModelInstance(viewResource.getResourceData());
					return newFormoseVMI;
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

		return null;
	}

	private FormoseInstance makeFormoseInstance() {

		VirtualModel formoseVirtualModel = getFormoseVirtualModel();
		System.out.println("formoseVM=" + formoseVirtualModel);
		if (formoseVirtualModel == null) {
			return null;
		}

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getVirtualModelInstanceRepository().getRootFolder(), null, this);
		action.setNewVirtualModelInstanceName(FMSConstants.FORMOSE_VIEW_NAME);
		action.setNewVirtualModelInstanceTitle(FMSConstants.FORMOSE_VIEW_NAME);
		action.setVirtualModel(formoseVirtualModel);

		CreationScheme formoseViewCreationScheme = (CreationScheme) formoseVirtualModel
				.getFlexoBehaviour(FMSConstants.FORMOSE_VIEW_CREATION_SCHEME_NAME);

		action.setCreationScheme(formoseViewCreationScheme);
		action.setParameterValue(formoseViewCreationScheme.getParameter("projectName"), getFocusedObject().getProjectName());
		action.setParameterValue(formoseViewCreationScheme.getParameter("projectDescription"), getFocusedObject().getProjectDescription());
		action.doAction();
		FMLRTVirtualModelInstance formoseView = action.getNewVirtualModelInstance();

		FormoseInstance newFormoseVMI = getFocusedObject().getModelFactory().newInstance(FormoseInstance.class);
		newFormoseVMI.setAccessedVirtualModelInstance(formoseView);
		return newFormoseVMI;

	}

}

/*
 
 public class ConvertToFormoseProject extends FMSAction<ConvertToFormoseProject, FlexoProject, FlexoObject> {

	public static final ConvertToFormoseProjectActionType ACTION_TYPE = new ConvertToFormoseProjectActionType();

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoProject.class);
	}

	public static final class ConvertToFormoseProjectActionType
			extends FlexoActionFactory<ConvertToFormoseProject, FlexoProject, FlexoObject> {

		private ConvertToFormoseProjectActionType() {
			super("convert_to_formose_project", FlexoActionFactory.convertMenu, FlexoActionFactory.defaultGroup,
					FlexoActionFactory.NORMAL_ACTION_TYPE);
		}

		@Override
		public ConvertToFormoseProject makeNewAction(final FlexoProject focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new ConvertToFormoseProject(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoProject project, final Vector<FlexoObject> globalSelection) {
			return !project.hasNature(project.getServiceManager().getProjectNatureService().getProjectNature(FormoseProjectNature.class));
		}

		@Override
		public boolean isEnabledForSelection(final FlexoProject project, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(project, globalSelection);
		}
	}

	private String projectName;
	private String description;
	private FormoseProject formoseProject;

	public ConvertToFormoseProject(final FlexoProject focusedObject, final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(FormoseEditor.class).getLoadedModuleInstance()
					.getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {
		final FormoseProjectNature nature = getServiceManager().getProjectNatureService().getProjectNature(FormoseProjectNature.class);
		final VirtualModel formoseViewPoint = nature.getFormoseViewPoint(getServiceManager());

		if (!nature.authorizeInit(getFocusedObject())) {
			throw new FlexoException("formose_viewpoint_not_found");
		}

		FMLRTVirtualModelInstance formoseView = nature.getFormoseView(getFocusedObject());
		if (formoseView == null) {
			CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
					.makeNewEmbeddedAction(getFocusedObject().getVirtualModelInstanceRepository().getRootFolder(), null, this);
			action.setNewVirtualModelInstanceName(FMSConstants.FORMOSE_VIEW_NAME);
			action.setNewVirtualModelInstanceTitle(FMSConstants.FORMOSE_VIEW_NAME);
			action.setVirtualModel(formoseViewPoint);

			// System.out.println("VirtualModel = " + formoseVirtualModel);
			// System.out.println("FML = " + formoseVirtualModel.getFMLRepresentation());
			// System.out.println("CreationScheme name = " + FMSConstants.FORMOSE_VIEW_CREATION_SCHEME_NAME);
			// System.out.println("CreationScheme=" +
			// formoseVirtualModel.getFlexoBehaviour(FMSConstants.FORMOSE_VIEW_CREATION_SCHEME_NAME));
			// System.out.println(
			// "FML=" + formoseVirtualModel.getFlexoBehaviour(FMSConstants.FORMOSE_VIEW_CREATION_SCHEME_NAME).getFMLRepresentation());

			CreationScheme formoseViewCreationScheme = (CreationScheme) formoseViewPoint
					.getFlexoBehaviour(FMSConstants.FORMOSE_VIEW_CREATION_SCHEME_NAME);

			action.setCreationScheme(formoseViewCreationScheme);
			action.setParameterValue(formoseViewCreationScheme.getParameter("projectName"), getProjectName());
			action.setParameterValue(formoseViewCreationScheme.getParameter("projectDescription"), getDescription());
			action.doAction();
			formoseView = action.getNewVirtualModelInstance();
		}

		getFocusedObject().setProjectDescription(getDescription());

		formoseProject = nature.getFormoseProject(getFocusedObject());

		// We have now to notify project of nature modifications
		getFocusedObject().getPropertyChangeSupport().firePropertyChange("asNature(String)", false, true);
		getFocusedObject().getPropertyChangeSupport().firePropertyChange("hasNature(String)", false, true);

		formoseProject.getPropertyChangeSupport().firePropertyChange("documentLibrary", null, formoseProject.getDocumentLibrary());
		formoseProject.getPropertyChangeSupport().firePropertyChange("formoseVirtualModelInstance", null,
				formoseProject.getFormoseVirtualModelInstance());

	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		return true;
	}

	// ====================
	// GETTERS AND SETTERS
	// ====================

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		if ((projectName == null && this.projectName != null) || (projectName != null && !projectName.equals(this.projectName))) {
			String oldValue = this.projectName;
			this.projectName = projectName;
			getPropertyChangeSupport().firePropertyChange("projectName", oldValue, projectName);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
		}
	}

	public FormoseProject getFormoseProject() {
		return formoseProject;
	}
}
*/
