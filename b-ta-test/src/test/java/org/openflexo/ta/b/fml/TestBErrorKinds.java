/**
 * 
 * Copyright (c) 2026, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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
 *          of EPL 1.0, the licensors of this Program grant you additional 
 *          permission to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.ta.b.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;
import org.openflexo.foundation.fml.cli.CommandInterpreter;
import org.openflexo.foundation.fml.cli.command.AbstractCommand;
import org.openflexo.foundation.fml.cli.command.FMLScript;
import org.openflexo.foundation.fml.cli.test.FMLScriptParserTestCase;
import org.openflexo.foundation.fml.rt.error.FMLExecutionFailure;
import org.openflexo.foundation.fml.rt.error.FMLNullReferenceError;
import org.openflexo.foundation.fml.rt.error.FMLTypeError;
import org.openflexo.foundation.fml.rt.error.FMLStackFrame;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.ta.b.BTechnologyAdapter;

/**
 * Failures of the B technology adapter raised as typed FML errors (CORE-F-11 phase 6, FORMOD-F-5): one script of {@code BErrorKinds}
 * per case, on the fixture {@code FML/BErrorKinds.fml}. Each failure is asserted on its type, on the frame of the behaviour and the line of
 * the failing statement; nothing is printed to stderr.
 */
public class TestBErrorKinds extends FMLScriptParserTestCase {

	private static final String MUST_FAIL = "mustFail";

	/**
	 * Run supplied script, stderr captured; return the failure of its command assigning {@code mustFail} (any other failure fails the
	 * test)
	 */
	private Throwable runScript(String name) throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
		CommandInterpreter interpreter = new CommandInterpreter(serviceManager, System.in, System.out, System.err, HOME_DIR);
		Resource fmlResource = ResourceLocator.locateResource("BErrorKinds/" + name);
		FMLScript script = parseFMLScript(fmlResource, interpreter);
		checkFMLScript(fmlResource.getRelativePath(), script);
		Throwable failure = null;
		PrintStream err = System.err;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		System.setErr(new PrintStream(captured, true));
		try {
			for (AbstractCommand command : script.getCommands()) {
				if (command.toString().trim().startsWith(MUST_FAIL)) {
					try {
						command.execute();
					} catch (Exception e) {
						failure = e;
						continue;
					}
					fail(name + ", line " + command.getLine() + ": " + command + " did not fail");
				}
				else {
					command.execute();
				}
			}
		} finally {
			System.setErr(err);
		}
		assertEquals(name + ": printed to stderr", "", captured.toString());
		return failure;
	}

	/**
	 * Assert that supplied failure is (or wraps) an FML error of supplied type, whose innermost frames are supplied ones: behaviour names
	 * and lines, innermost first
	 */
	private static FMLExecutionFailure assertError(Throwable failure, Class<?> type, Object... behavioursAndLines) {
		assertNotNull("No failure", failure);
		FMLExecutionFailure fmlFailure = FMLExecutionFailure.findFailure(failure);
		assertNotNull("Not an FML failure: " + failure, fmlFailure);
		assertEquals(String.valueOf(fmlFailure), type, fmlFailure.getClass());
		List<FMLStackFrame> frames = fmlFailure.getFMLStack().getFrames();
		assertTrue(frames.toString(), frames.size() >= behavioursAndLines.length / 2);
		for (int i = 0; i < behavioursAndLines.length / 2; i++) {
			FMLStackFrame frame = frames.get(i);
			assertEquals(frame.toString(), behavioursAndLines[2 * i], frame.getBehaviour());
			assertEquals(frame.toString(), behavioursAndLines[2 * i + 1], frame.getLine());
			assertNotNull(frame.toString(), frame.getStatement());
		}
		return fmlFailure;
	}

	@Test
	public void addSetToNullComponent() throws Exception {
		assertError(runScript("AddSetToNullComponent.fmlscript"), FMLNullReferenceError.class, "addSetToNullComponent()", 21);
	}

	@Test
	public void notAnInteger() throws Exception {
		assertError(runScript("NotAnInteger.fmlscript"), FMLTypeError.class, "notAnInteger()", 25);
	}
}
