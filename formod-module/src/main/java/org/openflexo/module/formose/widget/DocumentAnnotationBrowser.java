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

import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.controller.FlexoController;

/**
 * A browser that present a formose project in the documentation annotation perspective<br>
 * 
 * @author sylvain
 */
public class DocumentAnnotationBrowser extends AbstractFormoseProjectBrowser {

	private static final Resource BROWSER_FIB = ResourceLocator.locateResource("Fib/DocumentAnnotationBrowser.fib");

	public DocumentAnnotationBrowser(final FlexoController controller) {
		super(controller, BROWSER_FIB);
	}

	/*@Override
	public void setRootObject(FormoseProjectNature obj) {
		System.out.println("On sette bien " + obj);
		System.out.println("formoseInstance=" + obj.getFormoseInstance());
		System.out.println("accessedVirtualModelInstance=" + obj.getFormoseInstance().getAccessedVirtualModelInstance());
		super.setRootObject(obj);
	}*/
}
