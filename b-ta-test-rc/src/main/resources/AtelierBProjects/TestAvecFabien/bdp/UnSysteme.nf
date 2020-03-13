Normalised(
THEORY MagicNumberX IS
  MagicNumber(Machine(UnSysteme))==(3.5)
END
&
THEORY UpperLevelX IS
  First_Level(Machine(UnSysteme))==(Machine(UnSysteme));
  Level(Machine(UnSysteme))==(0)
END
&
THEORY LoadedStructureX IS
  Machine(UnSysteme)
END
&
THEORY ListSeesX IS
  List_Sees(Machine(UnSysteme))==(?)
END
&
THEORY ListUsesX IS
  List_Uses(Machine(UnSysteme))==(?)
END
&
THEORY ListIncludesX IS
  Inherited_List_Includes(Machine(UnSysteme))==(?);
  List_Includes(Machine(UnSysteme))==(?)
END
&
THEORY ListPromotesX IS
  List_Promotes(Machine(UnSysteme))==(?)
END
&
THEORY ListExtendsX IS
  List_Extends(Machine(UnSysteme))==(?)
END
&
THEORY ListVariablesX IS
  External_Context_List_Variables(Machine(UnSysteme))==(?);
  Context_List_Variables(Machine(UnSysteme))==(?);
  Abstract_List_Variables(Machine(UnSysteme))==(?);
  Local_List_Variables(Machine(UnSysteme))==(nn);
  List_Variables(Machine(UnSysteme))==(nn);
  External_List_Variables(Machine(UnSysteme))==(nn)
END
&
THEORY ListVisibleVariablesX IS
  Inherited_List_VisibleVariables(Machine(UnSysteme))==(?);
  Abstract_List_VisibleVariables(Machine(UnSysteme))==(?);
  External_List_VisibleVariables(Machine(UnSysteme))==(?);
  Expanded_List_VisibleVariables(Machine(UnSysteme))==(?);
  List_VisibleVariables(Machine(UnSysteme))==(?);
  Internal_List_VisibleVariables(Machine(UnSysteme))==(?)
END
&
THEORY ListInvariantX IS
  Gluing_Seen_List_Invariant(Machine(UnSysteme))==(btrue);
  Gluing_List_Invariant(Machine(UnSysteme))==(btrue);
  Expanded_List_Invariant(Machine(UnSysteme))==(btrue);
  Abstract_List_Invariant(Machine(UnSysteme))==(btrue);
  Context_List_Invariant(Machine(UnSysteme))==(btrue);
  List_Invariant(Machine(UnSysteme))==(nn: NATURAL & nn<=dd)
END
&
THEORY ListAssertionsX IS
  Expanded_List_Assertions(Machine(UnSysteme))==(btrue);
  Abstract_List_Assertions(Machine(UnSysteme))==(btrue);
  Context_List_Assertions(Machine(UnSysteme))==(btrue);
  List_Assertions(Machine(UnSysteme))==(btrue)
END
&
THEORY ListCoverageX IS
  List_Coverage(Machine(UnSysteme))==(btrue)
END
&
THEORY ListExclusivityX IS
  List_Exclusivity(Machine(UnSysteme))==(btrue & (nn<dd => btrue & not(nn>0)))
END
&
THEORY ListInitialisationX IS
  Expanded_List_Initialisation(Machine(UnSysteme))==(nn:=0);
  Context_List_Initialisation(Machine(UnSysteme))==(skip);
  List_Initialisation(Machine(UnSysteme))==(nn:=0)
END
&
THEORY ListParametersX IS
  List_Parameters(Machine(UnSysteme))==(?)
END
&
THEORY ListInstanciatedParametersX END
&
THEORY ListConstraintsX IS
  List_Context_Constraints(Machine(UnSysteme))==(btrue);
  List_Constraints(Machine(UnSysteme))==(btrue)
END
&
THEORY ListOperationsX IS
  Internal_List_Operations(Machine(UnSysteme))==(ML_In,ML_Out);
  List_Operations(Machine(UnSysteme))==(ML_In,ML_Out)
END
&
THEORY ListInputX IS
  List_Input(Machine(UnSysteme),ML_In)==(?);
  List_Input(Machine(UnSysteme),ML_Out)==(?)
END
&
THEORY ListOutputX IS
  List_Output(Machine(UnSysteme),ML_In)==(?);
  List_Output(Machine(UnSysteme),ML_Out)==(?)
END
&
THEORY ListHeaderX IS
  List_Header(Machine(UnSysteme),ML_In)==(ML_In);
  List_Header(Machine(UnSysteme),ML_Out)==(ML_Out)
END
&
THEORY ListOperationGuardX IS
  List_Operation_Guard(Machine(UnSysteme),ML_In)==(nn>0);
  List_Operation_Guard(Machine(UnSysteme),ML_Out)==(nn<dd)
END
&
THEORY ListPreconditionX IS
  List_Precondition(Machine(UnSysteme),ML_In)==(btrue);
  List_Precondition(Machine(UnSysteme),ML_Out)==(btrue)
END
&
THEORY ListSubstitutionX IS
  Expanded_List_Substitution(Machine(UnSysteme),ML_Out)==(btrue | nn<dd ==> nn:=nn+1);
  Expanded_List_Substitution(Machine(UnSysteme),ML_In)==(btrue | nn>0 ==> nn:=nn-1);
  List_Substitution(Machine(UnSysteme),ML_In)==(SELECT nn>0 THEN nn:=nn-1 END);
  List_Substitution(Machine(UnSysteme),ML_Out)==(SELECT nn<dd THEN nn:=nn+1 END)
END
&
THEORY ListConstantsX IS
  List_Valuable_Constants(Machine(UnSysteme))==(dd,nm);
  Inherited_List_Constants(Machine(UnSysteme))==(?);
  List_Constants(Machine(UnSysteme))==(dd,nm)
END
&
THEORY ListSetsX IS
  Context_List_Enumerated(Machine(UnSysteme))==(?);
  Context_List_Defered(Machine(UnSysteme))==(?);
  Context_List_Sets(Machine(UnSysteme))==(?);
  List_Valuable_Sets(Machine(UnSysteme))==(?);
  Inherited_List_Enumerated(Machine(UnSysteme))==(?);
  Inherited_List_Defered(Machine(UnSysteme))==(?);
  Inherited_List_Sets(Machine(UnSysteme))==(?);
  List_Enumerated(Machine(UnSysteme))==(?);
  List_Defered(Machine(UnSysteme))==(?);
  List_Sets(Machine(UnSysteme))==(?)
END
&
THEORY ListHiddenConstantsX IS
  Abstract_List_HiddenConstants(Machine(UnSysteme))==(?);
  Expanded_List_HiddenConstants(Machine(UnSysteme))==(?);
  List_HiddenConstants(Machine(UnSysteme))==(?);
  External_List_HiddenConstants(Machine(UnSysteme))==(?)
END
&
THEORY ListPropertiesX IS
  Abstract_List_Properties(Machine(UnSysteme))==(btrue);
  Context_List_Properties(Machine(UnSysteme))==(btrue);
  Inherited_List_Properties(Machine(UnSysteme))==(btrue);
  List_Properties(Machine(UnSysteme))==(dd: NATURAL & dd>0 & nm = dd|->dd)
END
&
THEORY ListSeenInfoX END
&
THEORY ListANYVarX IS
  List_ANY_Var(Machine(UnSysteme),ML_In)==(?);
  List_ANY_Var(Machine(UnSysteme),ML_Out)==(?)
END
&
THEORY ListOfIdsX IS
  List_Of_Ids(Machine(UnSysteme)) == (dd,nm | ? | nn | ? | ML_In,ML_Out | ? | ? | ? | UnSysteme);
  List_Of_HiddenCst_Ids(Machine(UnSysteme)) == (? | ?);
  List_Of_VisibleCst_Ids(Machine(UnSysteme)) == (dd,nm);
  List_Of_VisibleVar_Ids(Machine(UnSysteme)) == (? | ?);
  List_Of_Ids_SeenBNU(Machine(UnSysteme)) == (?: ?)
END
&
THEORY ConstantsEnvX IS
  Constants(Machine(UnSysteme)) == (Type(dd) == Cst(btype(INTEGER,?,?));Type(nm) == Cst(btype(INTEGER,?,?)*btype(INTEGER,?,?)))
END
&
THEORY VariablesEnvX IS
  Variables(Machine(UnSysteme)) == (Type(nn) == Mvl(btype(INTEGER,?,?)))
END
&
THEORY OperationsEnvX IS
  Operations(Machine(UnSysteme)) == (Type(ML_Out) == Cst(No_type,No_type);Type(ML_In) == Cst(No_type,No_type))
END
&
THEORY TCIntRdX IS
  predB0 == KO;
  extended_sees == KO;
  B0check_tab == KO;
  local_op == OK;
  abstract_constants_visible_in_values == KO;
  project_type == SYSTEM_TYPE;
  event_b_deadlockfreeness == KO;
  variant_clause_mandatory == OK;
  event_b_coverage == OK;
  event_b_exclusivity == OK;
  genFeasibilityPO == OK
END
)
