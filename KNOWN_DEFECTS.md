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

### FORMOD-D-2 — TestBMethology.testReloadProject fails about one run in three  ·  `BLOCKED`

**Symptom.** `formod-module`, uiTest `TestBMethology.testReloadProject` fails intermittently, right after the project is reloaded, on
the first methodology it asks the reloaded model for: `projectElement.execute("applicableSysMLKaosMethodology")` returns null, or the
next one does, or `getBElementMapping(...)` does. Measured 2026-10-07: two green runs, one red.

**Cause — not in formod.** The reloaded project holds two generations of the `FormoseVMI.fml.rt` resource, so the project element
exists twice and the identity comparison of `where (selected.declaringElement == this)` fails for the methodologies that fell on the
other side. Full measurement in `openflexo-core/KNOWN_DEFECTS.md`, CORE-D-28.

**Nothing to do here** until CORE-D-28 is fixed: the model and the test are right, what they ask of the platform is not delivered
reliably. Keep the test as it is — it is the reproduction.
