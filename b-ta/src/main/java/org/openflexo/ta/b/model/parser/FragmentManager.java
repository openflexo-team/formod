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

package org.openflexo.ta.b.model.parser;

import java.util.Collection;
import java.util.logging.Logger;

import org.openflexo.p2pp.FragmentRetriever;
import org.openflexo.p2pp.RawSource;
import org.openflexo.p2pp.RawSource.RawSourceFragment;
import org.openflexo.p2pp.RawSource.RawSourcePosition;
import org.openflexo.ta.b.parser.analysis.DepthFirstAdapter;
import org.openflexo.ta.b.parser.node.Node;
import org.openflexo.ta.b.parser.node.Token;

/**
 * 
 * 
 * @author sylvain
 * 
 */
public class FragmentManager extends DepthFirstAdapter implements FragmentRetriever<Node> {

	private static final Logger logger = Logger.getLogger(FragmentManager.class.getPackage().getName());

	// Raw source as when this analyzer was last parsed
	private RawSource rawSource;

	private RawSourcePosition startPosition;
	private RawSourcePosition endPosition;
	private int rowTranslation = 0;
	private int charTranslation = 0;

	/**
	 * Creates a new FragmentManager based on supplied rawSource
	 * 
	 * We handle here an issue with B grammar that does not support comments at the beginning of the source file To overpass here,
	 * workaround is to detect preliminary comment, and then removing it from the parsed source, while not forgetting translation indexes,
	 * in order to reconsider these primilary fragments while re pretty-printing B source
	 *
	 * @param rawSource
	 * @param rowTranslation
	 * @param charTranslation
	 */
	public FragmentManager(RawSource rawSource, int rowTranslation, int charTranslation) {
		this.rawSource = rawSource;
		this.rowTranslation = rowTranslation;
		this.charTranslation = charTranslation;
	}

	public RawSource getRawSource() {
		return rawSource;
	}

	public RawSourcePosition translatePosition(RawSourcePosition pos) {
		if (rowTranslation == 0) {
			return pos;
		}
		else {
			return pos.getOuterType().makePositionAfterChar(pos.getLine() + rowTranslation,
					(pos.getLine() == rowTranslation ? pos.getPos() /*+ charTranslation*/ : pos.getPos()));
		}
	}

	@Override
	public RawSourceFragment retrieveFragment(Node node) {
		startPosition = null;
		endPosition = null;
		node.apply(this);

		if (startPosition == null || endPosition == null) {
			logger.warning("Cannot retrieve fragment for node: " + node + " of " + node.getClass());
			return null;
		}

		return getRawSource().makeFragment(translatePosition(startPosition), translatePosition(endPosition));
	}

	public RawSourceFragment getFragment(Collection<? extends Node> nodes) {
		startPosition = null;
		endPosition = null;
		for (Node node : nodes) {
			node.apply(this);
		}
		return getRawSource().makeFragment(translatePosition(startPosition), translatePosition(endPosition));
	}

	@Override
	public void defaultCase(Node node) {
		super.defaultCase(node);
		if (node instanceof Token) {
			handleToken((Token) node);
		}
	}

	private void handleToken(Token token) {

		// System.out.println("Receiving Token " + token.getLine() + ":" + token.getPos() + ":" + token.getText() + " tokenEnd=" + tokenEnd
		// + " endPosition=" + endPosition);

		RawSourcePosition tokenStart = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos());
		RawSourcePosition tokenEnd = getRawSource().makePositionBeforeChar(token.getLine(), token.getPos() + token.getText().length());

		if (startPosition == null || tokenStart.compareTo(startPosition) < 0) {
			startPosition = tokenStart;
		}
		if (endPosition == null || tokenEnd.compareTo(endPosition) > 0) {
			endPosition = tokenEnd;
		}
	}

}
