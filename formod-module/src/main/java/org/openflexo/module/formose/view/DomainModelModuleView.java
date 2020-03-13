/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.module.formose.view;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.diana.swing.view.JDrawingView;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.swing.view.widget.DnDJTree;
import org.openflexo.gina.view.widget.browser.impl.FIBBrowserModel.BrowserCell;
import org.openflexo.module.formose.controller.DomainModelPerspective;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

@SuppressWarnings("serial")
public class DomainModelModuleView extends FMLControlledDiagramModuleView {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DomainModelModuleView.class.getPackage().getName());

	private DGListener dgListener;
	private DSListener dsListener;

	public DomainModelModuleView(FMLControlledDiagramEditor editor, DomainModelPerspective perspective) {
		super(editor, perspective);
		dgListener = new DGListener(perspective.getDomainModelBrowser().getFIBBrowserWidget().getTechnologyComponent().getJTree());
		dsListener = new DSListener();
	}

	@Override
	public DomainModelPerspective getPerspective() {
		return (DomainModelPerspective) super.getPerspective();
	}

	@Override
	public String getInfoMessage() {
		return "Domain Model modelling - CTRL-drag to draw edges";
	}

	@Override
	public void deleteModuleView() {
		super.deleteModuleView();
	}

	@Override
	public void willHide() {

		super.willHide();

		getPerspective().hideDomainModelBrowser();
	}

	@Override
	public void willShow() {

		super.willShow();

		FMLRTVirtualModelInstance fmlControlledDiagram = getRepresentedObject();
		FMLRTVirtualModelInstance domainModel;
		try {
			domainModel = fmlControlledDiagram.execute("domainModel");
			System.out.println("Hop, je dois afficher le domainModel: " + domainModel);
			getPerspective().showDomainModelBrowser(domainModel);
			// Activate drag&drop between browser and diagram
			getPerspective().getDomainModelBrowser().getFIBBrowserWidget().registerDragGestureListener(dgListener);
		} catch (TypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullReferenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidBindingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void show(FlexoController controller, FlexoPerspective perspective) {
		super.show(controller, perspective);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

	public JDrawingView<?> getDrawingView() {
		return getEditor().getDrawingView();
	}

	/**
	 * DGListener a listener that will start the drag. has access to top level's dsListener and dragSource
	 * 
	 * @see java.awt.dnd.DragGestureListener
	 * @see java.awt.dnd.DragSource
	 * @see java.awt.datatransfer.StringSelection
	 */
	public class DGListener implements DragGestureListener {

		private DnDJTree sourceComponent;

		public DGListener(DnDJTree sourceComponent) {
			this.sourceComponent = sourceComponent;
		}

		/**
		 * Start the drag if the operation is ok. uses java.awt.datatransfer.StringSelection to transfer the label's data
		 * 
		 * @param e
		 *            the event object
		 */
		@Override
		public void dragGestureRecognized(DragGestureEvent e) {

			// if the action is ok we go ahead
			// otherwise we punt

			if ((e.getDragAction() & DnDConstants.ACTION_MOVE) == 0) {
				return;
				// get the label's text and put it inside a Transferable
				// Transferable transferable = new StringSelection(
				// DragLabel.this.getText() );
			}

			BrowserCell dragNode = sourceComponent.getSelectedBrowserCell();
			if (dragNode != null) {
				// Get the Transferable Object
				// Transferable transferable = (Transferable) dragNode.getUserObject();
				Transferable transferable = dragNode;

				getEditor().setObjectBeingTransfered(transferable);

				try {
					// initial cursor, transferable, dsource listener
					e.startDrag(dropKO, transferable, dsListener);
					logger.fine("Starting drag for " + dragNode.getRepresentedObject());
					// getDrawingView().captureDraggedNode(JPaletteElementView.this, e);
				} catch (Exception idoe) {
					logger.warning("Unexpected exception " + idoe);
				}
			}

		}

	}

	/**
	 * DSListener a listener that will track the state of the DnD operation
	 * 
	 * @see java.awt.dnd.DragSourceListener
	 * @see java.awt.dnd.DragSource
	 * @see java.awt.datatransfer.StringSelection
	 */
	public class DSListener implements DragSourceListener {

		/**
		 * @param e
		 *            the event
		 */
		@Override
		public void dragDropEnd(DragSourceDropEvent e) {
			// Resets the screenshot stored by the palette view.
			if (getDrawingView() != null) {
				getDrawingView().resetCapturedNode();
			}
			if (!e.getDropSuccess()) {
				if (logger.isLoggable(Level.INFO)) {
					logger.info("Dropping was not successful");
				}
				return;
			}
			/*
			 * the dropAction should be what the drop target specified in
			 * acceptDrop
			 */
			// this is the action selected by the drop target
			if (e.getDropAction() == DnDConstants.ACTION_MOVE) {
				setName("");
			}
		}

		/**
		 * @param e
		 *            the event
		 */
		@Override
		public void dragEnter(DragSourceDragEvent e) {
			DragSourceContext context = e.getDragSourceContext();
			// intersection of the users selected action, and the source and
			// target actions
			int myaction = e.getDropAction();
			if ((myaction /*& dragAction*/) != 0) {
				context.setCursor(DragSource.DefaultCopyDrop);
			}
			else {
				context.setCursor(DragSource.DefaultCopyNoDrop);
			}
		}

		/**
		 * @param e
		 *            the event
		 */
		@Override
		public void dragOver(DragSourceDragEvent e) {
			// interface
			// getController().getPalette().setDragSourceContext(e.getDragSourceContext());
			getEditor().setDragSourceContext(e.getDragSourceContext());
		}

		/**
		 * @param e
		 *            the event
		 */
		@Override
		public void dragExit(DragSourceEvent e) {
			// interface
		}

		/**
		 * for example, press shift during drag to change to a link action
		 * 
		 * @param e
		 *            the event
		 */
		@Override
		public void dropActionChanged(DragSourceDragEvent e) {
			DragSourceContext context = e.getDragSourceContext();
			context.setCursor(DragSource.DefaultCopyNoDrop);
		}
	}

}
