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

package org.openflexo.ta.b.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.fml.BAbstractConstantRole;
import org.openflexo.ta.b.fml.BAbstractVariableRole;
import org.openflexo.ta.b.fml.BComponentRole;
import org.openflexo.ta.b.fml.BConcreteConstantRole;
import org.openflexo.ta.b.fml.BConcreteVariableRole;
import org.openflexo.ta.b.fml.BExpressionRole;
import org.openflexo.ta.b.fml.BOperationRole;
import org.openflexo.ta.b.fml.BPredicateRole;
import org.openflexo.ta.b.fml.BSetRole;
import org.openflexo.ta.b.fml.BSetValueRole;
import org.openflexo.ta.b.fml.atelierb.AddAtelierBComponent;
import org.openflexo.ta.b.fml.atelierb.AtelierBComponentRole;
import org.openflexo.ta.b.fml.editionaction.AddBAbstractConstant;
import org.openflexo.ta.b.fml.editionaction.AddBAbstractVariable;
import org.openflexo.ta.b.fml.editionaction.AddBConcreteConstant;
import org.openflexo.ta.b.fml.editionaction.AddBConcreteVariable;
import org.openflexo.ta.b.fml.editionaction.AddBOperation;
import org.openflexo.ta.b.fml.editionaction.AddBRefinedOperation;
import org.openflexo.ta.b.fml.editionaction.AddBSet;
import org.openflexo.ta.b.fml.editionaction.AddBSetValue;
import org.openflexo.ta.b.fml.editionaction.CreateBBinaryExpression;
import org.openflexo.ta.b.fml.editionaction.CreateBBinaryExpressionPredicate;
import org.openflexo.ta.b.fml.editionaction.CreateBBinaryPredicatePredicate;
import org.openflexo.ta.b.fml.editionaction.CreateBExpressionFromString;
import org.openflexo.ta.b.fml.editionaction.CreateBPredicateFromString;
import org.openflexo.ta.b.fml.editionaction.RemoveBPredicateInBPredicate;
import org.openflexo.ta.b.gui.BIconLibrary;
import org.openflexo.ta.b.model.BAbstractConstant;
import org.openflexo.ta.b.model.BAbstractVariable;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BConcreteConstant;
import org.openflexo.ta.b.model.BConcreteVariable;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSet;
import org.openflexo.ta.b.model.BSetValue;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;
import org.openflexo.ta.b.model.atelierb.AtelierBProjectObject;
import org.openflexo.ta.b.model.operation.BRefinedOperation;
import org.openflexo.ta.b.view.BComponentModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class BAdapterController extends TechnologyAdapterController<BTechnologyAdapter> {

	static final Logger logger = Logger.getLogger(BAdapterController.class.getPackage().getName());

	private InspectorGroup bInspectorGroup;
	private InspectorGroup atelierBInspectorGroup;

	@Override
	public Class<BTechnologyAdapter> getTechnologyAdapterClass() {
		return BTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		bInspectorGroup = controller.loadInspectorGroup("B", getTechnologyAdapter().getLocales(), getFMLTechnologyAdapterInspectorGroup());
		atelierBInspectorGroup = controller.loadInspectorGroup("AtelierB", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return bInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {

		// You can initialize here actions specific to that technology
	}

	/**
	 * Return icon representing underlying technology, required size is 32x32
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyBigIcon() {
		return BIconLibrary.B_TA_BIG_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return BIconLibrary.B_TA_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return BIconLibrary.B_TA_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return BIconLibrary.B_TA_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (BObject.class.isAssignableFrom(objectClass)) {
			return BIconLibrary.iconForObject((Class<? extends BObject>) objectClass);
		}
		if (AtelierBProjectObject.class.isAssignableFrom(objectClass)) {
			return BIconLibrary.iconForProjectObject((Class<? extends AtelierBProjectObject>) objectClass);
		}
		return null;
	}

	/**
	 * Return icon representing supplied role
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> roleClass) {
		if (BComponentRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BComponent.class);
		}
		if (BSetRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BSet.class);
		}
		if (BSetValueRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BSetValue.class);
		}
		if (BPredicateRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BPredicate.class);
		}
		if (BExpressionRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BExpression.class);
		}
		if (BAbstractConstantRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BAbstractConstant.class);
		}
		if (BConcreteConstantRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BConcreteConstant.class);
		}
		if (BAbstractVariableRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BAbstractVariable.class);
		}
		if (BConcreteVariableRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BConcreteVariable.class);
		}
		if (BOperationRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(BOperation.class);
		}
		if (AtelierBComponentRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(AtelierBComponent.class);
		}
		return null;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		if (AddAtelierBComponent.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(AtelierBComponent.class), IconLibrary.DUPLICATE);
		}
		if (AddBSet.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BSet.class), IconLibrary.DUPLICATE);
		}
		if (AddBSetValue.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BSetValue.class), IconLibrary.DUPLICATE);
		}
		if (AddBAbstractConstant.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BAbstractConstant.class), IconLibrary.DUPLICATE);
		}
		if (RemoveBPredicateInBPredicate.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BPredicate.class), IconLibrary.DELETE);
		}
		if (AddBConcreteConstant.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BConcreteConstant.class), IconLibrary.DUPLICATE);
		}
		if (AddBAbstractVariable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BAbstractVariable.class), IconLibrary.DUPLICATE);
		}
		if (AddBConcreteVariable.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BConcreteVariable.class), IconLibrary.DUPLICATE);
		}
		if (AddBOperation.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BOperation.class), IconLibrary.DUPLICATE);
		}
		if (AddBRefinedOperation.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BRefinedOperation.class), IconLibrary.DUPLICATE);
		}
		if (CreateBPredicateFromString.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BPredicate.class), IconLibrary.DUPLICATE);
		}
		if (CreateBBinaryPredicatePredicate.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BPredicate.class), IconLibrary.DUPLICATE);
		}
		if (CreateBBinaryExpressionPredicate.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BPredicate.class), IconLibrary.DUPLICATE);
		}
		if (CreateBExpressionFromString.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BExpression.class), IconLibrary.DUPLICATE);
		}
		if (CreateBBinaryExpression.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(BExpression.class), IconLibrary.DUPLICATE);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<BTechnologyAdapter> object, FlexoController controller) {
		return object instanceof BComponent;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<BTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof BComponent) {
			return ((BComponent) object).getResource().getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<BTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof BComponent) {
			BComponentModuleView returned = new BComponentModuleView((BComponent) object, controller, perspective);
			return returned;
		}
		return new EmptyPanel<>(controller, perspective, object);
	}

}
