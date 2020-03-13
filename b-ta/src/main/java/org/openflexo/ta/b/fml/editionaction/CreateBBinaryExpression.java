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
import org.openflexo.ta.b.model.expression.BAddExpression;
import org.openflexo.ta.b.model.expression.BBinaryExpression;
import org.openflexo.ta.b.model.expression.BBinaryExpression.BinaryOperator;
import org.openflexo.ta.b.model.expression.BCompositionExpression;
import org.openflexo.ta.b.model.expression.BIntersectionExpression;
import org.openflexo.ta.b.model.expression.BIntervalExpression;
import org.openflexo.ta.b.model.expression.BMinusOrSetSubstractExpression;
import org.openflexo.ta.b.model.expression.BMultOrCartExpression;
import org.openflexo.ta.b.model.expression.BPartialFunctionExpression;
import org.openflexo.ta.b.model.expression.BPartialInjectionExpression;
import org.openflexo.ta.b.model.expression.BPartialSurjectionExpression;
import org.openflexo.ta.b.model.expression.BRelationsExpression;
import org.openflexo.ta.b.model.expression.BTotalBijectionExpression;
import org.openflexo.ta.b.model.expression.BTotalFunctionExpression;
import org.openflexo.ta.b.model.expression.BTotalInjectionExpression;
import org.openflexo.ta.b.model.expression.BTotalSurjectionExpression;
import org.openflexo.ta.b.model.expression.BUnionExpression;
import org.openflexo.ta.b.model.predicate.BBinaryExpressionPredicate;

/**
 * Create a {@link BBinaryExpressionPredicate} with an operator and two other {@link BExpression}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(CreateBBinaryExpression.CreateBBinaryExpressionImpl.class)
@XMLElement
@FML("CreateBBinaryExpression")
public interface CreateBBinaryExpression extends BAction<BBinaryExpression> {

	@PropertyIdentifier(type = BinaryOperator.class)
	public static final String OPERATOR_KEY = "operator";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String LEFT_OPERAND_KEY = "leftOperand";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String RIGHT_OPERAND_KEY = "rightOperand";

	@Getter(value = OPERATOR_KEY)
	@XMLAttribute
	public BinaryOperator getOperator();

	@Setter(OPERATOR_KEY)
	public void setOperator(BinaryOperator operator);

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

	public static abstract class CreateBBinaryExpressionImpl extends
			TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BBinaryExpression> implements CreateBBinaryExpression {

		private static final Logger logger = Logger.getLogger(CreateBBinaryExpression.class.getPackage().getName());

		private DataBinding<BExpression> left;
		private DataBinding<BExpression> right;

		@Override
		public Type getAssignableType() {
			return BBinaryExpression.class;
		}

		@Override
		public BBinaryExpression execute(RunTimeEvaluationContext evaluationContext) {

			BBinaryExpression newExpression = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {

					if (getOperator() != null) {
						BExpression left = getLeftOperand().getBindingValue(evaluationContext);
						BExpression right = getRightOperand().getBindingValue(evaluationContext);
						if (left != null && right != null) {
							switch (getOperator()) {
								case Add:
									newExpression = resourceData.getFactory().makeExpression(BAddExpression.class, resourceData);
									break;
								case Composition:
									newExpression = resourceData.getFactory().makeExpression(BCompositionExpression.class, resourceData);
									break;
								case MinusOrSet:
									newExpression = resourceData.getFactory().makeExpression(BMinusOrSetSubstractExpression.class,
											resourceData);
									break;
								case MultOrCart:
									newExpression = resourceData.getFactory().makeExpression(BMultOrCartExpression.class, resourceData);
									break;
								case PartialFunction:
									newExpression = resourceData.getFactory().makeExpression(BPartialFunctionExpression.class,
											resourceData);
									break;
								case TotalFunction:
									newExpression = resourceData.getFactory().makeExpression(BTotalFunctionExpression.class, resourceData);
									break;
								case TotalBijection:
									newExpression = resourceData.getFactory().makeExpression(BTotalBijectionExpression.class, resourceData);
									break;
								case PartialInjection:
									newExpression = resourceData.getFactory().makeExpression(BPartialInjectionExpression.class, resourceData);
									break;
								case Relations:
									newExpression = resourceData.getFactory().makeExpression(BRelationsExpression.class, resourceData);
									break;
								case Intersection:
									newExpression = resourceData.getFactory().makeExpression(BIntersectionExpression.class, resourceData);
									break;
								case Interval:
									newExpression = resourceData.getFactory().makeExpression(BIntervalExpression.class, resourceData);
									break;
								case TotalSurjection:
									newExpression = resourceData.getFactory().makeExpression(BTotalSurjectionExpression.class, resourceData);
									break;
								case PartialSurjection:
									newExpression = resourceData.getFactory().makeExpression(BPartialSurjectionExpression.class, resourceData);
									break;
								case TotalInjection:
									newExpression = resourceData.getFactory().makeExpression(BTotalInjectionExpression.class, resourceData);
									break;
								case Union:
									newExpression = resourceData.getFactory().makeExpression(BUnionExpression.class, resourceData);
									break;
								default:
									break;
							}
							newExpression.setLeft(left);
							newExpression.setRight(right);
						}
						else if (left == null) {
							logger.warning("Cannot create left operand predicate");
						}
						else if (right == null) {
							logger.warning("Cannot create right operand predicate");
						}
					}
					else {
						logger.warning("Cannot create predicate without operator");
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

			return newExpression;

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
				left.setBindingName("leftOperand");
			}
			this.left = left;
		}

		@Override
		public DataBinding<BExpression> getRightOperand() {
			if (right == null) {
				right = new DataBinding<>(this, BExpression.class, DataBinding.BindingDefinitionType.GET);
				right.setBindingName("rightOperand");
			}
			return right;
		}

		@Override
		public void setRightOperand(DataBinding<BExpression> right) {
			if (right != null) {
				right.setOwner(this);
				right.setDeclaredType(BExpression.class);
				right.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				right.setBindingName("rightOperand");
			}
			this.right = right;
		}

	}

	@DefineValidationRule
	public static class LeftOperandBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<CreateBBinaryExpression> {
		public LeftOperandBindingIsRequiredAndMustBeValid() {
			super("'leftOperand'_binding_is_required_and_must_be_valid", CreateBBinaryExpression.class);
		}

		@Override
		public DataBinding<BExpression> getBinding(CreateBBinaryExpression object) {
			return object.getLeftOperand();
		}

	}

	@DefineValidationRule
	public static class RightOperandBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<CreateBBinaryExpression> {
		public RightOperandBindingIsRequiredAndMustBeValid() {
			super("'rightOperand'_binding_is_required_and_must_be_valid", CreateBBinaryExpression.class);
		}

		@Override
		public DataBinding<BExpression> getBinding(CreateBBinaryExpression object) {
			return object.getRightOperand();
		}

	}

}
