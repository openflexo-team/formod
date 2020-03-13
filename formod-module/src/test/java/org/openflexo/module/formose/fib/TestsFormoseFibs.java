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

package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openflexo.ApplicationContext;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.FIBLibrary;
import org.openflexo.gina.FIBLibrary.FIBLibraryImpl;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.test.GenericFIBTestCase;
import org.openflexo.gina.test.TestApplicationContext;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.rm.Resources;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.test.UITest;

/**
 * A parameterized suite of unit tests iterating on FIB component, and testing it
 * 
 * @author sylvain
 *
 */
@RunWith(Parameterized.class)
public class TestsFormoseFibs extends GenericFIBTestCase {

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> generateData() {
		return Resources.getMatchingResource(ResourceLocator.locateResource("Fib"), ".fib");
	}

	private final Resource fibResource;

	public TestsFormoseFibs(Resource fibResource, String name) {
		this.fibResource = fibResource;
	}

	@Test
	@Category(UITest.class)
	public void validateFib() {
		validateFIB(fibResource);
	}

	private static ApplicationContext serviceManager;
	private static FIBLibrary fibLibrary;

	@BeforeClass
	public static void instanciateTestServiceManager(/*Class<? extends TechnologyAdapter>... taClasses*/)
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		System.out.println("Init ServiceManager");
		serviceManager = new TestApplicationContext();
		/*for (Class<? extends TechnologyAdapter> technologyAdapterClass : taClasses) {
			serviceManager.activateTechnologyAdapter(
					serviceManager.getTechnologyAdapterService().getTechnologyAdapter(technologyAdapterClass));
		}*/
		// return serviceManager;
		assertNotNull(getFormoseViewPoint());

		fibLibrary = FIBLibraryImpl.createInstance(serviceManager.getTechnologyAdapterService());
	}

	@Override
	public FIBLibrary getFIBLibrary() {
		return fibLibrary;
	}

	public static VirtualModel getFormoseViewPoint() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		return serviceManager.getVirtualModelLibrary().getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
	}

	@Override
	protected void initFIBComponent(FIBComponent component) {
		super.initFIBComponent(component);
		try {
			component.setBindingFactory(new FMLFIBBindingFactory(getFormoseViewPoint()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
