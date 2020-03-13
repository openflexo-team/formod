/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * 
 * This file is part of Integration-tests, a component of the software infrastructure 
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

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.AtelierBProjectResourceFactory;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * We test creation of a AtelierBProject
 * 
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestCreateBlankAtelierBProject extends AbstractBTest {

	private static File generatedAtelierBProject;
	private static AtelierBProjectResource generatedAtelierBProjectResource;

	@BeforeClass
	public static void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
	}

	@Test
	@TestOrder(1)
	public void initializeBlankAtelierBProject() throws IOException, InterruptedException {

		DirectoryResourceCenter atelierBRC = makeNewDirectoryResourceCenter();
		File targetAtelierBProject = new File(atelierBRC.getRootDirectory(), "TestAtelierBProject");
		generatedAtelierBProject = AtelierBProjectResourceFactory.generateBlankAtelierBProject(targetAtelierBProject);
		// Waiting for new resource to be detected
		atelierBRC.getDirectoryWatcher().waitNextWatching();
		File dbDir = new File(targetAtelierBProject, AtelierBProjectResourceFactory.BDP_DIR);
		File dbFile = new File(dbDir, "TestAtelierBProject.db");
		// Testing that the resource has been detected as a AtelierBProjectResource
		generatedAtelierBProjectResource = atelierBRC.getResource(dbFile, AtelierBProjectResource.class);
		assertNotNull(generatedAtelierBProjectResource);
	}

	@Test
	@TestOrder(2)
	public void createSystem() throws ModelDefinitionException, IOException, ResourceLoadingCancelledException, FlexoException {

		AtelierBComponent newSystemComponent = generatedAtelierBProjectResource.getFactory().makeNewSystem("TestSystem");
		assertNotNull(newSystemComponent.getComponentResource());

		BSystem newSystem = (BSystem) newSystemComponent.getComponentResource().getResourceData();

		System.out.println(newSystemComponent.getComponentResource().getRawSource());
	}

}
