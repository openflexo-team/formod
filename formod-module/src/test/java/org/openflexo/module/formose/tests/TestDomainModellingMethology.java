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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoProjectAtRunTimeTestCaseWithGUI;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.model.action.GivesFormoseNature;
import org.openflexo.module.formose.model.action.InstantiateDomainModelMethodology;
import org.openflexo.module.formose.model.action.InstantiateSysMLKaosMethodology;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.test.UITest;

/**
 * We test here CartoEditor viewpoint
 * 
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestDomainModellingMethology extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	public static FlexoProject<?> project;
	private static FlexoEditor editor;
	private static VirtualModel formoseVP;

	private static FMLRTVirtualModelInstance view;

	private static FMLRTVirtualModelInstance formoseVMI;
	private static FMLRTVirtualModelInstance documentLibrary;

	private static FlexoConceptInstance projectElement;

	private static FMLRTVirtualModelInstance sysMLKaosMethodology;
	private static FlexoConceptInstance projectMapping; // SysMLKaosElementMapping
	private static FlexoConceptInstance diagramMapping; // DiagramMapping
	private static FMLRTVirtualModelInstance goalModelingDiagram; // GoalModelingDiagram

	private static FMLRTVirtualModelInstance domainModellingMethodology; // DomainModel-Methodology
	private static FlexoConceptInstance domainModellingProjectMapping; // DomainModellingElementMapping

	/**
	 * Instantiate test resource center
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(1)
	@Category(UITest.class)
	public void instantiateServiceManager() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testInstantiateResourceCenter()");

		instanciateTestServiceManager();

	}

	@Test
	@TestOrder(3)
	@Category(UITest.class)
	public void loadViewPoint() {

		log("loadViewPoint");

		String viewPointURI = FMSConstants.FORMOSE_VIEWPOINT_URI;
		log("Testing ViewPoint loading: " + viewPointURI);

		FlexoResource<FMLCompilationUnit> vpRes = serviceManager.getResourceManager().getResource(viewPointURI, FMLCompilationUnit.class);

		System.out.println("ViewPoint found in : " + vpRes.getIODelegate().getSerializationArtefact());

		assertNotNull(vpRes);
		assertFalse(vpRes.isLoaded());

		VirtualModel vp = ((CompilationUnitResource) vpRes).getCompilationUnit().getVirtualModel();
		assertTrue(vpRes.isLoaded());
		formoseVP = vp;

		assertNotNull(formoseVP);

		VirtualModel formoseVM = formoseVP.getVirtualModelNamed(FMSConstants.FORMOSE_VM_NAME);
		assertNotNull(formoseVM);

	}

	@Test
	@TestOrder(14)
	@Category(UITest.class)
	public void createProject() {

		log("createProject");

		editor = createStandaloneProject("TestCreateView");
		project = editor.getProject();

		assertNotNull(project.getVirtualModelInstanceRepository());

		assertFalse(project.hasNature(FormoseProjectNature.class));

	}

	@Test
	@TestOrder(15)
	@Category(UITest.class)
	public void givesProjectFormoseNature() throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		log("givesProjectFormoseNature");

		GivesFormoseNature givesNatureAction = GivesFormoseNature.actionType.makeNewAction(project, null, editor);
		givesNatureAction.doAction();
		assertTrue(givesNatureAction.hasActionExecutionSucceeded());

		assertNotNull(project.getVirtualModelInstanceRepository());

		assertTrue(project.hasNature(FormoseProjectNature.class));

		FormoseProjectNature formoseProjectNature = project.getNature(FormoseProjectNature.class);
		assertNotNull(formoseProjectNature);

		assertNotNull(view = formoseProjectNature.getFormoseInstance().getAccessedVirtualModelInstance());
		assertNotNull(documentLibrary = formoseProjectNature.getDocumentLibrary());

		assertNotNull(formoseVMI = formoseProjectNature.getFormoseVirtualModelInstance());

		assertFalse(view.isModified());
		// assertTrue(documentLibrary.isModified());
		assertTrue(formoseVMI.isModified());

		project.save();
		documentLibrary.getResource().save();
		formoseVMI.getResource().save();

		assertFalse(view.isModified());
		assertFalse(documentLibrary.isModified());
		assertFalse(formoseVMI.isModified());

		assertEquals(1, formoseVMI.getFlexoConceptInstances().size());

		projectElement = formoseVMI.getFlexoPropertyValue("projectElement");
		assertNotNull(projectElement);

		assertSame(projectElement, formoseVMI.getFlexoConceptInstances().get(0));

		assertSame(projectElement, formoseVMI.execute("projectElement"));

	}

	@Test
	@TestOrder(16)
	@Category(UITest.class)
	public void instantiateSysMLKaosMethodology() throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		log("instantiateSysMLKaosMethodology");

		InstantiateSysMLKaosMethodology instantiateSysMLKaos = InstantiateSysMLKaosMethodology.ACTION_TYPE.makeNewAction(projectElement,
				null, editor);
		instantiateSysMLKaos.doAction();
		assertTrue(instantiateSysMLKaos.hasActionExecutionSucceeded());

		sysMLKaosMethodology = instantiateSysMLKaos.getNewMethodology();
		assertNotNull(sysMLKaosMethodology);

		projectMapping = sysMLKaosMethodology.execute("getElementMapping({$element})", projectElement);
		assertNotNull(projectMapping);

		diagramMapping = projectMapping.execute("mainFunctionalGoalDiagram");
		assertNotNull(diagramMapping);

		goalModelingDiagram = diagramMapping.execute("goalModelingDiagram");
		assertNotNull(goalModelingDiagram);

		assertSame(diagramMapping.execute("goalModel"), goalModelingDiagram.execute("model"));

	}

	@Test
	@TestOrder(17)
	@Category(UITest.class)
	public void instantiateDomainModelMethodology() throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		log("instantiateDomainModelMethodology");

		InstantiateDomainModelMethodology instantiateDomainModelling = InstantiateDomainModelMethodology.ACTION_TYPE
				.makeNewAction(projectElement, null, editor);
		instantiateDomainModelling.doAction();
		assertTrue(instantiateDomainModelling.hasActionExecutionSucceeded());

		domainModellingMethodology = instantiateDomainModelling.getNewMethodology();
		assertNotNull(domainModellingMethodology);

		domainModellingProjectMapping = domainModellingMethodology.execute("getDomainModelElementMapping({$element})", projectElement);
		assertNotNull(domainModellingProjectMapping);

		assertSame(projectMapping, domainModellingProjectMapping.execute("sysMLKaosElementMapping"));

	}

}
