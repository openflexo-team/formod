/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.module.formose.controller.action;

import java.awt.Dimension;
import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.formose.FMSIconLibrary;
import org.openflexo.module.formose.model.action.RefineUsingAgentAllocation;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class RefineUsingAgentAllocationWizard extends FlexoActionWizard<RefineUsingAgentAllocation> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RefineUsingAgentAllocationWizard.class.getPackage().getName());

	private final ConfigureAgentAllocationRefinment configuration;

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	public RefineUsingAgentAllocationWizard(RefineUsingAgentAllocation action, FlexoController controller) {
		super(action, controller);
		addStep(configuration = new ConfigureAgentAllocationRefinment());
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("refine_using_agent_allocation");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMSIconLibrary.SYSML_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureAgentAllocationRefinment getConfiguration() {
		return configuration;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/ConfigureAgentAllocationRefinment.fib")
	public class ConfigureAgentAllocationRefinment extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public RefineUsingAgentAllocation getAction() {
			return RefineUsingAgentAllocationWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("configure_element");
		}

		@Override
		public boolean isValid() {

			if (getElement() == null) {
				setIssueMessage(
						getAction().getLocales().localizedForKey("you_should_provide_an_element_where_starting_sysml_kaos_decomposition"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_element"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public FlexoConceptInstance getElement() {
			return getAction().getElement();
		}

		public void setElementMapping(FlexoConceptInstance element) {
			if (element != getElement()) {
				FlexoConceptInstance oldValue = getElement();
				getAction().setElement(element);
				getPropertyChangeSupport().firePropertyChange("element", oldValue, element);
				checkValidity();
			}
		}

		public String getDescription() {
			return getAction().getDescription();
		}

		public void setDescription(String description) {
			if (!description.equals(getDescription())) {
				String oldValue = getDescription();
				getAction().setDescription(description);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
				checkValidity();
			}
		}

	}

}
