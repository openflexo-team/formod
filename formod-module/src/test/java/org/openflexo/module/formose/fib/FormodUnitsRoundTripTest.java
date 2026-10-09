package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.UseModelSlotDeclaration;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * The compilation units of the Formose resource center keep every technology they <code>use</code>, once loaded.
 *
 * <p>
 * A unit that declares a <code>use</code> for a technology it only employs through edition actions - <code>B::CreateBPredicateFromString</code>,
 * <code>XLS::AddExcelCell</code> - and has no model slot role of it, lost that technology while it was being deserialized: the resource
 * kept only the model slots that have a role, the model factory of the unit was rebuilt without the actions of the others, and the text
 * rebuilt from the model wrote them <code>XLS::null(...)</code>. That text is what the FML editor of the Modeller shows and saves.
 */
@RunWith(OrderedRunner.class)
public class FormodUnitsRoundTripTest extends OpenflexoTestCase {

	private static final String[] MODEL_URIS = { "http://formose.lacl.fr/DocumentLibrary.fml", "http://formose.lacl.fr/DocumentLibrary.fml/AbstractDocument.fml",
			"http://formose.lacl.fr/DocumentLibrary.fml/ExcelDocument.fml", "http://formose.lacl.fr/DocumentLibrary.fml/WordDocument.fml",
			"http://formose.lacl.fr/DomainModel/DomainModelling.fml", "http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModel.fml",
			"http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModelDiagram.fml", "http://formose.lacl.fr/Formose.fml/BMethodology.fml",
			"http://formose.lacl.fr/Formose.fml/DocumentAnnotationMethodology.fml", "http://formose.lacl.fr/Formose.fml/DomainModelMethodology.fml",
			"http://formose.lacl.fr/Formose.fml/FormoseCore.fml", "http://formose.lacl.fr/Formose.fml/Methodology.fml",
			"http://formose.lacl.fr/Formose.fml/SysMLKaosMethodology.fml", "http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml",
			"http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/GoalModelingDiagram.fml", "http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/SysMLKaosModel.fml" };

	@Test
	@TestOrder(1)
	public void test0LoadFormose() {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
	}

	/** Each <code>use</code> of a unit is still in the model slots of its resource, which build its model factory. */
	@Test
	@TestOrder(2)
	public void test1EveryUseIsKeptByTheResource() throws Exception {
		List<String> lost = new ArrayList<>();
		for (String uri : MODEL_URIS) {
			VirtualModel model = serviceManager.getVirtualModelLibrary().getVirtualModel(uri);
			assertNotNull("No VirtualModel " + uri, model);
			FMLCompilationUnit unit = model.getCompilationUnit();
			CompilationUnitResource resource = (CompilationUnitResource) unit.getResource();
			for (UseModelSlotDeclaration use : unit.getUseDeclarations()) {
				if (!resource.getUsedModelSlots().contains(use.getModelSlotClass())) {
					lost.add(model.getName() + " declares use of " + use.getModelSlotClass().getSimpleName() + " but its resource only keeps "
							+ resource.getUsedModelSlots());
				}
			}
		}
		assertTrue("Technologies lost while loading:\n" + String.join("\n", lost), lost.isEmpty());
	}

	/** The text rebuilt from the model names every edition action: none is written <code>null</code>. */
	@Test
	@TestOrder(3)
	public void test2PrettyPrintNamesEveryEditionAction() throws Exception {
		List<String> broken = new ArrayList<>();
		for (String uri : MODEL_URIS) {
			String text = serviceManager.getVirtualModelLibrary().getVirtualModel(uri).getCompilationUnit().getFMLPrettyPrint();
			int index = text.indexOf("::null(");
			if (index >= 0) {
				broken.add(uri.replaceAll(".*/", "") + ": " + text.substring(Math.max(0, index - 30), Math.min(text.length(), index + 60)).replace("\\n", " "));
			}
		}
		assertTrue("Edition actions written null:\n" + String.join("\n", broken), broken.isEmpty());
	}

}
