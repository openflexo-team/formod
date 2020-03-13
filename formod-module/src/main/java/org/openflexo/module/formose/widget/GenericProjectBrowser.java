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

import org.openflexo.foundation.FlexoProject;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * A browser that present a formose project<br>
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public class GenericProjectBrowser extends FIBBrowserView<FlexoProject<?>> {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/GenericProjectBrowser.fib");

	public GenericProjectBrowser(final FlexoController controller) {
		super(controller.getProject(), controller, BROWSER_FIB, controller.getModuleLocales());
	}

	@Override
	public void initializeFIBComponent() {
		FIBBrowser projectBrowser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ProjectBrowser");
		if (projectBrowser != null) {
			bindFlexoActionsToBrowser(projectBrowser);
		}

	}

	private Object selectedElement;

	public Object getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(Object selected) {
		selectedElement = selected;
	}

}
