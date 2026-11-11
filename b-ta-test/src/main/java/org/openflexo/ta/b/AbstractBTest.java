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

package org.openflexo.ta.b;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.rm.BResource;

public abstract class AbstractBTest extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(AbstractBTest.class.getPackage().getName());

	protected BResource getBResource(String documentName, FlexoResourceCenter<?> resourceCenter) {

		String documentURI = resourceCenter.getDefaultBaseURI() + "/" + documentName;
		// System.out.println("Searching " + documentURI);

		BResource documentResource = (BResource) serviceManager.getResourceManager().getResource(documentURI, null, BComponent.class);

		if (documentResource == null) {
			logger.warning("Cannot find document resource " + documentURI);
			for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
				System.out.println("> " + r.getURI());
			}
		}

		assertNotNull(documentResource);

		return documentResource;
	}

	protected BResource getBResource(String documentName) {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://www.openflexo.org/test/b");

		return getBResource(documentName, resourceCenter);
	}

	protected BComponent getBComponent(String documentName) {

		BResource documentResource = getBResource(documentName);
		assertNotNull(documentResource);

		BComponent component = null;
		try {
			component = documentResource.getResourceData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		assertNotNull(component);

		return component;
	}

	/**
	 * Semantic equality of two B objects, typically a component and its re-parsed pretty-print.
	 * 
	 * Excluded: the pretty-print delegate (the P2PP node), since PAMELA compares every property that is neither derived nor transient
	 * and each parse builds its own node (it stays a regular property of {@link BObject}, as for FML, so that updateWith() still
	 * transfers it); and the identifiers of {@link FlexoObject}, which each parse assigns anew (PAMELA-F-4).
	 */
	protected static boolean equalsIgnoringPrettyPrintDelegate(BObject object, BObject other) {
		return object.equalsObject(other, property -> !STRUCTURE_IGNORED_PROPERTIES.contains(property.getPropertyIdentifier()));
	}

	private static final Set<String> STRUCTURE_IGNORED_PROPERTIES = Set.of(BObject.PRETTY_PRINT_DELEGATE_KEY, FlexoObject.FLEXO_ID_KEY,
			FlexoObject.USER_IDENTIFIER_KEY);

}
