REFINEMENT R2
REFINES R1
SEES
	Ontology

VARIABLES
	subcomponantsLoc, validatedLoc, mergedLoc
INVARIANT
//	Grd_CaptureRawLocalizationData => Grd_LocalizeVehicle &
//	Grd_ValidateData => Grd_LocalizeVehicle &
//	Grd_MergeData => Grd_LocalizeVehicle &
//	Act_CaptureRawLocalizationData & Act_ValidateData & Act_MergeData => Act_LocalizeVehicle &
	subcomponantsLoc : SUBCOMPONENTS --> (LATITUDE * LONGITUDE) &
	validatedLoc : SUBCOMPONENTS +-> (LATITUDE * LONGITUDE) &
	mergedLoc : LATITUDE * LONGITUDE &
	estimatedLoc = mergedLoc

INITIALISATION
	subcomponantsLoc := SUBCOMPONENTS * {null |-> null} ||
	validatedLoc := SUBCOMPONENTS * {null |-> null} ||
	mergedLoc := null |-> null
EVENTS
	LocalizeVehicle = skip;
	CaptureRawLocalizationData = //blablabla2
		ANY
			subcomponantsLoc_post
		WHERE
			subcomponantsLoc_post : (SUBCOMPONENTS --> ((LATITUDE - {null}) * (LONGITUDE - {null})))
		THEN
			subcomponantsLoc := subcomponantsLoc_post
		END;
	GetGPSData = skip;
	GetWIFIData = skip;
	ValidateData = //blablabla3
		ANY
			validatedLoc_post
		WHERE
			validatedLoc_post : POW(subcomponantsLoc) &
			(ran(validatedLoc_post) <: (LATITUDE - {null}) * (LONGITUDE - {null}))
		THEN
			validatedLoc := validatedLoc_post
		END;
	CaptureRelativeLocalization = skip;
	UseSpeedSensor = skip;
	UseAccellSensor = skip;
	FilterData = skip;
	MergeData = //blablabla4
		ANY
			mergedLoc_post
		WHERE
			mergedLoc_post : ((LATITUDE - {null}) * (LONGITUDE - {null}))
		THEN
			mergedLoc := mergedLoc_post
		END
END