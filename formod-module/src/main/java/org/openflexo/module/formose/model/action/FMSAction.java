/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of BDA prototype, a component of the software infrastructure 
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

import java.util.List;

import org.openflexo.ApplicationContext;
import org.openflexo.action.ModuleSpecificFlexoAction;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.ModuleLoadingException;
import org.openflexo.module.formose.FMSModule;

/**
 * Abstract implementation for a {@link FlexoAction} specific to {@link FMSModule} module
 * 
 * @author sylvain
 */
public abstract class FMSAction<A extends FlexoAction<A, T1, T2>, T1 extends FlexoObject, T2 extends FlexoObject>
		extends FlexoAction<A, T1, T2> implements ModuleSpecificFlexoAction<FMSModule> {

	/**
	 * Instantiate a {@link FlexoAction} with a factory, a focused object and a global selection
	 * 
	 * @param actionFactory
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public FMSAction(FlexoActionFactory<A, T1, T2> actionFactory, T1 focusedObject, List<T2> globalSelection, FlexoEditor editor) {
		super(actionFactory, focusedObject, globalSelection, editor);
	}

	/**
	 * Instantiate a {@link FlexoAction} with a focused object and a global selection<br>
	 * The factory remains null
	 * 
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public FMSAction(T1 focusedObject, List<T2> globalSelection, FlexoEditor editor) {
		super(focusedObject, globalSelection, editor);
	}

	@Override
	public Class<FMSModule> getFlexoModuleClass() {
		return FMSModule.class;
	}

	@Override
	public LocalizedDelegate getLocales() {
		try {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModuleInstance(FMSModule.class).getLocales();
		} catch (ModuleLoadingException e) {
			e.printStackTrace();
			return super.getLocales();
		}
	}

}
