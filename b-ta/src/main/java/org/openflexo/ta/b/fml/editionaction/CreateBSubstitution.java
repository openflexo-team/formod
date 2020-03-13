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
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.substitution.BAssignSubstitution;
import org.openflexo.ta.b.model.substitution.BBecomeElementOfSubstitution;

/**
 * Create a {@link BSubstitution} with an operator and  two  {@link BExpression}
 * 
 * @author steve
 *
 */
@ModelEntity
@ImplementationClass(CreateBSubstitution.CreateBSubstitutionImpl.class)
@XMLElement
@FML("CreateBSubstitution")
public interface CreateBSubstitution extends BAction<BSubstitution> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String OPERATOR_KEY = "operator";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String LEFT_OPERAND_KEY = "leftOperand";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String RIGHT_OPERAND_KEY = "rightOperand";

	@Getter(value = OPERATOR_KEY)
	@XMLAttribute
	public DataBinding<String> getOperator();

	@Setter(OPERATOR_KEY)
	public void setOperator(DataBinding<String> operator);

	@Getter(value = LEFT_OPERAND_KEY)
	@XMLAttribute
	public DataBinding<BExpression> getLeftOperand();

	@Setter(LEFT_OPERAND_KEY)
	public void setLeftOperand(DataBinding<BExpression> left);

	@Getter(value = RIGHT_OPERAND_KEY)
	@XMLAttribute
	public DataBinding<BExpression> getRightOperand();

	@Setter(RIGHT_OPERAND_KEY)
	public void setRightOperand(DataBinding<BExpression> right);

	public static abstract class CreateBSubstitutionImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BSubstitution>
			implements CreateBSubstitution {

		private static final Logger logger = Logger.getLogger(CreateBSubstitution.class.getPackage().getName());

		private DataBinding<BExpression> left;
		private DataBinding<BExpression> right;
		private DataBinding<String> operator;

		@Override
		public Type getAssignableType() {
			return BSubstitution.class;
		}

		@Override
		public BSubstitution execute(RunTimeEvaluationContext evaluationContext) {

			BSubstitution newSubstitution = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					
					BExpression left = getLeftOperand().getBindingValue(evaluationContext);
					BExpression right = getRightOperand().getBindingValue(evaluationContext);
					String operator = getOperator().getBindingValue(evaluationContext);

					if (operator != null && left != null && right != null) {
						if(operator.equalsIgnoreCase("BecomeElementOfSubstitution")){
							BBecomeElementOfSubstitution newSubstitution1 = resourceData.getFactory().makeSubstitution(BBecomeElementOfSubstitution.class, resourceData);
							newSubstitution1.addToIdentifiers(left);
							newSubstitution1.setSet(right);
							newSubstitution = newSubstitution1;
						}
						else if(operator.equalsIgnoreCase("AssignSubstitution")){
							BAssignSubstitution newSubstitution1  = resourceData.getFactory().makeSubstitution(BAssignSubstitution.class, resourceData);
							newSubstitution1.addToLeftExpressions(left);
							newSubstitution1.addToRightExpressions(right);
							newSubstitution = newSubstitution1;
						}
						else{
							logger.warning("invalid operator: "+operator+". Must be: 'BecomeElementOfSubstitution' or 'AssignSubstitution'");
						}
						
						}
						else  {
							logger.warning("Cannot create substitution without valid left, right and operator operands");
						}
						
				}
				else {
					logger.warning("Cannot create predicate in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return newSubstitution;

		}

		@Override
		public DataBinding<BExpression> getLeftOperand() {
			if (left == null) {
				left = new DataBinding<>(this, BExpression.class, DataBinding.BindingDefinitionType.GET);
				left.setBindingName("leftOperand");
			}
			return left;
		}

		@Override
		public void setLeftOperand(DataBinding<BExpression> left) {
			if (left != null) {
				left.setOwner(this);
				left.setDeclaredType(BExpression.class);
				left.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				left.setBindingName(LEFT_OPERAND_KEY);
			}
			this.left = left;
		}
		
		@Override
		public DataBinding<String> getOperator() {
			if (operator == null) {
				operator = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				operator.setBindingName(OPERATOR_KEY);
			}
			return operator;
		}

		@Override
		public void setOperator(DataBinding<String> operator) {
			if (operator != null) {
				operator.setOwner(this);
				operator.setDeclaredType(String.class);
				operator.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				operator.setBindingName(OPERATOR_KEY);
			}
			this.operator = operator;
		}

		@Override
		public DataBinding<BExpression> getRightOperand() {
			if (right == null) {
				right = new DataBinding<>(this, BExpression.class, DataBinding.BindingDefinitionType.GET);
				right.setBindingName(RIGHT_OPERAND_KEY);
			}
			return right;
		}

		@Override
		public void setRightOperand(DataBinding<BExpression> right) {
			if (right != null) {
				right.setOwner(this);
				right.setDeclaredType(BExpression.class);
				right.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				right.setBindingName(RIGHT_OPERAND_KEY);
			}
			this.right = right;
		}

	}

	@DefineValidationRule
	public static class LeftOperandBindingIsRequiredAndMustBeValid
			extends BindingIsRequiredAndMustBeValid<CreateBSubstitution> {
		public LeftOperandBindingIsRequiredAndMustBeValid() {
			super("'leftOperand'_binding_is_required_and_must_be_valid", CreateBSubstitution.class);
		}

		@Override
		public DataBinding<BExpression> getBinding(CreateBSubstitution object) {
			return object.getLeftOperand();
		}

	}

	@DefineValidationRule
	public static class RightOperandBindingIsRequiredAndMustBeValid
			extends BindingIsRequiredAndMustBeValid<CreateBSubstitution> {
		public RightOperandBindingIsRequiredAndMustBeValid() {
			super("'rightOperand'_binding_is_required_and_must_be_valid", CreateBSubstitution.class);
		}

		@Override
		public DataBinding<BExpression> getBinding(CreateBSubstitution object) {
			return object.getRightOperand();
		}

	}
	
	@DefineValidationRule
	public static class OperatorBindingIsRequiredAndMustBeValid
			extends BindingIsRequiredAndMustBeValid<CreateBSubstitution> {
		public OperatorBindingIsRequiredAndMustBeValid() {
			super("'operator'_binding_is_required_and_must_be_valid", CreateBSubstitution.class);
		}

		@Override
		public DataBinding<String> getBinding(CreateBSubstitution object) {
			return object.getOperator();
		}

	}

}
