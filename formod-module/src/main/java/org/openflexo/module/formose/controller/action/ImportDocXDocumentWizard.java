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
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.formose.model.action.ImportDocXDocument;
import org.openflexo.technologyadapter.docx.gui.DocXIconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class ImportDocXDocumentWizard extends FlexoActionWizard<ImportDocXDocument> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ImportDocXDocumentWizard.class.getPackage().getName());

	private final ConfigureDocXImport configureDocXImport;

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	public ImportDocXDocumentWizard(ImportDocXDocument action, FlexoController controller) {
		super(action, controller);
		addStep(configureDocXImport = new ConfigureDocXImport());
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("import_docx_document");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DocXIconLibrary.DOCX_TECHNOLOGY_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public ConfigureDocXImport getConfigureDocXImport() {
		return configureDocXImport;
	}

	@FIBPanel("Fib/Wizard/ConfigureDocXImport.fib")
	public class ConfigureDocXImport extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public ImportDocXDocument getAction() {
			return ImportDocXDocumentWizard.this.getAction();
		}

		public FMLRTVirtualModelInstance getDocumentLibrary() {
			return getAction().getFocusedObject();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("select_docx_file_to_import");
		}

		@Override
		public boolean isValid() {

			if (getDocXFile() == null || !getDocXFile().exists()) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_must_choose_docx_file"), IssueMessageType.ERROR);
				return false;
			}
			if (StringUtils.isEmpty(getDocumentName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_must_define_document_name"), IssueMessageType.ERROR);
				return false;
			}
			if (StringUtils.isEmpty(getDocumentDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_document"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getDocumentName() {
			return getAction().getDocumentName();
		}

		public void setProjectName(String documentName) {
			if (!documentName.equals(getDocumentName())) {
				String oldValue = getDocumentName();
				getAction().setDocumentName(documentName);
				getPropertyChangeSupport().firePropertyChange("documentName", oldValue, documentName);
				checkValidity();
			}
		}

		public File getDocXFile() {
			return getAction().getDocXFile();
		}

		public void setDocXFile(File docXFile) {
			if (!docXFile.equals(getDocXFile())) {
				File oldValue = getDocXFile();
				getAction().setDocXFile(docXFile);
				getPropertyChangeSupport().firePropertyChange("docXFile", oldValue, docXFile);
				getPropertyChangeSupport().firePropertyChange("documentName", null, getDocumentName());
				checkValidity();
			}
		}

		public String getDocumentDescription() {
			return getAction().getDocumentDescription();
		}

		public void setDocumentDescription(String newDescription) {
			if (!newDescription.equals(getDocumentDescription())) {
				String oldValue = getDocumentDescription();
				getAction().setDocumentDescription(newDescription);
				getPropertyChangeSupport().firePropertyChange("documentDescription", oldValue, newDescription);
				checkValidity();
			}
		}

	}

}
