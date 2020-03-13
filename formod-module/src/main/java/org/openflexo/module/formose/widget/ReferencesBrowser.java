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

package org.openflexo.module.formose.widget;

import java.util.Collection;

import javax.swing.SwingUtilities;

import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.widget.browser.impl.FIBBrowserModel.BrowserCell;
import org.openflexo.module.formose.view.DocXRequirementModuleView;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.controller.FlexoController;

/**
 * A browser that present doc references for the DocumentLibrary<br>
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class ReferencesBrowser extends AbstractFormoseProjectBrowser {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/ReferencesBrowser.fib");

	public ReferencesBrowser(final FlexoController controller) {
		super(controller, BROWSER_FIB);
	}

	@Override
	public void setDataObject(Object dataObject) {
		super.setDataObject(dataObject);
		if (getDataObject() != null && getFIBController() != null) {
			getFIBController().setVariableValue("formoseView", getDataObject().getFormoseInstance().getAccessedVirtualModelInstance());
		}
	}

	@Override
	public ReferencesBrowserFIBController getFIBController() {
		return (ReferencesBrowserFIBController) super.getFIBController();
	}

	public static class ReferencesBrowserFIBController extends FormoseProjectBrowserFIBController {
		private FlexoConceptInstance focusedReference = null;
		private FlexoConceptInstance focusedRequirement = null;
		private FlexoConceptInstance focusedElement = null;
		private boolean enableSynchronization = false;

		public ReferencesBrowserFIBController(FIBComponent component) {
			super(component);
		}

		public FlexoConceptInstance getFocusedElement() {
			return focusedElement;
		}

		public FlexoConceptInstance getFocusedRequirement() {
			return focusedRequirement;
		}

		public FlexoConceptInstance getFocusedReference() {
			return focusedReference;
		}

		public void setEnableSynchronization(boolean enableSynchronization) {
			this.enableSynchronization = enableSynchronization;
		}

		@Override
		public void setSelectedElement(Object selected) {

			super.setSelectedElement(selected);

			if (!enableSynchronization) {
				return;
			}

			if (selected instanceof FlexoConceptInstance) {
				FlexoConceptInstance selectedFCI = (FlexoConceptInstance) selected;
				if (selectedFCI.getFlexoConcept().getName().equals("Element")) {
					focusedElement = selectedFCI;
					focusedRequirement = null;
					focusedReference = null;
				}
				else if (selectedFCI.getFlexoConcept().getName().equals("Requirement")) {
					focusedRequirement = selectedFCI;
					focusedElement = selectedFCI.getContainerFlexoConceptInstance();
					focusedReference = null;
				}
				else if (selectedFCI.getFlexoConcept().getName().equals("DocXReference")) {
					focusedReference = selectedFCI;
					FMLRTVirtualModelInstance abstractDocVMI = (FMLRTVirtualModelInstance) selectedFCI.getOwningVirtualModelInstance();
					if (getFlexoController().getCurrentModuleView() instanceof DocXRequirementModuleView
							&& ((DocXRequirementModuleView) getFlexoController().getCurrentModuleView())
									.getVirtualModelInstance() == abstractDocVMI) {
						// System.out.println("C'est le bon document, ca tombe bien");
					}
					else {
						// System.out.println("Zut pas le bon document");
						getFlexoController().setCurrentEditedObjectAsModuleView(abstractDocVMI,
								getFlexoController().getDocumentAnnotationPerspective());
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								if (browser != null) {
									// System.out.println("browser.getFIBBrowserWidget()=" + browser.getFIBBrowserWidget());
									if (browser.getFIBBrowserWidget() != null) {
										browser.getFIBBrowserWidget().addToSelection(selected);
									}
								}
							}
						});
					}
					TextSelection ts = selectedFCI.getFlexoPropertyValue("textSelection");
					// System.out.println("on selectionne " + ts);
					if (ts != null) {
						((DocXRequirementModuleView) getFlexoController().getCurrentModuleView()).getDocXEditor().getEditor().highlight(ts);
					}
				}
				else {
					focusedElement = null;
					focusedRequirement = null;
					focusedReference = null;
				}
				getPropertyChangeSupport().firePropertyChange("selectedElement", null, selectedFCI);
			}
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

			Collection<BrowserCell> referenceCells = browser.getFIBBrowserWidget().getBrowserModel().getBrowserCell(reference);
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

	}

	public void setEnableSynchronization(boolean enableSynchronization) {
		getFIBController().setEnableSynchronization(enableSynchronization);
	}

}
