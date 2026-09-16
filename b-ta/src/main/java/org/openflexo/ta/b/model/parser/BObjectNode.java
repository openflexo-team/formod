/**
 * 
 * Copyright (c) 2019, Openflexo
 * 
 * This file is part of B-parser, a component of the software infrastructure 
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

package org.openflexo.ta.b.model.parser;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.openflexo.p2pp.DynamicContents;
import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.PrettyPrintContext;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;
import org.openflexo.p2pp.StaticContents;
import org.openflexo.ta.b.model.BAbstractConstant;
import org.openflexo.ta.b.model.BAbstractVariable;
import org.openflexo.ta.b.model.BConcreteConstant;
import org.openflexo.ta.b.model.BConcreteVariable;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BExtendsClause;
import org.openflexo.ta.b.model.BImportsClause;
import org.openflexo.ta.b.model.BIncludesClause;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BPrettyPrintDelegate;
import org.openflexo.ta.b.model.BSeesClause;
import org.openflexo.ta.b.model.BSet;
import org.openflexo.ta.b.model.BSetValue;
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.BUsesClause;
import org.openflexo.ta.b.model.parser.nodes.BAbstractConstantNode;
import org.openflexo.ta.b.model.parser.nodes.BAbstractVariableNode;
import org.openflexo.ta.b.model.parser.nodes.BConcreteConstantNode;
import org.openflexo.ta.b.model.parser.nodes.BConcreteVariableNode;
import org.openflexo.ta.b.model.parser.nodes.BExtendsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BImportsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BIncludesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BSeesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BSetNode;
import org.openflexo.ta.b.model.parser.nodes.BSetValueNode;
import org.openflexo.ta.b.model.parser.nodes.BUsesClauseNode;
import org.openflexo.ta.b.parser.node.Node;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;
import org.openflexo.ta.b.parser.node.TIntegerLiteral;
import org.openflexo.ta.b.parser.node.Token;
import org.openflexo.toolbox.ChainedCollection;
import org.openflexo.toolbox.StringUtils;

/**
 * Maintains consistency between the model (represented by an {@link BObject}) and source code represented in B language
 * 
 * Works
 * 
 * @author sylvain
 * 
 */
public abstract class BObjectNode<N extends Node, T extends BObject> extends P2PPNode<N, T> implements BPrettyPrintDelegate<T> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BObjectNode.class.getPackage().getName());

	private final BSemanticsAnalyzer analyser;

	protected static final String INDENTATION = "\t";

	public BObjectNode(N astNode, BSemanticsAnalyzer analyser) {
		super(null, astNode, analyser.getFragmentManager());
		this.analyser = analyser;

		modelObject = buildModelObjectFromAST(astNode);
		modelObject.setPrettyPrintDelegate(this);
		modelObject.initializeDeserialization(getModelFactory());
		/*if (getClass().equals(BIncludesClauseNode.class)) {
			System.out.println("Pour " + getClass().getSimpleName() + " astNode=" + astNode + " of " + astNode.getClass().getSimpleName());
			System.out.println("parsedFragment=" + getLastParsedFragment());
		}*/
	}

	public BObjectNode(T aBObject, BSemanticsAnalyzer analyser) {
		super(aBObject, null, null);
		this.analyser = analyser;

		modelObject.setPrettyPrintDelegate(this);
		preparePrettyPrint(false);
	}

	public BModelFactory getModelFactory() {
		return analyser.getModelFactory();
	}

	public BSemanticsAnalyzer getAnalyser() {
		return analyser;
	}

	public PredicateFactory getPredicateFactory() {
		return analyser.getPredicateFactory();
	}

	public ExpressionFactory getExpressionFactory() {
		return analyser.getExpressionFactory();
	}

	public OperationFactory getOperationFactory() {
		return analyser.getOperationFactory();
	}

	public SubstitutionFactory getSubstitutionFactory() {
		return analyser.getSubstitutionFactory();
	}

	// Make this method visible
	@Override
	public void addToChildren(P2PPNode<?, ?> child) {
		super.addToChildren(child);
	}

	// Make this method visible
	@Override
	public void preparePrettyPrint(boolean hasParsedVersion) {
		super.preparePrettyPrint(hasParsedVersion);
	}

	/*protected void handleToken(Token token) {
	
		// System.out.println("Receiving Token " + token.getLine() + ":" + token.getPos() + ":" + token.getText() + " tokenEnd=" + tokenEnd
		// + " endPosition=" + endPosition);
	
		RawSourcePosition tokenStart = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos());
		RawSourcePosition tokenEnd = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length());
	
		if (startPosition == null || tokenStart.compareTo(startPosition) < 0) {
			startPosition = tokenStart;
			parsedFragment = null;
		}
		if (endPosition == null || tokenEnd.compareTo(endPosition) > 0) {
			endPosition = tokenEnd;
			parsedFragment = null;
		}
	
		if (getParent() instanceof BObjectNode) {
			((BObjectNode<?, ?>) getParent()).handleToken(token);
		}
	}*/

	/**
	 * Return original version of last serialized raw source, FOR THE ENTIRE compilation unit
	 * 
	 * @return
	 */
	@Override
	public RawSource getRawSource() {
		if (analyser != null) {
			return analyser.getRawSource();
		}
		return null;
	}

	@Override
	public String getRepresentation(PrettyPrintContext context) {
		return getTextualRepresentation(context);
	}

	@Override
	public String getNormalizedRepresentation(PrettyPrintContext context) {
		return getNormalizedTextualRepresentation(context);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <C> P2PPNode<?, C> makeObjectNode(C object) {
		if (object instanceof BExtendsClause) {
			return (P2PPNode<?, C>) new BExtendsClauseNode((BExtendsClause) object, getAnalyser());
		}
		if (object instanceof BImportsClause) {
			return (P2PPNode<?, C>) new BImportsClauseNode((BImportsClause) object, getAnalyser());
		}
		if (object instanceof BIncludesClause) {
			return (P2PPNode<?, C>) new BIncludesClauseNode((BIncludesClause) object, getAnalyser());
		}
		if (object instanceof BSeesClause) {
			return (P2PPNode<?, C>) new BSeesClauseNode((BSeesClause) object, getAnalyser());
		}
		if (object instanceof BUsesClause) {
			return (P2PPNode<?, C>) new BUsesClauseNode((BUsesClause) object, getAnalyser());
		}
		if (object instanceof BSet) {
			return (P2PPNode<?, C>) new BSetNode((BSet) object, getAnalyser());
		}
		if (object instanceof BSetValue) {
			return (P2PPNode<?, C>) new BSetValueNode((BSetValue) object, getAnalyser());
		}
		if (object instanceof BAbstractConstant) {
			return (P2PPNode<?, C>) new BAbstractConstantNode((BAbstractConstant) object, getAnalyser());
		}
		if (object instanceof BConcreteConstant) {
			return (P2PPNode<?, C>) new BConcreteConstantNode((BConcreteConstant) object, getAnalyser());
		}
		if (object instanceof BAbstractVariable) {
			return (P2PPNode<?, C>) new BAbstractVariableNode((BAbstractVariable) object, getAnalyser());
		}
		if (object instanceof BConcreteVariable) {
			return (P2PPNode<?, C>) new BConcreteVariableNode((BConcreteVariable) object, getAnalyser());
		}

		if (object instanceof BExpression) {
			return (P2PPNode<?, C>) getExpressionFactory().makeExpressionNode((BExpression) object);
		}
		if (object instanceof BPredicate) {
			return (P2PPNode<?, C>) getPredicateFactory().makePredicateNode((BPredicate) object);
		}
		if (object instanceof BSubstitution) {
			return (P2PPNode<?, C>) getSubstitutionFactory().makeSubstitutionNode((BSubstitution) object);
		}
		if (object instanceof BOperation) {
			return (P2PPNode<?, C>) getOperationFactory().makeOperationNode((BOperation) object);
		}

		System.err.println("Not supported: " + object);
		Thread.dumpStack();
		return null;
	}

	/**
	 * Return fragment matching supplied node in AST
	 * 
	 * @param token
	 * @return
	 */
	public RawSourceFragment getFragment(Node node) {
		if (node instanceof Token) {
			Token token = (Token) node;
			RawSourcePosition start = getAnalyser().getFragmentManager()
					.translatePosition(getRawSource().makePositionBeforeChar(token.getLine(), token.getPos()));
			RawSourcePosition end = getAnalyser().getFragmentManager()
					.translatePosition(getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length()));
			return getRawSource().makeFragment(start, end);
		}
		else {
			return getAnalyser().getFragmentManager().retrieveFragment(node);
		}
	}

	/**
	 * Return fragment matching supplied nodes in AST
	 * 
	 * @param token
	 * @return
	 */
	// @Override
	public RawSourceFragment getFragment(Node node, List<? extends Node> otherNodes) {
		ChainedCollection<Node> collection = new ChainedCollection<>();
		collection.add(node);
		collection.add(otherNodes);
		return getAnalyser().getFragmentManager().getFragment(collection);
	}

	protected RawSourceFragment getFragment(List<TIdentifierLiteral> idLitList) {
		return getAnalyser().getFragmentManager().getFragment(idLitList);
	}

	protected String getText(List<TIdentifierLiteral> idLitList) {
		return getAnalyser().getText(idLitList);
	}

	protected String getText(PExpression expression) {
		return getAnalyser().getText(expression);
	}

	protected Integer getInteger(TIntegerLiteral literal) {
		return getAnalyser().getInteger(literal);
	}

	/**
	 * Return the fragment of the first occurrence, among supplied spellings, of a keyword in the parsed source
	 * 
	 * Some B keywords have synonyms (CONSTANTS for CONCRETE_CONSTANTS, OPERATIONS for EVENTS...)
	 * 
	 * @return the fragment of the earliest spelling found, or null
	 */
	protected RawSourceFragment findKeywordFragment(RawSourcePosition from, RawSourcePosition to, String... spellings) {
		RawSourceFragment returned = null;
		for (String spelling : spellings) {
			RawSourceFragment f = findFragmentForward(spelling, from, to);
			if (f != null && (returned == null || f.getStartPosition().isBefore(returned.getStartPosition()))) {
				returned = f;
			}
		}
		return returned;
	}

	/**
	 * Return the fragment of a keyword leading this node ("SELECT", "card"...), searched in the parsed text of this node
	 */
	protected RawSourceFragment leadingKeywordFragment(String keyword) {
		return findFragmentForward(keyword, null, null);
	}

	/**
	 * Return the fragment of a keyword closing this node ("END", "~"...), searched in the parsed text of this node
	 */
	protected RawSourceFragment trailingKeywordFragment(String keyword) {
		return findFragmentBackward(keyword, null, null);
	}

	/**
	 * Return the fragment of an operator located between the parsed texts of two children of this node
	 */
	protected RawSourceFragment operatorFragment(String operator, BObject left, BObject right) {
		RawSourceFragment leftFragment = parsedFragmentOf(left);
		RawSourceFragment rightFragment = parsedFragmentOf(right);
		return findFragmentForward(operator, leftFragment != null ? leftFragment.getEndPosition() : null,
				rightFragment != null ? rightFragment.getStartPosition() : null);
	}

	/**
	 * Return the parsed fragment of the node representing supplied object, if any
	 */
	protected static RawSourceFragment parsedFragmentOf(BObject object) {
		if (object != null && object.getPrettyPrintDelegate() instanceof P2PPNode) {
			return ((P2PPNode<?, ?>) object.getPrettyPrintDelegate()).getLastParsedFragment();
		}
		return null;
	}

	/**
	 * Texts starting this node in the B syntax but kept out of its abstract syntax tree, such as "card(" in card(x), listed from the
	 * farthest to the nearest to the parsed tokens: { "card", "(" }
	 *
	 * @see #completeParsedFragment()
	 */
	protected String[] getLeadingTextsOutOfAST() {
		return new String[0];
	}

	/**
	 * Texts ending this node in the B syntax but kept out of its abstract syntax tree, such as the END of an ANY substitution, listed from
	 * the nearest to the farthest to the parsed tokens
	 *
	 * @see #completeParsedFragment()
	 */
	protected String[] getTrailingTextsOutOfAST() {
		return new String[0];
	}

	/**
	 * Called once the whole tree is built, children first: extends the parsed fragment of this node so that it covers the whole text it
	 * pretty-prints.
	 *
	 * The fragment of a node is computed from the tokens of its abstract syntax tree, which may miss some trailing keywords, and even
	 * some tokens of its children. A pretty-printed contents lying out of the fragment of its node is inserted instead of replaced.
	 */
	public void completeParsedFragment() {
		if (getASTNode() == null || getStartPosition() == null || getEndPosition() == null) {
			return;
		}
		for (P2PPNode<?, ?> child : getChildren()) {
			if (child.getStartPosition() != null && child.getStartPosition().isBefore(getStartPosition())) {
				setStartPosition(child.getStartPosition());
			}
			if (child.getEndPosition() != null && child.getEndPosition().isAfter(getEndPosition())) {
				setEndPosition(child.getEndPosition());
			}
		}
		String[] leadingTexts = getLeadingTextsOutOfAST();
		for (int i = leadingTexts.length - 1; i >= 0; i--) {
			RawSourceFragment f = findAdjacentTextBefore(leadingTexts[i], getStartPosition());
			if (f == null) {
				break;
			}
			setStartPosition(f.getStartPosition());
		}
		for (String text : getTrailingTextsOutOfAST()) {
			RawSourceFragment f = findAdjacentTextAfter(text, getEndPosition());
			if (f == null) {
				break;
			}
			setEndPosition(f.getEndPosition());
		}
	}

	/**
	 * Return the fragment of supplied text when it precedes supplied position, separated only by whitespace and comments
	 */
	private RawSourceFragment findAdjacentTextBefore(String text, RawSourcePosition position) {
		RawSource rawSource = getRawSource();
		RawSourcePosition start = rawSource.getStartPosition();
		if (!start.isBefore(position)) {
			return null;
		}
		String preceding = maskComments(rawSource.makeFragment(start, position).getRawText());
		int index = preceding.length();
		while (index > 0 && Character.isWhitespace(preceding.charAt(index - 1))) {
			index--;
		}
		int textStartIndex = index - text.length();
		if (textStartIndex >= 0 && preceding.startsWith(text, textStartIndex) && isDelimited(preceding, textStartIndex, text)) {
			RawSourcePosition textStart = start.increment(textStartIndex);
			return rawSource.makeFragment(textStart, textStart.increment(text.length()));
		}
		return null;
	}

	/**
	 * Return the fragment of supplied text when it follows supplied position, separated only by whitespace and comments
	 */
	private RawSourceFragment findAdjacentTextAfter(String text, RawSourcePosition position) {
		RawSource rawSource = getRawSource();
		RawSourcePosition end = rawSource.getEndPosition();
		if (!position.isBefore(end)) {
			return null;
		}
		String following = maskComments(rawSource.makeFragment(position, end).getRawText());
		int index = 0;
		while (index < following.length() && Character.isWhitespace(following.charAt(index))) {
			index++;
		}
		if (following.startsWith(text, index) && isDelimited(following, index, text)) {
			RawSourcePosition textStart = position.increment(index);
			return rawSource.makeFragment(textStart, textStart.increment(text.length()));
		}
		return null;
	}

	/**
	 * Keyword as written in parsed source when found, canonical spelling otherwise
	 */
	protected static String keywordText(RawSourceFragment fragment, String canonical) {
		return fragment != null ? fragment.getRawText() : canonical;
	}

	// B sources use free layout. P2PP looks for a prelude or postlude by an exact match of the canonical separator, and inserts that
	// separator again when it is not found. For a whitespace-only separator, the whitespace actually surrounding the parsed contents, even
	// none, is its parsed prelude or postlude.

	@Override
	public StaticContents<N, T> staticContents(String staticContents) {
		return staticContents(null, staticContents, null);
	}

	@Override
	public StaticContents<N, T> staticContents(String prelude, String staticContents, String postlude) {
		return new StaticContents<N, T>(this, prelude, staticContents, postlude, null) {
			@Override
			public RawSourceFragment getPreludeFragment() {
				RawSourceFragment returned = super.getPreludeFragment();
				return returned != null ? returned : whitespaceBefore(getPrelude(), getFragment());
			}

			@Override
			public RawSourceFragment getPostludeFragment() {
				RawSourceFragment returned = super.getPostludeFragment();
				return returned != null ? returned : whitespaceAfter(getPostlude(), getFragment());
			}
		};
	}

	@Override
	public DynamicContents<N, T> dynamicContents(Supplier<String> stringRepresentationSupplier) {
		return dynamicContents(null, stringRepresentationSupplier, null);
	}

	@Override
	public DynamicContents<N, T> dynamicContents(String prelude, Supplier<String> stringRepresentationSupplier) {
		return dynamicContents(prelude, stringRepresentationSupplier, null);
	}

	@Override
	public DynamicContents<N, T> dynamicContents(Supplier<String> stringRepresentationSupplier, String postlude) {
		return dynamicContents(null, stringRepresentationSupplier, postlude);
	}

	@Override
	public DynamicContents<N, T> dynamicContents(String prelude, Supplier<String> stringRepresentationSupplier, String postlude) {
		return new DynamicContents<N, T>(this, prelude, stringRepresentationSupplier, postlude, null) {
			@Override
			public RawSourceFragment getPreludeFragment() {
				RawSourceFragment returned = super.getPreludeFragment();
				return returned != null ? returned : whitespaceBefore(getPrelude(), getFragment());
			}

			@Override
			public RawSourceFragment getPostludeFragment() {
				RawSourceFragment returned = super.getPostludeFragment();
				return returned != null ? returned : whitespaceAfter(getPostlude(), getFragment());
			}
		};
	}

	private static boolean isWhitespace(String separator) {
		return StringUtils.isNotEmpty(separator) && separator.trim().isEmpty();
	}

	// The returned fragment may be empty: parsed contents not separated by any whitespace keep that layout
	private static RawSourceFragment whitespaceBefore(String prelude, RawSourceFragment fragment) {
		if (!isWhitespace(prelude) || fragment == null || fragment.getLength() == 0) {
			return null;
		}
		RawSourcePosition end = fragment.getStartPosition();
		RawSourcePosition start = end;
		while (start.canDecrement()) {
			RawSourcePosition previous = start.decrement();
			Character c = previous.getCharAfter();
			// A null char is a line end
			if (c != null && !Character.isWhitespace(c)) {
				break;
			}
			start = previous;
		}
		return fragment.getRawSource().makeFragment(start, end);
	}

	private static RawSourceFragment whitespaceAfter(String postlude, RawSourceFragment fragment) {
		if (!isWhitespace(postlude) || fragment == null || fragment.getLength() == 0) {
			return null;
		}
		RawSourcePosition start = fragment.getEndPosition();
		RawSourcePosition end = start;
		while (end.canIncrement()) {
			Character c = end.getCharAfter();
			if (c != null && !Character.isWhitespace(c)) {
				break;
			}
			end = end.increment();
		}
		return fragment.getRawSource().makeFragment(start, end);
	}

	/**
	 * Return the fragment of the first occurrence of supplied keyword or operator in the parsed source, searched between two positions.
	 *
	 * The B grammar does not keep keywords and operators as tokens in the abstract syntax tree, so their fragments have to be located in
	 * the raw source. Without a fragment, a static contents is inserted in the parsed text instead of replacing it, duplicating it.
	 *
	 * @param text
	 *            the keyword or operator to find
	 * @param from
	 *            position where the search starts (inclusive), node start when null
	 * @param to
	 *            position where the search ends (exclusive), node end when null
	 * @return the fragment, or null when this node was not parsed or the text was not found
	 */
	protected RawSourceFragment findFragmentForward(String text, RawSourcePosition from, RawSourcePosition to) {
		return findFragment(text, from, to, false);
	}

	/**
	 * Return the fragment of the last occurrence of supplied keyword or operator in the parsed source, searched between two positions.
	 *
	 * @see #findFragmentForward(String, RawSourcePosition, RawSourcePosition)
	 */
	protected RawSourceFragment findFragmentBackward(String text, RawSourcePosition from, RawSourcePosition to) {
		return findFragment(text, from, to, true);
	}

	private RawSourceFragment findFragment(String text, RawSourcePosition from, RawSourcePosition to, boolean backward) {
		if (getASTNode() == null || getRawSource() == null) {
			return null;
		}
		RawSourcePosition start = from != null ? from : getStartPosition();
		RawSourcePosition end = to != null ? to : getEndPosition();
		if (start == null || end == null || !start.isBefore(end)) {
			return null;
		}
		String searched = maskComments(getRawSource().makeFragment(start, end).getRawText());
		if (searched == null) {
			return null;
		}
		int index = backward ? searched.lastIndexOf(text) : searched.indexOf(text);
		while (index > -1) {
			if (isDelimited(searched, index, text)) {
				RawSourcePosition textStart = start.increment(index);
				return getRawSource().makeFragment(textStart, textStart.increment(text.length()));
			}
			index = backward ? searched.lastIndexOf(text, index - 1) : searched.indexOf(text, index + 1);
		}
		return null;
	}

	/**
	 * A keyword must not be part of an identifier: "id" is not found in "idle"
	 */
	private static boolean isDelimited(String searched, int index, String text) {
		if (isIdentifierChar(text.charAt(0)) && index > 0 && isIdentifierChar(searched.charAt(index - 1))) {
			return false;
		}
		int after = index + text.length();
		if (isIdentifierChar(text.charAt(text.length() - 1)) && after < searched.length() && isIdentifierChar(searched.charAt(after))) {
			return false;
		}
		return true;
	}

	private static boolean isIdentifierChar(char c) {
		return Character.isLetterOrDigit(c) || c == '_';
	}

	/**
	 * Replace the contents of B comments (block and line comments) by spaces, preserving offsets, so that a keyword is never found inside
	 * a comment
	 */
	private static String maskComments(String text) {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(text);
		int i = 0;
		while (i < sb.length() - 1) {
			String opening = sb.substring(i, i + 2);
			if (opening.equals("/*") || opening.equals("//")) {
				String closing = opening.equals("/*") ? "*/" : "\n";
				int commentEnd = sb.indexOf(closing, i + 2);
				int maskEnd = commentEnd > -1 ? (opening.equals("/*") ? commentEnd + 2 : commentEnd) : sb.length();
				for (int j = i; j < maskEnd; j++) {
					if (sb.charAt(j) != '\n') {
						sb.setCharAt(j, ' ');
					}
				}
				i = maskEnd;
			}
			else {
				i++;
			}
		}
		return sb.toString();
	}

}
