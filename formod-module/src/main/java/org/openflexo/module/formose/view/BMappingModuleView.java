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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.formose.controller.BPerspective;
import org.openflexo.module.formose.model.FormoseProjectNature;
import org.openflexo.swing.layout.JXMultiSplitPane;
import org.openflexo.swing.layout.JXMultiSplitPane.DividerPainter;
import org.openflexo.swing.layout.MultiSplitLayout;
import org.openflexo.swing.layout.MultiSplitLayout.Divider;
import org.openflexo.swing.layout.MultiSplitLayout.Leaf;
import org.openflexo.swing.layout.MultiSplitLayout.Split;
import org.openflexo.swing.layout.MultiSplitLayoutFactory;
import org.openflexo.ta.b.model.BComponent;
import org.openflexo.ta.b.view.BComponentView;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * A {@link ModuleView} representing a BMapping
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class BMappingModuleView extends JPanel implements ModuleView<FlexoConceptInstance>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BMappingModuleView.class.getPackage().getName());

	private final FlexoConceptInstance bMapping;
	private final BPerspective perspective;

	public BMappingModuleView(FlexoConceptInstance bMapping, BPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.bMapping = bMapping;
		this.perspective = perspective;

		Split<?> defaultLayout = getDefaultLayout();

		MultiSplitLayout centerLayout = new MultiSplitLayout(true, MSL_FACTORY);
		centerLayout.setLayoutMode(MultiSplitLayout.NO_MIN_SIZE_LAYOUT);
		centerLayout.setModel(defaultLayout);

		JXMultiSplitPane centerPanel = new JXMultiSplitPane(centerLayout);
		centerPanel.setDividerSize(DIVIDER_SIZE);
		centerPanel.setDividerPainter(new DividerPainter() {

			@Override
			protected void doPaint(Graphics2D g, Divider divider, int width, int height) {
				if (!divider.isVisible()) {
					return;
				}
				if (divider.isVertical()) {
					int x = (width - KNOB_SIZE) / 2;
					int y = (height - DIVIDER_KNOB_SIZE) / 2;
					for (int i = 0; i < 3; i++) {
						Graphics2D graph = (Graphics2D) g.create(x, y + i * (KNOB_SIZE + KNOB_SPACE), KNOB_SIZE + 1, KNOB_SIZE + 1);
						graph.setPaint(KNOB_PAINTER);
						graph.fillOval(0, 0, KNOB_SIZE, KNOB_SIZE);
					}
				}
				else {
					int x = (width - DIVIDER_KNOB_SIZE) / 2;
					int y = (height - KNOB_SIZE) / 2;
					for (int i = 0; i < 3; i++) {
						Graphics2D graph = (Graphics2D) g.create(x + i * (KNOB_SIZE + KNOB_SPACE), y, KNOB_SIZE + 1, KNOB_SIZE + 1);
						graph.setPaint(KNOB_PAINTER);
						graph.fillOval(0, 0, KNOB_SIZE, KNOB_SIZE);
					}
				}

			}
		});

		add(centerPanel, BorderLayout.CENTER);

		try {
			// Domain model diagram
			FMLRTVirtualModelInstance goalModelDiagramVMI = bMapping
					.execute("domainModelMapping.sysMLKaosElementMapping.mainFunctionalGoalDiagram.goalModelingDiagram");
			if (goalModelDiagramVMI != null && goalModelDiagramVMI.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				DiagramTechnologyAdapterController diagramTAC = getPerspective().getController().getDiagramTAC();
				FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(goalModelDiagramVMI, false,
						getPerspective().getController(), diagramTAC.getToolFactory());
				editor.setScale(0.7);
				centerPanel.add(new JScrollPane(editor.getDrawingView()), LayoutPosition.TOP_LEFT.name());
			}
			else {
				centerPanel.add(new JLabel("Missing goal model"), LayoutPosition.TOP_LEFT.name());
			}

			// Domain model diagram
			FMLRTVirtualModelInstance domainModelDiagramVMI = bMapping.execute("domainModelMapping.defaultDiagram");
			if (domainModelDiagramVMI != null && domainModelDiagramVMI.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				DiagramTechnologyAdapterController diagramTAC = getPerspective().getController().getDiagramTAC();
				FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(domainModelDiagramVMI, false,
						getPerspective().getController(), diagramTAC.getToolFactory());
				editor.setScale(0.7);
				centerPanel.add(new JScrollPane(editor.getDrawingView()), LayoutPosition.TOP_RIGHT.name());
			}
			else {
				centerPanel.add(new JLabel("Missing domain model"), LayoutPosition.TOP_RIGHT.name());
			}

			// B Context
			BComponent context = bMapping.execute("context.componentResource.resourceData");
			if (context != null) {
				centerPanel.add(new BComponentView(context, perspective.getController()), LayoutPosition.BOTTOM_RIGHT.name());
			}
			else {
				centerPanel.add(new JLabel("Missing B component"), LayoutPosition.BOTTOM_RIGHT.name());
			}

			// B Machine
			BComponent machine = bMapping.execute("machine.componentResource.resourceData");
			if (machine != null) {
				centerPanel.add(new BComponentView(machine, perspective.getController()), LayoutPosition.BOTTOM_LEFT.name());
			}
			else {
				centerPanel.add(new JLabel("Missing B component"), LayoutPosition.BOTTOM_LEFT.name());
			}
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

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public FormoseProjectNature getFormoseProjectNature() {
		return getPerspective().getController().getFormoseNature();
	}

	@Override
	public BPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		perspective.getController().removeModuleView(this);
	}

	@Override
	public FlexoConceptInstance getRepresentedObject() {
		return bMapping;
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
		getPerspective().setBottomLeftView(null);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		getPerspective().setBottomLeftView(null);

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

	public static enum LayoutPosition {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
	}

	public static enum LayoutRows {
		TOP, BOTTOM;
	}

	protected static final MultiSplitLayoutFactory MSL_FACTORY = new MultiSplitLayoutFactory.DefaultMultiSplitLayoutFactory();
	protected static final int KNOB_SIZE = 5;
	protected static final int KNOB_SPACE = 2;
	protected static final int DIVIDER_SIZE = KNOB_SIZE + 2 * KNOB_SPACE;
	protected static final int DIVIDER_KNOB_SIZE = 3 * KNOB_SIZE + 2 * KNOB_SPACE;

	protected static final Paint KNOB_PAINTER = new RadialGradientPaint(new Point((KNOB_SIZE - 1) / 2, (KNOB_SIZE - 1) / 2),
			(KNOB_SIZE - 1) / 2, new float[] { 0.0f, 1.0f }, new Color[] { Color.GRAY, Color.LIGHT_GRAY });

	protected static Split<?> getDefaultLayout() {
		Split root = MSL_FACTORY.makeColSplit();
		root.setName("ROOT");
		Split<?> top = getHorizontalSplit(LayoutPosition.TOP_LEFT, 0.5, LayoutPosition.TOP_RIGHT, 0.5);
		top.setWeight(0.5);
		top.setName(LayoutRows.TOP.name());
		Split<?> bottom = getHorizontalSplit(LayoutPosition.BOTTOM_LEFT, 0.5, LayoutPosition.BOTTOM_RIGHT, 0.5);
		bottom.setWeight(0.5);
		bottom.setName(LayoutRows.BOTTOM.name());
		root.setChildren(top, MSL_FACTORY.makeDivider(), bottom);
		return root;
	}

	protected static Split<?> getHorizontalSplit(LayoutPosition position1, double weight1, LayoutPosition position2, double weight2) {
		Split split = MSL_FACTORY.makeRowSplit();
		Leaf<?> l1 = MSL_FACTORY.makeLeaf(position1.name());
		l1.setWeight(weight1);
		Leaf<?> l2 = MSL_FACTORY.makeLeaf(position2.name());
		l2.setWeight(weight2);
		split.setChildren(l1, MSL_FACTORY.makeDivider(), l2);
		return split;
	}

}
