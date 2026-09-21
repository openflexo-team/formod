/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

package org.openflexo.module.formose;

/**
 * References all constants used in the scope of Formose project
 * 
 * @author sylvain
 */
public class FMSConstants {

	public static final String FORMOSE_RC_URI = "http://formose.lacl.fr/";

	// The VirtualModels of the Formose resource center, whose URI is the URI of the resource center followed by the path of the file

	public static final String FORMOSE_URI = FORMOSE_RC_URI + "Formose.fml";
	public static final String FORMOSE_CORE_URI = FORMOSE_URI + "/FormoseCore.fml";
	public static final String METHODOLOGY_URI = FORMOSE_URI + "/Methodology.fml";
	public static final String DOCUMENT_ANNOTATION_METHODOLOGY_URI = FORMOSE_URI + "/DocumentAnnotationMethodology.fml";
	public static final String SYSML_KAOS_METHODOLOGY_URI = FORMOSE_URI + "/SysMLKaosMethodology.fml";
	public static final String DOMAIN_MODEL_METHODOLOGY_URI = FORMOSE_URI + "/DomainModelMethodology.fml";
	public static final String B_METHODOLOGY_URI = FORMOSE_URI + "/BMethodology.fml";

	public static final String DOCUMENT_LIBRARY_URI = FORMOSE_RC_URI + "DocumentLibrary.fml";
	public static final String ABSTRACT_DOCUMENT_URI = DOCUMENT_LIBRARY_URI + "/AbstractDocument.fml";
	public static final String WORD_DOCUMENT_URI = DOCUMENT_LIBRARY_URI + "/WordDocument.fml";

	public static final String SYSML_KAOS_URI = FORMOSE_RC_URI + "SysMLKaos/SysMLKaos.fml";
	public static final String SYSML_KAOS_MODEL_URI = SYSML_KAOS_URI + "/SysMLKaosModel.fml";
	public static final String GOAL_MODELING_DIAGRAM_URI = SYSML_KAOS_URI + "/GoalModelingDiagram.fml";

	public static final String DOMAIN_MODELLING_URI = FORMOSE_RC_URI + "DomainModel/DomainModelling.fml";
	public static final String DOMAIN_MODEL_URI = DOMAIN_MODELLING_URI + "/DomainModel.fml";
	public static final String DOMAIN_MODEL_DIAGRAM_URI = DOMAIN_MODELLING_URI + "/DomainModelDiagram.fml";

	// The names of the VirtualModels, as a container names the ones it contains

	public static final String FORMOSE_CORE_VM_NAME = "FormoseCore";
	public static final String DOC_ANNOTATION_METHODOLOGY_VM_NAME = "DocumentAnnotationMethodology";
	public static final String SYSML_KAOS_METHODOLOGY_VM_NAME = "SysMLKaosMethodology";
	public static final String DOMAIN_MODEL_METHODOLOGY_VM_NAME = "DomainModelMethodology";
	public static final String B_METHODOLOGY_VM_NAME = "BMethodology";
	public static final String ABSTRACT_DOCUMENT_VM_NAME = "AbstractDocument";
	public static final String DOMAIN_MODEL_VM_NAME = "DomainModel";

	// The concepts of the core model and of the document library

	public static final String ELEMENT_CONCEPT_NAME = "Element";
	public static final String REQUIREMENT_CONCEPT_NAME = "Requirement";
	public static final String REFERENCE_CONCEPT_NAME = "Reference";

	// The concepts of the methodologies

	public static final String UNCLASSIFIED_CONCEPT_NAME = "Unclassified";
	public static final String ELEMENT_REFERENCE_CONCEPT_NAME = "ElementReference";
	public static final String REQUIREMENT_REFERENCE_CONCEPT_NAME = "RequirementReference";
	public static final String SYSML_KAOS_ELEMENT_MAPPING_CONCEPT_NAME = "SysMLKaosElementMapping";
	public static final String DIAGRAM_MAPPING_CONCEPT_NAME = "DiagramMapping";
	public static final String DOMAIN_MODELLING_ELEMENT_MAPPING_CONCEPT_NAME = "DomainModellingElementMapping";
	public static final String DOMAIN_MODEL_MAPPING_CONCEPT_NAME = "DomainModelMapping";
	public static final String B_ELEMENT_MAPPING_CONCEPT_NAME = "BElementMapping";
	public static final String B_MAPPING_CONCEPT_NAME = "BMapping";

	// The properties and behaviours of Element the module reads and calls

	public static final String ELEMENT_IDENTIFIER_ROLE_NAME = "identifier";
	public static final String DESCRIPTION_ROLE_NAME = "description";
	public static final String PARENT_ROLE_NAME = "parent";
	public static final String CHILDREN_ELEMENTS_ROLE_NAME = "childrenElements";
	public static final String PROJECT_ELEMENT_ROLE_NAME = "projectElement";
	public static final String REQUIREMENT_PROPERTY_NAME = "requirements";

	/** The document library, held by the document annotation methodology */
	public static final String DOCUMENT_LIBRARY_ROLE_NAME = "documentLibrary";

	/** The methodology declared on an element, or else the one applicable to its parent */
	public static final String APPLICABLE_SYSML_KAOS_METHODOLOGY_PROPERTY_NAME = "applicableSysMLKaosMethodology";
	public static final String APPLICABLE_DOMAIN_MODEL_METHODOLOGY_PROPERTY_NAME = "applicableDomainModelMethodology";
	public static final String APPLICABLE_B_METHODOLOGY_PROPERTY_NAME = "applicableBMethodology";

	/** Declares a methodology on an element */
	public static final String CREATE_SYSML_KAOS_METHODOLOGY_BEHAVIOUR_NAME = "createSysMLKaosMethodology";
	public static final String CREATE_DOMAIN_MODEL_METHODOLOGY_BEHAVIOUR_NAME = "createDomainModelMethodology";
	public static final String CREATE_B_METHODOLOGY_BEHAVIOUR_NAME = "createBMethodology";

	// The names the project gives to what it creates

	public static final String FORMOSE_VIEW_NAME = "FormoseView";
	public static final String FORMOSE_VMI_NAME = "FormoseVMI";
	public static final String FORMOSE_VIEW_CREATION_SCHEME_NAME = "initFormoseView";

	private FMSConstants() {
		// Only constants, prevent possible instantiation.
	}
}
