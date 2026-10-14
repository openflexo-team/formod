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

import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.PamelaModelFactory;
import org.openflexo.pamela.model.ModelEntity;
import org.openflexo.pamela.model.ModelProperty;

/**
 * Computes and resolves the serialization identifier of the {@link BObject}s that have no name - predicates, substitutions and
 * expressions - so that a reference to one of them survives a reload. A named object (a {@link BComponent}, a {@link BSet}, a
 * {@link BMember}, an operation) keeps its name as identifier and does not come here.
 * 
 * An identifier is the containment path of the object in its component, prefixed by {@link #PATH_PREFIX} and followed by a digest of
 * its normalized B representation: <code>&#64;invariant/right~1f3a0b7c</code>. Resolution follows the path, then checks that what it
 * finds still reads the same; when it does not, the component is scanned for an object whose representation has that digest. So the
 * path finds an object whose text was edited, and the digest finds an object that moved - a conjunct inserted before it, typically.
 * 
 * The representation is digested rather than written as it is because the identifier travels inside a
 * {@link org.openflexo.foundation.utils.FlexoObjectReference}, whose string form is
 * <code>&lt;resourceURI&gt;#&lt;userIdentifier&gt;_&lt;objectIdentifier&gt;</code>: an identifier holding a <code>#</code> or a
 * <code>_</code> - which B text does, <code>concept_Door : POW(SET)</code> - would be cut in the wrong place when read again. An
 * identifier that would hold one anyway is refused here rather than stored broken.
 * 
 * The containment tree is walked through the PAMELA properties of each object, so no object has to declare its children: any new kind
 * of predicate, substitution or expression is covered as soon as it is a model entity.
 * 
 * @author sylvain
 *
 */
public class BSerializationIdentifiers {

	private static final Logger logger = Logger.getLogger(BSerializationIdentifiers.class.getPackage().getName());

	/** Marks an identifier computed here, as opposed to the name of a named object */
	public static final String PATH_PREFIX = "@";

	/** Separates the containment path from the digest of the normalized representation */
	public static final char DIGEST_SEPARATOR = '~';

	/** Properties leading out of the containment tree, never followed */
	private static final List<String> EXCLUDED_PROPERTIES = Arrays.asList(InnerBComponent.COMPONENT_KEY,
			BObject.PRETTY_PRINT_DELEGATE_KEY, ResourceData.FLEXO_RESOURCE);

	private BSerializationIdentifiers() {
		// Utility class
	}

	/**
	 * Return the identifier under which supplied object, residing in supplied component, is to be serialized, or null when it cannot
	 * be computed (the component has no resource, hence no factory to introspect with, or the object is not in that component)
	 */
	public static String identifierFor(BComponent component, BObject object) {
		PamelaModelFactory factory = factoryFor(component);
		if (factory == null || object == null) {
			return null;
		}
		String path = pathTo(component, object, factory, "", newPathSet(component));
		if (path == null) {
			return null;
		}
		String digest = digestOf(object);
		String returned = PATH_PREFIX + path + (digest != null ? DIGEST_SEPARATOR + digest : "");
		if (returned.indexOf('#') >= 0 || returned.indexOf('_') >= 0) {
			// A reference holding one of those separators cannot be read again: better no identifier than a broken one
			logger.warning("Will not serialize identifier " + returned + ": a reference cannot hold '#' nor '_'");
			return null;
		}
		return returned;
	}

	/**
	 * Return the object of supplied component designated by supplied identifier, which {@link #identifierFor(BComponent, BObject)}
	 * computed, or null when neither its path nor its digest matches anything any more
	 */
	public static BObject resolve(BComponent component, String identifier) {
		PamelaModelFactory factory = factoryFor(component);
		if (factory == null || identifier == null || !identifier.startsWith(PATH_PREFIX)) {
			return null;
		}
		String path = identifier.substring(PATH_PREFIX.length());
		String digest = null;
		int separator = path.lastIndexOf(DIGEST_SEPARATOR);
		if (separator >= 0) {
			digest = path.substring(separator + 1);
			path = path.substring(0, separator);
		}
		BObject found = followPath(component, path, factory);
		if (found != null && (digest == null || digest.equals(digestOf(found)))) {
			return found;
		}
		if (digest != null) {
			// The object moved, or the path now leads to another one: look for what reads the same
			BObject byDigest = findByDigest(component, digest, factory, newPathSet(component));
			if (byDigest != null) {
				return byDigest;
			}
		}
		return found;
	}

	private static PamelaModelFactory factoryFor(BComponent component) {
		if (component == null || component.getResource() == null) {
			return null;
		}
		return component.getResource().getFactory();
	}

	/**
	 * Return a digest of the normalized B representation of supplied object, or null when it has no pretty-print delegate to produce
	 * one. The digest is the hexadecimal form of {@link String#hashCode()}, which the Java language specifies: it reads the same in
	 * another run and holds none of the characters a reference cannot carry
	 */
	private static String digestOf(BObject object) {
		if (object == null || object.getPrettyPrintDelegate() == null) {
			return null;
		}
		String representation = object.getNormalizedBRepresentation();
		if (representation == null) {
			return null;
		}
		return Integer.toHexString(representation.hashCode());
	}

	/**
	 * A set, by identity, of the objects of the path being walked. A walk never enters an object already on its path: some properties
	 * are back-pointers (a {@link BSetValue} holds its {@link BSet}) which the walk cannot tell from a child, and following them never
	 * ends (FORMOD-D-18)
	 */
	private static Set<BObject> newPathSet(BObject root) {
		Set<BObject> returned = Collections.newSetFromMap(new IdentityHashMap<>());
		returned.add(root);
		return returned;
	}

	/** Return the path leading from supplied object to supplied target, or null when the target is not under it */
	private static String pathTo(BObject object, BObject target, PamelaModelFactory factory, String pathToObject, Set<BObject> onPath) {
		String[] returned = new String[1];
		forEachChild(object, factory, (segment, child) -> {
			if (returned[0] != null || !onPath.add(child)) {
				return;
			}
			String pathToChild = pathToObject.isEmpty() ? segment : pathToObject + "/" + segment;
			if (child == target) {
				returned[0] = pathToChild;
			}
			else {
				returned[0] = pathTo(child, target, factory, pathToChild, onPath);
			}
			onPath.remove(child);
		});
		return returned[0];
	}

	private static BObject followPath(BComponent component, String path, PamelaModelFactory factory) {
		BObject current = component;
		for (String segment : path.split("/")) {
			current = childAt(current, segment, factory);
			if (current == null) {
				return null;
			}
		}
		return current;
	}

	private static BObject childAt(BObject object, String segment, PamelaModelFactory factory) {
		BObject[] returned = new BObject[1];
		forEachChild(object, factory, (childSegment, child) -> {
			if (childSegment.equals(segment)) {
				returned[0] = child;
			}
		});
		return returned[0];
	}

	private static BObject findByDigest(BObject object, String digest, PamelaModelFactory factory, Set<BObject> onPath) {
		BObject[] returned = new BObject[1];
		forEachChild(object, factory, (segment, child) -> {
			if (returned[0] != null || !onPath.add(child)) {
				return;
			}
			if (digest.equals(digestOf(child))) {
				returned[0] = child;
			}
			else {
				returned[0] = findByDigest(child, digest, factory, onPath);
			}
			onPath.remove(child);
		});
		return returned[0];
	}

	/**
	 * Return true when supplied value is part of the containment tree of its holder: a {@link BComponent} is never one, so the walk
	 * never leaves the component it started in; a back-pointer to an object of the same component is handled by the walks themselves. An object answering no component at all is taken as a child: a
	 * subtree parsed on its own carries no back-pointer, and what makes it a child is holding it, not the back-pointer
	 */
	private static boolean isChild(Object value, BObject holder) {
		if (!(value instanceof BObject) || value instanceof BComponent) {
			return false;
		}
		BComponent component = ((BObject) value).getComponent();
		return component == null || component == holder.getComponent();
	}

	/**
	 * Call supplied visitor on every {@link BObject} supplied object contains, with the path segment leading to it: the identifier of
	 * the PAMELA property holding it, and its index in that property when the property is multiple
	 */
	private static void forEachChild(BObject object, PamelaModelFactory factory, BiConsumer<String, BObject> visitor) {
		ModelEntity<?> entity = factory.getModelEntityForInstance(object);
		if (entity == null) {
			return;
		}
		Iterator<? extends ModelProperty<?>> properties;
		try {
			properties = entity.getProperties();
		} catch (ModelDefinitionException e) {
			logger.log(Level.WARNING, "Could not introspect " + object, e);
			return;
		}
		while (properties.hasNext()) {
			ModelProperty<?> property = properties.next();
			if (property.getGetterMethod() == null || EXCLUDED_PROPERTIES.contains(property.getPropertyIdentifier())) {
				continue;
			}
			Object value;
			try {
				value = property.getGetterMethod().invoke(object);
			} catch (ReflectiveOperationException | RuntimeException e) {
				// A derived property may well not be computable in this context: it holds no child then
				continue;
			}
			if (isChild(value, object)) {
				visitor.accept(property.getPropertyIdentifier(), (BObject) value);
			}
			else if (value instanceof List) {
				int index = 0;
				for (Object item : (List<?>) value) {
					if (isChild(item, object)) {
						visitor.accept(property.getPropertyIdentifier() + "[" + index + "]", (BObject) item);
					}
					index++;
				}
			}
		}
	}

}
