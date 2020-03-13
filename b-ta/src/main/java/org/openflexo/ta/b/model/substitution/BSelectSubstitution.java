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
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSubstitution;

/**
 * Select substitution
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = BSubstitution.BSubstitutionImpl.class)
@XMLElement
public interface BSelectSubstitution extends BSubstitution {

	@PropertyIdentifier(type = BPredicate.class)
	public static final String CONDITION_KEY = "condition";
	@PropertyIdentifier(type = BSubstitution.class)
	public static final String THEN_KEY = "then";
	@PropertyIdentifier(type = BSubstitution.class, cardinality = Cardinality.LIST)
	public static final String WHEN_SUBSTITUTIONS_KEY = "whenSubstitutions";
	@PropertyIdentifier(type = BSubstitution.class)
	public static final String ELSE_KEY = "else";

	/**
	 * Return condition predicate
	 * 
	 * @return
	 */
	@Getter(value = CONDITION_KEY)
	@XMLElement(context = "Condition_")
	public BPredicate getCondition();

	/**
	 * Sets condition predicate
	 * 
	 * @return
	 */
	@Setter(CONDITION_KEY)
	public void setCondition(BPredicate aPredicate);

	/**
	 * Return THEN substitution
	 * 
	 * @return
	 */
	@Getter(value = THEN_KEY)
	@XMLElement(context = "Then_")
	public BSubstitution getThen();

	/**
	 * Sets THEN substitution
	 * 
	 * @return
	 */
	@Setter(THEN_KEY)
	public void setThen(BSubstitution aSubstitution);

	/**
	 * Return when substitutions
	 * 
	 * @return
	 */
	@Getter(value = WHEN_SUBSTITUTIONS_KEY, cardinality = Cardinality.LIST)
	@XMLElement(context = "WhenSubstitution_")
	public List<BSubstitution> getWhenSubstitutions();

	@Adder(WHEN_SUBSTITUTIONS_KEY)
	@PastingPoint
	public void addToWhenSubstitutions(BSubstitution aSubstitution);

	@Remover(WHEN_SUBSTITUTIONS_KEY)
	public void removeFromWhenSubstitutions(BSubstitution aSubstitution);

	/**
	 * Return else substitution
	 * 
	 * @return
	 */
	@Getter(value = ELSE_KEY)
	@XMLElement(context = "Else_")
	public BSubstitution getElse();

	/**
	 * Sets else substitution
	 * 
	 * @return
	 */
	@Setter(ELSE_KEY)
	public void setElse(BSubstitution aSubstitution);

}
