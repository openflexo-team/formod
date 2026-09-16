/**
 * 
 * Copyright (c) 2019, Openflexo
 * 
 * This file is part of FML-parser, a component of the software infrastructure 
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

package org.openflexo.ta.b.model.parser.nodes;

import java.util.List;
import java.util.function.Supplier;

import org.openflexo.p2pp.PrettyPrintContext.Indentation;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;
import org.openflexo.ta.b.model.BAbstractConstant;
import org.openflexo.ta.b.model.BAbstractVariable;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BConcreteConstant;
import org.openflexo.ta.b.model.BConcreteVariable;
import org.openflexo.ta.b.model.BExtendsClause;
import org.openflexo.ta.b.model.BImportsClause;
import org.openflexo.ta.b.model.BIncludesClause;
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSeesClause;
import org.openflexo.ta.b.model.BSet;
import org.openflexo.ta.b.model.BUsesClause;
import org.openflexo.ta.b.model.parser.BObjectNode;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.parser.node.AMachineHeader;
import org.openflexo.ta.b.parser.node.Node;
import org.openflexo.ta.b.parser.node.PMachineHeader;

/**
 * @author sylvain
 * 
 */
public abstract class BComponentNode<N extends Node, T extends BComponent> extends BObjectNode<N, T> {

	private RawSourcePosition startPosition;
	private RawSourcePosition endPosition;

	public BComponentNode(N astNode, BSemanticsAnalyzer analyser) {
		super(astNode, analyser);
		startPosition = getRawSource().getStartPosition();
		endPosition = getRawSource().getEndPosition();
	}

	public BComponentNode(T concept, BSemanticsAnalyzer analyser) {
		super(concept, analyser);
		if (getRawSource() != null) {
			startPosition = getRawSource().getStartPosition();
			endPosition = getRawSource().getEndPosition();
		}
	}

	@Override
	public RawSourcePosition getStartPosition() {
		return startPosition;
	}

	@Override
	public RawSourcePosition getEndPosition() {
		return endPosition;
	}

	@Override
	public BComponentNode<N, T> deserialize() {
		return this;
	}

	@Override
	public final void preparePrettyPrint(boolean hasParsedVersion) {

		super.preparePrettyPrint(hasParsedVersion);

		performPrettyPrintHeader(hasParsedVersion);

		appendClauses("INCLUDES", () -> getModelObject().getIncludesClauses(), BIncludesClause.class);
		appendClauses("SEES", () -> getModelObject().getSeesClauses(), BSeesClause.class);
		appendClauses("IMPORTS", () -> getModelObject().getImportsClauses(), BImportsClause.class);
		appendClauses("EXTENDS", () -> getModelObject().getExtendsClauses(), BExtendsClause.class);
		appendClauses("USES", () -> getModelObject().getUsesClauses(), BUsesClause.class);

		appendSection(new String[] { "SETS" }, () -> getModelObject().getSets(), ";" + LINE_SEPARATOR, BSet.class);

		appendSection(new String[] { "ABSTRACT_CONSTANTS" }, () -> getModelObject().getAbstractConstants(), "," + LINE_SEPARATOR, BAbstractConstant.class);
		appendSection(new String[] { "CONCRETE_CONSTANTS", "CONSTANTS" }, () -> getModelObject().getConcreteConstants(), "," + LINE_SEPARATOR, BConcreteConstant.class);
		appendSection(new String[] { "ABSTRACT_VARIABLES", "VARIABLES" }, () -> getModelObject().getAbstractVariables(), "," + LINE_SEPARATOR, BAbstractVariable.class);
		appendSection(new String[] { "CONCRETE_VARIABLES" }, () -> getModelObject().getConcreteVariables(), "," + LINE_SEPARATOR, BConcreteVariable.class);

		appendSection(new String[] { "PROPERTIES" }, () -> getModelObject().getProperties());
		appendSection(new String[] { "INVARIANT" }, () -> getModelObject().getInvariant());

		appendSection(new String[] { "ASSERTIONS" }, () -> getModelObject().getAssertions(), ";" + LINE_SEPARATOR, BPredicate.class);

		appendSection(new String[] { "EVENTS", "OPERATIONS" }, () -> getModelObject().getOperations(), ";" + LINE_SEPARATOR, BOperation.class);

		append(staticContents(LINE_SEPARATOR, "END", LINE_SEPARATOR), findFragmentBackward("END", null, null), "END");
	}

	protected void performPrettyPrintHeader(boolean hasParsedVersion) {
		append(staticContents("", getKeyword(), LINE_SEPARATOR), getKeywordFragment(), "Keyword");
		append(dynamicContents(INDENTATION, () -> getModelObject().getName()), getComponentNameFragment(), "Name");
	}

	/**
	 * Visibility clauses (INCLUDES, SEES...) are listed on a single line
	 */
	private <C> void appendClauses(String keyword, Supplier<List<? extends C>> clauses, Class<C> clauseType) {
		RawSourceFragment keywordFragment = findKeywordFragment(null, null, keyword);
		when(() -> !clauses.get().isEmpty(), keyword).thenAppend(staticContents(LINE_SEPARATOR, keyword, LINE_SEPARATOR), keywordFragment);
		append(childrenContents(INDENTATION, ",", clauses, "", "", Indentation.DoNotIndent, clauseType), keyword + "Clauses");
	}

	/**
	 * A section introduced by a keyword, the first spelling being the canonical one
	 */
	private <C> void appendSection(String[] spellings, Supplier<List<? extends C>> items, String separator, Class<C> itemType) {
		RawSourceFragment keywordFragment = findKeywordFragment(null, null, spellings);
		when(() -> !items.get().isEmpty(), spellings[0]).thenAppend(
				staticContents(LINE_SEPARATOR, keywordText(keywordFragment, spellings[0]), LINE_SEPARATOR), keywordFragment);
		append(childrenContents("", "", items, separator, "", Indentation.Indent, itemType), spellings[0] + "Items");
	}

	private void appendSection(String[] spellings, Supplier<BPredicate> predicate) {
		RawSourceFragment keywordFragment = findKeywordFragment(null, null, spellings);
		when(() -> predicate.get() != null, spellings[0]).thenAppend(
				staticContents(LINE_SEPARATOR, keywordText(keywordFragment, spellings[0]), LINE_SEPARATOR), keywordFragment);
		append(childContents("", predicate, LINE_SEPARATOR, Indentation.Indent), spellings[0] + "Predicate");
	}

	protected RawSourceFragment getKeywordFragment() {
		return findFragmentForward(getKeyword(), null, getComponentNameFragment() != null ? getComponentNameFragment().getStartPosition() : null);
	}

	protected String getComponentName(PMachineHeader node) {
		if (node instanceof AMachineHeader) {
			return getText(((AMachineHeader) node).getName());
		}
		return null;
	}

	protected abstract String getKeyword();

	protected abstract RawSourceFragment getComponentNameFragment();
}
