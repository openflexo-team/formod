/**
 *
 * Copyright (c) 2026, Openflexo
 *
 * This file is part of Formose prototype, a component of the software infrastructure
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.FIBComponentResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.fib.binding.FMLControlledComponent;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.pamela.validation.ValidationError;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Checks the user interfaces the Formose resource center drives.
 *
 * They are stored in the <code>Xxx.fml/</code> container of the concept they represent, and resolved by convention
 * (<code>&lt;ConceptName&gt;.fib</code>), rather than federated through a gina-ta <code>FIBComponentModelSlot</code> as they were in
 * formod 2.1:
 * <ul>
 * <li><code>Formose.fml/Formose.fib</code> — the view of a Formose project (was <code>Fib/ProjectUI.fib</code>);</li>
 * <li><code>Formose.fml/FormoseCore.fml/Element.fib</code> — the view of an element (was <code>Fib/ElementUI.fib</code>);</li>
 * <li><code>Formose.fml/SysMLKaosMethodology.fml/SysMLKaosElementMapping.fib</code> — the view of a SysML/KAOS element mapping.</li>
 * </ul>
 *
 * A component that does not deserialize comes back null, silently, so each one is loaded, and its bindings are checked: an invalid
 * binding in a user interface is never reported at run-time, the widget simply stays empty.
 *
 * The controller the components name (FMSFIBController) lives in this module, which is why this test does: validating them from
 * formod-test would report every <code>controller.…</code> binding as unresolved.
 *
 * @author sylvain
 */
@RunWith(OrderedRunner.class)
public class FormodContainerUIsTest extends OpenflexoTestCase {

	public static final String FORMOSE_URI = "http://formose.lacl.fr/Formose.fml";
	public static final String FORMOSE_CORE_URI = FORMOSE_URI + "/FormoseCore.fml";
	public static final String SYSML_KAOS_METHODOLOGY_URI = FORMOSE_URI + "/SysMLKaosMethodology.fml";

	private static VirtualModel formose;

	@Test
	@TestOrder(1)
	public void test0LoadFormose() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
		formose = serviceManager.getVirtualModelLibrary().getVirtualModel(FORMOSE_URI);
		assertNotNull("No VirtualModel " + FORMOSE_URI, formose);
	}

	@Test
	@TestOrder(2)
	public void test1FormoseHasItsOwnView() throws InterruptedException {
		assertComponentIsValid(formose, "Formose.fib");
	}

	@Test
	@TestOrder(3)
	public void test2ElementHasItsView() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException, InterruptedException {
		VirtualModel formoseCore = serviceManager.getVirtualModelLibrary().getVirtualModel(FORMOSE_CORE_URI);
		assertNotNull(formoseCore);
		FlexoConcept element = formoseCore.getFlexoConcept("Element");
		assertNotNull("No concept Element in " + FORMOSE_CORE_URI, element);
		assertComponentIsValid(element, "Element.fib");
	}

	@Test
	@TestOrder(4)
	public void test3ElementMappingHasItsView()
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException, InterruptedException {
		VirtualModel methodology = serviceManager.getVirtualModelLibrary().getVirtualModel(SYSML_KAOS_METHODOLOGY_URI);
		assertNotNull(methodology);
		FlexoConcept elementMapping = methodology.getFlexoConcept("SysMLKaosElementMapping");
		assertNotNull("No concept SysMLKaosElementMapping in " + SYSML_KAOS_METHODOLOGY_URI, elementMapping);
		assertComponentIsValid(elementMapping, "SysMLKaosElementMapping.fib");
	}

	/**
	 * Asserts that the supplied concept resolves the named component in its container, that the component loads, and that every binding
	 * it declares is valid
	 */
	private void assertComponentIsValid(FlexoConcept concept, String expectedFileName) throws InterruptedException {
		assertNotNull("Concept " + concept.getName() + " resolves no user interface", concept.getUIComponentResource());
		assertEquals(expectedFileName, concept.getUIComponentResource().getURI()
				.substring(concept.getUIComponentResource().getURI().lastIndexOf('/') + 1));

		FIBComponentResource resource = concept.getUIComponentFlexoResource();
		assertNotNull("The component of " + concept.getName() + " is not a registered resource", resource);
		assertSame(concept.getUIComponentResource(), resource.getIODelegate().getSerializationArtefactAsResource());

		// Bound to its concept, as a module shows it: 'data' is typed by the concept, and the bindings are parsed as FML
		FIBComponent component = FMLControlledComponent.loadUIComponent(concept, null);
		assertNotNull(expectedFileName + " did not deserialize", component);
		for (ValidationError<?, ?> error : component.validate().getAllErrors()) {
			System.out.println("Invalid binding in " + expectedFileName + ", " + error.getValidable() + ": "
					+ error.getValidationReport().getValidationModel().localizedIssueDetailedInformations(error));
		}
		assertEquals(expectedFileName + " has invalid bindings", 0, component.validate().getErrorsCount());
	}

}
