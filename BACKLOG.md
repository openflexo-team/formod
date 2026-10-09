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

### FORMOD-F-2 — Let the B methodology wizard choose where the B models are generated  ·  `TODO`

**Problem.** `BMethodology` takes two Atelier B projects: the source project it reads, and the project it generates the B models into
(`create(DomainModelMethodology, AtelierBProjectResource sourceProject, AtelierBProjectResource generatedProject)`). The wizard of
`InstantiateBMethodology` asks for one project only — an existing one, or a blank one it creates — and passes it twice, so the B models
are generated into the source project. The `.fmlscript` scenario `T10_BMethodology` uses two distinct projects.

**Options.**
1. Add a second choice to the wizard (existing or new generated project), mirroring the first one.
2. Derive the generated project from the source one (a sibling blank project named after it), with no new question.

**Acceptance criteria.** The wizard creates the methodology on two distinct projects, and `TestBMethology` asserts that the B models land
in the generated project and leave the source project untouched.

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
