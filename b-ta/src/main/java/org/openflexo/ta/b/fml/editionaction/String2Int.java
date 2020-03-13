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

@ModelEntity
@ImplementationClass(String2Int.String2IntImpl.class)
@XMLElement
@FML("String2Int")
public interface String2Int extends BAction<Integer> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String STRING_KEY = "string";

	@Getter(value = STRING_KEY)
	@XMLAttribute
	public DataBinding<String> getString();

	@Setter(STRING_KEY)
	public void setString(DataBinding<String> name);

	public static abstract class String2IntImpl extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, Integer>
			implements String2Int {

		private static final Logger logger = Logger.getLogger(String2Int.class.getPackage().getName());

		private DataBinding<String> string;

		@Override
		public Type getAssignableType() {
			return Integer.class;
		}

		@Override
		public Integer execute(RunTimeEvaluationContext evaluationContext) {

			Integer integer = null;


			try {
					String string = getString().getBindingValue(evaluationContext);
					if (string != null) {
						integer = Integer.parseInt(string);
					}
					else {
						logger.warning("string required");
					}
				

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return integer;

		}

		@Override
		public DataBinding<String> getString() {
			if (string == null) {
				string = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				string.setBindingName("string");
			}
			return string;
		}

		@Override
		public void setString(DataBinding<String> string) {
			if (string != null) {
				string.setOwner(this);
				string.setDeclaredType(String.class);
				string.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				string.setBindingName("string");
			}
			this.string = string;
		}

	}

	@DefineValidationRule
	public static class StringBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<String2Int> {
		public StringBindingIsRequiredAndMustBeValid() {
			super("'string'_binding_is_required_and_must_be_valid", String2Int.class);
		}

		@Override
		public DataBinding<String> getBinding(String2Int object) {
			return object.getString();
		}

	}

}
