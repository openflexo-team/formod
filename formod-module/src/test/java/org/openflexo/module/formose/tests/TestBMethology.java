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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoProjectAtRunTimeTestCaseWithGUI;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.model.action.GivesFormoseNature;
import org.openflexo.module.formose.model.action.InstantiateBMethodology;
import org.openflexo.module.formose.model.action.InstantiateDomainModelMethodology;
import org.openflexo.module.formose.model.action.InstantiateSysMLKaosMethodology;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.AtelierBProjectResourceFactory;
import org.openflexo.technologyadapter.diagram.fml.DrawRectangleScheme;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.action.DrawRectangleSchemeAction;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.technologyadapter.diagram.model.action.LinkSchemeAction;
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
public class TestBMethology extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	public static FlexoProject<File> project;
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

	private static FMLRTVirtualModelInstance bMethodology; // B-Methodology
	private static FlexoConceptInstance bProjectMapping; // BElementMapping

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

		FlexoResource<VirtualModel> vpRes = serviceManager.getResourceManager().getResource(viewPointURI, VirtualModel.class);

		System.out.println("ViewPoint found in : " + vpRes.getIODelegate().getSerializationArtefact());
		// System.exit(-1);

		assertNotNull(vpRes);
		assertFalse(vpRes.isLoaded());

		VirtualModel vp = ((VirtualModelResource) vpRes).getVirtualModel();
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
		project = (FlexoProject<File>) editor.getProject();

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

		FlexoConceptInstance fg1gr = dropFunctionalGoal("FG1", 100, 100);
		FlexoConceptInstance fg2gr = dropFunctionalGoal("FG2", 50, 300);
		FlexoConceptInstance fg3gr = dropFunctionalGoal("FG3", 150, 300);

		FlexoConceptInstance refGR = refinesGoal(fg2gr, fg1gr);

		FlexoConceptInstance linkGR = refines(fg3gr, refGR);

		FlexoConceptInstance fg1 = fg1gr.execute("goal");
		FlexoConceptInstance fg2 = fg2gr.execute("goal");
		FlexoConceptInstance fg3 = fg3gr.execute("goal");
		FlexoConceptInstance ref = refGR.execute("refinement");

		System.out.println("fg1=" + fg1.debug());
		System.out.println("fg2=" + fg2.debug());
		System.out.println("fg3=" + fg3.debug());
		System.out.println("ref=" + ref.debug());

		System.out.println("fg2=" + fg2);
		System.out.println("refines=" + fg2.execute("refines"));
		System.out.println("parentGoal=" + fg2.execute("parentGoal"));

		assertSame(fg1, fg2.execute("parentGoal"));
		assertSame(fg1, fg3.execute("parentGoal"));

		FlexoConceptInstance gg1gr = drawGoalGroup("Level1", 10, 10, 500, 150, fg1gr);
		FlexoConceptInstance gg2gr = drawGoalGroup("Level2", 10, 150, 500, 500, fg2gr, fg3gr);

		FlexoConceptInstance gg1 = gg1gr.execute("goalGroup");
		FlexoConceptInstance gg2 = gg2gr.execute("goalGroup");

		System.out.println("goals for group1 =" + gg1.execute("goals"));
		System.out.println("goals for group2 =" + gg2.execute("goals"));

		assertTrue(((List) gg1.execute("goals")).contains(fg1));
		assertTrue(((List) gg2.execute("goals")).contains(fg2));
		assertTrue(((List) gg2.execute("goals")).contains(fg3));

	}

	private FlexoConceptInstance dropFunctionalGoal(String name, double x, double y) throws SaveResourceException, TypeMismatchException,
			NullReferenceException, InvocationTargetException, InvalidBindingException {

		FlexoConcept functionalGoalGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("FunctionalGoalGR");
		assertNotNull(functionalGoalGR);
		DropScheme dropScheme = (DropScheme) functionalGoalGR.getFlexoBehaviour("createFunctionalGoal", String.class, String.class,
				String.class);
		DropSchemeAction dropSchemeAction = new DropSchemeAction(dropScheme, goalModelingDiagram, null, editor);
		dropSchemeAction.setDropLocation(new DianaPoint(x, y));
		dropSchemeAction.setParameterValue(dropScheme.getParameters().get(0), name);

		// Perform the creation, then retrieve the new functional goal instance
		dropSchemeAction.doAction();
		assertTrue(dropSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance fgGR = dropSchemeAction.getFlexoConceptInstance();
		assertNotNull(fgGR);

		FlexoConceptInstance fg = fgGR.execute("goal");
		assertNotNull(fg);
		assertEquals("FunctionalGoal", fg.getFlexoConcept().getName());
		assertEquals(name, fg.execute("name"));

		return fgGR;
	}

	private FlexoConceptInstance refinesGoal(FlexoConceptInstance childConcept, FlexoConceptInstance parentConcept)
			throws SaveResourceException, TypeMismatchException, NullReferenceException, InvocationTargetException,
			InvalidBindingException {

		FlexoConcept refinementGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("RefinementGR");
		assertNotNull(refinementGR);
		LinkScheme linkScheme = (LinkScheme) refinementGR.getFlexoBehaviour("createRefinement");
		LinkSchemeAction linkSchemeAction = new LinkSchemeAction(linkScheme, goalModelingDiagram, null, editor);
		linkSchemeAction.setFromShape(childConcept.execute("shape"));
		linkSchemeAction.setToShape(parentConcept.execute("shape"));

		// Perform the creation, then retrieve the new functional goal instance
		linkSchemeAction.doAction();
		assertTrue(linkSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance refinementGRInstance = linkSchemeAction.getFlexoConceptInstance();
		assertNotNull(refinementGRInstance);
		return refinementGRInstance;
	}

	private FlexoConceptInstance refines(FlexoConceptInstance childConcept, FlexoConceptInstance refinement) throws SaveResourceException,
			TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {

		FlexoConcept refinementGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("ChildRefinmentLinkGR");
		assertNotNull(refinementGR);
		LinkScheme linkScheme = (LinkScheme) refinementGR.getFlexoBehaviour("linkChildRefinement");
		LinkSchemeAction linkSchemeAction = new LinkSchemeAction(linkScheme, goalModelingDiagram, null, editor);
		linkSchemeAction.setFromShape(childConcept.execute("shape"));
		linkSchemeAction.setToShape(refinement.execute("shape"));

		// Perform the creation, then retrieve the new functional goal instance
		linkSchemeAction.doAction();
		assertTrue(linkSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance linkGRInstance = linkSchemeAction.getFlexoConceptInstance();
		assertNotNull(linkGRInstance);
		return linkGRInstance;
	}

	private FlexoConceptInstance drawGoalGroup(String title, double x1, double y1, double x2, double y2, FlexoConceptInstance... instances)
			throws SaveResourceException, TypeMismatchException, NullReferenceException, InvocationTargetException,
			InvalidBindingException {

		FlexoConcept goalGroupGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("GoalGroupGR");
		assertNotNull(goalGroupGR);
		DrawRectangleScheme drScheme = (DrawRectangleScheme) goalGroupGR.getFlexoBehaviour("create", String.class);
		DrawRectangleSchemeAction dropSchemeAction = new DrawRectangleSchemeAction(drScheme, goalModelingDiagram, null, editor);
		dropSchemeAction.setFromLocation(new DianaPoint(x1, y1));
		dropSchemeAction.setToLocation(new DianaPoint(x2, y2));
		dropSchemeAction.setSelection(Arrays.asList(instances));
		dropSchemeAction.setParameterValue(drScheme.getParameters().get(0), title);

		dropSchemeAction.doAction();
		assertTrue(dropSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance ggGR = dropSchemeAction.getFlexoConceptInstance();
		assertNotNull(ggGR);

		FlexoConceptInstance gg = ggGR.execute("goalGroup");
		assertNotNull(gg);
		assertEquals("GoalGroup", gg.getFlexoConcept().getName());
		assertEquals(title, gg.execute("title"));

		return ggGR;
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

		FlexoConceptInstance domainModelMapping = domainModellingProjectMapping.execute("topLevelDomainModelMappings.get(0)");
		assertNotNull(domainModelMapping);

		FMLRTVirtualModelInstance diagram = domainModelMapping.execute("defaultDiagram");
		assertNotNull(diagram);

		FlexoConceptInstance conceptAGR = createDomainModelConcept("ConceptA", diagram);
		FlexoConceptInstance conceptBGR = createDomainModelConcept("ConceptB", diagram);
		FlexoConceptInstance conceptCGR = createDomainModelConcept("ConceptC", diagram);

		FlexoConceptInstance conceptDGR = createDomainModelConcept("ConceptD", diagram);
		declareParentConcept(conceptDGR, conceptAGR, diagram);
	}

	private FlexoConceptInstance createDomainModelConcept(String conceptName, FMLRTVirtualModelInstance diagram)
			throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {
		FlexoConcept conceptGR = diagram.getVirtualModel().getFlexoConcept("ConceptGR");
		assertNotNull(conceptGR);
		DropScheme dropScheme = (DropScheme) conceptGR.getFlexoBehaviour("dropConceptGRAtTopLevel");
		DropSchemeAction dropSchemeAction = new DropSchemeAction(dropScheme, diagram, null, editor);
		dropSchemeAction.setDropLocation(new DianaPoint(100, 100));
		// dropSchemeAction.setParameterValue(dropScheme.getParameters().get(0), "MyConcept");

		// Perform the creation, then retrieve the new functional goal instance
		dropSchemeAction.doAction();
		assertTrue(dropSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance myConceptGR = dropSchemeAction.getFlexoConceptInstance();
		assertNotNull(myConceptGR);
		FlexoConceptInstance conceptInstanceA = myConceptGR.execute("modelConcept");
		assertEquals("Concept", conceptInstanceA.getFlexoConcept().getName());
		conceptInstanceA.setFlexoPropertyValue("name", conceptName);
		assertNotNull(conceptInstanceA);
		assertEquals(conceptName, conceptInstanceA.execute("name"));

		return myConceptGR;
	}

	private void declareParentConcept(FlexoConceptInstance childConcept, FlexoConceptInstance parentConcept,
			FMLRTVirtualModelInstance diagram) throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		System.out.println("childConcept=" + childConcept);
		System.out.println("parentConcept=" + parentConcept);

		FlexoConcept parentConceptGR = diagram.getVirtualModel().getFlexoConcept("ParentConceptGR");
		assertNotNull(parentConceptGR);
		LinkScheme linkScheme = (LinkScheme) parentConceptGR.getFlexoBehaviour("linkConceptGRToConceptGR");
		LinkSchemeAction linkSchemeAction = new LinkSchemeAction(linkScheme, diagram, null, editor);
		linkSchemeAction.setFromShape(childConcept.execute("containerShape"));
		linkSchemeAction.setToShape(parentConcept.execute("containerShape"));

		// Perform the creation, then retrieve the new functional goal instance
		linkSchemeAction.doAction();
		assertTrue(linkSchemeAction.hasActionExecutionSucceeded());
		FlexoConceptInstance parentLinkGR = linkSchemeAction.getFlexoConceptInstance();
		assertNotNull(parentLinkGR);
	}

	private static File sourceAtelierBProject;
	private static AtelierBProjectResource sourceAtelierBProjectResource;

	@Test
	@TestOrder(18)
	@Category(UITest.class)
	public void initializeBlankAtelierBProject() throws IOException, InterruptedException {

		DirectoryResourceCenter atelierBRC = makeNewDirectoryResourceCenter();
		File targetAtelierBProject = new File(atelierBRC.getRootDirectory(), "TestAtelierBProject");
		sourceAtelierBProject = AtelierBProjectResourceFactory.generateBlankAtelierBProject(targetAtelierBProject);
		// Waiting for new resource to be detected
		atelierBRC.getDirectoryWatcher().waitNextWatching();
		File dbDir = new File(targetAtelierBProject, AtelierBProjectResourceFactory.BDP_DIR);
		File dbFile = new File(dbDir, "TestAtelierBProject.db");
		// Testing that the resource has been detected as a AtelierBProjectResource
		sourceAtelierBProjectResource = atelierBRC.getResource(dbFile, AtelierBProjectResource.class);
		assertNotNull(sourceAtelierBProjectResource);
	}

	@Test
	@TestOrder(19)
	@Category(UITest.class)
	public void instantiateBMethodology() throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		log("instantiateBMethodology");

		InstantiateBMethodology instantiateBMethodology = InstantiateBMethodology.ACTION_TYPE.makeNewAction(projectElement, null, editor);
		instantiateBMethodology.setSourceProjectFolder(sourceAtelierBProject);
		System.out.println("generatedAtelierBProject=" + sourceAtelierBProject);
		instantiateBMethodology.doAction();
		assertTrue(instantiateBMethodology.hasActionExecutionSucceeded());

		bMethodology = instantiateBMethodology.getNewMethodology();
		assertNotNull(bMethodology);

		bProjectMapping = bMethodology.execute("getBElementMapping({$element})", projectElement);
		assertNotNull(bProjectMapping);

		assertSame(domainModellingProjectMapping, bProjectMapping.execute("domainModellingElementMapping"));

		sourceAtelierBProjectResource.save();

		log("Level1");

		FlexoConceptInstance level1Mapping = bProjectMapping.execute("topLevelBMappings.get(0)");
		assertNotNull(level1Mapping);

		AtelierBComponent context1 = level1Mapping.execute("context");
		AtelierBComponent machine1 = level1Mapping.execute("machine");

		System.out.println(context1.getComponentResource().getName() + "\n");
		System.out.println(context1.getBPrettyPrint());

		System.out.println(machine1.getComponentResource().getName() + "\n");
		System.out.println(machine1.getBPrettyPrint());

		log("Level2");

		FlexoConceptInstance level2Mapping = level1Mapping.execute("children.get(0)");
		assertNotNull(level2Mapping);

		AtelierBComponent context2 = level2Mapping.execute("context");
		AtelierBComponent machine2 = level2Mapping.execute("machine");

		System.out.println(context2.getComponentResource().getName() + "\n");
		System.out.println(context2.getBPrettyPrint());

		System.out.println(machine2.getComponentResource().getName() + "\n");
		System.out.println(machine2.getBPrettyPrint());

		bMethodology.getResource().save();
		System.out.println("BMethodology: " + bMethodology.getResource().getIODelegate().getSerializationArtefact());

		for (FlexoResource<?> flexoResource : serviceManager.getResourceManager().getUnsavedResources()) {
			System.out.println("Saving: " + flexoResource);
			flexoResource.save();
		}

	}

	/**
	 * Reload the project, tests that uri, name and description are persistent
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 * @throws InvalidBindingException
	 * @throws InvocationTargetException
	 * @throws NullReferenceException
	 * @throws TypeMismatchException
	 */
	@Test
	@TestOrder(100)
	@Category(UITest.class)
	public void testReloadProject() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException, TypeMismatchException,
			NullReferenceException, InvocationTargetException, InvalidBindingException {

		log("testReloadProject");

		String oldFormoseViewURI = view.getURI();

		FlexoProject<File> oldProject = project;
		String oldURI = oldProject.getProjectURI();
		System.out.println("Old URI: " + oldURI);
		System.out.println("Old project dir: " + project.getProjectDirectory());
		// instanciateTestServiceManager();
		editor = reloadProject(project);
		project = (FlexoProject<File>) editor.getProject();
		String newURI = project.getProjectURI();
		System.out.println("New URI: " + newURI);
		assertNotNull(editor);
		assertNotNull(project);
		assertNotSame(oldProject, project);
		assertEquals(newURI, oldURI);

		FMLRTVirtualModelInstanceResource newFormoseViewResource = project.getVirtualModelInstanceRepository()
				.getVirtualModelInstance(oldFormoseViewURI);
		assertNotNull(newFormoseViewResource);
		newFormoseViewResource.loadResourceData();
		assertNotNull(view = newFormoseViewResource.getVirtualModelInstance());

		for (FMLRTVirtualModelInstanceResource resource : newFormoseViewResource.getVirtualModelInstanceResources()) {
			System.out.println(" > " + resource);
		}

		assertEquals(8, newFormoseViewResource.getVirtualModelInstanceResources().size());

		// System.out.println("view = " + view);
		// System.out.println("VM=" + view.getVirtualModel());

		assertNotNull(formoseVMI = view.execute("formoseVMI"));
		// System.out.println("formoseVMI = " + formoseVMI);
		// System.out.println("VM=" + formoseVMI.getVirtualModel());

		assertNotNull(projectElement = formoseVMI.execute("projectElement"));
		assertNotNull(sysMLKaosMethodology = projectElement.execute("applicableSysMLKaosMethodology"));
		assertNotNull(domainModellingMethodology = projectElement.execute("applicableDomainModelMethodology"));
		assertNotNull(bMethodology = projectElement.execute("applicableBMethodology"));

		bProjectMapping = bMethodology.execute("getBElementMapping({$element})", projectElement);
		assertNotNull(bProjectMapping);

		FlexoConceptInstance topLevel = bProjectMapping.execute("topLevelBMappings.get(0)");
		assertNotNull(topLevel);

		AtelierBComponent context = topLevel.execute("context");
		AtelierBComponent machine = topLevel.execute("machine");

		List<FlexoConceptInstance> instances = topLevel.getEmbeddedFlexoConceptInstances();
		System.out.println("instances = " + instances);

		/*assertEquals(5, instances.size());
		assertEquals("ConceptA", instances.get(0).execute("set.name"));
		assertEquals("ConceptB", instances.get(1).execute("set.name"));
		assertEquals("ConceptC", instances.get(2).execute("set.name"));
		
		BSet set1 = instances.get(0).execute("set");
		System.out.println("Set1: " + set1 + " of " + set1.getClass());*/

	}

	/*@AfterClass
	public static void tearDownClass() {
		System.out.println("On quitte sans rien fermer");
		System.exit(-1);
	}*/

}
