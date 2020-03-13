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

package org.openflexo.module.formose.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoProjectAtRunTimeTestCaseWithGUI;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstanceModelSlot;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.test.UITest;

/**
 * We test here Formose viewpoints
 * 
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestFormoseViewPoint extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	public static FlexoProject<?> project;
	private static VirtualModel formoseViewpoint;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	@Category(UITest.class)
	public void instantiateServiceManager() {

		log("testInstantiateResourceCenter()");

		instanciateTestServiceManager();

	}

	@Test
	@TestOrder(2)
	@Category(UITest.class)
	public void testSysMLKaos() {

		log("testSysMLKaos");

		VirtualModelResource sysmlKaosViewPointResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.SYSML_KAOS_VIEWPOINT_URI);
		assertNotNull(sysmlKaosViewPointResource);

		VirtualModel sysMLKaosVP;

		assertNotNull(sysMLKaosVP = sysmlKaosViewPointResource.getVirtualModel());

		assertNotNull(sysMLKaosVP);
		System.out.println(sysMLKaosVP.getFMLRepresentation());
		assertObjectIsValid(sysMLKaosVP);

		for (VirtualModel vm : sysMLKaosVP.getVirtualModels()) {
			System.out.println(vm.getFMLRepresentation());
			assertObjectIsValid(vm);
		}

	}

	@Test
	@TestOrder(3)
	@Category(UITest.class)
	public void testDomainModel() {

		log("testDomainModel");

		VirtualModelResource domainModelViewPointResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.DOMAIN_MODELLING_VIEWPOINT_URI);
		assertNotNull(domainModelViewPointResource);

		VirtualModel domainModelVP;

		assertNotNull(domainModelVP = domainModelViewPointResource.getVirtualModel());

		assertNotNull(domainModelVP);
		System.out.println(domainModelVP.getFMLRepresentation());
		assertObjectIsValid(domainModelVP);

		for (VirtualModel vm : domainModelVP.getVirtualModels()) {
			System.out.println(vm.getFMLRepresentation());
			assertObjectIsValid(vm);
		}

	}

	/**
	 * Lookup Formose ViewPoint
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(4)
	@Category(UITest.class)
	public void loadFormoseViewPoint() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("loadFormoseViewPoint()");

		formoseViewpoint = serviceManager.getVirtualModelLibrary().getVirtualModel(FMSConstants.FORMOSE_VIEWPOINT_URI);
		assertNotNull(formoseViewpoint);
		System.out.println("Found virtual model: " + formoseViewpoint);

		FMLRTVirtualModelInstanceModelSlot formoseVMIModelSlot = (FMLRTVirtualModelInstanceModelSlot) formoseViewpoint
				.getAccessibleProperty("formoseVMI");
		assertNotNull(formoseVMIModelSlot);
		System.out.println("Found " + formoseVMIModelSlot);
		// System.out.println("VM:" + formoseVMIModelSlot.getAccessedVirtualModelURI());
		// System.out.println("VM:" + formoseVMIModelSlot.getAccessedVirtualModelResource());
		// System.out.println("VM:" + formoseVMIModelSlot.getAccessedVirtualModel());

		assertObjectIsValid(formoseViewpoint);

		for (FlexoResource<?> flexoResource : formoseViewpoint.getResource().getContents()) {
			System.out.println("> " + flexoResource + " loaded=" + flexoResource.isLoaded());
		}

	}

	@Test
	@TestOrder(5)
	@Category(UITest.class)
	public void testDocumentLibrary() {

		log("testDocumentLibrary");

		VirtualModelResource dlViewPointResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.DOCUMENT_LIBRARY_VIEWPOINT_URI);
		assertNotNull(dlViewPointResource);

		VirtualModel documentLibraryVP;

		assertNotNull(documentLibraryVP = dlViewPointResource.getVirtualModel());

		assertNotNull(documentLibraryVP);
		System.out.println(documentLibraryVP.getFMLRepresentation());
		assertObjectIsValid(documentLibraryVP);

		for (VirtualModel vm : documentLibraryVP.getVirtualModels()) {
			System.out.println(vm.getFMLRepresentation());
			assertObjectIsValid(vm);
		}

		// assertEquals(4, documentLibraryVP.getVirtualModels(false).size());

		VirtualModel abstractDocumentVM, docXDocumentVM, excelDocumentVM;

		assertNotNull(abstractDocumentVM = documentLibraryVP.getVirtualModelNamed("AbstractDocument"));
		assertNotNull(docXDocumentVM = documentLibraryVP.getVirtualModelNamed("DocXDocument"));
		assertNotNull(excelDocumentVM = documentLibraryVP.getVirtualModelNamed("ExcelDocument"));

		assertSame(documentLibraryVP, abstractDocumentVM.getContainerVirtualModel());
		assertSame(documentLibraryVP, docXDocumentVM.getContainerVirtualModel());
		assertSame(documentLibraryVP, excelDocumentVM.getContainerVirtualModel());

		assertEquals(1, docXDocumentVM.getParentFlexoConcepts().size());

		/*VirtualModelImpl vm1 = (VirtualModelImpl) abstractDocumentVM;
		VirtualModelImpl vm2 = (VirtualModelImpl) docXDocumentVM.getParentFlexoConcepts().get(0);
		
		System.out.println("Resource.VM1=" + vm1.getResource());
		System.out.println("Resource.VM2=" + vm2.getResource());
		
		System.err.println("----------------> VM1:");
		vm1.getCreationException().printStackTrace();
		
		System.err.println("----------------> VM2:");
		vm2.getCreationException().printStackTrace();*/

		assertSame(abstractDocumentVM, docXDocumentVM.getParentFlexoConcepts().get(0));
	}

	@Test
	@TestOrder(6)
	@Category(UITest.class)
	public void testFormoseCore() {

		log("testFormoseCore");

		VirtualModelResource fmlResource = serviceManager.getVirtualModelLibrary().getVirtualModelResource(FMSConstants.FORMOSE_CORE_URI);
		assertNotNull(fmlResource);

		VirtualModel virtualModel;

		assertNotNull(virtualModel = fmlResource.getVirtualModel());

		assertNotNull(virtualModel);
		assertVirtualModelIsValid(virtualModel);

	}

	@Test
	@TestOrder(7)
	@Category(UITest.class)
	public void testMethology() {

		log("testMethology");

		VirtualModelResource fmlResource = serviceManager.getVirtualModelLibrary().getVirtualModelResource(FMSConstants.METHOLOGY_URI);
		assertNotNull(fmlResource);

		VirtualModel virtualModel;

		assertNotNull(virtualModel = fmlResource.getVirtualModel());

		assertNotNull(virtualModel);
		assertVirtualModelIsValid(virtualModel);

	}

	@Test
	@TestOrder(8)
	@Category(UITest.class)
	public void testDocumentAnnotationMethology() {

		log("testDocumentAnnotationMethology");

		VirtualModelResource fmlResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.DOCUMENT_ANNOTATION_METHOLOGY_URI);
		assertNotNull(fmlResource);

		VirtualModel virtualModel;

		assertNotNull(virtualModel = fmlResource.getVirtualModel());

		assertNotNull(virtualModel);
		assertVirtualModelIsValid(virtualModel);

	}

	@Test
	@TestOrder(9)
	@Category(UITest.class)
	public void testSysMLKaosMethodology() {

		log("testSysMLKaosMethodology");

		VirtualModelResource fmlResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.SYSML_KAOS_METHOLOGY_URI);
		assertNotNull(fmlResource);

		VirtualModel virtualModel;

		assertNotNull(virtualModel = fmlResource.getVirtualModel());

		assertNotNull(virtualModel);
		assertVirtualModelIsValid(virtualModel);

	}

	@Test
	@TestOrder(10)
	@Category(UITest.class)
	public void testDomainModelMethology() {

		log("testDomainModelMethology");

		VirtualModelResource fmlResource = serviceManager.getVirtualModelLibrary()
				.getVirtualModelResource(FMSConstants.DOMAIN_MODEL_METHOLOGY_URI);
		assertNotNull(fmlResource);

		VirtualModel virtualModel;

		assertNotNull(virtualModel = fmlResource.getVirtualModel());

		assertNotNull(virtualModel);
		assertVirtualModelIsValid(virtualModel);

	}

}
