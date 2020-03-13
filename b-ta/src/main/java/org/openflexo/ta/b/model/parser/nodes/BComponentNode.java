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

		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "INCLUDES" + LINE_SEPARATOR + INDENTATION, ",",
				() -> getModelObject().getIncludesClauses(), "", "", Indentation.DoNotIndent, BIncludesClause.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "SEES" + LINE_SEPARATOR + INDENTATION, ",",
				() -> getModelObject().getSeesClauses(), "", "", Indentation.DoNotIndent, BSeesClause.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "IMPORTS" + LINE_SEPARATOR + INDENTATION, ",",
				() -> getModelObject().getImportsClauses(), "", "", Indentation.DoNotIndent, BImportsClause.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "EXTENDS" + LINE_SEPARATOR + INDENTATION, ",",
				() -> getModelObject().getExtendsClauses(), "", "", Indentation.DoNotIndent, BExtendsClause.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "USES" + LINE_SEPARATOR + INDENTATION, ",",
				() -> getModelObject().getUsesClauses(), "", "", Indentation.DoNotIndent, BUsesClause.class);

		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "SETS" + LINE_SEPARATOR, ";" + LINE_SEPARATOR,
				() -> getModelObject().getSets(), "", "", Indentation.Indent, BSet.class);

		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "ABSTRACT_CONSTANTS" + LINE_SEPARATOR, "," + LINE_SEPARATOR,
				() -> getModelObject().getAbstractConstants(), "", "", Indentation.Indent, BAbstractConstant.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "CONCRETE_CONSTANTS" + LINE_SEPARATOR, "," + LINE_SEPARATOR,
				() -> getModelObject().getConcreteConstants(), "", "", Indentation.Indent, BConcreteConstant.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "ABSTRACT_VARIABLES" + LINE_SEPARATOR, "," + LINE_SEPARATOR,
				() -> getModelObject().getAbstractVariables(), "", "", Indentation.Indent, BAbstractVariable.class);
		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "CONCRETE_VARIABLES" + LINE_SEPARATOR, "," + LINE_SEPARATOR,
				() -> getModelObject().getConcreteVariables(), "", "", Indentation.Indent, BConcreteVariable.class);

		appendToChildPrettyPrintContents(LINE_SEPARATOR + "PROPERTIES" + LINE_SEPARATOR, () -> getModelObject().getProperties(),
				LINE_SEPARATOR, Indentation.Indent);
		appendToChildPrettyPrintContents(LINE_SEPARATOR + "INVARIANT" + LINE_SEPARATOR, () -> getModelObject().getInvariant(),
				LINE_SEPARATOR, Indentation.Indent);

		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "ASSERTIONS" + LINE_SEPARATOR, ";" + LINE_SEPARATOR,
				() -> getModelObject().getAssertions(), "", "", Indentation.Indent, BPredicate.class);

		appendToChildrenPrettyPrintContents(LINE_SEPARATOR + "EVENTS" + LINE_SEPARATOR, ";" + LINE_SEPARATOR,
				() -> getModelObject().getOperations(), "", "", Indentation.Indent, BOperation.class);

		appendStaticContents(LINE_SEPARATOR, "END", LINE_SEPARATOR, null); // TODO: match END fragment
	}

	protected void performPrettyPrintHeader(boolean hasParsedVersion) {
		appendStaticContents("", getKeyword(), LINE_SEPARATOR, null);// TODO: match fragment
		appendDynamicContents(INDENTATION, () -> getModelObject().getName(), getComponentNameFragment());
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
