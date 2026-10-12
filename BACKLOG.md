# formod — Backlog

Features and evolutions to develop in formod (the B technology adapter, the Formose module and its resource center). Each entry says
what problem it solves and when it is done. Bugs that are known and not fixed go to `KNOWN_DEFECTS.md`, not here.

Identifiers are stable and never reused: `FORMOD-F-<n>`. Reference them from commits and from other entries.

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

---

## Resource center

### FORMOD-F-1 — Two B methodologies built on missing VirtualModels  ·  `DONE`

**Problem.** Two legacy VirtualModels of `formod-rc` could not be migrated as they were: `Formose.fml/SysMLKaos-B-Methodology.fml`
mapped functional goals and refinements onto B events and proof obligations, mounting `http://formose.lacl.fr/BSystem.viewpoint` and
typing its roles with `BSystem.viewpoint/BSystemModel.fml#Event` and `#PO`; `Formose.fml/DomainModel-B-Methodology.fml` mapped domain
models, concepts and enumerated data sets onto B systems and sets, mounting `Formose.viewpoint/BSystemExtentions.fml` and
`/resources/DomainModel/DomainModel.viewpoint`. None of those VirtualModels exists in the repository, nor anywhere in its history
(checked 2026-09-18, from the initial import on).

**Decision (user, 2026-10-07): deleted.** `BMethodology` covers the same ground — goals to events, concepts to sets — with the Atelier
B technology adapter, and is exercised end to end by `T10_BMethodology`. Deleted with them: the `TestFormose.view` fixture of
`DomainModel-B-Methodology.fml/` (a 2.1 run-time instance pointing at `Formose.viewpoint` and the moved `Fib/ProjectUI.fib`, truncated
and unusable), their localized dictionaries, and the `SysMLKaos-B-Methodology` entry of the module's dictionaries.

`FormodValidationTest` no longer excludes anything: every VirtualModel of the resource center is covered.

## Formose module

### FORMOD-F-2 — The B methodology has two Atelier B project slots, only one is meant  ·  `DONE`

**Problem.** `BMethodology` declared two Atelier B project slots, `sourceProject` and `generatedProject`, and the wizard asked for one project and
passed it twice. Measured: `generatedProject` was never read (FML, Java, `.fib`), and every B component was created `in (sourceProject)`, in the
legacy `.fml.xml` as well. In the legacy file `generatedProject` is the older slot (`flexoID` 7) and `sourceProject` a later one (`flexoID` 2975),
which suggests the second slot was added afterwards and took over the generation: an inference, not a proof. No code reads B written by hand from
a source project.

**Decided with the user (2026-10-12).** There is one project: the Atelier B project the B models are generated in. First tried as two projects
(generate into `generatedProject`, a third wizard step), then dropped: the use case (SysML/KAOS then domain model) has no source project.

**Done.** `BMethodology` has one slot, `bProject`; `create(domainModelMethodology, bProject)`, `Element.createBMethodology(bProject)`; the wizard
asks for one project, as it did; `InstantiateBMethodology` forces the exploration of a project it has just created and reports a project that is
not recognized instead of passing `null`. Scripts T10/T11 and `TestBMethology` use one project (T10 and the test assert the models land in it).
Projects created before this change keep the old slot names in their saved methodology: to be thrown away (user's decision).

### FORMOD-F-3 — Let the graphical-representation concepts derive their inspector  ·  `DONE`

**Problem.** FORMOD-D-5 restored the 52 inspectors as the legacy had them: a `…GR` concept has its own inspector, which repeats entries of the
model concept it represents (`goal.name`, `modelConcept.name`, …). The free modelling editor does it differently:
`@Inspector(derived=<role>)` hands the inspection of the GR instance to the model concept's inspector, so there is one inspector per notion.

**Options.** Derive every GR whose entries all go through one role to that role; keep its own inspector for the few that show something of the
GR itself (connectors, labels).

**Acceptance.** Selecting a GR shows the inspector of its model concept; `FormodInspectorsTest` still passes with the lower count.

**Done (2026-10-09).** All 24 concrete `…GR` concepts of the goal and domain diagrams declare `@Inspector(derived=<role>)` (`derived` is a property
of each concept, not inherited), and their 23 `.inspector` files are gone (with the abstract `GoalGR` and `AgentGR`, now useless). Three model
concepts that had no inspector received the entries their representation showed: `Refinement`, `Contribution` and `Impact`. The
`FormodInspectorsTest` checks every derivation (valid binding, no inspector of its own).

### FORMOD-F-4 — Break the import cycle between FormoseCore and the methodologies  ·  `TODO`

**Problem.** `FormoseCore` imports `SysMLKaosMethodology`, `DomainModelMethodology` and `BMethodology` (to type the properties that give the methodology applicable to an element), and each of them imports `FormoseCore` (and `Methodology`, which does too). Loading a unit then runs into the guard of the resource loader and logs about 135 transient warnings (CORE-D-34); every unit is valid in the end.

**Options.** (a) Move the properties typed by the methodologies out of `FormoseCore` into a model both import; (b) type them by the common parent `Methodology` and let the methodologies declare the precise accessors; (c) keep the cycle and wait for the platform to load it quietly.

**Acceptance.** No `Unresolved type` / `Preventing StackOverflow` warning at the start of formod-app, `formod-test` and `formod-module` unchanged.
