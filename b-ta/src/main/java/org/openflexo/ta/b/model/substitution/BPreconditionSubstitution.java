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

package org.openflexo.ta.b.model.substitution;

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
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSubstitution;

/**
 * Precondition substitution
 * 
 * @author steve
 *
 */
@ModelEntity
@ImplementationClass(value = BSubstitution.BSubstitutionImpl.class)
@XMLElement
public interface BPreconditionSubstitution extends BSubstitution {

	@PropertyIdentifier(type = BSubstitution.class)
	public static final String SUBSTITUTION_KEY = "substitutions";
	@PropertyIdentifier(type = BPredicate.class)
	public static final String PREDICATE_KEY = "predicate";

	/**
	 * Return substitution
	 * 
	 * @return
	 */
	@Getter(value = SUBSTITUTION_KEY)
	@XMLElement(context = "Substitution")
	public BSubstitution getSubstitution();

	/**
	 * Sets substitution
	 * 
	 * @return
	 */
	@Setter(SUBSTITUTION_KEY)
	public void setSubstitution(BSubstitution aSubstitution);

	/**
	 * Return predicate
	 * 
	 * @return
	 */
	@Getter(value = PREDICATE_KEY)
	@XMLElement(context = PREDICATE_KEY)
	public BPredicate getPredicate();

	/**
	 * Sets predicate
	 * 
	 * @return
	 */
	@Setter(PREDICATE_KEY)
	public void setPredicate(BPredicate aPredicate);

}
