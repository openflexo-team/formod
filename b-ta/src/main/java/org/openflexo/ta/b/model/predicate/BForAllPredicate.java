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

package org.openflexo.ta.b.model.predicate;

import java.util.List;

import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BPredicate;

/**
 * Expression composing expressions using parenthesis
 * 
 * <pre>
 * ( Expr1 Expr2 Expr...)
 * </pre>
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = BPredicate.BPredicateImpl.class)
@XMLElement
public interface BForAllPredicate extends BPredicate {

	@PropertyIdentifier(type = BExpression.class, cardinality = Cardinality.LIST)
	public static final String EXPRESSIONS_KEY = "expressions";
	@PropertyIdentifier(type = BPredicate.class)
	public static final String IMPLICATION_KEY = "implication";

	/**
	 * Return expressions
	 * 
	 * @return
	 */
	@Getter(value = EXPRESSIONS_KEY, cardinality = Cardinality.LIST)
	@XMLElement
	public List<BExpression> getExpressions();

	@Adder(EXPRESSIONS_KEY)
	@PastingPoint
	public void addToExpressions(BExpression anExpression);

	@Remover(EXPRESSIONS_KEY)
	public void removeFromExpressions(BExpression anExpression);

	/**
	 * Return implication
	 * 
	 * @return
	 */
	@Getter(value = IMPLICATION_KEY)
	@XMLElement(context = "Implication_")
	public BPredicate getImplication();

	/**
	 * Sets implication
	 * 
	 * @return
	 */
	@Setter(IMPLICATION_KEY)
	public void setImplication(BPredicate aPredicate);

}
