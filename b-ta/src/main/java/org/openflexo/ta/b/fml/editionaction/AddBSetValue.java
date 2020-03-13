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

package org.openflexo.ta.b.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.BModelSlot;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BSet;
import org.openflexo.ta.b.model.BSetValue;

@ModelEntity
@ImplementationClass(AddBSetValue.AddBSetValueImpl.class)
@XMLElement
@FML("AddBSetValue")
public interface AddBSetValue extends BAction<BSetValue> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String INDENTIFIER_NAME_KEY = "identifierName";
	
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SET_KEY = "set";

	@Getter(value = INDENTIFIER_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getIdentifierName();

	@Getter(value = SET_KEY)
	@XMLAttribute
	public DataBinding<BSet> getSet();

	@Setter(INDENTIFIER_NAME_KEY)
	public void setIdentifierName(DataBinding<String> name);
	
	@Setter(SET_KEY)
	public void setSet(DataBinding<BSet> set);

	public static abstract class AddBSetValueImpl extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BSetValue>
			implements AddBSetValue {

		private static final Logger logger = Logger.getLogger(AddBSetValue.class.getPackage().getName());

		private DataBinding<String> identifierName;
		private DataBinding<BSet> set;

		@Override
		public Type getAssignableType() {
			return BSetValue.class;
		}

		@Override
		public BSetValue execute(RunTimeEvaluationContext evaluationContext) {

			BSetValue newSetValue = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String identifierName = getIdentifierName().getBindingValue(evaluationContext);
					BSet set = getSet().getBindingValue(evaluationContext);
					if (identifierName != null && set !=null) {
						newSetValue = resourceData.getFactory().makeSetValue(identifierName);
						newSetValue.setSet(set);
						set.addToEnumeratedValues(newSetValue);
					}
					else {
						logger.warning("Create a set value requires a name and a set");
					}
					
				}
				else {
					logger.warning("Cannot create set in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return newSetValue;

		}

		@Override
		public DataBinding<String> getIdentifierName() {
			if (identifierName == null) {
				identifierName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				identifierName.setBindingName(INDENTIFIER_NAME_KEY);
			}
			return identifierName;
		}
		
		@Override
		public DataBinding<BSet> getSet() {
			if (set == null) {
				set = new DataBinding<>(this, BSet.class, DataBinding.BindingDefinitionType.GET);
				set.setBindingName(SET_KEY);
			}
			return set;
		}

		@Override
		public void setIdentifierName(DataBinding<String> identifierName) {
			if (identifierName != null) {
				identifierName.setOwner(this);
				identifierName.setDeclaredType(String.class);
				identifierName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				identifierName.setBindingName(INDENTIFIER_NAME_KEY);
			}
			this.identifierName = identifierName;
		}
		
		@Override
		public void setSet(DataBinding<BSet> set) {
			if (set != null) {
				set.setOwner(this);
				set.setDeclaredType(BSet.class);
				set.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				set.setBindingName(SET_KEY);
			}
			this.set = set;
		}

	}

	@DefineValidationRule
	public static class IdentifierNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBSetValue> {
		public IdentifierNameBindingIsRequiredAndMustBeValid() {
			super("'identifierName'_binding_is_required_and_must_be_valid", AddBSetValue.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBSetValue object) {
			return object.getIdentifierName();
		}

	}
	
	@DefineValidationRule
	public static class SetBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBSetValue> {
		public SetBindingIsRequiredAndMustBeValid() {
			super("'set'_binding_is_required_and_must_be_valid", AddBSetValue.class);
		}

		@Override
		public DataBinding<BSet> getBinding(AddBSetValue object) {
			return object.getSet();
		}

	}

}
