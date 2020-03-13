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
import org.openflexo.ta.b.model.BPredicate.BPredicateImpl;
import org.openflexo.ta.b.model.predicate.BBinaryPredicatePredicate;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;

@ModelEntity
@ImplementationClass(RemoveBPredicateInBPredicate.RemoveBPredicateInBPredicateImpl.class)
@XMLElement
@FML("RemoveBPredicateInBPredicate")
public interface RemoveBPredicateInBPredicate extends BAction<BPredicate> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String CONTAINER_PREDICATE_KEY = "containerPredicate";
	
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUB_PREDICATE_KEY = "subPredicate";

	@Getter(value = CONTAINER_PREDICATE_KEY)
	@XMLAttribute
	public DataBinding<BPredicate> getContainerPredicate();

	@Getter(value = SUB_PREDICATE_KEY)
	@XMLAttribute
	public DataBinding<BPredicate> getSubPredicate();

	@Setter(CONTAINER_PREDICATE_KEY)
	public void setContainerPredicate(DataBinding<BPredicate> containerPredicate);
	
	@Setter(SUB_PREDICATE_KEY)
	public void setSubPredicate(DataBinding<BPredicate> subPredicate);

	public static abstract class RemoveBPredicateInBPredicateImpl extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BPredicate>
			implements RemoveBPredicateInBPredicate {

		private static final Logger logger = Logger.getLogger(RemoveBPredicateInBPredicate.class.getPackage().getName());

		private DataBinding<BPredicate> containerPredicate;
		private DataBinding<BPredicate> subPredicate;

		@Override
		public Type getAssignableType() {
			return BPredicate.class;
		}

		@Override
		public BPredicate execute(RunTimeEvaluationContext evaluationContext) {

			BPredicate newPredicate = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					BPredicate containerPredicate = getContainerPredicate().getBindingValue(evaluationContext);
					BPredicate subPredicate = getSubPredicate().getBindingValue(evaluationContext);
					if (containerPredicate != null && subPredicate !=null) {
						newPredicate = computePredicateRemoval(resourceData, containerPredicate);
					}
					else {
						logger.warning("RemoveBPredicateInBPredicate requires a containerPredicate and a subPredicate");
					}
					
				}
				else {
					logger.warning("Cannot proceed with RemoveBPredicateInBPredicate with null resource data");
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

		private BPredicate computePredicateRemoval(BComponent resourceData,  BPredicate containerPredicate2) {
			if(subPredicate == containerPredicate2 ){
				return null;
			}
			
			
			
			BBinaryPredicatePredicate newPred = ((BBinaryPredicatePredicate)((BPredicateImpl)containerPredicate2));
			
			if(newPred.getRight()==subPredicate){
				return newPred.getLeft();
			}
			else{
				BConjunctPredicate newPredicate =  resourceData.getFactory().makePredicate(BConjunctPredicate.class, resourceData);
				newPredicate.setLeft(computePredicateRemoval(resourceData, newPred.getLeft()));
				newPredicate.setRight(newPred.getRight());
				return newPredicate;
			}
		}

		@Override
		public DataBinding<BPredicate> getContainerPredicate() {
			if (containerPredicate == null) {
				containerPredicate = new DataBinding<>(this, BPredicate.class, DataBinding.BindingDefinitionType.GET);
				containerPredicate.setBindingName(CONTAINER_PREDICATE_KEY);
			}
			return containerPredicate;
		}
		
		@Override
		public DataBinding<BPredicate> getSubPredicate() {
			if (subPredicate == null) {
				subPredicate = new DataBinding<>(this, BPredicate.class, DataBinding.BindingDefinitionType.GET);
				subPredicate.setBindingName(SUB_PREDICATE_KEY);
			}
			return subPredicate;
		}

		@Override
		public void setContainerPredicate(DataBinding<BPredicate> containerPredicate) {
			if (containerPredicate != null) {
				containerPredicate.setOwner(this);
				containerPredicate.setDeclaredType(BPredicate.class);
				containerPredicate.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				containerPredicate.setBindingName(CONTAINER_PREDICATE_KEY);
			}
			this.containerPredicate = containerPredicate;
		}
		
		@Override
		public void setSubPredicate(DataBinding<BPredicate> subPredicate) {
			if (subPredicate != null) {
				subPredicate.setOwner(this);
				subPredicate.setDeclaredType(BPredicate.class);
				subPredicate.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				subPredicate.setBindingName(SUB_PREDICATE_KEY);
			}
			this.subPredicate = subPredicate;
		}

	}

	@DefineValidationRule
	public static class ContainerPredicateBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<RemoveBPredicateInBPredicate> {
		public ContainerPredicateBindingIsRequiredAndMustBeValid() {
			super("'containerPredicate'_binding_is_required_and_must_be_valid", RemoveBPredicateInBPredicate.class);
		}

		@Override
		public DataBinding<BPredicate> getBinding(RemoveBPredicateInBPredicate object) {
			return object.getContainerPredicate();
		}

	}
	
	@DefineValidationRule
	public static class SubPredicateBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<RemoveBPredicateInBPredicate> {
		public SubPredicateBindingIsRequiredAndMustBeValid() {
			super("'subPredicate'_binding_is_required_and_must_be_valid", RemoveBPredicateInBPredicate.class);
		}

		@Override
		public DataBinding<BPredicate> getBinding(RemoveBPredicateInBPredicate object) {
			return object.getSubPredicate();
		}

	}

}
