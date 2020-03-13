/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Cartoeditor, a component of the software infrastructure 
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.ta.b.model.parser.nodes.BComponentNode;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.ta.b.rm.BResourceRepository;
import org.openflexo.toolbox.StringUtils;

@RunWith(Parameterized.class)
public class TestPrettyPrintAllBComponents extends AbstractBTest {

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> generateData() {

		final ArrayList<Object[]> list = new ArrayList<Object[]>();

		instanciateTestServiceManager(BTechnologyAdapter.class);

		BTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(BTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			BResourceRepository<?> bRepository = technologicalAdapter.getBResourceRepository(resourceCenter);
			assertNotNull(bRepository);
			Collection<BResource> componentResources = bRepository.getAllResources();
			for (BResource componentResource : componentResources) {
				final Object[] construcArgs = { componentResource, componentResource.getName() };
				list.add(construcArgs);
			}
		}

		return list;
	}

	private BResource resource;

	public TestPrettyPrintAllBComponents(BResource resource, String name) {
		this.resource = resource;
	}

	@Test
	public void testBComponent() {
		System.out.println("Loading " + resource.getIODelegate().getSerializationArtefact());
		try {
			resource.loadResourceData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
			fail();
		}
		assertNotNull(resource.getLoadedResourceData());
		System.out.println("URI of document: " + resource.getURI());
		System.out.println("ResourceData: " + resource.getLoadedResourceData());
		System.out.println(resource.getFactory().stringRepresentation(resource.getLoadedResourceData()));

		testPrettyPrint(resource.getLoadedResourceData());
	}

	private void testPrettyPrint(BComponent component) {
		System.out.println("testPrettyPrint with " + component);

		BComponentNode<?, ?> rootNode = (BComponentNode<?, ?>) component.getPrettyPrintDelegate();
		RawSource rawSource = rootNode.getRawSource();
		System.out.println(rawSource.debug());
		debug(rootNode, 0);

		// Test syntax-preserving pretty-print
		try {
			String prettyPrint = component.getBPrettyPrint();
			System.out.println("prettyPrint=\n" + prettyPrint);
			BComponent reparsedComponent = BParser.parse(prettyPrint, component.getResource().getFactory());
			reparsedComponent.setResource(component.getResource());
			// System.out.println("component=" + component);
			System.out.println("reparsedComponent=" + reparsedComponent);
			assertTrue("Objects are not equals after pretty-print", component.equalsObject(reparsedComponent));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		// Test normalized pretty-print
		try {
			String normalizedB = component.getNormalizedBRepresentation();
			System.out.println("normalizedB=\n" + normalizedB);
			BComponent reparsedComponent = BParser.parse(normalizedB, component.getResource().getFactory());
			reparsedComponent.setResource(component.getResource());
			// System.out.println("component=" + component);
			System.out.println("reparsedComponent=" + reparsedComponent);
			assertTrue("Objects are not equals after normalized pretty-print", component.equalsObject(reparsedComponent));
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void debug(P2PPNode<?, ?> node, int indentLevel) {
		System.out.println(StringUtils.buildWhiteSpaceIndentation(indentLevel * 2) + "> " + node.getClass().getSimpleName() + " "
				+ node.getLastParsedFragment() + " prelude=" + node.getPrelude() + " postlude=" + node.getPostlude() + " contents="
				+ node.getRegisteredForContents());
		for (P2PPNode<?, ?> child : node.getChildren()) {
			debug(child, indentLevel + 1);
		}
	}

}
