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

import javax.swing.ImageIcon;

import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.icon.IconFactory;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.controller.FMSFIBController;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Main view for Domain Modelling perspective
 */
@SuppressWarnings("serial")
public class DomainModellingModuleView extends FIBModuleView<FormoseProjectNature> {

	public static Resource DOMAIN_MODELLING_MODULE_VIEW_FIB = ResourceLocator.locateResource("Fib/DomainModellingMainPanel.fib");

	private final FlexoPerspective perspective;

	public DomainModellingModuleView(FormoseProjectNature nature, FlexoController controller, FlexoPerspective perspective) {
		super(nature, controller, DOMAIN_MODELLING_MODULE_VIEW_FIB, controller.getModule().getLocales());
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

	public static class DomainModellingModuleViewFIBController extends FMSFIBController {
		public DomainModellingModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public DomainModellingModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public ImageIcon getProjectIcon() {
			return IconFactory.getImageIcon(FMSIconLibrary.FMS_BIG_ICON, FMSIconLibrary.DOMAIN_MODEL_BIG_MARKER);
		}

		public ImageIcon getDomainModelElementMappingIcon() {
			return IconFactory.getImageIcon(FMSIconLibrary.ELEMENT_BIG_ICON, FMSIconLibrary.DOMAIN_MODEL_BIG_MARKER);
		}

	}

}
