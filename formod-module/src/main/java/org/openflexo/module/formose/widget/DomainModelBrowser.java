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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.controller.FMSController;
import org.openflexo.module.formose.controller.FMSFIBController;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * Implementation of GoalDiagram browser
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class DomainModelBrowser extends FIBBrowserView<FMLRTVirtualModelInstance> {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/DomainModelBrowser.fib");

	private FIBBrowser browser;

	public DomainModelBrowser(final FlexoController controller) {
		super(null, controller, BROWSER_FIB, controller.getModuleLocales());
		getFIBController().setBrowser(this);
	}

	@Override
	public DomainModelBrowserFIBController getFIBController() {
		return (DomainModelBrowserFIBController) super.getFIBController();
	}

	@Override
	public FMSController getFlexoController() {
		return (FMSController) super.getFlexoController();
	}

	private VirtualModel domainModelViewpoint;

	public VirtualModel getDomainModelViewPoint() {
		if (domainModelViewpoint == null && getFlexoController() != null) {
			try {
				domainModelViewpoint = getFlexoController().getApplicationContext().getVirtualModelLibrary()
						.getVirtualModel(FMSConstants.DOMAIN_MODELLING_VIEWPOINT_URI);
			} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
				e.printStackTrace();
			}
		}
		return domainModelViewpoint;
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getDomainModelViewPoint()));

		browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "DomainModelBrowser");
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

	public static class DomainModelBrowserFIBController extends FMSFIBController {
		protected DomainModelBrowser browser;

		private String filteredName = null;
		private boolean isFiltered = false;
		private final List<FlexoConceptInstance> matchingValues;

		public DomainModelBrowserFIBController(FIBComponent component) {
			super(component, SwingViewFactory.INSTANCE);
			// Default parent localizer is the main localizer
			// setParentLocalizer(FlexoLocalization.getMainLocalizer());

			matchingValues = new ArrayList<>();
		}

		protected void setBrowser(DomainModelBrowser browser) {
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

		protected boolean isAcceptableValue(FlexoConceptInstance o) {
			// System.out.println("acceptable ? " + o);
			if (o == null) {
				return false;
			}

			return true;
		}

		private void updateMatchingValues() {
			final List<FlexoConceptInstance> oldMatchingValues = new ArrayList<>(getMatchingValues());
			// System.out.println("updateMatchingValues() with " + getFilteredName());
			matchingValues.clear();
			if (getAllSelectableValues() != null && getFilteredName() != null) {
				isFiltered = true;
				for (FlexoConceptInstance next : getAllSelectableValues()) {
					if (isAcceptableValue(next) && matches(next, getFilteredName())) {
						matchingValues.add(next);
					}
				}
			}
			System.out.println("Objects matching with " + getFilteredName() + " found " + matchingValues.size() + " values");

			/*SwingUtilities.invokeLater(() -> {
				pcSupport.firePropertyChange("matchingValues", oldMatchingValues, getMatchingValues());
				if (matchingValues.size() == 1) {
					setSelectedValue(matchingValues.get(0));
				}
			});*/

			getPropertyChangeSupport().firePropertyChange("matchingValues", oldMatchingValues, getMatchingValues());

			/*if (matchingValues.size() == 1) {
				setSelectedValue(matchingValues.get(0));
			}*/
		}

		private void clearMatchingValues() {
			isFiltered = false;
			List<FlexoConceptInstance> oldMatchingValues = new ArrayList<>(getMatchingValues());
			matchingValues.clear();
			getPropertyChangeSupport().firePropertyChange("matchingValues", oldMatchingValues, null);
		}

		protected boolean matches(FlexoConceptInstance o, String filteredName) {
			return o != null && StringUtils.isNotEmpty(o.getStringRepresentation())
					&& (o.getStringRepresentation()).toUpperCase().indexOf(filteredName.toUpperCase()) > -1;
		}

		public List<FlexoConceptInstance> getMatchingValues() {
			return matchingValues;
		}

		protected Set<FlexoConceptInstance> getAllSelectableValues() {
			Set<FlexoConceptInstance> returned = new HashSet<>();
			JFIBBrowserWidget browserWidget = browser.getFIBBrowserWidget();
			if (browserWidget == null) {
				return null;
			}
			Iterator<Object> it = browserWidget.getBrowserModel().recursivelyExploreModelToRetrieveContents();
			while (it.hasNext()) {
				Object o = it.next();
				if (FlexoConceptInstance.class.isAssignableFrom(o.getClass())) {
					returned.add((FlexoConceptInstance) o);
				}
			}
			return returned;
		}

		public boolean isFiltered() {
			return StringUtils.isNotEmpty(getFilteredName()) && isFiltered;
		}

		public String getFilteredName() {
			return filteredName;
		}

		public void setFilteredName(String filteredName) {
			if ((filteredName == null && this.filteredName != null) || (filteredName != null && !filteredName.equals(this.filteredName))) {
				String oldValue = this.filteredName;
				this.filteredName = filteredName;
				updateMatchingValues();
				getPropertyChangeSupport().firePropertyChange("filteredName", oldValue, filteredName);
			}
		}

	}

}
