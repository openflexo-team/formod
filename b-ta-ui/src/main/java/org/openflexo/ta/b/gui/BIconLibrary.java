/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.b.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.ta.b.model.BAbstractConstant;
import org.openflexo.ta.b.model.BAbstractVariable;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BConcreteConstant;
import org.openflexo.ta.b.model.BConcreteVariable;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BImplementation;
import org.openflexo.ta.b.model.BMachine;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BRefinement;
import org.openflexo.ta.b.model.BSet;
import org.openflexo.ta.b.model.BSetValue;
import org.openflexo.ta.b.model.BSystem;
import org.openflexo.ta.b.model.atelierb.AtelierBProjectObject;

public class BIconLibrary {

	private static final Logger logger = Logger.getLogger(BIconLibrary.class.getPackage().getName());

	public static final ImageIconResource B_TA_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/AtelierB_32x32.png"));

	public static final ImageIconResource B_TA_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/AtelierB_16x16.png"));

	public static final ImageIconResource B_SYSTEM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/System.png"));
	public static final ImageIconResource B_MACHINE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Machine.png"));
	public static final ImageIconResource B_REFINEMENT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Refinement.png"));
	public static final ImageIconResource B_IMPLEMENTATION_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/Implementation.png"));

	public static final ImageIconResource B_SET_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Set.png"));
	public static final ImageIconResource B_SET_VALUE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/SetValue.png"));
	public static final ImageIconResource B_VARIABLE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Variable.png"));
	public static final ImageIconResource B_CONSTANT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Constant.png"));
	public static final ImageIconResource B_OPERATION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Operation.png"));

	public static final ImageIconResource B_PREDICATE_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Predicate.png"));
	public static final ImageIconResource B_EXPRESSION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Expression.png"));

	public static ImageIcon iconForObject(Class<? extends BObject> objectClass) {
		if (BSystem.class.isAssignableFrom(objectClass)) {
			return B_SYSTEM_ICON;
		}
		else if (BMachine.class.isAssignableFrom(objectClass)) {
			return B_MACHINE_ICON;
		}
		else if (BRefinement.class.isAssignableFrom(objectClass)) {
			return B_REFINEMENT_ICON;
		}
		else if (BImplementation.class.isAssignableFrom(objectClass)) {
			return B_IMPLEMENTATION_ICON;
		}
		else if (BComponent.class.isAssignableFrom(objectClass)) {
			return B_SYSTEM_ICON;
		}
		else if (BSet.class.isAssignableFrom(objectClass)) {
			return B_SET_ICON;
		}
		else if (BSetValue.class.isAssignableFrom(objectClass)) {
			return B_SET_VALUE_ICON;
		}
		else if (BAbstractVariable.class.isAssignableFrom(objectClass)) {
			return B_VARIABLE_ICON;
		}
		else if (BConcreteVariable.class.isAssignableFrom(objectClass)) {
			return B_VARIABLE_ICON;
		}
		else if (BAbstractConstant.class.isAssignableFrom(objectClass)) {
			return B_CONSTANT_ICON;
		}
		else if (BConcreteConstant.class.isAssignableFrom(objectClass)) {
			return B_CONSTANT_ICON;
		}
		else if (BOperation.class.isAssignableFrom(objectClass)) {
			return B_OPERATION_ICON;
		}
		else if (BExpression.class.isAssignableFrom(objectClass)) {
			return B_EXPRESSION_ICON;
		}
		else if (BPredicate.class.isAssignableFrom(objectClass)) {
			return B_PREDICATE_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}

	public static ImageIcon iconForProjectObject(Class<? extends AtelierBProjectObject> objectClass) {
		return B_TA_ICON;
	}

}
