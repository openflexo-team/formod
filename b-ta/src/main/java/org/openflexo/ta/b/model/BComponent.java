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

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.toolbox.StringUtils;

/**
 * An abstract B component (can be a machine, a system, a refinement or an implementation)
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@Imports({ @Import(BSystem.class), @Import(BMachine.class), @Import(BRefinement.class), @Import(BImplementation.class),
		@Import(BPredicate.class), @Import(BExpression.class), @Import(BOperation.class), @Import(BSubstitution.class) })
public interface BComponent extends BNamedObject, ResourceData<BComponent> {

	public enum BComponentType {
		System, Machine, Refinement, Implementation
	}

	@PropertyIdentifier(type = BExtendsClause.class, cardinality = Cardinality.LIST)
	public static final String EXTENDS_CLAUSES_KEY = "extendsClauses";
	@PropertyIdentifier(type = BImportsClause.class, cardinality = Cardinality.LIST)
	public static final String IMPORTS_CLAUSES_KEY = "importsClauses";
	@PropertyIdentifier(type = BIncludesClause.class, cardinality = Cardinality.LIST)
	public static final String INCLUDES_CLAUSES_KEY = "includesClauses";
	@PropertyIdentifier(type = BSeesClause.class, cardinality = Cardinality.LIST)
	public static final String SEES_CLAUSES_KEY = "seesClauses";
	@PropertyIdentifier(type = BUsesClause.class, cardinality = Cardinality.LIST)
	public static final String USES_CLAUSES_KEY = "usesClauses";

	@PropertyIdentifier(type = BSet.class, cardinality = Cardinality.LIST)
	public static final String SETS_KEY = "sets";
	@PropertyIdentifier(type = BAbstractConstant.class, cardinality = Cardinality.LIST)
	public static final String ABSTRACT_CONSTANTS_KEY = "abstractConstants";
	@PropertyIdentifier(type = BConcreteConstant.class, cardinality = Cardinality.LIST)
	public static final String CONCRETE_CONSTANTS_KEY = "concreteConstants";
	@PropertyIdentifier(type = BAbstractVariable.class, cardinality = Cardinality.LIST)
	public static final String ABSTRACT_VARIABLES_KEY = "abstractVariables";
	@PropertyIdentifier(type = BConcreteVariable.class, cardinality = Cardinality.LIST)
	public static final String CONCRETE_VARIABLES_KEY = "concreteVariables";
	@PropertyIdentifier(type = BPredicate.class)
	public static final String PROPERTIES_KEY = "properties";
	@PropertyIdentifier(type = BPredicate.class)
	public static final String INVARIANT_KEY = "invariant";
	@PropertyIdentifier(type = BPredicate.class, cardinality = Cardinality.LIST)
	public static final String ASSERTIONS_KEY = "assertions";
	@PropertyIdentifier(type = BOperation.class, cardinality = Cardinality.LIST)
	public static final String OPERATIONS_KEY = "operations";

	@Getter(value = EXTENDS_CLAUSES_KEY, cardinality = Cardinality.LIST, inverse = BVisibilityClause.COMPONENT_KEY)
	@XMLElement
	public List<BExtendsClause> getExtendsClauses();

	@Adder(EXTENDS_CLAUSES_KEY)
	@PastingPoint
	public void addToExtendsClauses(BExtendsClause aVisibilityClause);

	@Remover(EXTENDS_CLAUSES_KEY)
	public void removeFromExtendsClauses(BExtendsClause aVisibilityClause);

	@Getter(value = IMPORTS_CLAUSES_KEY, cardinality = Cardinality.LIST, inverse = BVisibilityClause.COMPONENT_KEY)
	@XMLElement
	public List<BImportsClause> getImportsClauses();

	@Adder(IMPORTS_CLAUSES_KEY)
	@PastingPoint
	public void addToImportsClauses(BImportsClause aVisibilityClause);

	@Remover(IMPORTS_CLAUSES_KEY)
	public void removeFromImportsClauses(BImportsClause aVisibilityClause);

	@Getter(value = INCLUDES_CLAUSES_KEY, cardinality = Cardinality.LIST, inverse = BVisibilityClause.COMPONENT_KEY)
	@XMLElement
	public List<BIncludesClause> getIncludesClauses();

	@Adder(INCLUDES_CLAUSES_KEY)
	@PastingPoint
	public void addToIncludesClauses(BIncludesClause aVisibilityClause);

	@Remover(INCLUDES_CLAUSES_KEY)
	public void removeFromIncludesClauses(BIncludesClause aVisibilityClause);

	@Getter(value = SEES_CLAUSES_KEY, cardinality = Cardinality.LIST, inverse = BVisibilityClause.COMPONENT_KEY)
	@XMLElement
	public List<BSeesClause> getSeesClauses();

	@Adder(SEES_CLAUSES_KEY)
	@PastingPoint
	public void addToSeesClauses(BSeesClause aVisibilityClause);

	@Remover(SEES_CLAUSES_KEY)
	public void removeFromSeesClauses(BSeesClause aVisibilityClause);

	@Getter(value = USES_CLAUSES_KEY, cardinality = Cardinality.LIST, inverse = BVisibilityClause.COMPONENT_KEY)
	@XMLElement
	public List<BUsesClause> getUsesClauses();

	@Adder(USES_CLAUSES_KEY)
	@PastingPoint
	public void addToUsesClauses(BUsesClause aVisibilityClause);

	@Remover(USES_CLAUSES_KEY)
	public void removeFromUsesClauses(BUsesClause aVisibilityClause);

	@Getter(value = SETS_KEY, cardinality = Cardinality.LIST, inverse = BSet.COMPONENT_KEY)
	@XMLElement
	public List<BSet> getSets();

	@Adder(SETS_KEY)
	@PastingPoint
	public void addToSets(BSet aSet);

	@Remover(SETS_KEY)
	public void removeFromSets(BSet aSet);

	@Getter(value = ABSTRACT_CONSTANTS_KEY, cardinality = Cardinality.LIST, inverse = BAbstractConstant.COMPONENT_KEY)
	@XMLElement
	public List<BAbstractConstant> getAbstractConstants();

	@Adder(ABSTRACT_CONSTANTS_KEY)
	@PastingPoint
	public void addToAbstractConstants(BAbstractConstant aConstant);

	@Remover(ABSTRACT_CONSTANTS_KEY)
	public void removeFromAbstractConstants(BAbstractConstant aConstant);

	@Getter(value = CONCRETE_CONSTANTS_KEY, cardinality = Cardinality.LIST, inverse = BConcreteConstant.COMPONENT_KEY)
	@XMLElement
	public List<BConcreteConstant> getConcreteConstants();

	@Adder(CONCRETE_CONSTANTS_KEY)
	@PastingPoint
	public void addToConcreteConstants(BConcreteConstant aConstant);

	@Remover(CONCRETE_CONSTANTS_KEY)
	public void removeFromConcreteConstants(BConcreteConstant aConstant);

	@Getter(value = ABSTRACT_VARIABLES_KEY, cardinality = Cardinality.LIST, inverse = BAbstractVariable.COMPONENT_KEY)
	@XMLElement
	public List<BAbstractVariable> getAbstractVariables();

	@Adder(ABSTRACT_VARIABLES_KEY)
	@PastingPoint
	public void addToAbstractVariables(BAbstractVariable aVariable);

	@Remover(ABSTRACT_VARIABLES_KEY)
	public void removeFromAbstractVariables(BAbstractVariable aVariable);

	@Getter(value = CONCRETE_VARIABLES_KEY, cardinality = Cardinality.LIST, inverse = BConcreteVariable.COMPONENT_KEY)
	@XMLElement
	public List<BConcreteVariable> getConcreteVariables();

	@Adder(CONCRETE_VARIABLES_KEY)
	@PastingPoint
	public void addToConcreteVariables(BConcreteVariable aVariable);

	@Remover(CONCRETE_VARIABLES_KEY)
	public void removeFromConcreteVariables(BConcreteVariable aVariable);

	/**
	 * Return predicate contained in PROPERTIES
	 * 
	 * @return
	 */
	@Getter(value = PROPERTIES_KEY)
	@XMLElement(context = "Properties_")
	public BPredicate getProperties();

	/**
	 * Sets predicate contained in PROPERTIES
	 * 
	 * @return
	 */
	@Setter(PROPERTIES_KEY)
	public void setProperties(BPredicate predicate);

	/**
	 * Return predicate contained in INVARIANT
	 * 
	 * @return
	 */
	@Getter(value = INVARIANT_KEY)
	@XMLElement(context = "Invariant_")
	public BPredicate getInvariant();

	/**
	 * Sets predicate contained in INVARIANT
	 * 
	 * @return
	 */
	@Setter(INVARIANT_KEY)
	public void setInvariant(BPredicate predicate);

	/**
	 * Return predicates contained in ASSERTIONS
	 * 
	 * @return
	 */
	@Getter(value = ASSERTIONS_KEY, cardinality = Cardinality.LIST)
	@XMLElement(context = "Assertion_")
	public List<BPredicate> getAssertions();

	/**
	 * Add to predicates contained in ASSERTIONS
	 * 
	 * @param aPredicate
	 */
	@Adder(ASSERTIONS_KEY)
	@PastingPoint
	public void addToAssertions(BPredicate aPredicate);

	/**
	 * Remove from predicates contained in ASSERTIONS
	 * 
	 * @param aPredicate
	 */
	@Remover(ASSERTIONS_KEY)
	public void removeFromAssertions(BPredicate aPredicate);

	@Getter(value = OPERATIONS_KEY, cardinality = Cardinality.LIST, inverse = BSet.COMPONENT_KEY)
	@XMLElement
	public List<BOperation> getOperations();

	@Adder(OPERATIONS_KEY)
	@PastingPoint
	public void addToOperations(BOperation anOperation);

	@Remover(OPERATIONS_KEY)
	public void removeFromOperations(BOperation anOperation);

	@Override
	public BResource getResource();

	/**
	 * Retrieve object with supplied serialization identifier, asserting this object resides in this {@link BComponent}
	 * 
	 * @param objectId
	 * @return
	 */
	public BObject getObjectWithSerializationIdentifier(String objectId);

	public BComponentType getComponentType();

	// public String getBRepresentation();

	/**
	 * Default base implementation for {@link BComponent}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class BComponentImpl extends BObjectImpl implements BComponent {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BComponentImpl.class.getPackage().getName());

		@Override
		public BComponent getComponent() {
			return this;
		}

		@Override
		public BResource getResource() {
			return (BResource) performSuperGetter(FLEXO_RESOURCE);
		}

		/*@Override
		public String toString() {
			return ("[" + getImplementedInterface().getSimpleName() + "]\n");
		}*/

		public String getBRepresentation() {
			StringBuffer returned = new StringBuffer();
			returned.append("/* Generated with Formod\n");
			returned.append(" * Author: Openflexo\n");
			returned.append(" * Creation date: " + new Date() + "\n");
			returned.append(" */\n");
			returned.append("SYSTEM\n");
			returned.append("    " + getName() + "\n");
			if (StringUtils.isNotEmpty(getRefinesClause())) {
				returned.append(getRefinesClause());
			}
			returned.append("\n");
			if (getSeesClauses().size() > 0) {
				returned.append("SEES\n");
				returned.append("    ");
				boolean isFirst = true;
				for (BSeesClause bSeesClause : getSeesClauses()) {
					returned.append((isFirst ? "" : ",") + bSeesClause.getReferencedComponentName());
					isFirst = false;
				}
				returned.append("\n");
			}
			if (getExtendsClauses().size() > 0) {
				returned.append("EXTENDS\n");
				returned.append("    ");
				boolean isFirst = true;
				for (BExtendsClause bExtendsClause : getExtendsClauses()) {
					returned.append((isFirst ? "" : ",") + bExtendsClause.getReferencedComponentName());
					isFirst = false;
				}
				returned.append("\n");
			}

			returned.append("\n");
			returned.append("END\n");
			return returned.toString();
		}

		protected String getRefinesClause() {
			return "";
		}

		@Override
		public String getSerializationIdentifier() {
			return getName();
		}

		@Override
		public BObject getObjectWithSerializationIdentifier(String objectId) {

			// Is it the component itself ?
			if (getName().equals(objectId)) {
				return this;
			}

			// Is it a set ?
			for (BSet set : getSets()) {
				if (set.getName().equals(objectId)) {
					return set;
				}
			}

			// A Member ?
			for (BAbstractConstant constant : getAbstractConstants()) {
				if (constant.getName().equals(objectId)) {
					return constant;
				}
			}
			for (BConcreteConstant constant : getConcreteConstants()) {
				if (constant.getName().equals(objectId)) {
					return constant;
				}
			}
			for (BAbstractVariable variable : getAbstractVariables()) {
				if (variable != null && variable.getName() != null && variable.getName().equals(objectId)) {
					return variable;
				}
			}
			for (BConcreteVariable variable : getConcreteVariables()) {
				if (variable != null && variable.getName() != null && variable.getName().equals(objectId)) {
					return variable;
				}
			}

			logger.warning("To be implemented: getObjectWithSerializationIdentifier(objectId)");
			return null;
		}
	}

}
