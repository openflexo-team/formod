package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fib.binding.FMLControlledComponent;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.FIBComponentResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomAssignment;
import org.openflexo.gina.model.FIBModelFactory;
import org.openflexo.gina.model.container.FIBPanel;
import org.openflexo.gina.model.container.FIBTab;
import org.openflexo.gina.model.container.FIBTabPanel;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.utils.FIBInspector;
import org.openflexo.inspector.ModuleInspectorController;
import org.openflexo.pamela.validation.ValidationError;
import org.openflexo.pamela.validation.ValidationReport;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * The concept inspectors of the Formose resource center: each one is a <code>Xxx.inspector</code> stored in the container of the model
 * declaring the concept, named by an explicit <code>@Inspector("Xxx.inspector")</code>. They were rebuilt from the <code>Inspector</code>
 * of the legacy <code>.fml.xml</code> serialization, which the migration to textual FML had dropped.
 */
@RunWith(OrderedRunner.class)
public class FormodInspectorsTest extends OpenflexoTestCase {

	/** The models of the resource center declaring concepts that have an inspector */
	private static final String[] MODEL_URIS = { "http://formose.lacl.fr/DocumentLibrary.fml/AbstractDocument.fml",
			"http://formose.lacl.fr/DocumentLibrary.fml/WordDocument.fml", "http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModel.fml",
			"http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModelDiagram.fml", "http://formose.lacl.fr/Formose.fml/BMethodology.fml",
			"http://formose.lacl.fr/Formose.fml/DomainModelMethodology.fml", "http://formose.lacl.fr/Formose.fml/FormoseCore.fml",
			"http://formose.lacl.fr/Formose.fml/SysMLKaosMethodology.fml", "http://formose.lacl.fr/Formose.fml/Methodology.fml",
			"http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/GoalModelingDiagram.fml",
			"http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/SysMLKaosModel.fml" };

	/** Number of <code>.inspector</code> files rebuilt from the legacy serialization */
	private static final int EXPECTED_INSPECTORS = 33;

	@Test
	@TestOrder(1)
	public void test0LoadFormose() {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void test1EveryInspectorIsValid()
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException, InterruptedException {
		int count = 0;
		List<String> invalid = new ArrayList<>();
		for (String uri : MODEL_URIS) {
			VirtualModel model = serviceManager.getVirtualModelLibrary().getVirtualModel(uri);
			assertNotNull("No VirtualModel " + uri, model);
			List<FlexoConcept> concepts = new ArrayList<>();
			collect(model, concepts);
			for (FlexoConcept concept : concepts) {
				// A concept inherits the inspector of its parent, or hands it to another object (derived): only the ones declaring a file are checked
				if (!concept.hasMetaData(FlexoConcept.INSPECTOR_METADATA) || concept.hasDerivedInspector()) {
					continue;
				}
				count++;
				String problem = check(concept);
				if (problem != null) {
					invalid.add(problem);
				}
			}
		}
		assertTrue("Invalid inspectors:\n" + String.join("\n", invalid), invalid.isEmpty());
		assertEquals("Number of concepts declaring an inspector", EXPECTED_INSPECTORS, count);
	}

	private static void collect(FlexoConcept concept, List<FlexoConcept> into) {
		into.add(concept);
		for (FlexoConcept embedded : concept.getEmbeddedFlexoConcepts()) {
			collect(embedded, into);
		}
		if (concept instanceof VirtualModel) {
			for (FlexoConcept declared : ((VirtualModel) concept).getFlexoConcepts()) {
				if (declared != concept && !into.contains(declared)) {
					collect(declared, into);
				}
			}
		}
	}

	/** @return null when the inspector of supplied concept is fine, otherwise what is wrong with it */
	private static String check(FlexoConcept concept) throws InterruptedException {
		String fileName = concept.getName() + ".inspector";
		String uri = concept.getInspectorComponentResource().getURI();
		if (!fileName.equals(uri.substring(uri.lastIndexOf('/') + 1))) {
			return concept.getName() + " resolves " + uri + " rather than " + fileName;
		}
		// An explicit @Inspector("..."), not the naming convention alone: a later rename would silently orphan the component (CORE-F-4)
		if (!concept.hasMetaData(FlexoConcept.INSPECTOR_METADATA)) {
			return concept.getName() + " resolves its inspector by the naming convention alone";
		}
		FIBComponentResource resource = concept.getInspectorComponentFlexoResource();
		if (resource == null) {
			return fileName + " is not a registered resource";
		}
		FIBComponent component = FMLControlledComponent.loadInspectorComponent(concept, null);
		if (component == null) {
			return fileName + " did not load";
		}
		if (!(component instanceof FIBInspector)) {
			return fileName + " loaded as a " + component.getClass();
		}
		ValidationReport report = component.validate();
		for (ValidationError<?, ?> error : report.getAllErrors()) {
			return "Invalid binding in " + fileName + ", " + error.getValidable() + ": "
					+ report.getValidationModel().localizedIssueDetailedInformations(error) + " (" + report.getErrorsCount() + " in all)";
		}
		// A selector of instances names the concept it lists by URI, which no validation of bindings checks: an URI that designates nothing
		// (the legacy ones did, after the move off the .viewpoint folders) gives a selector with nothing to choose from
		for (FIBComponent widget : ((FIBContainer) component).getAllSubComponents()) {
			if (widget instanceof FIBCustom) {
				for (FIBCustomAssignment assignment : ((FIBCustom) widget).getAssignments()) {
					// Every property of the selector is listed as an assignment; only the ones given a value count
					if (assignment.getVariable() != null && "component.expectedFlexoConceptTypeURI".equals(assignment.getVariable().toString())
							&& assignment.getValue() != null && assignment.getValue().isSet()) {
						String expected = assignment.getValue().toString().replace("\"", "").replace("'", "");
						if (concept.getServiceManager().getVirtualModelLibrary().getFlexoConcept(expected) == null) {
							return "No concept " + expected + ", selected by " + widget.getName() + " in " + fileName;
						}
					}
				}
			}
		}
		return null;
	}


	/**
	 * The inspector of an instance is ADDITIVE: a FunctionalGoal shows what Goal shows (the name and description of the goal), then its own
	 * Type - the generated inspectors carry <code>index = depth * 100 + position</code>.
	 */
	@Test
	@TestOrder(3)
	public void test2FunctionalGoalShowsGoalThenItsOwnWidgets() throws Exception {
		FlexoConcept functionalGoal = concept("http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/SysMLKaosModel.fml", "FunctionalGoal");
		assertEquals(Arrays.asList("Goal", "FunctionalGoal"), contributorNames(functionalGoal));

		List<String> expected = new ArrayList<>(ownWidgetNames(concept("http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/SysMLKaosModel.fml", "Goal")));
		expected.addAll(ownWidgetNames(functionalGoal));
		assertEquals(expected, composedWidgetNames(functionalGoal));
	}

	/**
	 * A graphical representation has no inspector of its own: it hands the inspection to the object it stands for (the goal, the concept,
	 * the individual...), whose inspector is the one place where that object is described. The derivation is not inherited, so every
	 * concrete representation declares it.
	 */
	@Test
	@TestOrder(3)
	public void test2bGraphicalRepresentationsDeriveToWhatTheyStandFor() throws Exception {
		String goals = "http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/GoalModelingDiagram.fml";
		String domain = "http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModelDiagram.fml";
		String[][] derivations = { { goals, "FunctionalGoalGR", "goal" }, { goals, "NonFunctionalGoalGR", "goal" },
				{ goals, "ContributionGoalGR", "goal" }, { goals, "EnvironmentalAgentGR", "agent" }, { goals, "SoftwareAgentGR", "agent" },
				{ goals, "RefinementGR", "refinement" }, { goals, "ParentRefinementLinkGR", "parentGoalGR.goal" },
				{ goals, "ChildRefinmentLinkGR", "childrenGoalGR.goal" }, { goals, "AgentAssignmentGR", "agentAssignment" },
				{ goals, "ContributionGR", "contribution" }, { goals, "ImpactGR", "impact" }, { goals, "GoalGroupGR", "goalGroup" },
				{ domain, "ConceptGR", "modelConcept" }, { domain, "DefinedConceptGR", "modelConcept" },
				{ domain, "DefaultDataTypeGR", "modelConcept" }, { domain, "AssociationGR", "modelConcept" },
				{ domain, "IndividualGR", "individual" }, { domain, "MapletIndividualGR", "individual" },
				{ domain, "SimpleMapletIndividualGR", "individual" }, { domain, "ConceptAttributeGR", "modelConcept" },
				{ domain, "IndividualAttributeGR", "individual" }, { domain, "LogicalFormulaGR", "logicalFormula" },
				{ domain, "AssociationLeftArrowGR", "associationGR.modelConcept" },
				{ domain, "AssociationRightArrowGR", "associationGR.modelConcept" } };
		List<String> wrong = new ArrayList<>();
		for (String[] derivation : derivations) {
			FlexoConcept gr = concept(derivation[0], derivation[1]);
			if (!gr.hasDerivedInspector()) {
				wrong.add(derivation[1] + " does not derive its inspector");
				continue;
			}
			if (!derivation[2].equals(String.valueOf(gr.getDerivedInspector()))) {
				wrong.add(derivation[1] + " derives to " + gr.getDerivedInspector() + " instead of " + derivation[2]);
			}
			if (!gr.getDerivedInspector().isValid()) {
				wrong.add(derivation[1] + ": invalid binding " + gr.getDerivedInspector() + ": " + gr.getDerivedInspector().invalidBindingReason());
			}
			if (gr.getInspectorComponentResource() != null) {
				wrong.add(derivation[1] + " derives AND resolves its own inspector " + gr.getInspectorComponentResource().getURI());
			}
		}
		assertTrue("Wrong derivations:\n" + String.join("\n", wrong), wrong.isEmpty());
	}

	/**
	 * For every concept whose hierarchy contributes several inspectors: the composed widgets are those of the ancestors, then the
	 * concept's own, each in its own order - a widget named like an ancestor's replacing it.
	 */
	@Test
	@TestOrder(4)
	public void test3EveryComposedInspectorListsAncestorsFirst() throws Exception {
		int composed = 0;
		List<String> wrong = new ArrayList<>();
		for (String uri : MODEL_URIS) {
			List<FlexoConcept> concepts = new ArrayList<>();
			collect(serviceManager.getVirtualModelLibrary().getVirtualModel(uri), concepts);
			for (FlexoConcept concept : concepts) {
				if (concept.getInspectorContributingConcepts().size() < 2) {
					continue;
				}
				composed++;
				List<String> expected = new ArrayList<>();
				for (FlexoConcept contributor : concept.getInspectorContributingConcepts()) {
					for (String name : ownWidgetNames(contributor)) {
						expected.remove(name);
						expected.add(name);
					}
				}
				List<String> actual = composedWidgetNames(concept);
				if (!expected.equals(actual)) {
					wrong.add(concept.getName() + ": expected " + expected + " but got " + actual);
				}
			}
		}
		assertTrue("Wrong composition:\n" + String.join("\n", wrong), wrong.isEmpty());
		assertTrue("Expected the 6 concepts overriding an ancestor's inspector, found " + composed, composed >= 6);
	}

	/**
	 * Every methodology inherits the inspector of Methodology (an addition to the legacy, which declared none): it shows the name and the
	 * element the methodology is declared on, and nothing of its own.
	 */
	@Test
	@TestOrder(5)
	public void test4MethodologiesInheritTheMethodologyInspector() throws Exception {
		for (String model : new String[] { "SysMLKaosMethodology", "DocumentAnnotationMethodology", "DomainModelMethodology", "BMethodology" }) {
			FlexoConcept methodology = concept("http://formose.lacl.fr/Formose.fml/" + model + ".fml", model);
			assertEquals(model, Arrays.asList("Methodology"), contributorNames(methodology));
			assertEquals(model, Arrays.asList("nameLabel", "nameTextField", "declaringElementLabel", "declaringElementTextField"),
					composedWidgetNames(methodology));
		}
	}

	private FlexoConcept concept(String modelUri, String name)
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		VirtualModel model = serviceManager.getVirtualModelLibrary().getVirtualModel(modelUri);
		assertNotNull("No VirtualModel " + modelUri, model);
		List<FlexoConcept> concepts = new ArrayList<>();
		collect(model, concepts);
		for (FlexoConcept concept : concepts) {
			if (name.equals(concept.getName())) {
				return concept;
			}
		}
		throw new AssertionError("No concept " + name + " in " + modelUri);
	}

	private static List<String> contributorNames(FlexoConcept concept) {
		List<String> returned = new ArrayList<>();
		for (FlexoConcept contributor : concept.getInspectorContributingConcepts()) {
			returned.add(contributor.getName());
		}
		return returned;
	}

	/**
	 * The widgets the inspector this concept itself contributes holds, in file order: those of its panel, which is a plain one for the
	 * inspectors of Formose (the platform makes it a tab), or of its first tab for a component built with a TabPanel
	 */
	private static List<String> ownWidgetNames(FlexoConcept contributor) {
		FIBContainer component = (FIBContainer) FMLControlledComponent.loadInspectorComponent(contributor, null);
		List<String> returned = new ArrayList<>();
		FIBContainer tab = component;
		FIBComponent tabPanel = component.getSubComponentNamed("Tab");
		if (tabPanel instanceof FIBContainer) {
			tab = (FIBContainer) ((FIBContainer) tabPanel).getSubComponents().get(0);
		}
		for (FIBComponent widget : tab.getSubComponents()) {
			returned.add(widget.getName());
		}
		return returned;
	}

	/** The widgets the inspector of an instance of supplied concept shows, composed as the inspector controller does */
	private List<String> composedWidgetNames(FlexoConcept concept) throws Exception {
		List<FIBContainer> containers = new ArrayList<>();
		for (FlexoConcept contributor : concept.getInspectorContributingConcepts()) {
			containers.add((FIBContainer) FMLControlledComponent.loadInspectorComponent(contributor, null));
		}
		// Stand-in for FlexoConceptInstance.inspector: a TabPanel holding the platform's BasicTab
		FIBModelFactory factory = new FIBModelFactory(null, serviceManager.getTechnologyAdapterService(), FIBInspector.class);
		FIBInspector classInspector = factory.newInstance(FIBInspector.class);
		classInspector.setName("Inspector");
		classInspector.setLayout(FIBPanel.Layout.border);
		classInspector.setDataClass(org.openflexo.foundation.fml.rt.FlexoConceptInstance.class);
		FIBTabPanel classTabPanel = factory.newInstance(FIBTabPanel.class);
		classTabPanel.setName("Tab");
		FIBTab basicTab = factory.newFIBTab();
		basicTab.setName("BasicTab");
		classTabPanel.addToSubComponents(basicTab);
		classInspector.addToSubComponents(classTabPanel);

		ModuleInspectorController.mergeContainerInspectors(classInspector, containers, concept, null);

		// Several contributors make one tab named <Concept>Panel; a single one keeps the tab of its own component
		FIBContainer tab = (FIBContainer) classInspector.getTabPanel().getSubComponentNamed(concept.getName() + "Panel");
		if (tab == null) {
			for (FIBComponent candidate : classInspector.getTabPanel().getSubComponents()) {
				if (candidate instanceof FIBContainer && !"BasicTab".equals(candidate.getName())) {
					tab = (FIBContainer) candidate;
					break;
				}
			}
		}
		assertNotNull("No composed tab for " + concept, tab);
		List<String> returned = new ArrayList<>();
		for (FIBComponent widget : tab.getSubComponents()) {
			returned.add(widget.getName());
		}
		return returned;
	}

}
