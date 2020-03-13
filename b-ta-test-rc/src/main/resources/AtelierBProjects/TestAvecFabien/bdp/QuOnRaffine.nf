Normalised(
THEORY MagicNumberX IS
  MagicNumber(Refinement(QuOnRaffine))==(3.5)
END
&
THEORY UpperLevelX IS
  First_Level(Refinement(QuOnRaffine))==(Machine(UnSysteme));
  Level(Refinement(QuOnRaffine))==(1);
  Upper_Level(Refinement(QuOnRaffine))==(Machine(UnSysteme))
END
&
THEORY LoadedStructureX IS
  Refinement(QuOnRaffine)
END
&
THEORY ListSeesX IS
  List_Sees(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListIncludesX IS
  Inherited_List_Includes(Refinement(QuOnRaffine))==(?);
  List_Includes(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListPromotesX IS
  List_Promotes(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListExtendsX IS
  List_Extends(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListVariablesX IS
  External_Context_List_Variables(Refinement(QuOnRaffine))==(?);
  Context_List_Variables(Refinement(QuOnRaffine))==(?);
  Abstract_List_Variables(Refinement(QuOnRaffine))==(nn);
  Local_List_Variables(Refinement(QuOnRaffine))==(cc,bb,aa);
  List_Variables(Refinement(QuOnRaffine))==(cc,bb,aa);
  External_List_Variables(Refinement(QuOnRaffine))==(cc,bb,aa)
END
&
THEORY ListVisibleVariablesX IS
  Inherited_List_VisibleVariables(Refinement(QuOnRaffine))==(?);
  Abstract_List_VisibleVariables(Refinement(QuOnRaffine))==(?);
  External_List_VisibleVariables(Refinement(QuOnRaffine))==(?);
  Expanded_List_VisibleVariables(Refinement(QuOnRaffine))==(?);
  List_VisibleVariables(Refinement(QuOnRaffine))==(?);
  Internal_List_VisibleVariables(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListOfNewVariablesX IS
  List_Of_New_Variables(Refinement(QuOnRaffine))==(cc,bb,aa)
END
&
THEORY ListInvariantX IS
  Gluing_Seen_List_Invariant(Refinement(QuOnRaffine))==(btrue);
  Expanded_List_Invariant(Refinement(QuOnRaffine))==(btrue);
  Abstract_List_Invariant(Refinement(QuOnRaffine))==(nn: NATURAL & nn<=dd);
  Context_List_Invariant(Refinement(QuOnRaffine))==(btrue);
  List_Invariant(Refinement(QuOnRaffine))==(aa: NATURAL & bb: NATURAL & cc: NATURAL & aa+bb+cc = nn & (aa = 0 or cc = 0))
END
&
THEORY ListVariantX IS
  List_Variant(Refinement(QuOnRaffine))==(btrue)
END
&
THEORY ListAssertionsX IS
  Expanded_List_Assertions(Refinement(QuOnRaffine))==(btrue);
  Abstract_List_Assertions(Refinement(QuOnRaffine))==(btrue);
  Context_List_Assertions(Refinement(QuOnRaffine))==(btrue);
  List_Assertions(Refinement(QuOnRaffine))==(btrue)
END
&
THEORY ListCoverageX IS
  List_Coverage(Refinement(QuOnRaffine))==(btrue & ("ML_Out event guard" & nn<dd => aa+bb<dd & cc = 0 or (aa = 0 & bb>0 or aa>0)) & ("ML_In event guard" & nn>0 => aa>0 or (aa = 0 & bb>0 or aa>0)))
END
&
THEORY ListExclusivityX IS
  List_Exclusivity(Refinement(QuOnRaffine))==(btrue & ("IL_In event exclusivity" & ("IL_Out event guard" & (aa = 0 & bb>0)) => btrue & not(aa>0)) & ("IL_In event guard" & aa>0 => btrue))
END
&
THEORY ListInitialisationX IS
  Expanded_List_Initialisation(Refinement(QuOnRaffine))==(aa,bb,cc:=0,0,0);
  Context_List_Initialisation(Refinement(QuOnRaffine))==(skip);
  List_Initialisation(Refinement(QuOnRaffine))==(aa:=0 || bb:=0 || cc:=0)
END
&
THEORY ListOperationsX IS
  List_Operations(Refinement(QuOnRaffine),Machine(UnSysteme))==(IL_In,IL_Out);
  Internal_List_Operations(Refinement(QuOnRaffine))==(ML_In,ML_Out,IL_In,IL_Out);
  List_Operations(Refinement(QuOnRaffine))==(ML_In,ML_Out,IL_In,IL_Out)
END
&
THEORY ListInputX IS
  List_Input(Machine(UnSysteme),IL_Out)==(?);
  List_Input(Refinement(QuOnRaffine),ML_In)==(?);
  List_Input(Refinement(QuOnRaffine),ML_Out)==(?);
  List_Input(Refinement(QuOnRaffine),IL_In)==(?);
  List_Input(Refinement(QuOnRaffine),IL_Out)==(?);
  List_Input(Machine(UnSysteme),IL_In)==(?)
END
&
THEORY ListOutputX IS
  List_Output(Machine(UnSysteme),IL_Out)==(?);
  List_Output(Refinement(QuOnRaffine),ML_In)==(?);
  List_Output(Refinement(QuOnRaffine),ML_Out)==(?);
  List_Output(Refinement(QuOnRaffine),IL_In)==(?);
  List_Output(Refinement(QuOnRaffine),IL_Out)==(?);
  List_Output(Machine(UnSysteme),IL_In)==(?)
END
&
THEORY ListHeaderX IS
  List_Header(Machine(UnSysteme),IL_Out)==(IL_Out);
  List_Header(Refinement(QuOnRaffine),ML_In)==(ML_In);
  List_Header(Refinement(QuOnRaffine),ML_Out)==(ML_Out);
  List_Header(Refinement(QuOnRaffine),IL_In)==(IL_In);
  List_Header(Refinement(QuOnRaffine),IL_Out)==(IL_Out);
  List_Header(Machine(UnSysteme),IL_In)==(IL_In)
END
&
THEORY ListOperationGuardX IS
  List_Operation_Guard(Refinement(QuOnRaffine),ML_In)==(aa>0);
  List_Operation_Guard(Refinement(QuOnRaffine),ML_Out)==(aa+bb<dd & cc = 0);
  List_Operation_Guard(Refinement(QuOnRaffine),IL_In)==(aa>0);
  List_Operation_Guard(Refinement(QuOnRaffine),IL_Out)==(aa = 0 & bb>0)
END
&
THEORY ListPreconditionX IS
  Own_Precondition(Machine(UnSysteme),IL_Out)==(btrue);
  Own_Precondition(Refinement(QuOnRaffine),ML_In)==(btrue);
  List_Precondition(Refinement(QuOnRaffine),ML_In)==(btrue);
  Own_Precondition(Refinement(QuOnRaffine),ML_Out)==(btrue);
  List_Precondition(Refinement(QuOnRaffine),ML_Out)==(btrue);
  Own_Precondition(Refinement(QuOnRaffine),IL_In)==(btrue);
  List_Precondition(Refinement(QuOnRaffine),IL_In)==(btrue);
  Own_Precondition(Refinement(QuOnRaffine),IL_Out)==(btrue);
  List_Precondition(Refinement(QuOnRaffine),IL_Out)==(btrue);
  List_Precondition(Machine(UnSysteme),IL_Out)==(btrue);
  Own_Precondition(Machine(UnSysteme),IL_In)==(btrue);
  List_Precondition(Machine(UnSysteme),IL_In)==(btrue)
END
&
THEORY ListSubstitutionX IS
  Expanded_List_Substitution(Machine(UnSysteme),IL_Out)==(btrue | skip);
  Expanded_List_Substitution(Refinement(QuOnRaffine),IL_Out)==(btrue | aa = 0 & bb>0 ==> cc,bb:=cc+1,bb-1);
  Expanded_List_Substitution(Refinement(QuOnRaffine),IL_In)==(btrue | aa>0 ==> aa,bb:=aa-1,bb+1);
  Expanded_List_Substitution(Refinement(QuOnRaffine),ML_Out)==(btrue | aa+bb<dd & cc = 0 ==> aa:=aa+1);
  Expanded_List_Substitution(Refinement(QuOnRaffine),ML_In)==(btrue | aa>0 ==> aa:=aa-1);
  List_Substitution(Refinement(QuOnRaffine),ML_In)==(SELECT aa>0 THEN aa:=aa-1 END);
  List_Substitution(Refinement(QuOnRaffine),ML_Out)==(SELECT aa+bb<dd & cc = 0 THEN aa:=aa+1 END);
  List_Substitution(Refinement(QuOnRaffine),IL_In)==(SELECT aa>0 THEN aa:=aa-1 || bb:=bb+1 END);
  List_Substitution(Refinement(QuOnRaffine),IL_Out)==(SELECT aa = 0 & bb>0 THEN cc:=cc+1 || bb:=bb-1 END);
  List_Substitution(Machine(UnSysteme),IL_Out)==(skip);
  Expanded_List_Substitution(Machine(UnSysteme),IL_In)==(btrue | skip);
  List_Substitution(Machine(UnSysteme),IL_In)==(skip)
END
&
THEORY ListParametersX IS
  List_Parameters(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListInstanciatedParametersX END
&
THEORY ListConstraintsX IS
  List_Constraints(Refinement(QuOnRaffine))==(btrue);
  List_Context_Constraints(Refinement(QuOnRaffine))==(btrue)
END
&
THEORY ListConstantsX IS
  List_Valuable_Constants(Refinement(QuOnRaffine))==(dd,nm,ee);
  Inherited_List_Constants(Refinement(QuOnRaffine))==(dd,nm);
  List_Constants(Refinement(QuOnRaffine))==(ee)
END
&
THEORY ListSetsX IS
  Context_List_Enumerated(Refinement(QuOnRaffine))==(?);
  Context_List_Defered(Refinement(QuOnRaffine))==(?);
  Context_List_Sets(Refinement(QuOnRaffine))==(?);
  List_Valuable_Sets(Refinement(QuOnRaffine))==(?);
  Inherited_List_Enumerated(Refinement(QuOnRaffine))==(?);
  Inherited_List_Defered(Refinement(QuOnRaffine))==(?);
  Inherited_List_Sets(Refinement(QuOnRaffine))==(?);
  List_Enumerated(Refinement(QuOnRaffine))==(?);
  List_Defered(Refinement(QuOnRaffine))==(?);
  List_Sets(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListHiddenConstantsX IS
  Abstract_List_HiddenConstants(Refinement(QuOnRaffine))==(?);
  Expanded_List_HiddenConstants(Refinement(QuOnRaffine))==(?);
  List_HiddenConstants(Refinement(QuOnRaffine))==(?);
  External_List_HiddenConstants(Refinement(QuOnRaffine))==(?)
END
&
THEORY ListPropertiesX IS
  Abstract_List_Properties(Refinement(QuOnRaffine))==(dd: NATURAL & dd>0 & nm = dd|->dd);
  Context_List_Properties(Refinement(QuOnRaffine))==(btrue);
  Inherited_List_Properties(Refinement(QuOnRaffine))==(btrue);
  List_Properties(Refinement(QuOnRaffine))==(ee = dd)
END
&
THEORY ListSeenInfoX END
&
THEORY ListANYVarX IS
  List_ANY_Var(Refinement(QuOnRaffine),ML_In)==(?);
  List_ANY_Var(Refinement(QuOnRaffine),ML_Out)==(?);
  List_ANY_Var(Refinement(QuOnRaffine),IL_In)==(?);
  List_ANY_Var(Refinement(QuOnRaffine),IL_Out)==(?)
END
&
THEORY ListOfIdsX IS
  List_Of_Ids(Refinement(QuOnRaffine)) == (ee | ? | cc,bb,aa | ? | ML_In,ML_Out,IL_In,IL_Out | ? | ? | ? | QuOnRaffine);
  List_Of_HiddenCst_Ids(Refinement(QuOnRaffine)) == (? | ?);
  List_Of_VisibleCst_Ids(Refinement(QuOnRaffine)) == (ee);
  List_Of_VisibleVar_Ids(Refinement(QuOnRaffine)) == (? | ?);
  List_Of_Ids_SeenBNU(Refinement(QuOnRaffine)) == (?: ?)
END
&
THEORY ConstantsEnvX IS
  Constants(Refinement(QuOnRaffine)) == (Type(ee) == Cst(btype(INTEGER,?,?));Type(nm) == Cst(btype(INTEGER,?,?)*btype(INTEGER,?,?));Type(dd) == Cst(btype(INTEGER,?,?)))
END
&
THEORY VariablesEnvX IS
  Variables(Refinement(QuOnRaffine)) == (Type(cc) == Mvl(btype(INTEGER,?,?));Type(bb) == Mvl(btype(INTEGER,?,?));Type(aa) == Mvl(btype(INTEGER,?,?)))
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
  variant_clause_mandatory == KO;
  event_b_coverage == OK;
  event_b_exclusivity == OK;
  genFeasibilityPO == OK
END
)
