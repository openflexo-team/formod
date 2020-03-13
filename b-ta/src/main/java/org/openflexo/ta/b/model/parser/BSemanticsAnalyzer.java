/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Connie-core, a component of the software infrastructure 
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
import java.util.Stack;
import java.util.logging.Logger;

import org.openflexo.p2pp.P2PPNode;
import org.openflexo.p2pp.RawSource;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.model.BExpression;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.BObject;
import org.openflexo.ta.b.model.BOperation;
import org.openflexo.ta.b.model.BPredicate;
import org.openflexo.ta.b.model.BSubstitution;
import org.openflexo.ta.b.model.parser.nodes.BAbstractConstantNode;
import org.openflexo.ta.b.model.parser.nodes.BAbstractVariableNode;
import org.openflexo.ta.b.model.parser.nodes.BConcreteConstantNode;
import org.openflexo.ta.b.model.parser.nodes.BConcreteVariableNode;
import org.openflexo.ta.b.model.parser.nodes.BExpressionNode;
import org.openflexo.ta.b.model.parser.nodes.BExtendsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BImplementationNode;
import org.openflexo.ta.b.model.parser.nodes.BImportsClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BIncludesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BMachineNode;
import org.openflexo.ta.b.model.parser.nodes.BOperationNode;
import org.openflexo.ta.b.model.parser.nodes.BPredicateNode;
import org.openflexo.ta.b.model.parser.nodes.BRefinementNode;
import org.openflexo.ta.b.model.parser.nodes.BSeesClauseNode;
import org.openflexo.ta.b.model.parser.nodes.BSetNode;
import org.openflexo.ta.b.model.parser.nodes.BSetValueNode;
import org.openflexo.ta.b.model.parser.nodes.BSubstitutionNode;
import org.openflexo.ta.b.model.parser.nodes.BSystemNode;
import org.openflexo.ta.b.model.parser.nodes.BUsesClauseNode;
import org.openflexo.ta.b.parser.analysis.DepthFirstAdapter;
import org.openflexo.ta.b.parser.node.AAbstractConstantsMachineClause;
import org.openflexo.ta.b.parser.node.AAbstractMachineParseUnit;
import org.openflexo.ta.b.parser.node.AAssertionsMachineClause;
import org.openflexo.ta.b.parser.node.AConcreteVariablesMachineClause;
import org.openflexo.ta.b.parser.node.AConstantsMachineClause;
import org.openflexo.ta.b.parser.node.ADeferredSetSet;
import org.openflexo.ta.b.parser.node.AEnumeratedSetSet;
import org.openflexo.ta.b.parser.node.AExtendsMachineClause;
import org.openflexo.ta.b.parser.node.AIdentifierExpression;
import org.openflexo.ta.b.parser.node.AImplementationMachineParseUnit;
import org.openflexo.ta.b.parser.node.AImportsMachineClause;
import org.openflexo.ta.b.parser.node.AIncludesMachineClause;
import org.openflexo.ta.b.parser.node.AInitialisationMachineClause;
import org.openflexo.ta.b.parser.node.AInvariantMachineClause;
import org.openflexo.ta.b.parser.node.AMachineMachineVariant;
import org.openflexo.ta.b.parser.node.AMachineReference;
import org.openflexo.ta.b.parser.node.AOperationsMachineClause;
import org.openflexo.ta.b.parser.node.APropertiesMachineClause;
import org.openflexo.ta.b.parser.node.ARefinementMachineParseUnit;
import org.openflexo.ta.b.parser.node.ASeesMachineClause;
import org.openflexo.ta.b.parser.node.ASystemMachineVariant;
import org.openflexo.ta.b.parser.node.AUsesMachineClause;
import org.openflexo.ta.b.parser.node.AVariablesMachineClause;
import org.openflexo.ta.b.parser.node.Node;
import org.openflexo.ta.b.parser.node.PExpression;
import org.openflexo.ta.b.parser.node.PMachineVariant;
import org.openflexo.ta.b.parser.node.POperation;
import org.openflexo.ta.b.parser.node.PPredicate;
import org.openflexo.ta.b.parser.node.PSubstitution;
import org.openflexo.ta.b.parser.node.Start;
import org.openflexo.ta.b.parser.node.TIdentifierLiteral;
import org.openflexo.ta.b.parser.node.TIntegerLiteral;

/**
 * This class implements the semantics analyzer for a parsed B resource.<br>
 * 
 * @author sylvain
 * 
 */
public class BSemanticsAnalyzer extends DepthFirstAdapter {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BSemanticsAnalyzer.class.getPackage().getName());

	private final BModelFactory modelFactory;

	private final PredicateFactory predicateFactory;
	private final ExpressionFactory expressionFactory;
	private final OperationFactory operationFactory;
	private final SubstitutionFactory substitutionFactory;

	// Stack of BObjectNode beeing build during semantics analyzing
	protected Stack<BObjectNode<?, ?>> bNodes = new Stack<>();

	private FragmentManager fragmentManager;

	// Raw source as when this analyzer was last parsed
	private RawSource rawSource;

	private BObjectNode<?, ?> rootNode;

	/**
	 * Create a new B semantics analyzer, asserting that no initial raw source is found
	 * 
	 * @param modelFactory
	 * @param tree
	 * @param rawSource
	 */
	public BSemanticsAnalyzer(BModelFactory modelFactory) {
		this.modelFactory = modelFactory;
		predicateFactory = new PredicateFactory(modelFactory, this);
		expressionFactory = new ExpressionFactory(modelFactory, this);
		operationFactory = new OperationFactory(modelFactory, this);
		substitutionFactory = new SubstitutionFactory(modelFactory, this);
	}

	/**
	 * Create a new B semantics analyzer, asserting an initial raw source has just been parsed
	 * 
	 * @param modelFactory
	 * @param tree
	 * @param rawSource
	 */
	public BSemanticsAnalyzer(BModelFactory modelFactory, Start tree, RawSource rawSource) {
		this(modelFactory, tree, rawSource, 0, 0);
	}

	/**
	 * Create a new B semantics analyzer
	 * 
	 * We handle here an issue with B grammar that does not support comments at the beginning of the source file To overpass here,
	 * workaround is to detect preliminary comment, and then removing it from the parsed source, while not forgetting translation indexes,
	 * in order to reconsider these primilary fragments while re pretty-printing B source
	 * 
	 * @param modelFactory
	 * @param tree
	 * @param rawSource
	 * @param rowTranslation
	 * @param charTranslation
	 */
	public BSemanticsAnalyzer(BModelFactory modelFactory, Start tree, RawSource rawSource, int rowTranslation, int charTranslation) {
		this(modelFactory);
		this.rawSource = rawSource;
		fragmentManager = new FragmentManager(rawSource, rowTranslation, charTranslation);
		tree.apply(this);
		finalizeDeserialization();
	}

	public BModelFactory getModelFactory() {
		return modelFactory;
	}

	public PredicateFactory getPredicateFactory() {
		return predicateFactory;
	}

	public ExpressionFactory getExpressionFactory() {
		return expressionFactory;
	}

	public OperationFactory getOperationFactory() {
		return operationFactory;
	}

	public SubstitutionFactory getSubstitutionFactory() {
		return substitutionFactory;
	}

	public BObject getBObject() {
		return rootNode.getModelObject();
	}

	public BComponent getBComponent() {
		return getBObject() != null ? (BComponent) getBObject() : null;
	}

	public RawSource getRawSource() {
		return rawSource;
	}

	public FragmentManager getFragmentManager() {
		return fragmentManager;
	}

	protected final void finalizeDeserialization() {
		finalizeDeserialization(rootNode);
		rootNode.initializePrettyPrint(rootNode, rootNode.makePrettyPrintContext());
	}

	protected final void finalizeDeserialization(BObjectNode<?, ?> node) {
		node.finalizeDeserialization();
		for (P2PPNode<?, ?> child : node.getChildren()) {
			finalizeDeserialization((BObjectNode<?, ?>) child);
		}
	}

	public void push(BObjectNode<?, ?> node) {
		if (this.rootNode == null) {
			this.rootNode = node;
		}
		if (!bNodes.isEmpty()) {
			BObjectNode<?, ?> current = bNodes.peek();
			current.addToChildren(node);
		}
		bNodes.push(node);
	}

	public <N extends BObjectNode<?, ?>> N pop() {
		N builtFMLNode = (N) bNodes.pop();
		builtFMLNode.deserialize();
		return builtFMLNode;
	}

	@Override
	public void defaultCase(Node node) {
		super.defaultCase(node);
		/*if (node instanceof Token && !bNodes.isEmpty()) {
			BObjectNode<?, ?> currentNode = bNodes.peek();
			if (currentNode != null) {
				// System.out.println("Token: " + ((Token) node).getText() + " de " + ((Token) node).getLine() + ":" + ((Token)
				// node).getPos()
				// + ":" + ((Token) node).getOffset());
				currentNode.handleToken((Token) node);
			}
		}*/
	}

	@Override
	public void defaultIn(Node node) {
		// System.out.println("IN " + node.getClass());
		super.defaultIn(node);

		if (rootNode == null) {
			if (node instanceof PPredicate) {
				getPredicateFactory().makePredicateNode((PPredicate) node);
			}
			if (node instanceof PExpression) {
				getExpressionFactory().makeExpressionNode((PExpression) node);
			}
			if (node instanceof PSubstitution) {
				getSubstitutionFactory().makeSubstitutionNode((PSubstitution) node);
			}
		}

	}

	@Override
	public void defaultOut(Node node) {
		// System.out.println("OUT " + node.getClass());
		super.defaultOut(node);
	}

	public BExpression makeExpression(PExpression e) throws NotImplementedException {
		BExpressionNode<?, ?> node = expressionFactory.makeExpressionNode(e);
		return node.getModelObject();
	}

	public BPredicate makePredicate(PPredicate p) throws NotImplementedException {
		BPredicateNode<?, ?> node = predicateFactory.makePredicateNode(p);
		return node.getModelObject();
	}

	public BOperation makeOperation(POperation o) throws NotImplementedException {
		BOperationNode<?, ?> node = operationFactory.makeOperationNode(o);
		return node.getModelObject();
	}

	public BSubstitution makeSubstitution(PSubstitution s) throws NotImplementedException {
		BSubstitutionNode<?, ?> node = substitutionFactory.makeSubstitutionNode(s);
		return node.getModelObject();
	}

	@Override
	public void inAAbstractMachineParseUnit(AAbstractMachineParseUnit node) {
		super.inAAbstractMachineParseUnit(node);
		PMachineVariant variant = node.getVariant();
		if (variant instanceof AMachineMachineVariant) {
			push(new BMachineNode(node, this));
		}
		if (variant instanceof ASystemMachineVariant) {
			push(new BSystemNode(node, this));
		}
	}

	@Override
	public void outAAbstractMachineParseUnit(AAbstractMachineParseUnit node) {
		super.outAAbstractMachineParseUnit(node);
		pop();
	}

	@Override
	public void inARefinementMachineParseUnit(ARefinementMachineParseUnit node) {
		super.inARefinementMachineParseUnit(node);
		push(new BRefinementNode(node, this));
	}

	@Override
	public void outARefinementMachineParseUnit(ARefinementMachineParseUnit node) {
		super.outARefinementMachineParseUnit(node);
		pop();
	}

	@Override
	public void inAImplementationMachineParseUnit(AImplementationMachineParseUnit node) {
		super.inAImplementationMachineParseUnit(node);
		push(new BImplementationNode(node, this));
	}

	@Override
	public void outAImplementationMachineParseUnit(AImplementationMachineParseUnit node) {
		super.outAImplementationMachineParseUnit(node);
		pop();
	}

	private enum VisibilityParsingMode {
		None, Includes, Imports, Extends
	}

	private VisibilityParsingMode visibilityParsingMode = VisibilityParsingMode.None;

	@Override
	public void inAMachineReference(AMachineReference node) {
		super.inAMachineReference(node);
		switch (visibilityParsingMode) {
			case Includes:
				push(new BIncludesClauseNode(node, this));
				break;
			case Imports:
				push(new BImportsClauseNode(node, this));
				break;
			case Extends:
				push(new BExtendsClauseNode(node, this));
				break;
			default:
				logger.warning("Unexpected MachineReference " + node);
				break;
		}
	}

	@Override
	public void outAMachineReference(AMachineReference node) {
		super.outAMachineReference(node);
		switch (visibilityParsingMode) {
			case Includes:
			case Imports:
			case Extends:
				pop();
				break;
			default:
				logger.warning("Unexpected MachineReference " + node);
				break;
		}
	}

	@Override
	public void inAIncludesMachineClause(AIncludesMachineClause node) {
		visibilityParsingMode = VisibilityParsingMode.Includes;
		super.inAIncludesMachineClause(node);
	}

	@Override
	public void outAIncludesMachineClause(AIncludesMachineClause node) {
		super.outAIncludesMachineClause(node);
		visibilityParsingMode = VisibilityParsingMode.None;
	}

	@Override
	public void inAImportsMachineClause(AImportsMachineClause node) {
		visibilityParsingMode = VisibilityParsingMode.Imports;
		super.inAImportsMachineClause(node);
	}

	@Override
	public void outAImportsMachineClause(AImportsMachineClause node) {
		super.outAImportsMachineClause(node);
		visibilityParsingMode = VisibilityParsingMode.None;
	}

	@Override
	public void inAExtendsMachineClause(AExtendsMachineClause node) {
		visibilityParsingMode = VisibilityParsingMode.Extends;
		super.inAExtendsMachineClause(node);
	}

	@Override
	public void outAExtendsMachineClause(AExtendsMachineClause node) {
		super.outAExtendsMachineClause(node);
		visibilityParsingMode = VisibilityParsingMode.None;
	}

	@Override
	public void inASeesMachineClause(ASeesMachineClause node) {
		for (PExpression expression : node.getMachineNames()) {
			push(new BSeesClauseNode(expression, this));
			pop();
		}
		super.inASeesMachineClause(node);
	}

	@Override
	public void inAUsesMachineClause(AUsesMachineClause node) {
		for (PExpression expression : node.getMachineNames()) {
			push(new BUsesClauseNode(expression, this));
			pop();
		}
		super.inAUsesMachineClause(node);
	}

	@Override
	public void inADeferredSetSet(ADeferredSetSet node) {
		push(new BSetNode(node, this));
		super.inADeferredSetSet(node);
	}

	@Override
	public void outADeferredSetSet(ADeferredSetSet node) {
		super.outADeferredSetSet(node);
		pop();
	}

	@Override
	public void inAEnumeratedSetSet(AEnumeratedSetSet node) {
		/*String setName = getText(node.getIdentifier());
		List<String> values = new ArrayList<String>();
		for (PExpression expression : node.getElements()) {
			values.add(getText(expression));
		}
		getBComponent().addToSets(modelFactory.makeSet(setName, values));*/
		push(new BSetNode(node, this));
		for (PExpression expression : node.getElements()) {
			push(new BSetValueNode(expression, this));
			pop();
		}
		super.inAEnumeratedSetSet(node);
	}

	@Override
	public void outAEnumeratedSetSet(AEnumeratedSetSet node) {
		super.outAEnumeratedSetSet(node);
		pop();
	}

	@Override
	public void inAAbstractConstantsMachineClause(AAbstractConstantsMachineClause node) {
		for (PExpression expression : node.getIdentifiers()) {
			push(new BAbstractConstantNode(expression, this));
			pop();
		}
		super.inAAbstractConstantsMachineClause(node);
	}

	@Override
	public void inAConstantsMachineClause(AConstantsMachineClause node) {
		for (PExpression expression : node.getIdentifiers()) {
			push(new BConcreteConstantNode(expression, this));
			pop();
		}
		super.inAConstantsMachineClause(node);
	}

	@Override
	public void inAVariablesMachineClause(AVariablesMachineClause node) {
		for (PExpression expression : node.getIdentifiers()) {
			push(new BAbstractVariableNode(expression, this));
			pop();
		}
		super.inAVariablesMachineClause(node);
	}

	@Override
	public void inAConcreteVariablesMachineClause(AConcreteVariablesMachineClause node) {
		for (PExpression expression : node.getIdentifiers()) {
			push(new BConcreteVariableNode(expression, this));
			pop();
		}
		super.inAConcreteVariablesMachineClause(node);
	}

	@Override
	public void inAPropertiesMachineClause(APropertiesMachineClause node) {
		BPredicateNode<?, ?> predicateNode = predicateFactory.makePredicateNode(node.getPredicates());
		BObject bObject = getBObject();
		if (bObject instanceof BComponent) {
			((BComponent) bObject).setProperties(predicateNode.getModelObject());
		}
		super.inAPropertiesMachineClause(node);
	}

	@Override
	public void inAInvariantMachineClause(AInvariantMachineClause node) {
		BPredicateNode<?, ?> predicateNode = predicateFactory.makePredicateNode(node.getPredicates());
		BObject bObject = getBObject();
		if (bObject instanceof BComponent) {
			((BComponent) bObject).setInvariant(predicateNode.getModelObject());
		}
		super.inAInvariantMachineClause(node);
	}

	@Override
	public void inAAssertionsMachineClause(AAssertionsMachineClause node) {
		for (PPredicate pPredicate : node.getPredicates()) {
			BPredicateNode<?, ?> predicateNode = predicateFactory.makePredicateNode(pPredicate);
			BObject bObject = getBObject();
			if (bObject instanceof BComponent) {
				((BComponent) bObject).addToAssertions(predicateNode.getModelObject());
			}
		}
		super.inAAssertionsMachineClause(node);
	}

	@Override
	public void inAInitialisationMachineClause(AInitialisationMachineClause node) {
		// TODO Auto-generated method stub
		super.inAInitialisationMachineClause(node);
	}

	@Override
	public void inAOperationsMachineClause(AOperationsMachineClause node) {
		for (POperation pOperation : node.getOperations()) {
			BOperationNode<?, ?> operationNode = operationFactory.makeOperationNode(pOperation);
			BObject bObject = getBObject();
			if (bObject instanceof BComponent) {
				((BComponent) bObject).addToOperations(operationNode.getModelObject());
			}
		}
		super.inAOperationsMachineClause(node);
	}

	protected String getText(List<TIdentifierLiteral> idLitList) {
		String returned = null;
		for (TIdentifierLiteral tIdentifierLiteral : idLitList) {
			// System.out.println("> " + tIdentifierLiteral.getText());
			if (returned != null) {
				returned += "." + tIdentifierLiteral.getText();
			}
			else {
				returned = tIdentifierLiteral.getText();
			}
		}
		return returned;
	}

	protected String getText(PExpression expression) {
		if (expression instanceof AIdentifierExpression) {
			return getText(((AIdentifierExpression) expression).getIdentifier());
		}
		return expression.toString();
	}

	protected Integer getInteger(TIntegerLiteral literal) {
		return Integer.parseInt(literal.getText());
	}

}
