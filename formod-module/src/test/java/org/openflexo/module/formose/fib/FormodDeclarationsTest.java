package org.openflexo.module.formose.fib;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.FMLCompilationUnit;
import org.openflexo.foundation.fml.editionaction.DeclarationAction;
import org.openflexo.foundation.fml.parser.fmlnodes.controlgraph.DeclarationActionNode;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Every declaration of the Formose resource center whose initializer is an edition action - <code>ExcelRow row = XLS::AddExcelRow(...)</code>,
 * <code>BExpression e = B::CreateBExpressionFromString(...)</code>, <code>Goal g = select unique Goal from ... where ...</code>, <code>MatchingSet m =
 * begin match ...</code> - holds that action once the unit is loaded. The parser used to log "DeclarationAction with EditionAction not
 * implemented yet" for each of them, though they were all built (and the scenarios execute them): this is what shows it, form by form.
 */
@RunWith(OrderedRunner.class)
public class FormodDeclarationsTest extends OpenflexoTestCase {

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

	@Test
	@TestOrder(2)
	public void test1EveryDeclarationHoldsItsAction() throws Exception {
		List<String> empty = new ArrayList<>();
		Map<String, Integer> kinds = new TreeMap<>();
		for (String uri : MODEL_URIS) {
			FMLCompilationUnit unit = serviceManager.getVirtualModelLibrary().getVirtualModel(uri).getCompilationUnit();
			List<DeclarationAction<?>> declarations = new ArrayList<>();
			collect((P2PPNode<?, ?>) unit.getPrettyPrintDelegate(), declarations);
			for (DeclarationAction<?> declaration : declarations) {
				if (declaration.getAssignableAction() == null) {
					empty.add(unit.getVirtualModel().getName() + ": " + declaration.getVariableName());
				}
				else {
					kinds.merge(declaration.getAssignableAction().getClass().getInterfaces()[0].getSimpleName(), 1, Integer::sum);
				}
			}
		}
		assertTrue("Declarations with no action:\n" + String.join("\n", empty), empty.isEmpty());
		// The test would prove nothing without the forms that go through the production of the actions
		for (String expected : new String[] { "AddExcelRow", "CreateBExpressionFromString", "SelectUniqueFlexoConceptInstance", "SelectFlexoConceptInstance",
				"InitiateMatching" }) {
			assertNotNull("No declaration holding a " + expected + " in the Formose units, only " + kinds, kinds.get(expected));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void collect(P2PPNode<?, ?> node, List<DeclarationAction<?>> into) {
		if (node instanceof DeclarationActionNode) {
			into.add((DeclarationAction<?>) ((DeclarationActionNode) node).getModelObject());
		}
		for (Object child : node.getChildren()) {
			collect((P2PPNode<?, ?>) child, into);
		}
	}
}
