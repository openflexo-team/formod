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
import java.io.File;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.formose.model.action.InstantiateBMethodology;
import org.openflexo.ta.b.gui.BIconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class InstantiateBMethodologyWizard extends FlexoActionWizard<InstantiateBMethodology> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(InstantiateBMethodologyWizard.class.getPackage().getName());

	private final ConfigureBMethodology configuration;
	private final IndicatesAtelierBSourceProject sourceFolderIndication;

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	public InstantiateBMethodologyWizard(InstantiateBMethodology action, FlexoController controller) {
		super(action, controller);
		addStep(configuration = new ConfigureBMethodology());
		addStep(sourceFolderIndication = new IndicatesAtelierBSourceProject());
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("instantiate_B_methodology");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(BIconLibrary.B_TA_BIG_ICON, IconLibrary.BIG_NEW_MARKER).getImage();
	}

	public ConfigureBMethodology getConfiguration() {
		return configuration;
	}

	@FIBPanel("Fib/Wizard/ConfigureBMethodology.fib")
	public class ConfigureBMethodology extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public InstantiateBMethodology getAction() {
			return InstantiateBMethodologyWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("configure_methodology");
		}

		@Override
		public boolean isValid() {

			if (getElement() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_an_element_where_starting_B_methodology"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_methodology"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public FlexoConceptInstance getElement() {
			return getAction().getElement();
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

	@FIBPanel("Fib/Wizard/IndicatesAtelierBSourceProject.fib")
	public class IndicatesAtelierBSourceProject extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public InstantiateBMethodology getAction() {
			return InstantiateBMethodologyWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("indicates_atelierB_source_project");
		}

		public String getChoice() {
			return getAction().getChoice();
		}

		public void setChoice(String choice) {
			if ((choice == null && getChoice() != null) || (choice != null && !choice.equals(getChoice()))) {
				String oldValue = getChoice();
				getAction().setChoice(choice);
				getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
				checkValidity();
			}
		}

		@Override
		public boolean isValid() {

			if (getChoice() == null)
				return false;

			if (getChoice().equals("open_existing_Bproject")) {
				if (getSourceProjectFolder() == null || !getSourceProjectFolder().exists()) {
					setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_valid_source_project_folder"),
							IssueMessageType.ERROR);
					return false;
				}

				if (getSourceProjectFolder().exists() && !(new File(getSourceProjectFolder(), "bdp").exists())) {
					setIssueMessage(getAction().getLocales().localizedForKey("it_does_not_appear_to_be_a_valid_atelierB_project"),
							IssueMessageType.ERROR);
					return false;
				}
			}
			else {
				if (getSourceCreationFolder() == null || !getSourceCreationFolder().exists()) {
					setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_valid_source_creation_folder"),
							IssueMessageType.ERROR);
					return false;
				}
				if (StringUtils.isEmpty(getNewProjectName())) {
					setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_valid_project_name"),
							IssueMessageType.ERROR);
					return false;
				}
			}

			return true;

		}

		public File getSourceCreationFolder() {
			return getAction().getSourceCreationFolder();
		}

		public void setSourceCreationFolder(File sourceCreationFolder) {
			if (!sourceCreationFolder.equals(getSourceCreationFolder())) {
				File oldValue = getSourceCreationFolder();
				getAction().setSourceCreationFolder(sourceCreationFolder);
				getPropertyChangeSupport().firePropertyChange("sourceCreationFolder", oldValue, sourceCreationFolder);
				checkValidity();
			}
		}

		public String getNewProjectName() {
			return getAction().getNewProjectName();
		}

		public void setNewProjectName(String newProjectName) {
			if (!newProjectName.equals(getNewProjectName())) {
				String oldValue = getNewProjectName();
				getAction().setNewProjectName(newProjectName);
				getPropertyChangeSupport().firePropertyChange("newProjectName", oldValue, newProjectName);
				checkValidity();
			}
		}

		public File getSourceProjectFolder() {
			return getAction().getSourceProjectFolder();
		}

		public void setSourceProjectFolder(File sourceProjectFolder) {
			if (!sourceProjectFolder.equals(getSourceProjectFolder())) {
				File oldValue = getSourceProjectFolder();
				getAction().setSourceProjectFolder(sourceProjectFolder);
				getPropertyChangeSupport().firePropertyChange("sourceProjectFolder", oldValue, sourceProjectFolder);
				checkValidity();
			}
		}

	}

}
