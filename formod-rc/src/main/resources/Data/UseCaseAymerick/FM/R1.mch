SYSTEM
    R1
SEES
    Ontology

VARIABLES
    estimatedLoc
INVARIANT
    estimatedLoc : LATITUDE * LONGITUDE

INITIALISATION
    estimatedLoc := null |-> null
EVENTS
    LocalizeVehicle = // blablabla1
    BEGIN
        estimatedLoc :: (LATITUDE - {null}) * (LONGITUDE - {null})
    END
END