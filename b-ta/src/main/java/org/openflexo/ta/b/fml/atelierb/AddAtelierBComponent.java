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

package org.openflexo.ta.b.fml.atelierb;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.ta.b.AtelierBProjectModelSlot;
import org.openflexo.ta.b.model.AbstractBRefinement;
import org.openflexo.ta.b.model.BComponent.BComponentType;
import org.openflexo.ta.b.model.BModelFactory;
import org.openflexo.ta.b.model.atelierb.AtelierBComponent;
import org.openflexo.ta.b.model.atelierb.AtelierBProject;

@ModelEntity
@ImplementationClass(AddAtelierBComponent.AddAtelierBComponentImpl.class)
@XMLElement
@FML("AddAtelierBComponent")
public interface AddAtelierBComponent extends AtelierBAction<AtelierBComponent> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String COMPONENT_NAME_KEY = "componentName";
	@PropertyIdentifier(type = BComponentType.class)
	public static final String COMPONENT_TYPE_KEY = "componentType";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String REFINES_KEY = "refines";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String EXTENDS_KEY = "extends";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SEES_KEY = "sees";

	@Getter(value = COMPONENT_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getComponentName();

	@Setter(COMPONENT_NAME_KEY)
	public void setComponentName(DataBinding<String> componentName);

	@Getter(value = COMPONENT_TYPE_KEY)
	@XMLAttribute
	public BComponentType getComponentType();

	@Setter(COMPONENT_TYPE_KEY)
	public void setComponentType(BComponentType componentType);

	public boolean isRefinement();

	@Getter(value = REFINES_KEY)
	@XMLAttribute
	public DataBinding<AtelierBComponent> getRefines();

	@Setter(REFINES_KEY)
	public void setRefines(DataBinding<AtelierBComponent> component);

	@Getter(value = EXTENDS_KEY)
	@XMLAttribute
	public DataBinding<AtelierBComponent> getExtends();

	@Setter(EXTENDS_KEY)
	public void setExtends(DataBinding<AtelierBComponent> component);

	@Getter(value = SEES_KEY)
	@XMLAttribute
	public DataBinding<AtelierBComponent> getSees();

	@Setter(SEES_KEY)
	public void setSees(DataBinding<AtelierBComponent> component);

	public static abstract class AddAtelierBComponentImpl
			extends TechnologySpecificActionDefiningReceiverImpl<AtelierBProjectModelSlot, AtelierBProject, AtelierBComponent>
			implements AddAtelierBComponent {

		private static final Logger logger = Logger.getLogger(AddAtelierBComponent.class.getPackage().getName());

		private DataBinding<String> componentName;
		private DataBinding<AtelierBComponent> refines;
		private DataBinding<AtelierBComponent> extendsB;
		private DataBinding<AtelierBComponent> sees;

		@Override
		public Type getAssignableType() {
			return AtelierBComponent.class;
		}

		@Override
		public void setComponentType(BComponentType componentType) {
			performSuperSetter(COMPONENT_TYPE_KEY, componentType);
			getPropertyChangeSupport().firePropertyChange("isRefinement", !isRefinement(), isRefinement());
		}

		@Override
		public boolean isRefinement() {
			return getComponentType() == BComponentType.Refinement || getComponentType() == BComponentType.Implementation;
		}

		@Override
		public AtelierBComponent execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			AtelierBComponent newComponent = null;

			AtelierBProject resourceData = getReceiver(evaluationContext);

			try {
				if (resourceData != null) {
					String componentName = getComponentName().getBindingValue(evaluationContext);
					if (componentName != null) {
						System.out.println("OK, faut creer le composant " + componentName + " dans " + resourceData);
						System.out.println("type: " + getComponentType());
						System.out.println("refines: " + getRefines().getBindingValue(evaluationContext));

						try {

							switch (getComponentType()) {
								case System:
									newComponent = resourceData.getFactory().makeNewSystem(componentName);
									break;
								case Refinement:
									newComponent = resourceData.getFactory().makeNewRefinement(componentName);
									break;
								case Machine:
									newComponent = resourceData.getFactory().makeNewMachine(componentName);
									break;
								case Implementation:
									newComponent = resourceData.getFactory().makeNewImplementation(componentName);
									break;
								default:
									break;
							}
							BModelFactory bModelFactory = newComponent.getComponentResource().getFactory();

							if (isRefinement()) {
								if (getRefines().isSet() && getRefines().isValid()) {
									AtelierBComponent refines = getRefines().getBindingValue(evaluationContext);
									((AbstractBRefinement) newComponent.getComponentResource().getBComponent())
											.setRefinesComponent(refines.getComponentResource());
								}
							}

							if (getExtends().isSet() && getExtends().isValid()) {
								AtelierBComponent extendsC = getExtends().getBindingValue(evaluationContext);
								if (extendsC != null) {
									newComponent.getComponentResource().getBComponent()
											.addToExtendsClauses(bModelFactory.makeExtendsClause(extendsC.getComponentResource()));
								}
							}
							System.out.println("sees=" + getSees());
							if (getSees().isSet() && getSees().isValid()) {
								System.out.println("valid");
								AtelierBComponent sees = getSees().getBindingValue(evaluationContext);
								System.out.println("see=" + sees);
								if (sees != null) {
									newComponent.getComponentResource().getBComponent()
											.addToSeesClauses(bModelFactory.makeSeesClause(sees.getComponentResource()));
								}
							}

						} catch (SaveResourceException e) {
							e.printStackTrace();
							throw new FlexoException(e);
						} catch (ModelDefinitionException e) {
							e.printStackTrace();
							throw new FlexoException(e);
						} catch (IOException e) {
							e.printStackTrace();
							throw new FlexoException(e);
						}

						resourceData.getAtelierBProjectDefinition().addToComponents(newComponent);

						// newComponent = resourceData.getFactory().makeDSLComponent(componentName);
						// resourceData.addToComponents(newComponent);
					}
					else {
						logger.warning("Create a component requires a name");
					}
				}
				else {
					logger.warning("Cannot create component in null resource data");
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return newComponent;

		}

		@Override
		public DataBinding<String> getComponentName() {
			if (componentName == null) {
				componentName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				componentName.setBindingName("componentName");
			}
			return componentName;
		}

		@Override
		public void setComponentName(DataBinding<String> componentName) {
			if (componentName != null) {
				componentName.setOwner(this);
				componentName.setDeclaredType(String.class);
				componentName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				componentName.setBindingName("componentName");
			}
			this.componentName = componentName;
		}

		@Override
		public DataBinding<AtelierBComponent> getRefines() {
			if (refines == null) {
				refines = new DataBinding<>(this, AtelierBComponent.class, DataBinding.BindingDefinitionType.GET);
				refines.setBindingName("refines");
			}
			return refines;
		}

		@Override
		public void setRefines(DataBinding<AtelierBComponent> refines) {
			if (refines != null) {
				refines.setOwner(this);
				refines.setDeclaredType(AtelierBComponent.class);
				refines.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				refines.setBindingName("refines");
			}
			this.refines = refines;
		}

		@Override
		public DataBinding<AtelierBComponent> getExtends() {
			if (extendsB == null) {
				extendsB = new DataBinding<>(this, AtelierBComponent.class, DataBinding.BindingDefinitionType.GET);
				extendsB.setBindingName("extends");
			}
			return extendsB;
		}

		@Override
		public void setExtends(DataBinding<AtelierBComponent> extendsB) {
			if (extendsB != null) {
				extendsB.setOwner(this);
				extendsB.setDeclaredType(AtelierBComponent.class);
				extendsB.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				extendsB.setBindingName("extends");
			}
			this.extendsB = extendsB;
		}

		@Override
		public DataBinding<AtelierBComponent> getSees() {
			if (sees == null) {
				sees = new DataBinding<>(this, AtelierBComponent.class, DataBinding.BindingDefinitionType.GET);
				sees.setBindingName("sees");
			}
			return sees;
		}

		@Override
		public void setSees(DataBinding<AtelierBComponent> sees) {
			if (sees != null) {
				sees.setOwner(this);
				sees.setDeclaredType(AtelierBComponent.class);
				sees.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sees.setBindingName("sees");
			}
			this.sees = sees;
		}

		@Override
		public BComponentType getComponentType() {
			BComponentType returned = (BComponentType) performSuperGetter(COMPONENT_TYPE_KEY);
			if (returned == null) {
				return BComponentType.Machine;
			}
			return returned;
		}

	}

	@DefineValidationRule
	public static class ComponentNameBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddAtelierBComponent> {
		public ComponentNameBindingIsRequiredAndMustBeValid() {
			super("'componentName'_binding_is_required_and_must_be_valid", AddAtelierBComponent.class);
		}

		@Override
		public DataBinding<String> getBinding(AddAtelierBComponent object) {
			return object.getComponentName();
		}

	}

}
