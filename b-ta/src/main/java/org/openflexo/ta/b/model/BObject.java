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

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.DeserializationInitializer;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.Updater;
import org.openflexo.ta.b.BTechnologyAdapter;

/**
 * Common API for all objects involved in B model
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface BObject extends FlexoObject, InnerResourceData<BComponent>, TechnologyObject<BTechnologyAdapter> {

	@PropertyIdentifier(type = BPrettyPrintDelegate.class)
	public static final String PRETTY_PRINT_DELEGATE_KEY = "prettyPrintDelegate";

	/**
	 * Return the model factory which manages this {@link BObject}
	 * 
	 * @return
	 */
	public BModelFactory getFactory();

	/**
	 * Return serialization identifier of this object<br>
	 * Assert that the {@link BComponent} (the ResourceData) in which this object is stored is kwown<br>
	 * (You dont have to serialize BComponent)
	 * 
	 * @return
	 */
	public String getSerializationIdentifier();

	/**
	 * Return {@link BComponent} in which this object is declared
	 * 
	 * @return
	 */
	public BComponent getComponent();

	@Getter(value = PRETTY_PRINT_DELEGATE_KEY, ignoreType = true)
	public BPrettyPrintDelegate<?> getPrettyPrintDelegate();

	@Setter(PRETTY_PRINT_DELEGATE_KEY)
	public void setPrettyPrintDelegate(BPrettyPrintDelegate<?> delegate);

	@Updater(PRETTY_PRINT_DELEGATE_KEY)
	public void updateWithPrettyPrintDelegate(BPrettyPrintDelegate<?> delegate);

	public String getBPrettyPrint();

	public String getNormalizedBRepresentation();

	@DeserializationInitializer
	public void initializeDeserialization(BModelFactory factory);

	/**
	 * Default base implementation for {@link BObject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class BObjectImpl extends FlexoObjectImpl implements BObject {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(BObjectImpl.class.getPackage().getName());

		@Override
		public BTechnologyAdapter getTechnologyAdapter() {
			if (getResourceData() != null && getResourceData().getResource() != null) {
				return getResourceData().getResource().getTechnologyAdapter();
			}
			return null;
		}

		@Override
		public void initializeDeserialization(BModelFactory factory) {
			// Do something here when required
		}

		@Override
		public BModelFactory getFactory() {
			return getResourceData().getResource().getFactory();
		}

		@Override
		public final BComponent getResourceData() {
			return getComponent();
		}

		@Override
		public final String toString() {
			return ("[" + getImplementedInterface().getSimpleName() + "]");
		}

		@Override
		public String getBPrettyPrint() {
			if (getPrettyPrintDelegate() != null) {
				return getPrettyPrintDelegate().getRepresentation(getPrettyPrintDelegate().makePrettyPrintContext());
			}
			return "<Cannot pretty-print:" + this + ">";
		}

		@Override
		public String getNormalizedBRepresentation() {
			if (getPrettyPrintDelegate() != null) {
				return getPrettyPrintDelegate().getNormalizedRepresentation(getPrettyPrintDelegate().makePrettyPrintContext());
			}
			return "<Cannot pretty-print:" + this + ">";
		}

		@Override
		public void updateWithPrettyPrintDelegate(BPrettyPrintDelegate<?> delegate) {
			System.out.println("-----------------> OK, ca marche dans " + this);
			((BPrettyPrintDelegate) delegate).setModelObject(this);
			setPrettyPrintDelegate(delegate);
		}

	}
}
