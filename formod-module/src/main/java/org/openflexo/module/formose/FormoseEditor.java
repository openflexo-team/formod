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

package org.openflexo.module.formose;

import org.openflexo.module.NatureSpecificModule;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.utils.FMSPreferences;

/**
 * A module dedicated to {@link FormoseProject} edition
 * 
 * @author sylvain
 *
 */
public class FormoseEditor extends NatureSpecificModule<FMSModule, FormoseProjectNature> {

	public static FormoseEditor INSTANCE;

	public FormoseEditor() {
		super(FMSModule.FMS_MODULE_NAME, FMSModule.FMS_MODULE_SHORT_NAME, FMSModule.class, FMSPreferences.class, "formose", "10700", "FMS",
				FMSIconLibrary.FMS_SMALL_ICON, FMSIconLibrary.FMS_MEDIUM_ICON, FMSIconLibrary.FMS_MEDIUM_ICON, FMSIconLibrary.FMS_BIG_ICON,
				FormoseProjectNature.class);
		// use that way because the iterator.next in ModuleLoader (line 124) explicitly call constructor : can't use a private constructor
		// with
		// public static final INSTANCE;
		// For more info see comment in FME Module class
		INSTANCE = this;
	}

	@Override
	public String getHTMLDescription() {
		return "<html><b>ForMod</b> is the prototype developped in the Formose project. The <b>Formose</b> ANR project (ANR-14-CE28-0009) aims to design a formally-grounded, model-based requirements engineering (RE) method for critical complex systems, supported by an open-source environment. The project has been launched on November 17, 2014. The main partners are: ClearSy, LACL, Institut Mines-Telecom, OpenFlexo, and THALES. </html>";
	}

	@Override
	public int getExpectedProgressLoadingSteps() {
		return 200;
	}

}
