# formod — Known defects

Bugs of formod (the B technology adapter, the Formose module and its resource center) that are known and not fixed yet. Each entry
says what is verified, how to reproduce it, and how to work around it. Features and evolutions go to `BACKLOG.md`, not here.

Identifiers are stable and never reused: `FORMOD-D-<n>`. Reference them from commits and from other entries.

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

---

## B parser

### FORMOD-D-1 — The B grammar has no real literals  ·  `TODO`

**Symptom.** Parsing a B expression holding a real number fails: `BParser.parse("1.5", factory, EntryPointKind.Expression)` throws
`ParseException: [1,2] expecting: EOF`.

**Reproduction (verified 2026-09-16 by execution).** `TestParseBExpression.testExpression7` in `b-ta-test` fails with that exception.

**Mechanism — verified in the grammar.** `b-ta-parser/src/main/sablecc/b.sablecc` only declares `integer_literal = digit+`; there is
no real literal token nor any `expression` alternative for one. The lexer reads `1`, then `.` cannot continue the expression.

**Not a 2.99 migration regression.** The grammar is unchanged since the initial import (2020-03), so the test could not pass before
the migration either.

**Workaround.** None in B text: real values cannot be written in expressions handled by the B technology adapter.

### FORMOD-D-2 — TestBMethology.testReloadProject fails about one run in three  ·  `DONE`

**Symptom.** `formod-module`, uiTest `TestBMethology.testReloadProject` fails intermittently, right after the project is reloaded, on
the first methodology it asks the reloaded model for: `projectElement.execute("applicableSysMLKaosMethodology")` returns null, or the
next one does, or `getBElementMapping(...)` does. Measured 2026-10-07: two green runs, one red.

**Cause — not in formod.** The reloaded project holds two generations of the `FormoseVMI.fml.rt` resource, so the project element
exists twice and the identity comparison of `where (selected.declaringElement == this)` fails for the methodologies that fell on the
other side. Full measurement in `openflexo-core/KNOWN_DEFECTS.md`, CORE-D-28.

**Resolved by CORE-D-28 (2026-10-07).** Unloading a resource now resets the `FlexoObjectReference`s that cached one of its objects, so
a reference resolved before the project was closed no longer answers the dropped generation. Nothing changed in formod: the model and
the test were right. Measured: `TestBMethology` 6 runs in a row green (9/9) after the fix, against 1 red in 3 before. The test is kept
as it is — it is the cross-repository reproduction; the one inside openflexo-core is `TestReloadedResourceReferences`.

## Lost in the migration to textual FML — found by running the application (2026-10-07)

The four entries below were all invisible to the tests (which call the behaviours with explicit arguments and validate bindings), and
all found in the first interactive session of `formod-app`. They share one cause: the legacy `.fml.xml` carried information that has no
loud counterpart in textual FML, so it vanished without an error.

### FORMOD-D-3 — A functional goal dropped on a diagram has no goal and no label  ·  `DONE`

**Symptom.** Dropping a `FunctionalGoal` from the palette puts a shape on the diagram, with no label, and the `goal` role of its
`FunctionalGoalGR` is null: no goal exists in the model. Same for the other goal and agent drops.

**Mechanism — verified by the log and by reading the code.** `ContextualPalette.handleFMLControlledDrop` builds a `DropSchemeAction` from the
drop scheme alone and runs it: the arguments of the palette binding (`call=new FunctionalGoalGR::createFunctionalGoal("Goal", "", "")`) are
not used. The legacy `name` parameter was `isRequired`, so the platform asked for it in a wizard (`DropSchemeActionWizard.isSkipable()` is
false while a required parameter is empty). The migrated parameter was not `required`, hence valid when empty, the wizard was skipped, and
`FunctionalGoal.create` ran with a null name. The log shows `Found not initialized parameter … name, type, description` twice.

**Fixed (2026-10-07).** `required` restored — see FORMOD-D-4. Also restored, found on the way: the nine `label=` of the shapes and connectors
of `GoalModelingDiagram` (the legacy `GRSpec featureName="label"`), which the migration had dropped.

### FORMOD-D-4 — `isRequired` of the behaviour parameters was dropped  ·  `DONE`

**Symptom and extent.** The legacy declared 191 required parameters (`isRequired="true"`); the migrated FML kept one. Every wizard that
should insist on a name lets an empty value through.

**Fixed (2026-10-07).** 161 `required` restored by a script matching, per concept and behaviour, the legacy parameter with the current one
(a few were renamed: `concept` → `aConcept`, `from`/`to` → `fromGR`/`toGR`, `createLinke` → `createLink`). Left alone on purpose: the creation
schemes of the events (`FormoseCore` `NewElement`…, `SysMLKaosModel` events), which are fired by code and never shown in a wizard. The two
legacy methodologies built on missing VirtualModels are gone (FORMOD-F-1). `serializeGoalModel` got `required` on its workbook to stand for the
legacy `skipConfirmationPanel="false"`, which has no FML spelling on a plain behaviour.

### FORMOD-D-5 — The concept inspectors were dropped  ·  `DONE`

**Symptom.** Selecting any instance of a Formose concept showed the generic inspector: the entries of the legacy `<Inspector>` (goal name,
type, description, the cardinalities of an association, …) were gone. 52 concepts, about 120 entries.

**Fixed (2026-10-07).** One `Xxx.inspector` per concept in the container of its model, with an explicit `@Inspector("Xxx.inspector")`,
generated from the legacy serialization (`legacy_inspectors_to_container.py`, in the `migrate-fml-serialization` skill). Validated by
`FormodInspectorsTest` (52, bindings included). The graphical-representation concepts keep their own inspector (faithful to the legacy)
rather than deriving to the model concept (done later, FORMOD-F-3).

**Added, not restored (2026-10-08).** `Methodology.inspector` (name and declaring element, read-only): the legacy declared an inspector with no entry for the
methodologies. The four methodologies inherit it through the additive composition of inspectors (asserted by `FormodInspectorsTest`).

### FORMOD-D-6 — Required roles never set: methodologies and requirements are invalid  ·  `DONE`

**Symptom.** The methodologies show an error cross when a project is opened: `Missing required role name` (`Methodology.name`), `user`
(`Transition.user`), `status` (`Requirement.status`). All three are `[1,1]` and nothing set them — **already the case in the legacy**
(`cardinality="One"`, never assigned), so not a migration regression.

**Fixed (2026-10-07), decided with the user.** `name` is set in the creation scheme of each methodology (the view name for SysML/KAOS, the
model name otherwise), `status` is `"Not proved"` at creation (first value of the documented list), `user` is `System.getProperty("user.name")`.
Asserted in T1, T3, T6, T9 and T10.

### FORMOD-D-7 — `@Renderer("name")`: the expression was written as a string  ·  `DONE`

**Symptom.** The 14 `@Renderer` of the Formose models were written with quotes around the expression (`@Renderer("identifier")`, `@Renderer("element.identifier")`…) — the form of the legacy `renderer="…"` attribute. In FML the expression is written without quotes; quotes denote a string. The parser tolerated it (measured: the string representation of an element was still its identifier, `LandingGear`, and not the constant), and the FML printer wrote the expression back without the quotes: the source file did not say what the model meant.

**Fixed (2026-10-09).** The 14 occurrences are written `@Renderer(name)`, `@Renderer(identifier)`, `@Renderer(element.identifier)`… The scenarios assert it (`T1`: an element, `T4`: a goal).

### FORMOD-D-8 — The B components of a hand-made domain model are named `<element>null`  ·  `DONE`

**Symptom.** In the running application, a domain model mapping created by hand (`createTopLevelDomainModelMapping`, `createChildDomainModel`)
generated the Atelier B components `Projectnull.sys`, `ProjectnullContext.sys` and `Projectnull.ref`, and a second one raised a
`DuplicateURIException`. Seen on 2026-10-12 with a project where the domain model had no group of goals.

**Cause (measured).** `BMapping.createContext` / `createMachine` named the components `element.identifier + domainModelMapping.goalGroup.title`.
A mapping created by name has no `goalGroup`, so the title was `null`. The legacy `.fml.xml` had the same expression: not a migration regression.

**Fix (2026-10-12).** `DomainModelMapping` now stores its `title` (the title of its group of goals, or the name it was created with), set by
`createDomainModel`, and `BMethodology` names the components after it. `T9_DomainModelMethodology` asserts the titles and `T10_BMethodology`
generates the B models of a named mapping (`LandingGearSensors`, `LandingGearSensorsContext`). T10 fails with the old expression (32 tests,
1 failure) and passes with the fix (32/32).

**Not covered.** A mapping saved before the fix has no stored title: it keeps generating `…null` names. A project created after the fix is fine.

### FORMOD-D-9 — A group of goals drawn on the goal diagram has no title  ·  `DONE`

**Symptom.** In the running application (project `TestFormose9`, 2026-10-12), drawing a group of goals on the goal diagram opened no dialog, and
the group had a `null` title. Creating the domain model methodology then derived one domain model per group, named `Project-null`: two groups gave
two mappings with the same URI (`DuplicateURIException` ×3 in the log: the model instance, the diagram instance, the diagram resource).

**Cause (measured).** The legacy `DrawRectangleScheme` of `GoalGroupGR` carried `isRequired="true"` (widget `TEXT_FIELD`) on its `title`
parameter; the migrated `drawGoalGroup(String title)` did not: the same loss as FORMOD-D-4, which missed this scheme. Without `required`
the creation wizard is not shown.

**Fixed (2026-10-12).** `drawGoalGroup(required String title)`, with no default value on purpose: two groups with the same default title would
give the same domain model name again. `formod-test` 32/32. A scan of every legacy `isRequired="true"` parameter against the migrated files
found no other scheme in this case (the other differences are renamed parameters, and the parameters of events, left without `required` on purpose).

**Not covered.** Nothing prevents two groups from having the same title (same duplicate URI): not measured whether the application reports it
better than a `DuplicateURIException` in the log.

### FORMOD-D-10 — The B views of a reloaded project are empty  ·  `DONE`

**Symptom.** After closing and reopening a project holding a B methodology, the B perspective showed no B code (2026-10-12, `TestFormose12`/`13`).

**Cause (measured).** The references to the generated components (`AtelierBComponentActorReference`) find the Atelier B project through the model slot of their role, which a role infers from its `container` binding (`FlexoRole.getInferedModelSlot`). The legacy `AtelierBComponentRole` declarations carried `container="sourceProject"`; the migration dropped it, claiming it was not a property of the role. After a reload `getFlexoRole().getModelSlot()` was `null`, so `context` and `machine` were not found again. Reproduced headless by `TestBMethology.testReloadProject`, which asserted nothing about B.

**Fixed (2026-10-12).** `AtelierBComponent context/machine with AtelierBComponentRole(container=container.container.bProject)`; the header of `BMethodology.fml` says why. `BComponent.getObjectWithSerializationIdentifier` also looks events up by name (an `EventMapping` lost its event after a reload). `testReloadProject` asserts the context, the machine (with content) and the events. Confirmed in the application by the user.

### FORMOD-D-11 — INITIALISATION was generated as an event the B parser cannot read  ·  `DONE`

**Symptom.** After saving and reloading, two of four generated B files failed to load (`ParserException: [6,2] expecting … identifier literal`).

**Cause (measured).** `BMethodology` created an event named `INITIALISATION` inside `EVENTS` (a refinement extended it), as the legacy did. The grammar's `event` rule takes an `identifier_literal`, and `INITIALISATION` is a keyword, so the written text could not be read back; real Atelier B projects (`UnSysteme.sys`, `QuOnRaffine.ref`) write it as a clause before the events. The B technology adapter had no model for that clause (`inAInitialisationMachineClause` was an empty TODO).

**Fixed (2026-10-12), decided with the user.** `BComponent.initialisation` (a substitution), parsed by `BSemanticsAnalyzer`, written by `BComponentNode` between the invariant and `EVENTS`/`OPERATIONS`, and omitted when empty. `BMethodology` sets the clause with the body its rules fill (`initialisationOperation` is gone). Tests: `TestBPrettyPrint.testInitialisation` (read, write, read again), `TestBMethology.generatedBFilesCanBeReadBack` (every generated file is parsed back), T10.

**Not verified.** That Atelier B itself accepts the generated files (clause form follows its sample projects; a system with no variable has no clause at all). The `ref_and`/`ref_or` refinement keywords of the events are the Formose ones, read back fine by our parser.

### FORMOD-D-12 — A refinement was written with an empty `REFINES`  ·  `DONE`

**Symptom.** A B project whose methodology was never saved could not be loaded again: `[4,1] expecting identifier`.

**Cause (measured).** `makeNewRefinement` writes the file at once, before the refined component is set; the skeleton `REFINES` / `END` is not parseable.

**Fixed (2026-10-12).** `AddAtelierBComponent` writes the refinement again once `refines`, `extends` and `sees` are set. `TestBMethology.instantiateBMethodology` checks every `.ref` before any save (red without the fix, after the first version of its regular expression, which accepted `REFINES`/`END`, was corrected).

### FORMOD-D-13 — Refinement and contribution nodes lost their position  ·  `DONE`

**Symptom.** A refinement node (AND/OR) appeared far to the right of the two goals it refines.

**Cause (measured).** The legacy placed these nodes with `GraphicalAction`s (middle of the two shapes); five schemes lost them in the migration: `createRefinement`, `representRefinment`, `newPositiveContribution`, `newNegativeContribution`, `representContribution`. Same family as FORMOD-D-3/4/9.

**Fixed (2026-10-12).** The five placements are written again (middle of the two shapes, minus half the node size). Confirmed in the application for refinements. Not measured: a contribution node, and `representRefinment` after a reload.

### FORMOD-D-14 — The save panel opened twice  ·  `DONE`

**Symptom.** Cmd-S showed the "review unsaved resources" panel, then showed it again.

**Cause (inferred, not observed in the log).** The File menu held two entries with the same Cmd-S accelerator calling `reviewModifiedResources()`: `FileMenu.SaveItem` (base) and `FMSFileMenu.SaveModifiedItem` (formod). Removed the formod one. The user confirmed the panel opens once.

### FORMOD-D-15 — References to B predicates and to the INITIALISATION substitution are lost on reload  ·  `TODO`

**Symptom.** After a reload, `Concept2Property.typingPredicate` and the rules' `initialisationOperationBody` are empty (their reference has the identifier `null`: `…#FLX_null`).

**Measured.** `BComponent.getObjectWithSerializationIdentifier` finds a component, sets, constants, variables and (since FORMOD-D-10) events, by name; predicates and substitutions have no identifier. The B views do not depend on it, but a later update of the generation cannot remove or change those objects.

**To decide.** An identifier scheme for predicates and substitutions (position-based, or generated and written in a comment), or rebuild these roles from the B text at load time.

### FORMOD-D-16 — Five inspectors give a FlexoConceptInstance where a VirtualModelInstance is expected  ·  `TODO`

**Symptom.** Warning `component.virtualModelInstance=data.container … argument type mismatch` when `BMapping` is selected.

**Measured.** `Assignment variable="component.virtualModelInstance" value="data.container"` in `BMapping.inspector`, `SysMLKaosElementMapping.inspector`, `Concept.inspector` and (twice) `AgentAssignment.inspector`. For a nested concept `data.container` is the enclosing instance. **Not measured:** the effect (the selector is probably empty); the fix I would try is `data.owningVirtualModelInstance`.

### FORMOD-D-17 — Default names of dropped objects were identical  ·  `DONE`

**Symptom.** Defaults added so that drops need no dialog gave every concept the name `Concept` (B: duplicate declarations, `DuplicateURIException`).

**Fixed (2026-10-12).** `DomainModel.newName(prefix)` (delegating to the root domain model) and `SysMLKaosModel.newName(prefix)` number the names (`Concept1`, `Individual2`, `Goal1`, `Agent2`…), never reusing a number. The five domain drops and the five goal/agent drops use them as default value. Trap measured: in a default value `container` is the container of the diagram, and `container.newName(…)` was rejected at load (`unresolved path element`), while a slot of the diagram (`domainModel`, `kaosModel`) resolves.
