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
	public static final String FORMOSE_VIEWPOINT_URI = "http://formose.lacl.fr/Formose.viewpoint";
	public static final String DOCUMENT_LIBRARY_VIEWPOINT_URI = "http://formose.lacl.fr/DocumentLibrary.viewpoint";
	public static final String SYSML_KAOS_VIEWPOINT_URI = "http://formose.lacl.fr/SysMLKaos/SysMLKaos.viewpoint";
	public static final String DOMAIN_MODELLING_VIEWPOINT_URI = "http://formose.lacl.fr//resources/DomainModel/DomainModelling.fml";
	public static final String DOMAIN_MODEL_URI = "http://formose.lacl.fr//resources/DomainModel/DomainModelling.fml/DomainModel.fml";

	public static final String FORMOSE_CORE_URI = "http://formose.lacl.fr/Formose.viewpoint/FormoseCore.fml";
	public static final String METHOLOGY_URI = "http://formose.lacl.fr/Formose.viewpoint/Methodology.fml";
	public static final String DOCUMENT_ANNOTATION_METHOLOGY_URI = "http://formose.lacl.fr/Formose.viewpoint/DocumentAnnotation-Methodology.fml";
	public static final String SYSML_KAOS_METHOLOGY_URI = "http://formose.lacl.fr/Formose.viewpoint/SysMLKaos-Methodology.fml";
	public static final String DOMAIN_MODEL_METHOLOGY_URI = "http://formose.lacl.fr/Formose.viewpoint/DomainModel-Methodology.fml";

	public static final String KAOS_RELATIVE_PATH = "Kaos/";
	public static final String DOCX_RELATIVE_PATH = "DocX/";

	// public static final String REQUIREMENT_DOCUMENT_VM_NAME = "RequirementDocumentVM";
	// public static final String IDENTIFIED_REQUIREMENT_NAME = "IdentifiedRequirement";
	// public static final String NAME_ROLE_NAME = "name";
	// public static final String IDENTIFIED_FRAGMENTS_ROLE_NAME = "identifiedFragments";
	// public static final String IDENTIFIED_FRAGMENT_NAME = "IdentifiedFragment";
	// public static final String PARENT_IDENTIFIED_REQUIREMENT_NAME = "identifiedRequirement";
	// public static final String FRAGMENT_ROLE_NAME = "docFragment";

	// public static final String DOC_REFERENCES_VM_NAME = "DocReferences";
	public static final String ABSTRACT_DOCUMENT_VM_NAME = "AbstractDocument";
	public static final String REFERENCE_GROUP_CONCEPT_NAME = "ReferenceGroup";
	public static final String REFERENCE_CONCEPT_NAME = "Reference";
	// public static final String REFERENCE_GROUP_ROLE_NAME = "referenceGroup";
	public static final String REFERENCES_ROLE_NAME = "references";
	public static final String NAME_ROLE_NAME = "name";
	public static final String DESCRIPTION_ROLE_NAME = "description";
	public static final String RAW_TEXT_ROLE_NAME = "rawText";

	public static final String FORMOSE_VM_NAME = "FormoseCore";
	public static final String ELEMENT_CONCEPT_NAME = "Element";
	public static final String ELEMENT_IDENTIFIER_ROLE_NAME = "identifier";
	public static final String PARENT_ROLE_NAME = "parent";
	public static final String CHILDREN_ELEMENTS_ROLE_NAME = "childrenElements";
	public static final String REQUIREMENT_PROPERTY_NAME = "requirements";
	public static final String REQUIREMENT_CONCEPT_NAME = "Requirement";
	public static final String PROJECT_ELEMENT_ROLE_NAME = "projectElement";
	public static final String METHODOLOGY_ROLE_NAME = "methodology";
	public static final String GOAL_MODELING_DIAGRAM_ROLE_NAME = "goalModelingDiagram";
	public static final String OPEN_GOAL_MODELING_DIAGRAM_NAME = "openGoalModelingDiagram";

	public static final String SYSML_KAOS_METHODOLOGY_VM_NAME = "SysMLKaos-Methodology";
	public static final String SYSML_KAOS_ELEMENT_MAPPING_CONCEPT_NAME = "SysMLKaosElementMapping";
	public static final String DIAGRAM_MAPPING_CONCEPT_NAME = "DiagramMapping";

	public static final String DOC_ANNOTATION_METHODOLOGY_VM_NAME = "DocumentAnnotation-Methodology";
	public static final String UNCLASSIFIED_CONCEPT_NAME = "Unclassified";
	public static final String ELEMENT_REFERENCE_CONCEPT_NAME = "ElementReference";
	public static final String REQUIREMENT_REFERENCE_CONCEPT_NAME = "RequirementReference";

	public static final String DOMAIN_MODEL_METHODOLOGY_VM_NAME = "DomainModel-Methodology";
	public static final String DOMAIN_MODEL_VM_NAME = "DomainModel";
	public static final String DOMAIN_MODELLING_ELEMENT_MAPPING_CONCEPT_NAME = "DomainModellingElementMapping";
	public static final String DOMAIN_MODEL_MAPPING_CONCEPT_NAME = "DomainModelMapping";

	public static final String B_METHODOLOGY_VM_NAME = "B-Methodology";
	public static final String B_ELEMENT_MAPPING_CONCEPT_NAME = "BElementMapping";
	public static final String B_MAPPING_CONCEPT_NAME = "BMapping";

	public static final String FORMOSE_VIEW_NAME = "FormoseView";
	public static final String REQUIREMENT_DOCUMENT_VMI_NAME = "RequirementDocumentVMI";
	public static final String FORMOSE_VMI_NAME = "FormoseVMI";
	public static final String FORMOSE_VIEW_CREATION_SCHEME_NAME = "initFormoseView";

	private FMSConstants() {
		// Only constants, prevent possible instantiation.
	}
}
