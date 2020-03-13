package org.openflexo.ta.b.model;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;
import org.openflexo.ta.b.model.atelierb.AtelierBProject;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.BResource;

public class TestAtelierBProject extends AbstractBTest {
	protected static final Logger logger = Logger.getLogger(TestAtelierBProject.class.getPackage().getName());

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	@Test
	public void test1() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://www.openflexo.org/test/b");

		AtelierBProjectResource projectResource = (AtelierBProjectResource) serviceManager.getResourceManager()
				.getResource("http://www.openflexo.org/test/b/AtelierBProjects/Test1", null, AtelierBProject.class);
		assertNotNull(projectResource);

		// for (FlexoResource<?> resource : projectResource.getContents()) {
		// System.out.println("> resource " + resource + " " + resource.getURI() + " container=" + resource.getContainer());
		// }

		BResource system1Resource = (BResource) serviceManager.getResourceManager()
				.getResource("http://www.openflexo.org/test/b/AtelierBProjects/Test1/System1.sys", null, BComponent.class);
		assertNotNull(system1Resource);

		assertEquals(2, projectResource.getContents().size());
		assertTrue(projectResource.getContents().contains(system1Resource));

		// for (FlexoResource<?> resource : resourceCenter.getAllResources()) {
		// System.out.println("> resource " + resource + " " + resource.getURI() + " container=" + resource.getContainer());
		// }

		AtelierBProject project = projectResource.getAtelierBProject();
		assertNotNull(project);

		System.out.println(project.getFactory().stringRepresentation(project));

		AtelierBComponent componentDefinition = projectResource.getAtelierBProject().getAtelierBProjectDefinition()
				.getComponentNamed("System1");
		assertNotNull(componentDefinition);

		BResource componentResource = componentDefinition.getComponentResource();
		assertNotNull(componentResource);

		assertFalse(componentResource.isLoaded());

		assertNotNull(componentResource.loadResourceData());

	}

	@Test
	public void test2() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://www.openflexo.org/test/b");

		AtelierBProjectResource projectResource = (AtelierBProjectResource) serviceManager.getResourceManager()
				.getResource("http://www.openflexo.org/test/b/AtelierBProjects/TestAvecFabien", null, AtelierBProject.class);
		assertNotNull(projectResource);

		BResource systemResource = (BResource) serviceManager.getResourceManager()
				.getResource("http://www.openflexo.org/test/b/AtelierBProjects/TestAvecFabien/UnSysteme.sys", null, BComponent.class);
		assertNotNull(systemResource);
		BResource refinmentResource = (BResource) serviceManager.getResourceManager()
				.getResource("http://www.openflexo.org/test/b/AtelierBProjects/TestAvecFabien/QuOnRaffine.ref", null, BComponent.class);
		assertNotNull(refinmentResource);

		assertEquals(2, projectResource.getContents().size());
		assertTrue(projectResource.getContents().contains(systemResource));
		assertTrue(projectResource.getContents().contains(refinmentResource));

		AtelierBProject project = projectResource.getAtelierBProject();
		assertNotNull(project);

		System.out.println(project.getFactory().stringRepresentation(project));

		AtelierBComponent systemDefinition = projectResource.getAtelierBProject().getAtelierBProjectDefinition()
				.getComponentNamed("UnSysteme");
		assertNotNull(systemDefinition);
		assertSame(systemResource, systemDefinition.getComponentResource());
		assertFalse(systemResource.isLoaded());
		assertNotNull(systemResource.loadResourceData());

		AtelierBComponent refinmentDefinition = projectResource.getAtelierBProject().getAtelierBProjectDefinition()
				.getComponentNamed("QuOnRaffine");
		assertNotNull(refinmentDefinition);
		assertSame(refinmentResource, refinmentDefinition.getComponentResource());
		assertFalse(refinmentResource.isLoaded());
		assertNotNull(refinmentResource.loadResourceData());

	}

}
