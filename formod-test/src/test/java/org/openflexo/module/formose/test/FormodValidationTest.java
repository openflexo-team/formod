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

package org.openflexo.module.formose.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.pamela.validation.ValidationError;
import org.openflexo.pamela.validation.ValidationIssue;
import org.openflexo.pamela.validation.ValidationModel;
import org.openflexo.pamela.validation.ValidationReport;
import org.openflexo.pamela.validation.ValidationWarning;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * Checks that every VirtualModel of the Formose resource center parses and is FML-valid.
 *
 * A {@link CompilationUnitResource} whose source does not parse is loaded as an EMPTY VirtualModel, which then validates with zero
 * errors: "0 errors" alone proves nothing, so each compilation unit must first be parseable.
 *
 * Two legacy VirtualModels are deliberately left out (see {@link #NOT_MIGRATED}): they reference VirtualModels that never existed in
 * this repository, so they cannot be migrated as they are (FORMOD-F-1 in BACKLOG.md).
 *
 * @author sylvain
 */
@RunWith(Parameterized.class)
public class FormodValidationTest extends OpenflexoTestCase {

	public static final String FORMOSE_RC_URI = "http://formose.lacl.fr/";

	/** Legacy VirtualModels kept as they are, whose migration is tracked by FORMOD-F-1 in BACKLOG.md */
	public static final List<String> NOT_MIGRATED = Arrays.asList("http://formose.lacl.fr/Formose.fml/SysMLKaos-B-Methodology.fml",
			"http://formose.lacl.fr/Formose.fml/DomainModel-B-Methodology.fml");

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> generateData() {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
		List<Object[]> returned = new ArrayList<>();
		for (FlexoResource<?> resource : serviceManager.getResourceManager().getRegisteredResources()) {
			if (resource instanceof CompilationUnitResource && resource.getResourceCenter() != null
					&& FORMOSE_RC_URI.equals(resource.getResourceCenter().getDefaultBaseURI()) && !NOT_MIGRATED.contains(resource.getURI())) {
				returned.add(new Object[] { resource, resource.getURI() });
			}
		}
		return returned;
	}

	private final CompilationUnitResource resource;

	public FormodValidationTest(CompilationUnitResource resource, String uri) {
		this.resource = resource;
	}

	@Test
	public void validateVirtualModel() throws Exception {
		VirtualModel virtualModel = resource.getResourceData().getVirtualModel();
		assertNotNull("No VirtualModel in " + resource.getURI(), virtualModel);

		// A ParseException leaves an EMPTY compilation unit behind, which validates clean
		assertFalse(resource.getURI() + " does not parse (look for 'ParserException token:... line:N' in the log)",
				resource.isUnparseable());

		ValidationModel validationModel = serviceManager.getVirtualModelLibrary().getFMLValidationModel();
		ValidationReport report = validationModel.validate(virtualModel.getCompilationUnit());

		System.out.println("Validation of " + resource.getURI() + ": errors=" + report.getErrorsCount() + " warnings="
				+ report.getWarningsCount());
		for (ValidationIssue<?, ?> issue : report.getAllIssues()) {
			String kind = issue instanceof ValidationError ? "ERROR" : issue instanceof ValidationWarning ? "WARNING" : "INFO";
			System.out.println("  [" + kind + "] " + validationModel.localizedIssueMessage(issue) + "  >> " + describe(issue.getValidable()));
		}

		assertEquals(virtualModel.getName() + " must load without FML validation error", 0, report.getErrorsCount());
	}

	private static String describe(Object o) {
		if (o instanceof FMLObject) {
			return o.getClass().getSimpleName() + " : " + ((FMLObject) o).getStringRepresentation();
		}
		return String.valueOf(o);
	}
}
