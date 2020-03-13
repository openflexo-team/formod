REFINEMENT R3
REFINES R2
SEES
	Ontology

VARIABLES
	sensorsLoc, keptLock

INVARIANT
	sensorsLoc : SUBSENSORS +-> (LATITUDE * LONGITUDE) &
	keptLock : SUBCOMPONENTS +-> (LATITUDE * LONGITUDE) &
	validatedLoc = keptLock

INITIALISATION
	sensorsLoc := SUBSENSORS * {null |-> null} ||
	keptLock := SUBCOMPONENTS * {null |-> null}

EVENTS
	LocalizeVehicle = skip;
	CaptureRawLocalizationData = skip;
	GetGPSData = skip;
	GetWIFIData = skip;
	ValidateData =skip;
	CaptureRelativeLocalization = //blablablaN
		ANY
			sensorsLoc_post
		WHERE
			subcomponantsLoc : (SUBCOMPONENTS --> ((LATITUDE - {null}) * (LONGITUDE - {null}))) &
			sensorsLoc_post : (SUBSENSORS --> ((LATITUDE - {null}) * (LONGITUDE - {null}))) &
			sensorsLoc_post /= {}
		THEN
			sensorsLoc := sensorsLoc_post
		END;
	UseSpeedSensor = skip;
	UseAccellSensor = skip;
	FilterData = //blablablaN
		ANY
			keptLock_post
		WHERE
			sensorsLoc : (SUBSENSORS --> ((LATITUDE - {null}) * (LONGITUDE - {null}))) &
			sensorsLoc /= {} &
			keptLock_post : POW(subcomponantsLoc) &
			ran(keptLock_post) <: ((LATITUDE - {null}) * (LONGITUDE - {null}))
		THEN
			keptLock := keptLock_post
		END;
	MergeData = skip
END