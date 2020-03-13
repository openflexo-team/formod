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

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.controller.FMSController;
import org.openflexo.module.formose.controller.FMSFIBController;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * Abstract implementation of a browser of formose project
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public abstract class AbstractFormoseProjectBrowser extends FIBBrowserView<FormoseProjectNature> {

	private FIBBrowser browser;

	public AbstractFormoseProjectBrowser(final FlexoController controller, Resource fibResource) {
		super(controller.getProject() != null ? controller.getProject().getNature(FormoseProjectNature.class) : null, controller,
				fibResource, controller.getModuleLocales());
		getFIBController().setBrowser(this);
	}

	@Override
	public FormoseProjectBrowserFIBController getFIBController() {
		return (FormoseProjectBrowserFIBController) super.getFIBController();
	}

	@Override
	public FMSController getFlexoController() {
		return (FMSController) super.getFlexoController();
	}

	private VirtualModel formoseViewpoint;

	public VirtualModel getFormoseViewPoint() {
		if (formoseViewpoint == null && getFlexoController() != null) {
			try {
				formoseViewpoint = getFlexoController().getApplicationContext().getVirtualModelLibrary()
						.getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
			} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
				e.printStackTrace();
			}
		}
		return formoseViewpoint;
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getFormoseViewPoint()));

		browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "FormoseProjectBrowser");
		if (browser != null) {
			bindFlexoActionsToBrowser(browser);
		}
	}

	public FIBBrowser getBrowser() {
		return browser;
	}

	public JFIBBrowserWidget<?> getFIBBrowserWidget() {
		return (JFIBBrowserWidget<?>) getFIBController().viewForComponent(retrieveFIBBrowser((FIBContainer) getFIBComponent()));
	}

	/*@Override
	public void setDataObject(Object dataObject) {
	
		// System.out.println("setDataObject with " + dataObject + " in " + this);
	
		if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getFormoseViewPoint()));
		}
		super.setDataObject(dataObject);
	
		if (getDataObject() != null && getFIBController() != null) {
			// System.out.println("formoseView=" + getDataObject().getFormoseInstance().getAccessedVirtualModelInstance());
			getFIBController().setVariableValue("formoseView", getDataObject().getFormoseInstance().getAccessedVirtualModelInstance());
		}
	
	}*/

	private Object selectedElement;

	public Object getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(Object selected) {
		selectedElement = selected;
	}

	public static class FormoseProjectBrowserFIBController extends FMSFIBController {
		protected AbstractFormoseProjectBrowser browser;

		public FormoseProjectBrowserFIBController(FIBComponent component) {
			super(component, SwingViewFactory.INSTANCE);
			// Default parent localizer is the main localizer
			// setParentLocalizer(FlexoLocalization.getMainLocalizer());
		}

		protected void setBrowser(AbstractFormoseProjectBrowser browser) {
			this.browser = browser;
		}

		public Object getSelectedElement() {
			if (browser != null) {
				return browser.getSelectedElement();
			}
			return null;
		}

		public void setSelectedElement(Object selected) {
			if (browser != null) {
				browser.setSelectedElement(selected);
			}
		}

	}

}
