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

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.model.BExpression;

/**
 * Abstract binary expression addressing two expressions
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BTotalFunctionExpression.class), @Import(BTotalInjectionExpression.class), @Import(BCompositionExpression.class),
		@Import(BPartialFunctionExpression.class), @Import(BUnionExpression.class), @Import(BMultOrCartExpression.class),
		@Import(BMinusOrSetSubstractExpression.class), @Import(BAddExpression.class), @Import(BTotalSurjectionExpression.class),
		@Import(BIntervalExpression.class), @Import(BPartialSurjectionExpression.class), @Import(BTotalBijectionExpression.class),
		@Import(BPartialInjectionExpression.class), @Import(BRelationsExpression.class), @Import(BIntersectionExpression.class)})
public interface BBinaryExpression extends BExpression {

	public enum BinaryOperator {
		Add, Composition, MinusOrSet, MultOrCart, PartialFunction, TotalFunction, TotalSurjection, TotalInjection, Union, Interval,
		PartialSurjection, TotalBijection, PartialInjection, Relations, Intersection
	}

	@PropertyIdentifier(type = BExpression.class)
	public static final String LEFT_KEY = "left";
	@PropertyIdentifier(type = BExpression.class)
	public static final String RIGHT_KEY = "right";

	/**
	 * Return left expression
	 * 
	 * @return
	 */
	@Getter(value = LEFT_KEY)
	@XMLElement(context = "Left_")
	public BExpression getLeft();

	/**
	 * Sets left expression
	 * 
	 * @return
	 */
	@Setter(LEFT_KEY)
	public void setLeft(BExpression anExpression);

	/**
	 * Return right expression
	 * 
	 * @return
	 */
	@Getter(value = RIGHT_KEY)
	@XMLElement(context = "Right_")
	public BExpression getRight();

	/**
	 * Sets right expression
	 * 
	 * @return
	 */
	@Setter(RIGHT_KEY)
	public void setRight(BExpression anExpression);

}
