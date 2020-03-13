/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.module.formose.view;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.gina.view.widget.browser.impl.FIBBrowserModel.BrowserCell;
import org.openflexo.icon.IconFactory;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.controller.DocumentAnnotationPerspective;
import org.openflexo.module.formose.controller.FMSFIBController;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.FlexoFIBView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Main view for DocumentationAnnotation perspective
 */
@SuppressWarnings("serial")
public class DocumentAnnotationModuleView extends FIBModuleView<FormoseProjectNature> {

	public static Resource DOCUMENTATION_ANNOTATION_MODULE_VIEW_FIB = ResourceLocator.locateResource("Fib/DocumentAnnotationMainPanel.fib");

	private final FlexoPerspective perspective;

	public DocumentAnnotationModuleView(FormoseProjectNature nature, FlexoController controller, FlexoPerspective perspective) {
		super(nature, controller, DOCUMENTATION_ANNOTATION_MODULE_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;

		// FIBBrowserWidget<?, ?> browserView = (FIBBrowserWidget<?, ?>) getFIBView("ElementBrowser");
		// System.out.println("Found browser: " + browserView);
		/*browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ElementBrowser");
		if (browser != null) {
			bindFlexoActionsToBrowser(browser);
		}*/

		setDataObject(nature);

	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public FormoseProjectNature getDataObject() {
		return (FormoseProjectNature) super.getDataObject();
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getDataObject().getFormoseViewPoint()));

		/*if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getFormoseVirtualModel()));
		}*/

	}

	@Override
	public void setDataObject(Object dataObject) {

		// System.out.println("------> setDataObject with " + dataObject + " in " + this);

		/*if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getFormoseViewPoint()));
		}*/

		if (dataObject instanceof FormoseProjectNature && getFIBController() != null) {
			// System.out.println("formoseInstance=" + ((FormoseProjectNature) dataObject).getFormoseInstance());
			// System.out.println("formoseView=" + ((FormoseProjectNature)
			// dataObject).getFormoseInstance().getAccessedVirtualModelInstance());
			getFIBController().setVariableValue("formoseView",
					((FormoseProjectNature) dataObject).getFormoseInstance().getAccessedVirtualModelInstance());
		}

		super.setDataObject(dataObject);
	}

	public static class DocumentAnnotationModuleViewFIBController extends FMSFIBController {
		public DocumentAnnotationModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public DocumentAnnotationModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory,
				FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public ImageIcon getProjectIcon() {
			return IconFactory.getImageIcon(FMSIconLibrary.FMS_BIG_ICON, FMSIconLibrary.DOC_BIG_MARKER);
		}

		public void moveReferenceToElement(FlexoConceptInstance reference, FlexoConceptInstance destinationElement) {
			// System.out.println("Moving reference " + reference + " to Element " + destinationElement);

			FlexoConceptInstance destinationReference = getFlexoController().getElementReference(destinationElement);

			FlexoConceptInstance sourceParent = getParentForReference(reference);

			if (sourceParent.getFlexoConcept() == getFlexoController().getFormoseNature().getElementReferenceConcept()) {
				moveReference(reference, sourceParent, destinationReference);
			}
			else if (sourceParent.getFlexoConcept() == getFlexoController().getFormoseNature().getRequirementReferenceConcept()) {
				moveReference(reference, sourceParent, destinationReference);
			}
			else if (sourceParent.getFlexoConcept().getName().equals("Unclassified")) {
				moveUnclassifiedReference(reference, sourceParent, destinationReference);
			}

		}

		public void moveReferenceToRequirement(FlexoConceptInstance reference, FlexoConceptInstance destinationElement) {
			// System.out.println("Moving reference " + reference + " to Requirement " + destinationElement);

			FlexoConceptInstance destinationReference = getFlexoController().getRequirementReference(destinationElement);

			FlexoConceptInstance sourceParent = getParentForReference(reference);

			if (sourceParent.getFlexoConcept() == getFlexoController().getFormoseNature().getElementReferenceConcept()) {
				moveReference(reference, sourceParent, destinationReference);
			}
			else if (sourceParent.getFlexoConcept() == getFlexoController().getFormoseNature().getRequirementReferenceConcept()) {
				moveReference(reference, sourceParent, destinationReference);
			}
			else if (sourceParent.getFlexoConcept().getName().equals("Unclassified")) {
				moveUnclassifiedReference(reference, sourceParent, destinationReference);
			}
		}

		public JFIBBrowserWidget<?> getReferencesBrowser() {
			return (JFIBBrowserWidget<?>) viewForComponent(
					FlexoFIBView.retrieveFIBBrowserNamed((FIBContainer) getRootComponent(), "ReferencesBrowser"));
		}

		/**
		 * Return FlexoConcept instance in which supplied reference (instance of Reference concept) is found in browser<br>
		 * It might be:
		 * <ul>
		 * <li>an instance of ElementReference</li>
		 * <li>an instance of RequirementReference</li>
		 * <li>the singleton Unclassified instance</li>
		 * </ul>
		 * 
		 * @param element
		 *            an instance of of Element concept
		 * @return an instance of ElementReference concept
		 */
		protected FlexoConceptInstance getParentForReference(FlexoConceptInstance reference) {

			Collection<BrowserCell> referenceCells = getReferencesBrowser().getBrowserModel().getBrowserCell(reference);
			// System.out.println("referenceCells=" + referenceCells);

			if (referenceCells.size() > 0) {
				BrowserCell referenceCell = referenceCells.iterator().next();
				// System.out.println("reference: " + referenceCell);
				// System.out.println("parent cell: " + referenceCell.getParent());
				// System.out.println("named reference: " + referenceCell.getParent().getRepresentedObject());

				if (referenceCell != null && referenceCell.getParent() != null
						&& referenceCell.getParent().getRepresentedObject() instanceof FlexoConceptInstance) {
					FlexoConceptInstance parentObject = (FlexoConceptInstance) referenceCell.getParent().getRepresentedObject();
					if (parentObject.getFlexoConcept() == getFlexoController().getFormoseNature().getElementConcept()) {
						return getFlexoController().getElementReference(parentObject);
					}
					if (parentObject.getFlexoConcept() == getFlexoController().getFormoseNature().getRequirementConcept()) {
						return getFlexoController().getRequirementReference(parentObject);
					}
					return parentObject;
				}
			}

			return null;
		}

		public void openReference(FlexoConceptInstance reference) {
			getFlexoController().setCurrentEditedObjectAsModuleView(reference.getVirtualModelInstance(),
					getFlexoController().getDocumentAnnotationPerspective());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					((DocumentAnnotationPerspective) getFlexoController().getCurrentPerspective()).getReferencesBrowser().getFIBController()
							.setSelectedElement(reference);
				}
			});

		}

	}

}
