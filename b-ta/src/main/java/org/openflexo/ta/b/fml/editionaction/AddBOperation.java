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
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.operation.BNormalOperation;
import org.openflexo.ta.b.model.substitution.BSkipSubstitution;

@ModelEntity
@ImplementationClass(AddBOperation.AddBOperationImpl.class)
@XMLElement
@FML("AddBOperation")
public interface AddBOperation extends BAction<BOperation> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String INDENTIFIER_NAME_KEY = "identifierName";

	@Getter(value = INDENTIFIER_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getIdentifierName();

	@Setter(INDENTIFIER_NAME_KEY)
	public void setIdentifierName(DataBinding<String> name);

	public static abstract class AddBOperationImpl extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BOperation>
			implements AddBOperation {

		private static final Logger logger = Logger.getLogger(AddBOperation.class.getPackage().getName());

		private DataBinding<String> identifierName;

		@Override
		public Type getAssignableType() {
			return BOperation.class;
		}

		@Override
		public BOperation execute(RunTimeEvaluationContext evaluationContext) {

			BNormalOperation newOperation = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String identifierName = getIdentifierName().getBindingValue(evaluationContext);
					if (identifierName != null) {
						newOperation = resourceData.getFactory().makeNormalOperation(identifierName);
						newOperation.setOperationBody(resourceData.getFactory().makeSubstitution(BSkipSubstitution.class, resourceData));
						resourceData.addToOperations(newOperation);
					}
					else {
						logger.warning("Create an operation requires a name");
					}
				}
				else {
					logger.warning("Cannot create operation in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return newOperation;

		}

		@Override
		public DataBinding<String> getIdentifierName() {
			if (identifierName == null) {
				identifierName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				identifierName.setBindingName("identifierName");
			}
			return identifierName;
		}

		@Override
		public void setIdentifierName(DataBinding<String> identifierName) {
			if (identifierName != null) {
				identifierName.setOwner(this);
				identifierName.setDeclaredType(String.class);
				identifierName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				identifierName.setBindingName("identifierName");
			}
			this.identifierName = identifierName;
		}

	}

	@DefineValidationRule
	public static class IdentifierNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBOperation> {
		public IdentifierNameBindingIsRequiredAndMustBeValid() {
			super("'identifierName'_binding_is_required_and_must_be_valid", AddBOperation.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBOperation object) {
			return object.getIdentifierName();
		}

	}

}
