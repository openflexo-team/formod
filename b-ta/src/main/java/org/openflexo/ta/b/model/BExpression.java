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

import java.util.logging.Logger;

import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.ta.b.model.expression.BBinaryExpression;
import org.openflexo.ta.b.model.expression.BBooleanSetExpression;
import org.openflexo.ta.b.model.expression.BConvertBoolExpression;
import org.openflexo.ta.b.model.expression.BCoupleExpression;
import org.openflexo.ta.b.model.expression.BEmptySetExpression;
import org.openflexo.ta.b.model.expression.BFalseExpression;
import org.openflexo.ta.b.model.expression.BFunctionExpression;
import org.openflexo.ta.b.model.expression.BIdentifierExpression;
import org.openflexo.ta.b.model.expression.BImageExpression;
import org.openflexo.ta.b.model.expression.BIntSetExpression;
import org.openflexo.ta.b.model.expression.BIntegerExpression;
import org.openflexo.ta.b.model.expression.BIntegerSetExpression;
import org.openflexo.ta.b.model.expression.BMaxIntExpression;
import org.openflexo.ta.b.model.expression.BMinIntExpression;
import org.openflexo.ta.b.model.expression.BNatSetExpression;
import org.openflexo.ta.b.model.expression.BNaturalSetExpression;
import org.openflexo.ta.b.model.expression.BSetExtensionExpression;
import org.openflexo.ta.b.model.expression.BStrictNatSetExpression;
import org.openflexo.ta.b.model.expression.BStrictNaturalSetExpression;
import org.openflexo.ta.b.model.expression.BTrueExpression;
import org.openflexo.ta.b.model.expression.BUnaryExpression;

/**
 * A B expression
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BIdentifierExpression.class), @Import(BConvertBoolExpression.class), @Import(BIntSetExpression.class), @Import(BIntegerSetExpression.class),
		@Import(BNatSetExpression.class), @Import(BStrictNatSetExpression.class), @Import(BNaturalSetExpression.class),
		@Import(BStrictNaturalSetExpression.class), @Import(BMaxIntExpression.class), @Import(BMinIntExpression.class),
		@Import(BBooleanSetExpression.class), @Import(BTrueExpression.class), @Import(BFalseExpression.class),
		@Import(BEmptySetExpression.class), @Import(BIntegerExpression.class), @Import(BBinaryExpression.class),
		@Import(BUnaryExpression.class), @Import(BFunctionExpression.class), @Import(BSetExtensionExpression.class),
		@Import(BCoupleExpression.class), @Import(BImageExpression.class) })
public interface BExpression extends BObject, InnerBComponent {

	/**
	 * Default base implementation for {@link BExpression}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class BExpressionImpl extends BObjectImpl implements BExpression {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BObjectImpl.class.getPackage().getName());

		@Override
		public String getSerializationIdentifier() {
			// Cannot serialize id for expression
			return null;
		}
	}

}
