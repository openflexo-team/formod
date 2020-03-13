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
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.predicate.BBinaryPredicatePredicate;
import org.openflexo.ta.b.model.predicate.BBinaryPredicatePredicate.BinaryOperator;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.predicate.BDisjunctPredicate;
import org.openflexo.ta.b.model.predicate.BImplicationPredicate;

/**
 * Create a {@link BBinaryPredicatePredicate} with an operator and two other {@link BPredicate}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(CreateBBinaryPredicatePredicate.CreateBBinaryPredicatePredicateImpl.class)
@XMLElement
@FML("CreateBBinaryPredicatePredicate")
public interface CreateBBinaryPredicatePredicate extends BAction<BBinaryPredicatePredicate> {

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
	public DataBinding<BPredicate> getLeftOperand();

	@Setter(LEFT_OPERAND_KEY)
	public void setLeftOperand(DataBinding<BPredicate> left);

	@Getter(value = RIGHT_OPERAND_KEY)
	@XMLAttribute
	public DataBinding<BPredicate> getRightOperand();

	@Setter(RIGHT_OPERAND_KEY)
	public void setRightOperand(DataBinding<BPredicate> right);

	public static abstract class CreateBBinaryPredicatePredicateImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BBinaryPredicatePredicate>
			implements CreateBBinaryPredicatePredicate {

		private static final Logger logger = Logger.getLogger(CreateBBinaryPredicatePredicate.class.getPackage().getName());

		private DataBinding<BPredicate> left;
		private DataBinding<BPredicate> right;

		@Override
		public Type getAssignableType() {
			return BBinaryPredicatePredicate.class;
		}

		@Override
		public BBinaryPredicatePredicate execute(RunTimeEvaluationContext evaluationContext) {

			BBinaryPredicatePredicate newPredicate = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {

					if (getOperator() != null) {
						BPredicate left = getLeftOperand().getBindingValue(evaluationContext);
						BPredicate right = getRightOperand().getBindingValue(evaluationContext);
						if (left != null && right != null) {
							switch (getOperator()) {
								case Conjunct:
									newPredicate = resourceData.getFactory().makePredicate(BConjunctPredicate.class, resourceData);
									break;
								case Disjunct:
									newPredicate = resourceData.getFactory().makePredicate(BDisjunctPredicate.class, resourceData);
									break;
								case Implication:
									newPredicate = resourceData.getFactory().makePredicate(BImplicationPredicate.class, resourceData);
									break;
								default:
									break;
							}
							newPredicate.setLeft(left);
							newPredicate.setRight(right);
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

			return newPredicate;

		}

		@Override
		public DataBinding<BPredicate> getLeftOperand() {
			if (left == null) {
				left = new DataBinding<>(this, BPredicate.class, DataBinding.BindingDefinitionType.GET);
				left.setBindingName("leftOperand");
			}
			return left;
		}

		@Override
		public void setLeftOperand(DataBinding<BPredicate> left) {
			if (left != null) {
				left.setOwner(this);
				left.setDeclaredType(BPredicate.class);
				left.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				left.setBindingName("leftOperand");
			}
			this.left = left;
		}

		@Override
		public DataBinding<BPredicate> getRightOperand() {
			if (right == null) {
				right = new DataBinding<>(this, BPredicate.class, DataBinding.BindingDefinitionType.GET);
				right.setBindingName("rightOperand");
			}
			return right;
		}

		@Override
		public void setRightOperand(DataBinding<BPredicate> right) {
			if (right != null) {
				right.setOwner(this);
				right.setDeclaredType(BPredicate.class);
				right.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				right.setBindingName("rightOperand");
			}
			this.right = right;
		}

	}

	@DefineValidationRule
	public static class LeftOperandBindingIsRequiredAndMustBeValid
			extends BindingIsRequiredAndMustBeValid<CreateBBinaryPredicatePredicate> {
		public LeftOperandBindingIsRequiredAndMustBeValid() {
			super("'leftOperand'_binding_is_required_and_must_be_valid", CreateBBinaryPredicatePredicate.class);
		}

		@Override
		public DataBinding<BPredicate> getBinding(CreateBBinaryPredicatePredicate object) {
			return object.getLeftOperand();
		}

	}

	@DefineValidationRule
	public static class RightOperandBindingIsRequiredAndMustBeValid
			extends BindingIsRequiredAndMustBeValid<CreateBBinaryPredicatePredicate> {
		public RightOperandBindingIsRequiredAndMustBeValid() {
			super("'rightOperand'_binding_is_required_and_must_be_valid", CreateBBinaryPredicatePredicate.class);
		}

		@Override
		public DataBinding<BPredicate> getBinding(CreateBBinaryPredicatePredicate object) {
			return object.getRightOperand();
		}

	}

}
