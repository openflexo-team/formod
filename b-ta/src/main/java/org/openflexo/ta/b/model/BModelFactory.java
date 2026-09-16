/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.b.model;

import java.util.logging.Logger;

import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.pamela.PamelaMetaModelLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.pamela.factory.PamelaModelFactory;
import org.openflexo.ta.b.model.BComponent.BComponentType;
import org.openflexo.ta.b.model.operation.BNormalOperation;
import org.openflexo.ta.b.model.operation.BRefinedOperation;
import org.openflexo.ta.b.model.parser.BSemanticsAnalyzer;
import org.openflexo.ta.b.model.parser.nodes.BImplementationNode;
import org.openflexo.ta.b.model.parser.nodes.BMachineNode;
import org.openflexo.ta.b.model.parser.nodes.BRefinementNode;
import org.openflexo.ta.b.model.parser.nodes.BSystemNode;
import org.openflexo.ta.b.rm.BResource;

/**
 * A {@link PamelaModelFactory} used to manage a {@link BComponent}<br>
 * One instance of this class should be used for each {@link BResource}
 * 
 * @author sylvain
 * 
 */
public class BModelFactory extends PamelaModelFactory implements PamelaResourceModelFactory<BResource> {

	private static final Logger logger = Logger.getLogger(BModelFactory.class.getPackage().getName());

	private final BResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	// private RelativePathResourceConverter relativePathResourceConverter;

	public BModelFactory(BResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(PamelaMetaModelLibrary.retrieveMetaModel(BComponent.class));
		this.resource = resource;
		setEditingContext(editingContext);
		/*addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
		if (resource != null && resource.getIODelegate() != null && resource.getIODelegate().getSerializationArtefactAsResource() != null) {
			relativePathResourceConverter
					.setContainerResource(resource.getIODelegate().getSerializationArtefactAsResource().getContainer());
		}*/
	}

	@Override
	public BResource getResource() {
		return resource;
	}

	public BResource getResourceWithName(String resourceName) {
		ResourceRepository<BResource, ?> repository = resource.getTechnologyAdapter().getBResourceRepository(resource.getResourceCenter());
		// A B component is stored in a file named after it (R2 in R2.mch): look for it first without loading anything.
		// Asking each resource its component name loads it, and a resource being loaded does not know its name yet, so that a
		// component refining another one being loaded could not be found (loading R2 looks for R1, which loads R3, which looks for R2)
		for (BResource r : repository.getAllResources()) {
			String fileName = r.getName();
			if (fileName != null && fileName.contains(".") && fileName.substring(0, fileName.lastIndexOf('.')).equals(resourceName)) {
				return r;
			}
		}
		for (BResource r : repository.getAllResources()) {
			if (r.getComponentName() != null && r.getComponentName().equals(resourceName)) {
				// System.out.println("Found resource " + resourceName + " : " + r);
				return r;
			}
		}
		logger.warning("Cannot find BResource " + resourceName);
		return null;
	}

	/**
	 * Create new {@link BComponent} according to supplied component type
	 * 
	 * This newly created component is supposed to be new (and not deserialized), and thus, create a new fresh BComponentNode managing its
	 * pretty-print
	 * 
	 * @param componentType
	 * @return
	 */
	public BComponent makeComponent(BComponentType componentType) {
		switch (componentType) {
			case System:
				BSystem returnedSystem = makeSystem();
				// Init pretty-print
				BSystemNode systemNode = new BSystemNode(returnedSystem, new BSemanticsAnalyzer(this));
				return returnedSystem;
			case Refinement:
				BRefinement returnedRefinement = makeRefinement();
				// Init pretty-print
				BRefinementNode refinementNode = new BRefinementNode(returnedRefinement, new BSemanticsAnalyzer(this));
				return returnedRefinement;
			case Machine:
				BMachine returnedMachine = makeMachine();
				// Init pretty-print
				BMachineNode machineNode = new BMachineNode(returnedMachine, new BSemanticsAnalyzer(this));
				return returnedMachine;
			case Implementation:
				BImplementation returnedImplementation = makeImplementation();
				// Init pretty-print
				BImplementationNode implementationNode = new BImplementationNode(returnedImplementation, new BSemanticsAnalyzer(this));
				return returnedImplementation;
			default:
				return null;
		}
	}

	public BSystem makeSystem() {
		BSystem returned = newInstance(BSystem.class);
		return returned;
	}

	public BMachine makeMachine() {
		BMachine returned = newInstance(BMachine.class);
		return returned;
	}

	public BRefinement makeRefinement() {
		BRefinement returned = newInstance(BRefinement.class);
		return returned;
	}

	public BImplementation makeImplementation() {
		BImplementation returned = newInstance(BImplementation.class);
		return returned;
	}

	public BIncludesClause makeIncludesClause(BResource referencedComponent) {
		BIncludesClause returned = newInstance(BIncludesClause.class);
		returned.setReferencedComponent(referencedComponent);
		return returned;
	}

	public BImportsClause makeImportsClause(BResource referencedComponent) {
		BImportsClause returned = newInstance(BImportsClause.class);
		returned.setReferencedComponent(referencedComponent);
		return returned;
	}

	public BSeesClause makeSeesClause(BResource referencedComponent) {
		BSeesClause returned = newInstance(BSeesClause.class);
		returned.setReferencedComponent(referencedComponent);
		return returned;
	}

	public BExtendsClause makeExtendsClause(BResource referencedComponent) {
		BExtendsClause returned = newInstance(BExtendsClause.class);
		returned.setReferencedComponent(referencedComponent);
		return returned;
	}

	public BUsesClause makeUsesClause(BResource referencedComponent) {
		BUsesClause returned = newInstance(BUsesClause.class);
		returned.setReferencedComponent(referencedComponent);
		return returned;
	}

	public BSet makeSet(String name) {
		BSet returned = newInstance(BSet.class);
		returned.setName(name);
		return returned;
	}

	public BSetValue makeSetValue(String name) {
		BSetValue returned = newInstance(BSetValue.class);
		returned.setName(name);
		return returned;
	}

	public BAbstractConstant makeAbstractConstant(String name) {
		BAbstractConstant returned = newInstance(BAbstractConstant.class);
		returned.setName(name);
		return returned;
	}

	public BConcreteConstant makeConcreteConstant(String name) {
		BConcreteConstant returned = newInstance(BConcreteConstant.class);
		returned.setName(name);
		return returned;
	}

	public BAbstractVariable makeAbstractVariable(String name) {
		BAbstractVariable returned = newInstance(BAbstractVariable.class);
		returned.setName(name);
		return returned;
	}

	public BConcreteVariable makeConcreteVariable(String name) {
		BConcreteVariable returned = newInstance(BConcreteVariable.class);
		returned.setName(name);
		return returned;
	}

	public <P extends BPredicate> P makePredicate(Class<P> predicateClass, BComponent component) {
		P returned = newInstance(predicateClass);
		returned.setComponent(component);
		return returned;
	}

	public <E extends BExpression> E makeExpression(Class<E> expressionClass, BComponent component) {
		E returned = newInstance(expressionClass);
		returned.setComponent(component);
		return returned;
	}

	public BNormalOperation makeNormalOperation(String name) {
		BNormalOperation returned = newInstance(BNormalOperation.class);
		returned.setName(name);
		return returned;
	}

	public BRefinedOperation makeRefinedOperation(String name) {
		BRefinedOperation returned = newInstance(BRefinedOperation.class);
		returned.setName(name);
		return returned;
	}

	public <O extends BNormalOperation> O makeOperation(Class<O> operationClass, BComponent component) {
		O returned = newInstance(operationClass);
		returned.setComponent(component);
		return returned;
	}

	public <S extends BSubstitution> S makeSubstitution(Class<S> substitutionClass, BComponent component) {
		S returned = newInstance(substitutionClass);
		returned.setComponent(component);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {
		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(resource));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {
		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			// System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

}
