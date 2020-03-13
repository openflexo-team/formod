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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * This class is used to interpret a {@link FlexoProject} as a Formose project <br>
 *
 * 
 * @author sylvain
 */
@ModelEntity
@XMLElement
@ImplementationClass(FormoseProjectNature.FormoseProjectNatureImpl.class)
public interface FormoseProjectNature extends ProjectNature<FormoseProjectNature> {

	@PropertyIdentifier(type = FormoseInstance.class)
	public static final String FORMOSE_INSTANCE = "formoseInstance";

	@Getter(value = FORMOSE_INSTANCE, inverse = FormoseInstance.NATURE)
	@XMLElement
	public FormoseInstance getFormoseInstance();

	@Setter(FORMOSE_INSTANCE)
	public void setFormoseInstance(FormoseInstance formoseVirtualModelInstance);

	public VirtualModel getFormoseViewPoint();

	public VirtualModel getDocumentLibraryVirtualModel();

	public VirtualModel getAbstractDocumentVirtualModel();

	public FlexoConcept getReferenceConcept();

	public VirtualModel getFormoseVirtualModel();

	public FlexoConcept getElementConcept();

	public FlexoConcept getRequirementConcept();

	public VirtualModel getDocAnnotationMethodologyVirtualModel();

	public FlexoConcept getElementReferenceConcept();

	public FlexoConcept getRequirementReferenceConcept();

	public List<FMLRTVirtualModelInstance> getKaosDiagrams();

	public FMLRTVirtualModelInstance getDocumentLibrary();

	public FMLRTVirtualModelInstance getFormoseVirtualModelInstance();

	public FMLRTVirtualModelInstance getDocAnnotationMethodologyVirtualModelInstance();

	public FlexoConceptInstance getProjectElement();

	public abstract class FormoseProjectNatureImpl extends ProjectNatureImpl<FormoseProjectNature> implements FormoseProjectNature {
		private static final Logger logger = Logger.getLogger(FormoseProjectNature.class.getPackage().getName());

		private VirtualModel formoseViewpoint;
		private VirtualModel documentLibraryVirtualModel;

		@Override
		public VirtualModel getFormoseViewPoint() {
			if (formoseViewpoint == null && getServiceManager() != null) {
				try {
					formoseViewpoint = getServiceManager().getVirtualModelLibrary().getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
				} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
					e.printStackTrace();
				}
			}
			return formoseViewpoint;
		}

		@Override
		public VirtualModel getDocumentLibraryVirtualModel() {
			if (documentLibraryVirtualModel == null && getServiceManager() != null) {
				try {
					documentLibraryVirtualModel = getServiceManager().getVirtualModelLibrary()
							.getVirtualModel(FMSConstants.DOCUMENT_LIBRARY_VIEWPOINT_URI);
				} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
					e.printStackTrace();
				}
			}
			return documentLibraryVirtualModel;
		}

		@Override
		public VirtualModel getAbstractDocumentVirtualModel() {
			if (getDocumentLibraryVirtualModel() != null) {
				return getDocumentLibraryVirtualModel().getVirtualModelNamed(FMSConstants.ABSTRACT_DOCUMENT_VM_NAME);
			}
			return null;
		}

		@Override
		public FlexoConcept getReferenceConcept() {
			return getAbstractDocumentVirtualModel().getFlexoConcept(FMSConstants.REFERENCE_CONCEPT_NAME);
		}

		@Override
		public VirtualModel getFormoseVirtualModel() {
			return getFormoseViewPoint().getVirtualModelNamed(FMSConstants.FORMOSE_VM_NAME);
		}

		@Override
		public FlexoConcept getElementConcept() {
			return getFormoseVirtualModel().getFlexoConcept(FMSConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementConcept() {
			return getFormoseVirtualModel().getFlexoConcept(FMSConstants.REQUIREMENT_CONCEPT_NAME);
		}

		// Concepts declared in DocumentAnnotation-Methodology

		@Override
		public VirtualModel getDocAnnotationMethodologyVirtualModel() {
			return getFormoseViewPoint().getVirtualModelNamed(FMSConstants.DOC_ANNOTATION_METHODOLOGY_VM_NAME);
		}

		@Override
		public FlexoConcept getElementReferenceConcept() {
			return getDocAnnotationMethodologyVirtualModel().getFlexoConcept(FMSConstants.ELEMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementReferenceConcept() {
			return getDocAnnotationMethodologyVirtualModel().getFlexoConcept(FMSConstants.REQUIREMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public List<FMLRTVirtualModelInstance> getKaosDiagrams() {
			VirtualModel ceVP = getFormoseViewPoint();
			FMLRTVirtualModelInstance view = getFormoseInstance().getAccessedVirtualModelInstance();

			final List<FMLRTVirtualModelInstance> returned = new ArrayList<>();
			if (ceVP != null && view != null) {

			}
			return returned;
		}

		public FMLRTVirtualModelInstance getFormoseView() {
			return getFormoseInstance().getAccessedVirtualModelInstance();
		}

		@Override
		public FMLRTVirtualModelInstance getDocumentLibrary() {
			if (getFormoseInstance().getAccessedVirtualModelInstance() != null) {
				List<VirtualModelInstance<?, ?>> vmiList = getFormoseView()
						.getVirtualModelInstancesForVirtualModel(getDocumentLibraryVirtualModel());
				if (vmiList != null && vmiList.size() > 0) {
					return (FMLRTVirtualModelInstance) vmiList.get(0);
				}
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getFormoseVirtualModelInstance() {
			if (getFormoseView() != null) {
				if (getFormoseView().getVirtualModelInstancesForVirtualModel(getFormoseVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getFormoseView().getVirtualModelInstancesForVirtualModel(getFormoseVirtualModel())
							.get(0);
				}
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getDocAnnotationMethodologyVirtualModelInstance() {
			if (getFormoseView() != null) {
				if (getFormoseView().getVirtualModelInstancesForVirtualModel(getDocAnnotationMethodologyVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getFormoseView()
							.getVirtualModelInstancesForVirtualModel(getDocAnnotationMethodologyVirtualModel()).get(0);
				}
			}
			return null;
		}

		@Override
		public FlexoConceptInstance getProjectElement() {
			if (getFormoseVirtualModelInstance() != null) {
				FlexoConceptInstanceRole role = (FlexoConceptInstanceRole) getFormoseVirtualModel()
						.getAccessibleRole(FMSConstants.PROJECT_ELEMENT_ROLE_NAME);
				return getFormoseVirtualModelInstance().getFlexoPropertyValue(role);
			}
			return null;
		}

	}

}

/*public class FormoseProjectNature implements ProjectNature<FormoseProjectNature, FormoseProject> {

	private static final Logger LOGGER = Logger.getLogger(FormoseProjectNature.class.getPackage().getName());

	private static final Resource ERROR_DIALOG = ResourceLocator.locateResource("Fib/Dialog/ErrorProjectNatureDialog.fib");

	private ProjectNatureService projectNatureService;

	private final Map<FlexoProject, FormoseProject> projectsMap;

	public FormoseProjectNature() {
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

/*@ModelEntity
@ImplementationClass(value = FormoseProjectImpl.class)
public interface FormoseProject extends FlexoObject, ProjectWrapper<FormoseProjectNature>, ResourceData<FormoseProject> {

	public static final String PROJECT_KEY = "project";
	public static final String PROJECT_NATURE_KEY = "projectNature";

	@Override
	@Getter(value = PROJECT_KEY, ignoreType = true)
	public FlexoProject<?> getProject();

	@Setter(PROJECT_KEY)
	public void setProject(FlexoProject<?> project);

	@Override
	@Getter(value = PROJECT_NATURE_KEY, ignoreType = true)
	public FormoseProjectNature getProjectNature();

	@Setter(PROJECT_NATURE_KEY)
	public void setProjectNature(FormoseProjectNature projectNature);

	public FMLRTVirtualModelInstance getFormoseView();

	public void init(FlexoProject<?> project, FormoseProjectNature nature);

	public String getName();

	public VirtualModel getAbstractDocumentVirtualModel();

	public FlexoConcept getReferenceConcept();

	public VirtualModel getFormoseVirtualModel();

	public FlexoConcept getElementConcept();

	public FlexoConcept getRequirementConcept();

	public VirtualModel getDocAnnotationMethodologyVirtualModel();

	public FlexoConcept getElementReferenceConcept();

	public FlexoConcept getRequirementReferenceConcept();

	public VirtualModel getFormoseViewPoint();

	public VirtualModel getDocumentLibraryVirtualModel();

	public FMLRTVirtualModelInstance getFormoseVirtualModelInstance();

	public FMLRTVirtualModelInstance getDocAnnotationMethodologyVirtualModelInstance();

	public FMLRTVirtualModelInstance getDocumentLibrary();

	public List<FMLRTVirtualModelInstance> getKaosDiagrams();

	public FlexoConceptInstance getProjectElement();

	public List<FlexoConceptInstance> getChildrenElements(FlexoConceptInstance element);

	public static abstract class FormoseProjectImpl extends FlexoObjectImpl implements FormoseProject {

		@Override
		public void init(FlexoProject project, FormoseProjectNature nature) {
			this.setProject(project);
			this.setProjectNature(nature);
		}

		@Override
		public String getName() {
			if (getProject() != null) {
				return getProject().getName();
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getFormoseView() {
			return getProjectNature().getFormoseView(getProject());
		}

		@Override
		public VirtualModel getFormoseViewPoint() {
			final List<FlexoResourceCenter<?>> lst = getProject().getServiceManager().getResourceCenterService().getResourceCenters();
			VirtualModel formoseVirtualModel = null;
			for (FlexoResourceCenter<?> resourceCenter : lst) {
				try {
					formoseVirtualModel = resourceCenter.getServiceManager().getVirtualModelLibrary()
							.getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
				} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (formoseVirtualModel != null) {
					break;
				}
			}
			return formoseVirtualModel;
		}

		@Override
		public VirtualModel getDocumentLibraryVirtualModel() {
			if (getProject() != null) {
				final List<FlexoResourceCenter<?>> lst = getProject().getServiceManager().getResourceCenterService().getResourceCenters();
				VirtualModel documentLibraryVirtualModel = null;
				for (FlexoResourceCenter<?> resourceCenter : lst) {
					try {
						documentLibraryVirtualModel = resourceCenter.getServiceManager().getVirtualModelLibrary()
								.getVirtualModel(FMSConstants.DOCUMENT_LIBRARY_VIEWPOINT_URI);
					} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (documentLibraryVirtualModel != null) {
						break;
					}
				}
				return documentLibraryVirtualModel;
			}
			return null;
		}

		@Override
		public VirtualModel getAbstractDocumentVirtualModel() {
			if (getDocumentLibraryVirtualModel() != null) {
				return getDocumentLibraryVirtualModel().getVirtualModelNamed(FMSConstants.ABSTRACT_DOCUMENT_VM_NAME);
			}
			return null;
		}

		@Override
		public FlexoConcept getReferenceConcept() {
			return getAbstractDocumentVirtualModel().getFlexoConcept(FMSConstants.REFERENCE_CONCEPT_NAME);
		}

		@Override
		public VirtualModel getFormoseVirtualModel() {
			return getFormoseViewPoint().getVirtualModelNamed(FMSConstants.FORMOSE_VM_NAME);
		}

		@Override
		public FlexoConcept getElementConcept() {
			return getFormoseViewPoint().getFlexoConcept(FMSConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementConcept() {
			return getFormoseViewPoint().getFlexoConcept(FMSConstants.REQUIREMENT_CONCEPT_NAME);
		}

		// Concepts declared in DocumentAnnotation-Methodology

		@Override
		public VirtualModel getDocAnnotationMethodologyVirtualModel() {
			return getFormoseViewPoint().getVirtualModelNamed(FMSConstants.DOC_ANNOTATION_METHODOLOGY_VM_NAME);
		}

		@Override
		public FlexoConcept getElementReferenceConcept() {
			return getDocAnnotationMethodologyVirtualModel().getFlexoConcept(FMSConstants.ELEMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementReferenceConcept() {
			return getDocAnnotationMethodologyVirtualModel().getFlexoConcept(FMSConstants.REQUIREMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public List<FMLRTVirtualModelInstance> getKaosDiagrams() {
			VirtualModel ceVP = getFormoseViewPoint();
			FMLRTVirtualModelInstance view = getFormoseView();

			final List<FMLRTVirtualModelInstance> returned = new ArrayList<FMLRTVirtualModelInstance>();
			if (ceVP != null && view != null) {

			}
			return returned;
		}

		@Override
		public FMLRTVirtualModelInstance getDocumentLibrary() {
			if (getFormoseView() != null) {
				List<VirtualModelInstance<?, ?>> vmiList = getFormoseView()
						.getVirtualModelInstancesForVirtualModel(getDocumentLibraryVirtualModel());
				if (vmiList != null && vmiList.size() > 0) {
					return (FMLRTVirtualModelInstance) vmiList.get(0);
				}
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getFormoseVirtualModelInstance() {
			if (getFormoseView() != null) {
				if (getFormoseView().getVirtualModelInstancesForVirtualModel(getFormoseVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getFormoseView().getVirtualModelInstancesForVirtualModel(getFormoseVirtualModel())
							.get(0);
				}
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getDocAnnotationMethodologyVirtualModelInstance() {
			if (getFormoseView() != null) {
				if (getFormoseView().getVirtualModelInstancesForVirtualModel(getDocAnnotationMethodologyVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getFormoseView()
							.getVirtualModelInstancesForVirtualModel(getDocAnnotationMethodologyVirtualModel()).get(0);
				}
			}
			return null;
		}

		@Override
		public FlexoConceptInstance getProjectElement() {
			if (getFormoseVirtualModelInstance() != null) {
				FlexoConceptInstanceRole role = (FlexoConceptInstanceRole) getFormoseVirtualModel()
						.getAccessibleRole(FMSConstants.PROJECT_ELEMENT_ROLE_NAME);
				return getFormoseVirtualModelInstance().getFlexoPropertyValue(role);
			}
			return null;
		}

		@Override
		public List<FlexoConceptInstance> getChildrenElements(FlexoConceptInstance element) {
			System.out.println("Tiens qui sont les enfants de: " + element);
			return null;
		}
	}
}*/
