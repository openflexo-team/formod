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

package org.openflexo.ta.b.model;

import java.util.List;

import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.ta.b.model.operation.BNormalOperation;
import org.openflexo.ta.b.model.operation.BRefinedOperation;

/**
 * A B operation
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BNormalOperation.class), @Import(BRefinedOperation.class) })
public interface BOperation extends BNamedObject, InnerBComponent {
	@PropertyIdentifier(type = BExpression.class, cardinality = Cardinality.LIST)
	public static final String PARAMETERS_KEY = "parameters";
	@PropertyIdentifier(type = BExpression.class, cardinality = Cardinality.LIST)
	public static final String RETURN_VALUES_KEY = "returnValues";
	@PropertyIdentifier(type = BSubstitution.class)
	public static final String OPERATION_BODY_KEY = "operationBody";

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

	/**
	 * Return return values
	 * 
	 * @return
	 */
	@Getter(value = RETURN_VALUES_KEY, cardinality = Cardinality.LIST)
	@XMLElement(context = "ReturnValue")
	public List<BExpression> getReturnValues();

	@Adder(RETURN_VALUES_KEY)
	@PastingPoint
	public void addToReturnValues(BExpression anExpression);

	@Remover(RETURN_VALUES_KEY)
	public void removeFromReturnValues(BExpression anExpression);

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";

	/**
	 * Return operation body
	 * 
	 * @return
	 */
	@Getter(value = OPERATION_BODY_KEY)
	@XMLElement(context = "OperationBody_")
	public BSubstitution getOperationBody();

	/**
	 * Sets name of this object
	 * 
	 * @return
	 */
	@Setter(OPERATION_BODY_KEY)
	public void setOperationBody(BSubstitution substitution);
}
