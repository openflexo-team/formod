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
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.module.formose.FMSConstants;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.module.formose.model.action.GivesFormoseNature;
import org.openflexo.module.formose.model.action.InstantiateSysMLKaosMethodology;
import org.openflexo.module.formose.model.action.RefineUsingAgentAllocation;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
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
public class TestSysMLKaosMethology extends OpenflexoProjectAtRunTimeTestCaseWithGUI {

	public static FlexoProject<?> project;
	private static FlexoEditor editor;
	private static VirtualModel formoseVP;

	private static FMLRTVirtualModelInstance view;

	private static FMLRTVirtualModelInstance formoseVMI;
	private static FMLRTVirtualModelInstance documentLibrary;

	private static FlexoConceptInstance projectElement;

	private static FMLRTVirtualModelInstance sysMLKaosMethodology;

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

		elementMapping = sysMLKaosMethodology.execute("getElementMapping({$element})", projectElement);
		assertNotNull(elementMapping);

		diagramMapping = elementMapping.execute("mainFunctionalGoalDiagram");
		assertNotNull(diagramMapping);

		goalModelingDiagram = diagramMapping.execute("goalModelingDiagram");
		assertNotNull(goalModelingDiagram);

		assertSame(diagramMapping.execute("goalModel"), goalModelingDiagram.execute("model"));

	}

	private static FlexoConceptInstance elementMapping; // SysMLKaosElementMapping
	private static FlexoConceptInstance diagramMapping; // DiagramMapping
	private static FMLRTVirtualModelInstance goalModelingDiagram; // GoalModelingDiagram

	private static FlexoConceptInstance fg1GR;
	private static FlexoConceptInstance fg1;
	private static FlexoConceptInstance agent1GR;
	private static FlexoConceptInstance agent1;
	private static FlexoConceptInstance agentAssignement1GR;
	private static FlexoConceptInstance agentAssignement1;

	@Test
	@TestOrder(17)
	@Category(UITest.class)
	public void dropFunctionalGoal() throws SaveResourceException, TypeMismatchException, NullReferenceException, InvocationTargetException,
			InvalidBindingException {

		FlexoConcept functionalGoalGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("FunctionalGoalGR");
		assertNotNull(functionalGoalGR);
		DropScheme dropScheme = (DropScheme) functionalGoalGR.getFlexoBehaviour("createFunctionalGoal", String.class, String.class,
				String.class);
		DropSchemeAction dropSchemeAction = new DropSchemeAction(dropScheme, goalModelingDiagram, null, editor);
		dropSchemeAction.setDropLocation(new DianaPoint(100, 100));
		dropSchemeAction.setParameterValue(dropScheme.getParameters().get(0), "FG1");

		// Perform the creation, then retrieve the new functional goal instance
		dropSchemeAction.doAction();
		assertTrue(dropSchemeAction.hasActionExecutionSucceeded());
		fg1GR = dropSchemeAction.getFlexoConceptInstance();
		assertNotNull(fg1GR);

		fg1 = fg1GR.execute("goal");
		assertNotNull(fg1);
		assertEquals("FunctionalGoal", fg1.getFlexoConcept().getName());

	}

	@Test
	@TestOrder(18)
	@Category(UITest.class)
	public void dropAgent() throws SaveResourceException, TypeMismatchException, NullReferenceException, InvocationTargetException,
			InvalidBindingException {

		FlexoConcept agentGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("SoftwareAgentGR");
		assertNotNull(agentGR);
		DropScheme dropScheme = (DropScheme) agentGR.getFlexoBehaviour("createSoftwareAgent", String.class, String.class);
		DropSchemeAction dropSchemeAction = new DropSchemeAction(dropScheme, goalModelingDiagram, null, editor);
		dropSchemeAction.setDropLocation(new DianaPoint(200, 100));
		dropSchemeAction.setParameterValue(dropScheme.getParameters().get(0), "Agent1");

		// Perform the creation, then retrieve the new functional goal instance
		dropSchemeAction.doAction();
		assertTrue(dropSchemeAction.hasActionExecutionSucceeded());
		agent1GR = dropSchemeAction.getFlexoConceptInstance();
		assertNotNull(agent1GR);

		agent1 = agent1GR.execute("agent");
		assertNotNull(agent1);
		assertEquals("SoftwareAgent", agent1.getFlexoConcept().getName());

	}

	@Test
	@TestOrder(19)
	@Category(UITest.class)
	public void assignAgent() throws SaveResourceException, TypeMismatchException, NullReferenceException, InvocationTargetException,
			InvalidBindingException {

		FlexoConcept agentAssignmentGR = goalModelingDiagram.getVirtualModel().getFlexoConcept("AgentAssignmentGR");
		assertNotNull(agentAssignmentGR);
		LinkScheme linkScheme = (LinkScheme) agentAssignmentGR.getFlexoBehaviour("linkAgentToGoal");
		LinkSchemeAction linkSchemeAction = new LinkSchemeAction(linkScheme, goalModelingDiagram, null, editor);
		linkSchemeAction.setFromShape(agent1GR.execute("shape"));
		linkSchemeAction.setToShape(fg1GR.execute("shape"));

		// Perform the creation, then retrieve the new functional goal instance
		linkSchemeAction.doAction();
		assertTrue(linkSchemeAction.hasActionExecutionSucceeded());
		agentAssignement1GR = linkSchemeAction.getFlexoConceptInstance();
		assertNotNull(agentAssignement1GR);

		agentAssignement1 = agentAssignement1GR.execute("agentAssignment");
		assertNotNull(agentAssignement1);
		assertEquals("AgentAssignment", agentAssignement1.getFlexoConcept().getName());

	}

	private static FlexoConceptInstance agent1Element; // Element
	private static FlexoConceptInstance agent1Mapping; // SysMLKaosElementMapping
	private static FlexoConceptInstance agent1DiagramMapping; // DiagramMapping
	private static FMLRTVirtualModelInstance agent1GoalModelingDiagram; // GoalModelingDiagram

	@Test
	@TestOrder(20)
	@Category(UITest.class)
	public void refineUsingAgentAllocation() throws SaveResourceException, TypeMismatchException, NullReferenceException,
			InvocationTargetException, InvalidBindingException {

		log("refineUsingAgentAllocation");

		RefineUsingAgentAllocation refineAction = RefineUsingAgentAllocation.ACTION_TYPE.makeNewAction(elementMapping, null, editor);
		refineAction.doAction();
		assertTrue(refineAction.hasActionExecutionSucceeded());

		List<FlexoConceptInstance> children = projectElement.execute("childrenElements");
		assertEquals(1, children.size());

		agent1Element = children.get(0);
		assertEquals("Element", agent1Element.getFlexoConcept().getName());

		agent1Mapping = sysMLKaosMethodology.execute("getElementMapping({$element})", agent1Element);
		assertNotNull(agent1Mapping);

		assertEquals(agent1, agent1Mapping.execute("agent"));

		agent1DiagramMapping = agent1Mapping.execute("mainFunctionalGoalDiagram");
		assertNotNull(agent1DiagramMapping);

		agent1GoalModelingDiagram = agent1DiagramMapping.execute("goalModelingDiagram");
		assertNotNull(agent1GoalModelingDiagram);

		assertNotNull(agent1DiagramMapping.execute("goalModel"));
		assertSame(agent1DiagramMapping.execute("goalModel"), agent1GoalModelingDiagram.execute("model"));

	}

}
