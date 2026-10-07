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
