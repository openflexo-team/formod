# formod — Known defects

Bugs of formod (the B technology adapter, the Formose module and its resource center) that are known and not fixed yet. Each entry
says what is verified, how to reproduce it, and how to work around it. Features and evolutions go to `BACKLOG.md`, not here.

Identifiers are stable and never reused: `FORMOD-D-<n>`. Reference them from commits and from other entries.

Status legend: `TODO` · `IN PROGRESS` · `BLOCKED` · `DONE` · `DEFERRED`.

Fixed defects are moved to [`FIXED_DEFECTS.md`](./FIXED_DEFECTS.md) once their status is `DONE`.

---

## B parser

### FORMOD-D-1 — The B grammar has no real literals  ·  `DEFERRED`

**Symptom.** Parsing a B expression holding a real number fails: `BParser.parse("1.5", factory, EntryPointKind.Expression)` throws
`ParseException: [1,2] expecting: EOF`.

**Mechanism — verified in the grammar.** `b-ta-parser/src/main/sablecc/b.sablecc` only declares `integer_literal = digit+`; there is
no real literal token nor any `expression` alternative for one. The lexer reads `1`, then `.` cannot continue the expression. Same
for `hex_literal`, which the grammar declares as a token and no production ever uses.

**Not a 2.99 migration regression.** The grammar comes from [probparsers](https://github.com/bendisposto/probparsers) and is
unchanged since the initial import (2020-03), so the test could not pass before the migration either. Atelier B has since added the
`REAL` and `FLOAT` types and literals in decimal point notation (`1.0`), which upstream ProB accepts: `1.5` is legal B, and the
grammar here predates that extension.

**Deferred (2026-10-13), decided with the user.** Nothing in the corpus writes a real value — no `.mch`, `.ref`, `.imp` or `.sys`
holds a real literal, and the Formose methodology generates none — so the limitation is recorded rather than fixed. The test that
reported it (`TestParseBExpression.testExpression7`) became `testRealLiteralIsNotSupported`, which asserts the current behaviour,
names this entry and says which assertion to invert. `b-ta-test` is green again: 135 tests, 0 failures.

**What a fix would be.** A `real_literal = digit+ '.' digit+` token (digits required on both sides so that `1..5` still lexes as the
interval and `a.b` as a composed identifier) plus an `expression_atomic` alternative and its AST node; and, to be of any use, the
`REAL` and `FLOAT` base-type sets as expressions, so that `x : REAL` can be declared. On the Java side, `BRealExpression` /
`BRealSetExpression` / `BFloatSetExpression` with their P2PP nodes, `@Import`ed in `BExpression` and dispatched in
`ExpressionFactory`, all on the pattern of `BIntegerExpression` and `BNatSetExpression`.

**Workaround.** None in B text: real values cannot be written in expressions handled by the B technology adapter.
