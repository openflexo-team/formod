# formod — Backlog

Features and evolutions to develop in formod (the B technology adapter, the Formose module and its resource center). Each entry says
what problem it solves and when it is done. Bugs that are known and not fixed go to `KNOWN_DEFECTS.md`, not here.

Identifiers are stable and never reused: `FORMOD-F-<n>`. Reference them from commits and from other entries.

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

---

## Resource center

### FORMOD-F-1 — Decide the fate of the two B methodologies built on missing VirtualModels  ·  `TODO`

**Problem.** Two legacy VirtualModels of `formod-rc` were left in their `.fml.xml` serialization during the 2.99 migration, because
they cannot be migrated as they are:

- `Formose.fml/SysMLKaos-B-Methodology.fml` maps functional goals and refinements onto B events and proof obligations. It mounts
  `http://formose.lacl.fr/BSystem.viewpoint` and types its roles with `BSystem.viewpoint/BSystemModel.fml#Event` and `#PO`;
- `Formose.fml/DomainModel-B-Methodology.fml` maps domain models, concepts and enumerated data sets onto B systems and sets. It mounts
  `Formose.viewpoint/BSystemExtentions.fml` and `/resources/DomainModel/DomainModel.viewpoint`, and types its roles with
  `BusinessDomainModel.fml#EnumeratedDataSet` and `BSystemModel.fml#Set`.

None of these VirtualModels exists in the repository, nor anywhere in its history (checked 2026-09-18, from the initial import on).
The Formose module does not use either methodology: only `BMethodology` is instantiated (`InstantiateBMethodology`,
`FMSConstants.B_METHODOLOGY_VM_NAME`). `DomainModel-B-Methodology.fml/` also holds a `TestFormose.view` fixture.

Both are excluded from `FormodValidationTest` by name (`NOT_MIGRATED`), with a reference to this entry.

**Options.**
1. Delete them: `BMethodology` covers the same ground (goals to events, concepts to sets) with the Atelier B technology adapter.
2. Rewrite them against the 2.99 VirtualModels (`SysMLKaosModel`, `DomainModel`) and the B technology adapter, if their mapping
   (proof obligations from refinements, in particular) is still wanted.
3. Recover the missing `BSystem` VirtualModels from wherever they lived before the initial import, and migrate the three together.

**Acceptance criteria.** No legacy `.fml.xml` is left in `formod-rc`, `NOT_MIGRATED` is empty, and whatever survives validates and is
exercised by a `.fmlscript` scenario of `formod-test`.
