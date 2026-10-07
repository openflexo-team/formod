# formod-rc — the Formose resource center

Base URI `http://formose.lacl.fr/`, declared in
`src/main/resources/META-INF/resourceCenters/org.openflexo.foundation.resource.FlexoResourceCenter`. Every resource of this center
takes its URI from that base followed by its path, so moving a file renames its resource.

## The models

| Folder | What it holds |
|---|---|
| `Formose.fml/` | The Formose project itself: `FormoseCore` (the element tree), `Methodology` (the base of every methodology) and the four methodologies — document annotation, SysML/KAOS, domain model, B. `Glossary` completes it. |
| `DocumentLibrary.fml/` | The library of federated documents: `AbstractDocument` and its two concrete forms, `WordDocument` (a `.docx`) and `ExcelDocument` (a workbook). |
| `SysMLKaos/` | The SysML/KAOS goal models: `SysMLKaos.fml` with `SysMLKaosModel` and `GoalModelingDiagram`, plus the diagram specification `GoalDiagram.diagramspecification` and its palette. |
| `DomainModel/` | The domain models: `DomainModelling.fml` with `DomainModel` and `DomainModelDiagram`, plus the diagram specification `DomainModelDiagramSpec.diagramspecification`. |

The user interfaces of these models live in the container of the concept they represent, by the 2.99 convention
(`Formose.fml/Formose.fib`, `Formose.fml/FormoseCore.fml/Element.fib`,
`Formose.fml/SysMLKaosMethodology.fml/SysMLKaosElementMapping.fib`). The `formod-test` module validates every model and runs the
`.fmlscript` scenarios that exercise them.

## The data

None of it is required to load the models; it is what the Formose case study works on.

| Path | What it is | Used by |
|---|---|---|
| `Data/LandingGearSystemRqt.docx`, `Data/Landing_System2.docx` | The requirements of the landing gear system | `T2_DocumentLibrary`, the document annotation methodology |
| `Data/landing_system.pdf` | The same requirements, as published | nothing in the models |
| `Data/UseCaseAymerick/` | A case study: the diagrams as PDF (`Diags/`) and the B machines of its formal model (`FM/*.mch`) | nothing in the models |
| `Ontologies/*.owl` | The SysML/KAOS ontologies of the landing gear, and the domain model template | nothing in the models (the OWL technology adapter is still a dependency of the project) |
| `SysMLKAOS_models_serialization_template.xlsx` | The workbook a SysML/KAOS model is serialized into: two sheets, `Goal Model` and `Domain Model` | `T12_ExcelDocument`; `SysMLKaos` declares an Excel slot for such a workbook |
| `Icons/FormoseBackground.jpg` | The background of the project and element views | `Formose.fib`, `Element.fib` |
