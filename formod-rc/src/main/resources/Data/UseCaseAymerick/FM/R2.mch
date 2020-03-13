REFINEMENT R2
REFINES R1
SEES
    Ontology

VARIABLES
    subcomponantsLoc, validatedLoc, mergedLoc
INVARIANT
//    Grd_CaptureRawLocalizationData => Grd_LocalizeVehicle &
//    Grd_ValidateData => Grd_LocalizeVehicle &
//    Grd_MergeData => Grd_LocalizeVehicle &
//    Act_CaptureRawLocalizationData & Act_ValidateData & Act_MergeData => Act_LocalizeVehicle &
    subcomponantsLoc : SUBCOMPONENTS --> (LATITUDE * LONGITUDE) &
    validatedLoc : SUBCOMPONENTS --> (LATITUDE * LONGITUDE) &
    mergedLoc : LATITUDE * LONGITUDE &
    estimatedLoc = mergedLoc

INITIALISATION
    subcomponantsLoc := SUBCOMPONENTS * {null |-> null} ||
    validatedLoc := SUBCOMPONENTS * {null |-> null} ||
    mergedLoc := null |-> null
EVENTS
    CaptureRawLocalizationData = //blablabla2
    ANY
    	subcomponantsLoc_post
    WHERE
    	subcomponantsLoc_post : (SUBCOMPONENTS --> ((LATITUDE - {null}) * (LONGITUDE - {null})))
    THEN
        subcomponantsLoc := subcomponantsLoc_post
    END;
    ValidateData = //blablabla3
    ANY
        validatedLoc2
    WHERE
        validatedLoc2 : POW(subcomponantsLoc) &
        (ran(validatedLoc2) <: (LATITUDE - {null}) * (LONGITUDE - {null}))
    THEN
        validatedLoc := validatedLoc2
    END;
    MergeData = //blablabla4
    ANY
    	mergedLoc_post
    WHERE
    	mergedLoc_post : ((LATITUDE - {null}) * (LONGITUDE - {null}))
    THEN
        mergedLoc := mergedLoc_post
    END
 END