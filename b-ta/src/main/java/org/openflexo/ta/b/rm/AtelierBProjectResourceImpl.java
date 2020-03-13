/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.ta.b.rm;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.InJarIODelegate;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.rm.BasicResourceImpl;
import org.openflexo.rm.ClasspathResourceLocatorImpl;
import org.openflexo.rm.FileSystemResourceLocatorImpl;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.atelierb.AtelierBProject;
import org.openflexo.ta.b.model.atelierb.AtelierBProjectFactory;

public abstract class AtelierBProjectResourceImpl extends PamelaResourceImpl<AtelierBProject, AtelierBProjectFactory>
		implements AtelierBProjectResource {

	static final Logger logger = Logger.getLogger(AtelierBProjectResourceImpl.class.getPackage().getName());

	// private static XMLRootElementReader reader = new XMLRootElementReader();

	@Override
	public Class<AtelierBProject> getResourceDataClass() {
		return AtelierBProject.class;
	}

	/**
	 * Return virtual model stored by this resource<br>
	 * Load the resource data when unloaded
	 */
	@Override
	public AtelierBProject getAtelierBProject() {
		try {
			return getResourceData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return virtual model stored by this resource when loaded<br>
	 * Do not force the resource data to be loaded
	 */
	@Override
	public AtelierBProject getLoadedAtelierBProject() {
		if (isLoaded()) {
			return getAtelierBProject();
		}
		return null;
	}

	@Override
	public BTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(BTechnologyAdapter.class);
		}
		return null;
	}

	@Override
	public List<BResource> getBComponentResources() {
		return getContents(BResource.class);
	}

	@Override
	public boolean delete(Object... context) {
		if (super.delete(context)) {
			getServiceManager().getResourceManager().addToFilesToDelete(ResourceLocator.retrieveResourceAsFile(getDirectory()));
			// isDeleted = true;
			// also remove the parent folder if empty, created by openflexo
			/*
			 * if (!(getDirectory().length() > 0)) { getDirectory().delete(); }
			 * else { logger.
			 * warning("Diagram specification folder cannot be deleted because it is not empty"
			 * ); }
			 */
			return true;
		}

		return false;
	}

	@Override
	public Resource getDirectory() {
		if (getIODelegate() instanceof FileIODelegate) {
			String parentPath = ((FileIODelegate) getIODelegate()).getFile().getParentFile().getAbsolutePath();
			if (ResourceLocator.locateResource(parentPath) == null) {
				FileSystemResourceLocatorImpl.appendDirectoryToFileSystemResourceLocator(parentPath);
			}
			return ResourceLocator.locateResource(parentPath);
		}
		else if (getIODelegate() instanceof InJarIODelegate) {
			InJarResourceImpl resource = ((InJarIODelegate) getIODelegate()).getInJarResource();
			String parentPath = FilenameUtils.getFullPath(resource.getRelativePath());
			BasicResourceImpl parent = ((ClasspathResourceLocatorImpl) (resource.getLocator())).getJarResourcesList().get(parentPath);
			return parent;
		}
		return null;
	}

}
