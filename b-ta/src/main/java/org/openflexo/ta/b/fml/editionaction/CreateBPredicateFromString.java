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

import org.openflexo.foundation.fml.rt.error.FMLTechnologyError;
import org.openflexo.foundation.fml.rt.error.FMLNullReferenceError;
import org.openflexo.foundation.fml.rt.error.FMLEvaluation;
import org.openflexo.foundation.fml.rt.error.FMLExecutionException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.annotations.FMLAttribute;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.validation.BindingIsRequiredAndMustBeValid;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.ta.b.BModelSlot;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.parser.lexer.CustomLexer.EntryPointKind;

/**
 * Create a {@link BPredicate} while parsing a {@link String}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(CreateBPredicateFromString.CreateBPredicateFromStringImpl.class)
@FML("CreateBPredicateFromString")
public interface CreateBPredicateFromString extends BAction<BPredicate> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String PREDICATE_AS_STRING_KEY = "predicateAsString";

	@Getter(value = PREDICATE_AS_STRING_KEY)
	@FMLAttribute(value = PREDICATE_AS_STRING_KEY, required = true, description = "<html>B text of the predicate</html>")
	public DataBinding<String> getPredicateAsString();

	@Setter(PREDICATE_AS_STRING_KEY)
	public void setPredicateAsString(DataBinding<String> predicateAsString);

	public static abstract class CreateBPredicateFromStringImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BModelSlot, BComponent, BPredicate> implements CreateBPredicateFromString {

		private static final Logger logger = Logger.getLogger(CreateBPredicateFromString.class.getPackage().getName());

		private DataBinding<String> predicateAsString;

		@Override
		public Type getAssignableType() {
			return BPredicate.class;
		}

		@Override
		public BPredicate execute(RunTimeEvaluationContext evaluationContext) throws FMLExecutionException {

			BPredicate newPredicate = null;

			BComponent resourceData = getReceiver(evaluationContext);

			if (resourceData != null) {
				String stringToParse = FMLEvaluation.evaluate(getPredicateAsString(), evaluationContext, this);
				if (stringToParse != null) {
					try {
						newPredicate = (BPredicate) BParser.parse(stringToParse, resourceData.getFactory(), EntryPointKind.Predicate);
					} catch (ParseException | RuntimeException e) {
						// A text that is not a B predicate is an error that FML catches (BMethodology: "the formula is not a B predicate and
						// is ignored"; CORE-F-11 phase 7, decided with the user). The parser also fails, unchecked, on some texts that are no
						// predicate at all, the empty one for instance
						throw new FMLTechnologyError("Not a B predicate: '" + stringToParse + "' (" + e.getMessage() + ")", e);
					} catch (IOException e) {
						throw new FMLTechnologyError("Cannot read the B predicate '" + stringToParse + "': " + e.getMessage(), e);
					}
				}
				// No text: no predicate, as for a text that is not B
			}
			else {
				throw new FMLNullReferenceError("Cannot create predicate: no B component to work in");
			}

			if (newPredicate != null) {
				// Parsing a predicate on its own leaves it with no component: attach it to the one it is created in, as the factory
				// does for the objects it makes. Without it the predicate has no serialization identifier - which is its containment
				// path in its component - and a role pointing at it is lost on reload (FORMOD-D-15)
				newPredicate.setComponent(resourceData);
			}

			return newPredicate;

		}

		@Override
		public DataBinding<String> getPredicateAsString() {
			if (predicateAsString == null) {
				predicateAsString = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				predicateAsString.setBindingName("predicateAsString");
			}
			return predicateAsString;
		}

		@Override
		public void setPredicateAsString(DataBinding<String> predicateAsString) {
			if (predicateAsString != null) {
				predicateAsString.setOwner(this);
				predicateAsString.setDeclaredType(String.class);
				predicateAsString.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				predicateAsString.setBindingName("predicateAsString");
			}
			this.predicateAsString = predicateAsString;
		}

	}

	@DefineValidationRule
	public static class IdentifierNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<CreateBPredicateFromString> {
		public IdentifierNameBindingIsRequiredAndMustBeValid() {
			super("'predicateAsString'_binding_is_required_and_must_be_valid", CreateBPredicateFromString.class);
		}

		@Override
		public DataBinding<String> getBinding(CreateBPredicateFromString object) {
			return object.getPredicateAsString();
		}

	}

}
