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
