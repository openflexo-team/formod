package org.openflexo.ta.b.model;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.p2pp.testing.matrix.EditCase;
import org.openflexo.p2pp.testing.matrix.EditMatrix;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.ta.b.rm.BResourceRepository;

/**
 * The edit matrix of the p2pp test harness on B: every atomic edit of every B component of the test resource centers (see
 * {@link BEditGenerator}), checked against the invariants of the harness (see {@link EditMatrix}). One test per kind of edit and position
 * ({@code add-variable/first}, {@code remove-set/only}...).
 * <p>
 * The groups that show a known defect are listed in {@code b-matrix-known-defects.txt}.
 */
@RunWith(Parameterized.class)
public class TestBEditMatrix extends AbstractBTest {

	private static BModelFactory factory;

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> generateData() throws Exception {
		instanciateTestServiceManager(BTechnologyAdapter.class);
		BTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(BTechnologyAdapter.class);
		List<EditCase> cases = new ArrayList<>();
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			BResourceRepository<?> bRepository = technologicalAdapter.getBResourceRepository(resourceCenter);
			assertNotNull(bRepository);
			for (BResource resource : bRepository.getAllResources()) {
				factory = resource.getFactory();
				String source = sourceOf(resource);
				BComponent component = BParser.parse(source, factory);
				cases.addAll(BEditGenerator.generate(resource.getName(), source, component));
			}
		}
		return EditMatrix.group(cases);
	}

	private static String sourceOf(BResource resource) throws IOException {
		try (InputStream in = ((StreamIODelegate<?>) resource.getIODelegate()).getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			// The RawSource of p2pp reads rows: the line separators of the file are not part of the text it can reproduce
			return r.lines().collect(Collectors.joining("\n"));
		}
	}

	private final String signature;
	private final List<EditCase> cases;

	public TestBEditMatrix(String signature, List<EditCase> cases) {
		this.signature = signature;
		this.cases = cases;
	}

	@Test
	public void invariantsHold() throws Exception {
		EditMatrix.run(new BTarget(factory), signature, cases,
				EditMatrix.knownDefects(TestBEditMatrix.class, "/b-matrix-known-defects.txt").get(signature));
	}
}
