package org.openflexo.ta.b.model;

import java.io.IOException;

import org.openflexo.ta.b.model.parser.BParser;
import org.openflexo.ta.b.model.parser.ParseException;
import org.openflexo.p2pp.testing.PrettyPrintTarget;

/**
 * B as a language of the p2pp test harness ({@code p2pp-test}): a source is parsed by {@link BParser} with the model factory of a loaded
 * resource.
 * <p>
 * The model of a component is compared through its normalized pretty-print, which is computed from the model alone: two components are the
 * same when that text is. The edits are those of {@link BEdits}.
 */
public class BTarget implements PrettyPrintTarget {

	private final BModelFactory factory;

	public BTarget(BModelFactory factory) {
		this.factory = factory;
	}

	@Override
	public Document parse(String source) throws ParseException, IOException {
		BComponent component = BParser.parse(source, factory);
		return new Document() {
			@Override
			public String dump() {
				return component.getNormalizedBRepresentation();
			}

			@Override
			public String derived() {
				return component.getBPrettyPrint();
			}

			@Override
			public String normalized() {
				return component.getNormalizedBRepresentation();
			}

			@Override
			public void apply(String command) {
				BEdits.apply(component, factory, command);
			}
		};
	}
}
