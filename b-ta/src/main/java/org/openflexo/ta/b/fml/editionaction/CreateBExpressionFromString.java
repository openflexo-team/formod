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

import java.io.IOException;
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
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.predicate.BEqualPredicate;
import org.openflexo.ta.b.parser.lexer.CustomLexer.EntryPointKind;

/**
 * Create a {@link BExpression} while parsing a {@link String}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(CreateBExpressionFromString.CreateBExpressionFromStringImpl.class)
@XMLElement
@FML("CreateBExpressionFromString")
public interface CreateBExpressionFromString extends BAction<BExpression> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String PREDICATE_AS_STRING_KEY = "expressionAsString";

	@Getter(value = PREDICATE_AS_STRING_KEY)
	@XMLAttribute
	public DataBinding<String> getExpressionAsString();

	@Setter(PREDICATE_AS_STRING_KEY)
	public void setExpressionAsString(DataBinding<String> expressionAsString);

	public static abstract class CreateBExpressionFromStringImpl extends
			TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BExpression> implements CreateBExpressionFromString {

		private static final Logger logger = Logger.getLogger(CreateBExpressionFromString.class.getPackage().getName());

		private DataBinding<String> expressionAsString;

		@Override
		public Type getAssignableType() {
			return BExpression.class;
		}

		@Override
		public BExpression execute(RunTimeEvaluationContext evaluationContext) {

			BExpression newExpression = null;

			BComponent resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String stringToParse = getExpressionAsString().getBindingValue(evaluationContext);
					if (stringToParse != null) {
						try {
							BEqualPredicate predicate = (BEqualPredicate) BParser.parse("e="+stringToParse, resourceData.getFactory(),
									EntryPointKind.Predicate);
							
							
							//newExpression = (BExpression) BParser.parse(stringToParse, resourceData.getFactory(), EntryPointKind.Expression);
							newExpression = predicate.getRight();
						} catch (ParseException e) {
							logger.warning("Cannot parse expression: " + stringToParse);
						} catch (IOException e) {
							logger.warning("Unexpected IO exception");
							e.printStackTrace();
						}
					}
					else {
						logger.warning("Cannot create expression without string to parse");
					}
				}
				else {
					logger.warning("Cannot create expression in null resource data");
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
		public DataBinding<String> getExpressionAsString() {
			if (expressionAsString == null) {
				expressionAsString = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				expressionAsString.setBindingName("expressionAsString");
			}
			return expressionAsString;
		}

		@Override
		public void setExpressionAsString(DataBinding<String> expressionAsString) {
			if (expressionAsString != null) {
				expressionAsString.setOwner(this);
				expressionAsString.setDeclaredType(String.class);
				expressionAsString.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				expressionAsString.setBindingName("expressionAsString");
			}
			this.expressionAsString = expressionAsString;
		}

	}

	@DefineValidationRule
	public static class IdentifierNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<CreateBExpressionFromString> {
		public IdentifierNameBindingIsRequiredAndMustBeValid() {
			super("'expressionAsString'_binding_is_required_and_must_be_valid", CreateBExpressionFromString.class);
		}

		@Override
		public DataBinding<String> getBinding(CreateBExpressionFromString object) {
			return object.getExpressionAsString();
		}

	}

}
