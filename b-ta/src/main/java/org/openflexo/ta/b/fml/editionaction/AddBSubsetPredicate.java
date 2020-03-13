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
import org.openflexo.ta.b.model.expression.BIdentifierExpression;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.predicate.BSubsetPredicate;

@ModelEntity
@ImplementationClass(AddBSubsetPredicate.AddBSubsetPredicateImpl.class)
@XMLElement
@FML("AddBSubsetPredicate")
public interface AddBSubsetPredicate extends BAction<BSubsetPredicate> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUB_SET_NAME_KEY = "subSetName";
	
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUPER_SET_NAME_KEY = "superSetName";
	
	@PropertyIdentifier(type = DataBinding.class)
	public static final String IS_INVARIANT_KEY = "isInvariant";

	@Getter(value = SUB_SET_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getSubSetName();
	
	@Getter(value = SUPER_SET_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getSuperSetName();
	
	@Getter(value = IS_INVARIANT_KEY)
	@XMLAttribute
	public DataBinding<Boolean> getIsInvariant();

	@Setter(SUB_SET_NAME_KEY)
	public void setSubSetName(DataBinding<String> name);
	
	@Setter(SUPER_SET_NAME_KEY)
	public void setSuperSetName(DataBinding<String> name);
	
	@Setter(IS_INVARIANT_KEY)
	public void setIsInvariant(DataBinding<Boolean> isInvariant);

	public static abstract class AddBSubsetPredicateImpl extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BSubsetPredicate>
			implements AddBSubsetPredicate {

		private static final Logger logger = Logger.getLogger(AddBSubsetPredicate.class.getPackage().getName());

		private DataBinding<String> subSetName;
		private DataBinding<String> superSetName;
		private DataBinding<Boolean> isInvariant;

		@Override
		public Type getAssignableType() {
			return BSubsetPredicate.class;
		}

		@Override
		public BSubsetPredicate execute(RunTimeEvaluationContext evaluationContext) {

			BSubsetPredicate subsetPredicate = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String subSetName = getSubSetName().getBindingValue(evaluationContext);
					String superSetName = getSuperSetName().getBindingValue(evaluationContext);
					Boolean isInvariant = getIsInvariant().getBindingValue(evaluationContext);
					if (subSetName != null && superSetName != null && isInvariant != null) {
						
						subsetPredicate = resourceData.getFactory().makePredicate(BSubsetPredicate.class, resourceData);
						BIdentifierExpression subSet = resourceData.getFactory().makeExpression(BIdentifierExpression.class, resourceData);
						subSet.setIdentifier(subSetName);
						subsetPredicate.setLeft(subSet);
						BIdentifierExpression superSet = resourceData.getFactory().makeExpression(BIdentifierExpression.class, resourceData);
						superSet.setIdentifier(superSetName);
						subsetPredicate.setRight(superSet);
						
						BPredicate predicate = null;
						if(isInvariant){
							predicate = resourceData.getInvariant();
						}
						else{
							predicate = resourceData.getProperties();
						}
						
						BConjunctPredicate newPredicate = resourceData.getFactory().makePredicate(BConjunctPredicate.class, resourceData);
						newPredicate.setLeft(predicate);
						newPredicate.setRight(subsetPredicate);
						
						if(isInvariant){
							resourceData.setInvariant(newPredicate);
						}
						else{
							resourceData.setProperties(newPredicate);
						}
						
					}
					else {
						logger.warning("Create a subsetPredicate requires a subSetName, a superSetName and a flag isInvariant");
					}
				}
				else {
					logger.warning("Cannot create subsetPredicate in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return subsetPredicate;

		}

		@Override
		public DataBinding<String> getSubSetName() {
			if (subSetName == null) {
				subSetName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				subSetName.setBindingName(SUB_SET_NAME_KEY);
			}
			return subSetName;
		}
		
		@Override
		public DataBinding<String> getSuperSetName() {
			if (superSetName == null) {
				superSetName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				superSetName.setBindingName(SUPER_SET_NAME_KEY);
			}
			return superSetName;
		}
		
		@Override
		public DataBinding<Boolean> getIsInvariant() {
			if (isInvariant == null) {
				isInvariant = new DataBinding<>(this, Boolean.class, DataBinding.BindingDefinitionType.GET);
				isInvariant.setBindingName(IS_INVARIANT_KEY);
			}
			return isInvariant;
		}

		@Override
		public void setSubSetName(DataBinding<String> subSetName) {
			if (subSetName != null) {
				subSetName.setOwner(this);
				subSetName.setDeclaredType(String.class);
				subSetName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				subSetName.setBindingName(SUB_SET_NAME_KEY);
			}
			this.subSetName = subSetName;
		}
		
		@Override
		public void setSuperSetName(DataBinding<String> superSetName) {
			if (superSetName != null) {
				superSetName.setOwner(this);
				superSetName.setDeclaredType(String.class);
				superSetName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				superSetName.setBindingName(SUPER_SET_NAME_KEY);
			}
			this.superSetName = superSetName;
		}
		
		@Override
		public void setIsInvariant(DataBinding<Boolean> isInvariant) {
			if (isInvariant != null) {
				isInvariant.setOwner(this);
				isInvariant.setDeclaredType(Boolean.class);
				isInvariant.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				isInvariant.setBindingName(IS_INVARIANT_KEY);
			}
			this.isInvariant = isInvariant;
		}

	}

	@DefineValidationRule
	public static class SubSetNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBSubsetPredicate> {
		public SubSetNameBindingIsRequiredAndMustBeValid() {
			super("'subSetName'_binding_is_required_and_must_be_valid", AddBSubsetPredicate.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBSubsetPredicate object) {
			return object.getSubSetName();
		}

	}
	
	@DefineValidationRule
	public static class SuperSetNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBSubsetPredicate> {
		public SuperSetNameBindingIsRequiredAndMustBeValid() {
			super("'superSetName'_binding_is_required_and_must_be_valid", AddBSubsetPredicate.class);
		}

		@Override
		public DataBinding<String> getBinding(AddBSubsetPredicate object) {
			return object.getSuperSetName();
		}

	}
	
	@DefineValidationRule
	public static class IsInvariantBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddBSubsetPredicate> {
		public IsInvariantBindingIsRequiredAndMustBeValid() {
			super("'isInvariant'_binding_is_required_and_must_be_valid", AddBSubsetPredicate.class);
		}

		@Override
		public DataBinding<Boolean> getBinding(AddBSubsetPredicate object) {
			return object.getIsInvariant();
		}

	}

}
