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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.predicate.BConjunctPredicate;
import org.openflexo.ta.b.model.substitution.BParallelSubstitution;

/**
 * Tests that a {@link BObject} with no name - a predicate, a substitution, an expression - can be referenced across a reload
 * (FORMOD-D-15): its serialization identifier is its containment path in its component followed by a digest of its normalized B
 * representation, and resolving that identifier in the component parsed again from the same text gives the same object back.
 * 
 * @author sylvain
 *
 */
public class TestBObjectSerializationIdentifier extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestBObjectSerializationIdentifier.class.getPackage().getName());

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	/**
	 * System6 declares INVARIANT (count : NATURAL & total : NATURAL) and INITIALISATION count := 0 || total := 0: its invariant is a
	 * conjunction of two predicates and its initialisation a parallel substitution of two assignments, none of which has a name
	 */
	private BComponent getSystem6() {
		BComponent component = getBComponent("AtelierB/Example1/TestInitialisation.sys");
		assertEquals("System6", component.getName());
		return component;
	}

	/** The digest an identifier carries: see BSerializationIdentifiers */
	private String digestOf(BObject object) {
		return Integer.toHexString(object.getNormalizedBRepresentation().hashCode());
	}

	/** Parse the text of supplied component again, as a reload does, and give the new component the same resource */
	private BComponent parseAgain(BComponent component) {
		try {
			BComponent returned = BParser.parse(component.getBPrettyPrint(), component.getResource().getFactory());
			returned.setResource(component.getResource());
			return returned;
		} catch (ParseException | IOException e) {
			fail(e.getMessage());
			return null;
		}
	}

	@Test
	public void testComponentIsIdentifiedByItsName() {
		BComponent component = getSystem6();
		assertEquals("System6", component.getSerializationIdentifier());
		assertSame(component, component.getObjectWithSerializationIdentifier("System6"));
	}

	@Test
	public void testPredicateIsIdentifiedByItsPath() {
		BComponent component = getSystem6();
		assertTrue(component.getInvariant() instanceof BConjunctPredicate);
		BPredicate left = ((BConjunctPredicate) component.getInvariant()).getLeft();

		String identifier = left.getSerializationIdentifier();
		assertNotNull(identifier);
		assertEquals("@invariant/left~" + digestOf(left), identifier);
		assertSame(left, component.getObjectWithSerializationIdentifier(identifier));
	}

	@Test
	public void testSubstitutionIsIdentifiedByItsPath() {
		BComponent component = getSystem6();
		assertTrue(component.getInitialisation() instanceof BParallelSubstitution);
		BSubstitution assignment = ((BParallelSubstitution) component.getInitialisation()).getSubstitutions().get(1);

		String identifier = assignment.getSerializationIdentifier();
		assertNotNull(identifier);
		assertEquals("@initialisation/substitutions[1]~" + digestOf(assignment), identifier);
		assertSame(assignment, component.getObjectWithSerializationIdentifier(identifier));
	}

	@Test
	public void testIdentifiersAreResolvedInTheComponentParsedAgain() {
		BComponent component = getSystem6();
		String invariantId = ((BConjunctPredicate) component.getInvariant()).getRight().getSerializationIdentifier();
		String initialisationId = component.getInitialisation().getSerializationIdentifier();

		// What a reload does: the same text is parsed again, into other objects
		BComponent reloaded = parseAgain(component);
		assertNotSame(component, reloaded);

		BObject invariant = reloaded.getObjectWithSerializationIdentifier(invariantId);
		assertNotNull("Identifier " + invariantId + " was not resolved after a reload", invariant);
		assertSame(((BConjunctPredicate) reloaded.getInvariant()).getRight(), invariant);

		BObject initialisation = reloaded.getObjectWithSerializationIdentifier(initialisationId);
		assertNotNull("Identifier " + initialisationId + " was not resolved after a reload", initialisation);
		assertSame(reloaded.getInitialisation(), initialisation);
	}

	/** A conjunct inserted before the referenced one shifts no path here, but the representation finds it again */
	@Test
	public void testAMovedPredicateIsFoundByItsRepresentation() {
		BComponent component = getSystem6();
		BPredicate right = ((BConjunctPredicate) component.getInvariant()).getRight();
		String identifier = right.getSerializationIdentifier();

		// Swapping the two conjuncts leaves the identifier pointing at the other predicate
		BPredicate left = ((BConjunctPredicate) component.getInvariant()).getLeft();
		((BConjunctPredicate) component.getInvariant()).setLeft(null);
		((BConjunctPredicate) component.getInvariant()).setRight(null);
		((BConjunctPredicate) component.getInvariant()).setLeft(right);
		((BConjunctPredicate) component.getInvariant()).setRight(left);

		assertSame(right, component.getObjectWithSerializationIdentifier(identifier));

		// The component is loaded once for the whole class: leave it as it was found
		((BConjunctPredicate) component.getInvariant()).setLeft(null);
		((BConjunctPredicate) component.getInvariant()).setRight(null);
		((BConjunctPredicate) component.getInvariant()).setLeft(left);
		((BConjunctPredicate) component.getInvariant()).setRight(right);
	}

	/** A path leading nowhere any more still finds the object its digest designates */
	@Test
	public void testAPathLeadingNowhereFallsBackOnTheDigest() {
		BComponent component = getSystem6();
		BPredicate left = ((BConjunctPredicate) component.getInvariant()).getLeft();
		assertTrue(left.getNormalizedBRepresentation(), left.getNormalizedBRepresentation().endsWith(" : NATURAL"));

		assertSame(left, component.getObjectWithSerializationIdentifier("@invariant/left/left~" + digestOf(left)));
	}

	/** A digest nothing matches any more leaves the path decide: the object whose text was edited */
	@Test
	public void testAChangedDigestFallsBackOnThePath() {
		BComponent component = getSystem6();
		BPredicate left = ((BConjunctPredicate) component.getInvariant()).getLeft();

		assertSame(left, component.getObjectWithSerializationIdentifier("@invariant/left~deadbeef"));
	}

	/** An identifier neither the path nor the digest matches resolves to null, with no exception */
	@Test
	public void testAnUnknownIdentifierIsNotResolved() {
		BComponent component = getSystem6();
		// invariant/left/left is the identifier expression of the member predicate; one level deeper, nothing is left
		assertNull(component.getObjectWithSerializationIdentifier("@invariant/left/left/left~deadbeef"));
		assertNull(component.getObjectWithSerializationIdentifier("@operations[12]"));
		assertNull(component.getObjectWithSerializationIdentifier("NoSuchVariable"));
	}

	/**
	 * An identifier is carried by a FlexoObjectReference, whose string form is cut on '#' and on the last '_': an identifier holding
	 * one of them would be read back wrong, so none must
	 */
	@Test
	public void testAnIdentifierCarriesNoReferenceSeparator() {
		BComponent component = getSystem6();
		BConjunctPredicate invariant = (BConjunctPredicate) component.getInvariant();

		for (BObject object : new BObject[] { invariant, invariant.getLeft(), invariant.getRight(), component.getInitialisation(),
				((BParallelSubstitution) component.getInitialisation()).getSubstitutions().get(0) }) {
			String identifier = object.getSerializationIdentifier();
			assertNotNull(identifier);
			assertEquals(identifier, -1, identifier.indexOf('#'));
			assertEquals(identifier, -1, identifier.indexOf('_'));
		}
	}

}
