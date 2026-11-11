package org.openflexo.ta.b.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openflexo.p2pp.testing.matrix.EditCase;

/**
 * Generates every atomic edit of a B component, in the language of {@link BEdits}: rename the component; for each of the lists of sets,
 * abstract constants and abstract variables, add at every index, remove each element, move each element to every other index, rename each
 * element (to a shorter and a longer name).
 */
public final class BEditGenerator {

	private static final String[] KINDS = { "set", "constant", "variable" };

	private BEditGenerator() {
	}

	public static List<EditCase> generate(String sourceName, String source, BComponent component) {
		List<EditCase> cases = new ArrayList<>();
		String componentName = component.getName();
		for (String newName : Arrays.asList("Q", "RenamedWithALongName")) {
			cases.add(new EditCase(sourceName, source, "rename-component", "-", Collections.singletonList("rename component to " + newName),
					Collections.singletonList("rename component to " + componentName)));
		}
		for (String k : KINDS) {
			List<String> names = new ArrayList<>();
			for (BNamedObject o : BEdits.kind(k).list(component)) {
				names.add(o.getName());
			}
			String gen = "Gen_" + k;
			int n = names.size();
			for (int i = 0; i <= n; i++) {
				cases.add(new EditCase(sourceName, source, "add-" + k, addPosition(n, i), Collections.singletonList("add " + k + " " + gen + " at " + i),
						Collections.singletonList("remove " + k + " " + gen)));
			}
			for (int i = 0; i < n; i++) {
				String name = names.get(i);
				cases.add(new EditCase(sourceName, source, "remove-" + k, removePosition(n, i), Collections.singletonList("remove " + k + " " + name), null));
				for (int t = 0; t < n; t++) {
					if (t != i) {
						cases.add(new EditCase(sourceName, source, "move-" + k, t == 0 ? "first" : t == n - 1 ? "last" : "middle",
								Collections.singletonList("move " + k + " " + name + " to " + t),
								Collections.singletonList("move " + k + " " + name + " to " + i)));
					}
				}
				for (String newName : Arrays.asList("Q", "RenamedWithALongName")) {
					cases.add(new EditCase(sourceName, source, "rename-" + k, "-", Collections.singletonList("rename " + k + " " + name + " to " + newName),
							Collections.singletonList("rename " + k + " " + newName + " to " + name)));
				}
			}
		}
		return cases;
	}

	private static String addPosition(int size, int index) {
		return size == 0 ? "empty" : index == 0 ? "first" : index == size ? "last" : "middle";
	}

	private static String removePosition(int size, int index) {
		return size == 1 ? "only" : index == 0 ? "first" : index == size - 1 ? "last" : "middle";
	}
}
