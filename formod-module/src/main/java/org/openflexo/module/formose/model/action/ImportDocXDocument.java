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
import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.LongRunningAction;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.FormoseEditor;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * @author sylvain
 */
public class ImportDocXDocument extends FMSAction<ImportDocXDocument, FMLRTVirtualModelInstance, FlexoObject> implements LongRunningAction {

	public static final ImportDocXDocumentActionType ACTION_TYPE = new ImportDocXDocumentActionType();

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FMLRTVirtualModelInstance.class);
	}

	public static final class ImportDocXDocumentActionType
			extends FlexoActionFactory<ImportDocXDocument, FMLRTVirtualModelInstance, FlexoObject> {

		private ImportDocXDocumentActionType() {
			super("import_docx_document", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE);
		}

		@Override
		public ImportDocXDocument makeNewAction(final FMLRTVirtualModelInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new ImportDocXDocument(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FMLRTVirtualModelInstance documentLibrary, final Vector<FlexoObject> globalSelection) {
			return documentLibrary.getVirtualModel().getURI().equals(FMSConstants.DOCUMENT_LIBRARY_VIEWPOINT_URI);
		}

		@Override
		public boolean isEnabledForSelection(final FMLRTVirtualModelInstance documentLibrary, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(documentLibrary, globalSelection);
		}
	}

	// ====================
	// FIELDS
	// ====================
	private File docXFile;
	private String documentName;
	private String documentDescription;

	private FMLRTVirtualModelInstance newDocumentVMI;

	public ImportDocXDocument(final FMLRTVirtualModelInstance focusedObject, final Vector<FlexoObject> globalSelection,
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

	@Override
	protected void doAction(final Object context) throws FlexoException {

		if (getDocXFile() != null) {

			FMLRTVirtualModelInstance documentLibrary = getFocusedObject();
			VirtualModel documentLibraryVP = documentLibrary.getVirtualModel();

			ActionScheme importDocXScheme = (ActionScheme) documentLibraryVP.getFlexoBehaviour("importDocXDocument",
					DocXDocumentResource.class, String.class, String.class);
			ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(importDocXScheme, getFocusedObject());

			ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
			action.setParameterValue(importDocXScheme.getParameter("documentName"), getDocumentName());
			if (getDocumentDescription() != null) {
				action.setParameterValue(importDocXScheme.getParameter("documentDescription"), getDocumentDescription());
			}

			try {
				action.setParameterValue(importDocXScheme.getParameter("docXDocumentResource"), getDocXResource());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			action.doAction();

			newDocumentVMI = (FMLRTVirtualModelInstance) action.getReturnedValue();
		}

	}

	public FMLRTVirtualModelInstance getNewDocumentVMI() {
		return newDocumentVMI;
	}

	private DocXDocumentResource getDocXResource() throws IOException {
		if (getDocXFile() != null) {
			DocXDocumentResource docXResource = null;
			for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
				if (rc instanceof FileSystemBasedResourceCenter) {
					FileSystemBasedResourceCenter fsRC = (FileSystemBasedResourceCenter) rc;
					docXResource = fsRC.getResource(getDocXFile(), DocXDocumentResource.class);
					if (docXResource != null) {
						break;
					}
				}
			}

			if (docXResource == null) {
				File newResourceCenterDir = getDocXFile().getParentFile();
				DirectoryResourceCenter newResourceCenter = DirectoryResourceCenter
						.instanciateNewDirectoryResourceCenter(newResourceCenterDir, getServiceManager().getResourceCenterService());
				getServiceManager().getResourceCenterService().addToResourceCenters(newResourceCenter);
				docXResource = newResourceCenter.getResource(getDocXFile(), DocXDocumentResource.class);
			}

			return docXResource;
		}
		return null;

	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (getDocXFile() == null || !(getDocXFile().exists())) {
			return false;
		}
		return true;
	}

	// ====================
	// GETTERS AND SETTERS
	// ====================

	public String getDocumentName() {
		if (documentName == null && getDocXFile() != null) {
			return getDocXFile().getName();
		}
		return documentName;
	}

	public void setDocumentName(String documentName) {
		if ((documentName == null && this.documentName != null) || (documentName != null && !documentName.equals(this.documentName))) {
			String oldValue = this.documentName;
			this.documentName = documentName;
			getPropertyChangeSupport().firePropertyChange("documentName", oldValue, documentName);
		}
	}

	public File getDocXFile() {
		return docXFile;
	}

	public void setDocXFile(File requirementDocXFile) {
		if ((requirementDocXFile == null && this.docXFile != null)
				|| (requirementDocXFile != null && !requirementDocXFile.equals(this.docXFile))) {
			File oldValue = this.docXFile;
			this.docXFile = requirementDocXFile;
			getPropertyChangeSupport().firePropertyChange("docXFile", oldValue, requirementDocXFile);
			getPropertyChangeSupport().firePropertyChange("documentName", null, getDocumentName());
		}
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String description) {
		if ((description == null && this.documentDescription != null)
				|| (description != null && !description.equals(this.documentDescription))) {
			String oldValue = this.documentDescription;
			this.documentDescription = description;
			getPropertyChangeSupport().firePropertyChange("documentDescription", oldValue, description);
		}
	}

	@Override
	public int getExpectedProgressSteps() {
		return 10;
	}

}
