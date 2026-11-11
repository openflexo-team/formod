package org.openflexo.ta.b.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The language of edits of B for the p2pp test harness: changes of the model of a component, applied by {@link BTarget}. It covers the
 * name of the component and three lists of it, which exercise a conditional section keyword (absent when the list is empty), and the
 * separators of a list of children:
 *
 * <pre>
 * rename component to NAME
 * add set|constant|variable NAME [at N]       (abstract constants and variables)
 * remove set|constant|variable NAME
 * rename set|constant|variable NAME to NEW
 * move set|constant|variable NAME to N        (N: index in the list)
 * </pre>
 *
 * Indexes start at 0; without {@code at}, the element is added last.
 */
public final class BEdits {

	private static final String ID = "([A-Za-z_][A-Za-z0-9_]*)";
	private static final String KIND = "(set|constant|variable)";

	private static final Pattern RENAME_COMPONENT = Pattern.compile("rename component to ([A-Za-z_][A-Za-z0-9_.]*)");
	private static final Pattern ADD = Pattern.compile("add " + KIND + " " + ID + "(?: at (\\d+))?");
	private static final Pattern REMOVE = Pattern.compile("remove " + KIND + " " + ID);
	private static final Pattern RENAME = Pattern.compile("rename " + KIND + " " + ID + " to " + ID);
	private static final Pattern MOVE = Pattern.compile("move " + KIND + " " + ID + " to (\\d+)");

	private BEdits() {
	}

	/** One of the lists of a component */
	interface Kind {
		List<? extends BNamedObject> list(BComponent c);

		BNamedObject make(BModelFactory f, String name);

		void add(BComponent c, BNamedObject o);

		void remove(BComponent c, BNamedObject o);

		void move(BComponent c, BNamedObject o, int index);
	}

	static Kind kind(String name) {
		switch (name) {
			case "set":
				return new Kind() {
					@Override
					public List<? extends BNamedObject> list(BComponent c) {
						return c.getSets();
					}

					@Override
					public BNamedObject make(BModelFactory f, String n) {
						return f.makeSet(n);
					}

					@Override
					public void add(BComponent c, BNamedObject o) {
						c.addToSets((BSet) o);
					}

					@Override
					public void remove(BComponent c, BNamedObject o) {
						c.removeFromSets((BSet) o);
					}

					@Override
					public void move(BComponent c, BNamedObject o, int index) {
						c.moveSetToIndex((BSet) o, index);
					}
				};
			case "constant":
				return new Kind() {
					@Override
					public List<? extends BNamedObject> list(BComponent c) {
						return c.getAbstractConstants();
					}

					@Override
					public BNamedObject make(BModelFactory f, String n) {
						return f.makeAbstractConstant(n);
					}

					@Override
					public void add(BComponent c, BNamedObject o) {
						c.addToAbstractConstants((BAbstractConstant) o);
					}

					@Override
					public void remove(BComponent c, BNamedObject o) {
						c.removeFromAbstractConstants((BAbstractConstant) o);
					}

					@Override
					public void move(BComponent c, BNamedObject o, int index) {
						c.moveAbstractConstantToIndex((BAbstractConstant) o, index);
					}
				};
			default:
				return new Kind() {
					@Override
					public List<? extends BNamedObject> list(BComponent c) {
						return c.getAbstractVariables();
					}

					@Override
					public BNamedObject make(BModelFactory f, String n) {
						return f.makeAbstractVariable(n);
					}

					@Override
					public void add(BComponent c, BNamedObject o) {
						c.addToAbstractVariables((BAbstractVariable) o);
					}

					@Override
					public void remove(BComponent c, BNamedObject o) {
						c.removeFromAbstractVariables((BAbstractVariable) o);
					}

					@Override
					public void move(BComponent c, BNamedObject o, int index) {
						c.moveAbstractVariableToIndex((BAbstractVariable) o, index);
					}
				};
		}
	}

	private static BNamedObject find(Kind kind, BComponent c, String name) {
		for (BNamedObject o : kind.list(c)) {
			if (name.equals(o.getName())) {
				return o;
			}
		}
		throw new IllegalArgumentException("No '" + name + "' in the component");
	}

	public static void apply(BComponent component, BModelFactory factory, String command) {
		Matcher m;
		if ((m = RENAME_COMPONENT.matcher(command)).matches()) {
			component.setName(m.group(1));
		}
		else if ((m = ADD.matcher(command)).matches()) {
			Kind kind = kind(m.group(1));
			BNamedObject o = kind.make(factory, m.group(2));
			kind.add(component, o);
			if (m.group(3) != null) {
				kind.move(component, o, Integer.parseInt(m.group(3)));
			}
		}
		else if ((m = REMOVE.matcher(command)).matches()) {
			Kind kind = kind(m.group(1));
			kind.remove(component, find(kind, component, m.group(2)));
		}
		else if ((m = RENAME.matcher(command)).matches()) {
			find(kind(m.group(1)), component, m.group(2)).setName(m.group(3));
		}
		else if ((m = MOVE.matcher(command)).matches()) {
			Kind kind = kind(m.group(1));
			kind.move(component, find(kind, component, m.group(2)), Integer.parseInt(m.group(3)));
		}
		else {
			throw new IllegalArgumentException("Unknown edit: " + command);
		}
	}
}
