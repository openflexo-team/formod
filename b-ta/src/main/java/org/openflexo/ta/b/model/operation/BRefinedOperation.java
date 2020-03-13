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

package org.openflexo.ta.b.model.operation;

import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * B refined operation
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = BNormalOperation.BNormalOperationImpl.class)
@XMLElement
public interface BRefinedOperation extends BNormalOperation {

	@PropertyIdentifier(type = String.class)
	public static final String REFINEMENT_KEYWORD_KEY = "refinementKeyword";
	@PropertyIdentifier(type = String.class)
	public static final String REFINED_OPERATION_NAME_KEY = "refinedOperationName";

	/**
	 * Return refinement keyword
	 * 
	 * @return
	 */
	@Getter(value = REFINEMENT_KEYWORD_KEY)
	@XMLAttribute
	public String getRefinementKeyword();

	/**
	 * Sets refinement keyword
	 * 
	 * @return
	 */
	@Setter(REFINEMENT_KEYWORD_KEY)
	public void setRefinementKeyword(String refinementKeyword);

	/**
	 * Return refined operation name
	 * 
	 * @return
	 */
	@Getter(value = REFINED_OPERATION_NAME_KEY)
	@XMLAttribute
	public String getRefinedOperationName();

	/**
	 * Sets refined operation name
	 * 
	 * @return
	 */
	@Setter(REFINED_OPERATION_NAME_KEY)
	public void setRefinedOperationName(String aName);

}
