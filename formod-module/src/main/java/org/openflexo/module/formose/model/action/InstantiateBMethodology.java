/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.module.formose.model.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.InvalidArgumentException;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FormoseEditor;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.AtelierBProjectResourceFactory;

/**
 * @author sylvain
 */
public class InstantiateBMethodology extends FMSAction<InstantiateBMethodology, FlexoConceptInstance, FlexoObject> {

	public static final FlexoActionFactory<InstantiateBMethodology, FlexoConceptInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<InstantiateBMethodology, FlexoConceptInstance, FlexoObject>(
			"instantiate_B_methodology", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public InstantiateBMethodology makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new InstantiateBMethodology(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return element != null && element.getFlexoConcept() != null
					&& element.getFlexoConcept().getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection)
					&& element.getFlexoConcept().getName().equals(FMSConstants.ELEMENT_CONCEPT_NAME)
					&& element.getFlexoPropertyValue("applicableBMethodology") == null
					&& element.getFlexoPropertyValue("applicableDomainModelMethodology") != null;
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	private String description;
	private File sourceProjectFolder;
	private File generatedProjectFolder;
	private File sourceCreationFolder;
	private String choice;
	private String newProjectName;

	private FlexoConceptInstance element;

	public InstantiateBMethodology(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
			final FlexoEditor editor) {
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

	// createSysMLKaosMethodology

	public String getChoice() {
		return choice == null ? "open_existing_Bproject" : choice;
	}

	public void setChoice(String choice) {
		if ((choice == null && this.choice != null) || (choice != null && !choice.equals(this.choice))) {
			String oldValue = this.choice;
			this.choice = choice;
			getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
		}
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {
		File projectFolder = null;
		if (getChoice().equals("open_existing_Bproject")) {
			if (getSourceProjectFolder() == null || !getSourceProjectFolder().exists()) {
				throw new InvalidArgumentException("No source project folder specified");
			}
			projectFolder = getSourceProjectFolder();
		}
		else {

			try {
				projectFolder = AtelierBProjectResourceFactory
						.generateBlankAtelierBProject(new File(getSourceCreationFolder(), getNewProjectName()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new FlexoException(e);
			}
		}

		FlexoResourceCenter<File> resourceCenter = getServiceManager().getResourceCenterService()
				.getResourceCenterContaining(projectFolder);

		/*for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
			System.out.println(" > " + rc);
		}
		
		System.out.println("resourceCenter=" + resourceCenter);*/

		if (resourceCenter == null) {
			// No resource center containing folder, declares a new one
			FlexoResourceCenterService rcService = getServiceManager().getResourceCenterService();
			try {
				resourceCenter = DirectoryResourceCenter.instanciateNewDirectoryResourceCenter(projectFolder.getParentFile(), rcService);
			} catch (IOException e) {
				throw new FlexoException(e);
			}
			rcService.addToResourceCenters(resourceCenter);
		}
		File dbFile = new File(new File(projectFolder, "bdp"), projectFolder.getName() + ".db");
		AtelierBProjectResource sourceProjectResource = resourceCenter.getResource(dbFile, AtelierBProjectResource.class);

		System.out.println("sourceProjectResource=" + sourceProjectResource);

		try {
			newMethodology = getFocusedObject().execute("createBMethodology({$source},{$generated})", sourceProjectResource,
					sourceProjectResource);
		} catch (TypeMismatchException e) {
			e.printStackTrace();
			throw new FlexoException(e);
		} catch (NullReferenceException e) {
			e.printStackTrace();
			throw new FlexoException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new FlexoException(e);
		} catch (InvalidBindingException e) {
			e.printStackTrace();
			throw new FlexoException(e);
		}

		getFocusedObject().getPropertyChangeSupport().firePropertyChange(FMSConstants.METHODOLOGY_ROLE_NAME, null, getNewMethodology());
	}

	private FMLRTVirtualModelInstance newMethodology;

	public FMLRTVirtualModelInstance getNewMethodology() {
		return newMethodology;
	}

	public FlexoConceptInstance getElement() {
		FMLRTVirtualModelInstance domainModelMethodology = getFocusedObject().getFlexoPropertyValue("applicableDomainModelMethodology");
		if (domainModelMethodology != null) {
			try {
				return domainModelMethodology.execute("declaringElement");
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
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

	public FMLRTVirtualModelInstance getFormoseVMI() {
		return (FMLRTVirtualModelInstance) getElement().getOwningVirtualModelInstance();
	}

	public File getSourceProjectFolder() {
		return sourceProjectFolder;
	}

	public void setSourceProjectFolder(File sourceProjectFolder) {
		if ((sourceProjectFolder == null && this.sourceProjectFolder != null)
				|| (sourceProjectFolder != null && !sourceProjectFolder.equals(this.sourceProjectFolder))) {
			File oldValue = this.sourceProjectFolder;
			this.sourceProjectFolder = sourceProjectFolder;
			getPropertyChangeSupport().firePropertyChange("sourceProjectFolder", oldValue, sourceProjectFolder);
		}
	}

	public File getSourceCreationFolder() {
		return sourceCreationFolder;
	}

	public void setSourceCreationFolder(File sourceCreationFolder) {
		if ((sourceCreationFolder == null && this.sourceCreationFolder != null)
				|| (sourceCreationFolder != null && !sourceCreationFolder.equals(this.sourceCreationFolder))) {
			File oldValue = this.sourceCreationFolder;
			this.sourceCreationFolder = sourceCreationFolder;
			getPropertyChangeSupport().firePropertyChange("sourceCreationFolder", oldValue, sourceCreationFolder);
		}
	}

	public String getNewProjectName() {
		return newProjectName;
	}

	public void setNewProjectName(String newProjectName) {
		if ((newProjectName == null && this.newProjectName != null)
				|| (newProjectName != null && !newProjectName.equals(this.newProjectName))) {
			String oldValue = this.newProjectName;
			this.newProjectName = newProjectName;
			getPropertyChangeSupport().firePropertyChange("newProjectName", oldValue, newProjectName);
		}
	}

	public File getGeneratedProjectFolder() {
		return generatedProjectFolder;
	}

	public void setGeneratedProjectFolder(File generatedProjectFolder) {
		if ((generatedProjectFolder == null && this.generatedProjectFolder != null)
				|| (generatedProjectFolder != null && !generatedProjectFolder.equals(this.generatedProjectFolder))) {
			File oldValue = this.generatedProjectFolder;
			this.generatedProjectFolder = generatedProjectFolder;
			getPropertyChangeSupport().firePropertyChange("generatedProjectFolder", oldValue, generatedProjectFolder);
		}
	}

}
