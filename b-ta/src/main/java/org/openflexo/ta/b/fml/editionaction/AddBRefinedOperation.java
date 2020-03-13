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
import org.openflexo.ta.b.model.operation.BRefinedOperation;
import org.openflexo.ta.b.model.substitution.BSkipSubstitution;

@ModelEntity
@ImplementationClass(AddBRefinedOperation.AddBRefinedOperationImpl.class)
@XMLElement
@FML("AddBRefinedOperation")
public interface AddBRefinedOperation extends BAction<BRefinedOperation> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String INDENTIFIER_NAME_KEY = "identifierName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String REFINEMENT_KEYWORD_KEY = "refinementKeyword";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String REFINED_OPERATION_NAME_KEY = "refinedOperationName";

	@Getter(value = INDENTIFIER_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getIdentifierName();

	@Setter(INDENTIFIER_NAME_KEY)
	public void setIdentifierName(DataBinding<String> name);

	@Getter(value = REFINEMENT_KEYWORD_KEY)
	@XMLAttribute
	public DataBinding<String> getRefinementKeyword();

	@Setter(REFINEMENT_KEYWORD_KEY)
	public void setRefinementKeyword(DataBinding<String> refinementKeyword);

	@Getter(value = REFINED_OPERATION_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getRefinedOperationName();

	@Setter(REFINED_OPERATION_NAME_KEY)
	public void setRefinedOperationName(DataBinding<String> refinedOperationName);

	public static abstract class AddBRefinedOperationImpl extends
			TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BRefinedOperation> implements AddBRefinedOperation {

		private static final Logger logger = Logger.getLogger(AddBRefinedOperation.class.getPackage().getName());

		private DataBinding<String> identifierName;
		private DataBinding<String> refinementKeyword;
		private DataBinding<String> refinedOperationName;

		@Override
		public Type getAssignableType() {
			return BRefinedOperation.class;
		}

		@Override
		public BRefinedOperation execute(RunTimeEvaluationContext evaluationContext) {

			BRefinedOperation newOperation = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String identifierName = getIdentifierName().getBindingValue(evaluationContext);
					if (identifierName != null) {
						System.out.println("******* create REFINED event");
						newOperation = resourceData.getFactory().makeRefinedOperation(identifierName);
						newOperation.setOperationBody(resourceData.getFactory().makeSubstitution(BSkipSubstitution.class, resourceData));
						resourceData.addToOperations(newOperation);
					}
					else {
						logger.warning("Create a refined operation requires a name");
					}
					String refinementKeyword = getRefinementKeyword().getBindingValue(evaluationContext);
					if (refinementKeyword != null) {
						System.out.println("******* refinementKeyword=" + refinementKeyword);
						newOperation.setRefinementKeyword(refinementKeyword);
					}
					else {
						logger.warning("Create a refined operation requires a refinement keyword");
					}
					String refinedOperationName = getRefinedOperationName().getBindingValue(evaluationContext);
					if (refinedOperationName != null) {
						System.out.println("******* refinedOperationName=" + refinedOperationName);
						newOperation.setRefinedOperationName(refinedOperationName);
					}
					else {
						logger.warning("Create a refined operation requires a refined operation name");
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

		@Override
		public DataBinding<String> getRefinementKeyword() {
			if (refinementKeyword == null) {
				refinementKeyword = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				refinementKeyword.setBindingName("refinementKeyword");
			}
			return refinementKeyword;
		}

		@Override
		public void setRefinementKeyword(DataBinding<String> refinementKeyword) {
			if (refinementKeyword != null) {
				refinementKeyword.setOwner(this);
				refinementKeyword.setDeclaredType(String.class);
				refinementKeyword.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				refinementKeyword.setBindingName("refinementKeyword");
			}
			this.refinementKeyword = refinementKeyword;
		}

		@Override
		public DataBinding<String> getRefinedOperationName() {
			if (refinedOperationName == null) {
				refinedOperationName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				refinedOperationName.setBindingName("refinedOperationName");
			}
			return refinedOperationName;
		}

		@Override
		public void setRefinedOperationName(DataBinding<String> refinedOperationName) {
			if (refinedOperationName != null) {
				refinedOperationName.setOwner(this);
				refinedOperationName.setDeclaredType(String.class);
				refinedOperationName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				refinedOperationName.setBindingName("identifierName");
			}
			this.refinedOperationName = refinedOperationName;
		}

	}

	@DefineValidationRule
	public static class IdentifierNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBRefinedOperation> {
		public IdentifierNameBindingIsRequiredAndMustBeValid() {
			super("'identifierName'_binding_is_required_and_must_be_valid", AddBRefinedOperation.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBRefinedOperation object) {
			return object.getIdentifierName();
		}

	}

	@DefineValidationRule
	public static class RefinementKeywordBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBRefinedOperation> {
		public RefinementKeywordBindingIsRequiredAndMustBeValid() {
			super("'refinementKeyword'_binding_is_required_and_must_be_valid", AddBRefinedOperation.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBRefinedOperation object) {
			return object.getRefinementKeyword();
		}

	}

	@DefineValidationRule
	public static class RefinedOperationNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBRefinedOperation> {
		public RefinedOperationNameBindingIsRequiredAndMustBeValid() {
			super("'refinedOperationName'_binding_is_required_and_must_be_valid", AddBRefinedOperation.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBRefinedOperation object) {
			return object.getRefinedOperationName();
		}

	}

}
