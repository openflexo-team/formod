package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * The palette bindings of the diagrams of Formose: each element of a palette must resolve the drop scheme it runs.
 *
 * <p>
 * A binding written <code>call=new FunctionalGoalGR::createFunctionalGoal()</code> for a scheme that has parameters does NOT resolve: the
 * binding is read as <code>new FunctionalGoalGR()</code>, with no drop scheme, and the palette element silently cannot be dropped (the
 * log only says "Dropping was not successful"). The call has to carry arguments matching the signature of the scheme - which the palette
 * then ignores (CORE-D-30) but needs in order to resolve.
 */
public class FormodPaletteBindingsTest extends OpenflexoTestCase {

	private static final String[] DIAGRAM_MODELS = { "http://formose.lacl.fr/SysMLKaos/SysMLKaos.fml/GoalModelingDiagram.fml",
			"http://formose.lacl.fr/DomainModel/DomainModelling.fml/DomainModelDiagram.fml" };

	@Test
	public void testEveryPaletteElementResolvesItsDropScheme() throws Exception {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
		List<String> unresolved = new ArrayList<>();
		int bindings = 0;
		for (String uri : DIAGRAM_MODELS) {
			VirtualModel model = serviceManager.getVirtualModelLibrary().getVirtualModel(uri);
			assertNotNull("No VirtualModel " + uri, model);
			TypedDiagramModelSlot slot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(model);
			assertNotNull("No diagram slot in " + uri, slot);
			for (FMLDiagramPaletteElementBinding binding : slot.getPaletteElementBindings()) {
				bindings++;
				if (binding.getDropScheme() == null) {
					unresolved.add(model.getName() + ": " + binding.getPaletteElementId() + " calls " + binding.getCall());
				}
			}
		}
		assertTrue("Palette elements with no drop scheme (they cannot be dropped):\n" + String.join("\n", unresolved), unresolved.isEmpty());
		assertTrue("Expected the palette bindings of both diagrams, found " + bindings, bindings >= 15);
	}

}
