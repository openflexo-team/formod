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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.fml.cli.CommandInterpreter;
import org.openflexo.foundation.fml.cli.ParseException;
import org.openflexo.foundation.fml.cli.command.FMLCommandExecutionException;
import org.openflexo.foundation.fml.cli.command.FMLScript;
import org.openflexo.foundation.fml.cli.command.fml.FMLAssertException;
import org.openflexo.foundation.fml.cli.test.FMLScriptParserTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.rm.Resources;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.rm.AtelierBProjectResource;
import org.openflexo.ta.b.rm.AtelierBProjectResourceFactory;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * A parameterized suite of FML-script driven tests of the Formose resource center.
 *
 * Each {@code .fmlscript} under {@code FormodAutomatedTests} is parsed and executed; every {@code assert} it contains must succeed. The
 * scripts assert business values, not merely that the FML compiles.
 *
 * @author sylvain
 */
@RunWith(Parameterized.class)
public class FormodAutomatedTests extends FMLScriptParserTestCase {

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> generateData() {
		return Resources.getMatchingResource(ResourceLocator.locateResource("FormodAutomatedTests"), ".fmlscript");
	}

	private final Resource fmlResource;
	private CommandInterpreter commandInterpreter;

	public FormodAutomatedTests(Resource fmlResource, String name) throws ParseException, ModelDefinitionException, IOException {
		this.fmlResource = fmlResource;
		initServiceManager();
	}

	@Test
	public void checkScript() throws ModelDefinitionException, ParseException, IOException, FMLCommandExecutionException {
		FMLScript script = parseFMLScript(fmlResource, commandInterpreter);
		checkFMLScript(fmlResource.getRelativePath(), script);
		try {
			script.execute();
		} catch (FMLAssertException e) {
			fail(e.getMessage());
		}
	}

	private void initServiceManager() throws ParseException, ModelDefinitionException, IOException {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class, DocXTechnologyAdapter.class, ExcelTechnologyAdapter.class,
				BTechnologyAdapter.class);
		FlexoEditor editor = new DefaultFlexoEditor(null, serviceManager);
		assertNotNull(editor);
		commandInterpreter = new CommandInterpreter(serviceManager, System.in, System.out, System.err, HOME_DIR);
		provisionAtelierBProjects();
	}

	/** Stable URI under which the scripts load the blank Atelier B project the B methodology generates its models in */
	public static final String SOURCE_ATELIER_B_PROJECT_URI = "http://formose.lacl.fr/test/SourceAtelierBProject";
	/** Stable URI under which the scripts load the second blank Atelier B project the B methodology is given */
	public static final String GENERATED_ATELIER_B_PROJECT_URI = "http://formose.lacl.fr/test/GeneratedAtelierBProject";

	/**
	 * Infrastructure only: an Atelier B project cannot be created from a script, so two blank ones are generated in a fresh directory
	 * resource center and registered under stable URIs, which the scripts then load
	 */
	private void provisionAtelierBProjects() throws IOException {
		DirectoryResourceCenter atelierBRC = makeNewDirectoryResourceCenter();
		provisionAtelierBProject(atelierBRC, "SourceAtelierBProject", SOURCE_ATELIER_B_PROJECT_URI);
		provisionAtelierBProject(atelierBRC, "GeneratedAtelierBProject", GENERATED_ATELIER_B_PROJECT_URI);
	}

	private static void provisionAtelierBProject(DirectoryResourceCenter rc, String name, String uri) throws IOException {
		File projectDirectory = new File(rc.getRootDirectory(), name);
		AtelierBProjectResourceFactory.generateBlankAtelierBProject(projectDirectory);
		try {
			rc.getDirectoryWatcher().waitNextWatching();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		File dbFile = new File(new File(projectDirectory, AtelierBProjectResourceFactory.BDP_DIR), name + ".db");
		AtelierBProjectResource resource = rc.getResource(dbFile, AtelierBProjectResource.class);
		assertNotNull("Atelier B project " + name + " was not registered", resource);
		resource.setURI(uri);
	}
}
