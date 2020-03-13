/**
 * 
 * Copyright (c) 2016-, Openflexo
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

package org.openflexo.module.formose.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoProjectAtRunTimeTestCaseWithGUI;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.test.UITest;

/**
 * We test here Formose RC packaging
 * 
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestFormoseJarRC extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	public static FlexoProject<?> project;
	private static VirtualModel formoseVP;
	private static FlexoResource<VirtualModel> vpRes;

	private static FMLTechnologyAdapter fmlAdapter;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	@Category(UITest.class)
	public void instantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");

		instanciateTestServiceManager(FMLTechnologyAdapter.class, FMLRTTechnologyAdapter.class);

	}

	@Test
	@TestOrder(3)
	@Category(UITest.class)
	public void loadViewPoint() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		fmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FMLTechnologyAdapter.class);

		log("loadViewPoint");

		String viewPointURI = "http://formose.lacl.fr/Formose.viewpoint";
		log("Testing ViewPoint loading: " + viewPointURI);

		vpRes = serviceManager.getResourceManager().getResource(viewPointURI, VirtualModel.class);
		assertNotNull(vpRes);
		assertFalse(vpRes.isLoaded());

		formoseVP = fmlAdapter.getVirtualModelLibrary().getVirtualModel(viewPointURI);

		assertNotNull(formoseVP);

	}

}
