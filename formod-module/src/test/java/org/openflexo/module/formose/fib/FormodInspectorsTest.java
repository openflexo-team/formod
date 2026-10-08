package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
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
import org.openflexo.gina.utils.FIBInspector;
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
			"http://formose.lacl.fr/Formose.fml/SysMLKaosMethodology.fml",
			"http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/GoalModelingDiagram.fml",
			"http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/SysMLKaosModel.fml" };

	/** Number of <code>.inspector</code> files rebuilt from the legacy serialization */
	private static final int EXPECTED_INSPECTORS = 52;

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
				// A concept inherits the inspector of its parent: only the ones declaring their own are checked
				if (!concept.hasMetaData(FlexoConcept.INSPECTOR_METADATA)) {
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
		return null;
	}

}
