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

package org.openflexo.ta.b.model.expression;

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
@ImplementationClass(value = BExpression.BExpressionImpl.class)
@XMLElement
public interface BFunctionExpression extends BExpression {

	@PropertyIdentifier(type = BExpression.class)
	public static final String IDENTIFIER_KEY = "identifier";
	@PropertyIdentifier(type = BExpression.class, cardinality = Cardinality.LIST)
	public static final String PARAMETERS_KEY = "parameters";

	/**
	 * Return identifier
	 * 
	 * @return
	 */
	@Getter(value = IDENTIFIER_KEY)
	@XMLElement(context = "Identifier_")
	public BExpression getIdentifier();

	/**
	 * Sets expression
	 * 
	 * @return
	 */
	@Setter(IDENTIFIER_KEY)
	public void setIdentifier(BExpression anExpression);

	/**
	 * Return parameters
	 * 
	 * @return
	 */
	@Getter(value = PARAMETERS_KEY, cardinality = Cardinality.LIST)
	@XMLElement(context = "Parameter_")
	public List<BExpression> getParameters();

	@Adder(PARAMETERS_KEY)
	@PastingPoint
	public void addToParameters(BExpression anExpression);

	@Remover(PARAMETERS_KEY)
	public void removeFromParameters(BExpression anExpression);

}
