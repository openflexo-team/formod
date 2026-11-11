package org.openflexo.ta.b.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import org.openflexo.p2pp.testing.UntouchedChecks;
import org.openflexo.ta.b.AbstractBTest;
import org.openflexo.ta.b.BTechnologyAdapter;
import org.openflexo.ta.b.rm.BResource;
import org.openflexo.ta.b.rm.BResourceRepository;

/**
 * The first level of the p2pp test harness on every B component of the test resource centers: the source parses; its derived pretty-print is
 * the source, to the character; the derived and the normalized pretty-prints parse back to the same model, which normalizes to the same text.
 * <p>
 * It complements {@link TestPrettyPrintAllBComponents}, which compares the model after a round trip and never the text.
 */
@RunWith(Parameterized.class)
public class TestBPrettyPrintInvariants extends AbstractBTest {

	@Parameterized.Parameters(name = "{1}")
	public static Collection<Object[]> generateData() {
		final List<Object[]> list = new ArrayList<>();
		instanciateTestServiceManager(BTechnologyAdapter.class);
		BTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(BTechnologyAdapter.class);
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			BResourceRepository<?> bRepository = technologicalAdapter.getBResourceRepository(resourceCenter);
			assertNotNull(bRepository);
			for (BResource componentResource : bRepository.getAllResources()) {
				list.add(new Object[] { componentResource, componentResource.getName() });
			}
		}
		return list;
	}

	private final BResource resource;

	public TestBPrettyPrintInvariants(BResource resource, String name) {
		this.resource = resource;
	}

	private String sourceOf(BResource resource) throws IOException {
		try (InputStream in = ((StreamIODelegate<?>) resource.getIODelegate()).getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			// The RawSource of p2pp reads rows: the line separators of the file are not part of the text it can reproduce
			return r.lines().collect(Collectors.joining("\n"));
		}
	}

	@Test
	public void invariantsHold() throws Exception {
		String source = sourceOf(resource);
		resource.loadResourceData();
		List<String> failures = UntouchedChecks.check(new BTarget(resource.getFactory()), source);
		if (!failures.isEmpty()) {
			fail(resource.getName() + ": " + failures.size() + " invariant(s) fail\n" + String.join("\n", failures));
		}
	}
}
