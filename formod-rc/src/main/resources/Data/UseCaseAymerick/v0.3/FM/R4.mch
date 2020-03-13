REFINEMENT R4
REFINES R3
SEES
	Ontology

VARIABLES
	speedLoc, accellLoc

INVARIANT
	speedLoc : {speed} +-> (LATITUDE * LONGITUDE) &
	accellLoc : {accell} +-> (LATITUDE * LONGITUDE) &
	sensorsLoc = speedLoc or sensorsLoc = accellLoc or sensorsLoc = speedLoc \/ accellLoc

INITIALISATION
	speedLoc := {speed} * {null |-> null} ||
	accellLoc := {accell} * {null |-> null}

EVENTS
	LocalizeVehicle = skip;
	CaptureRawLocalizationData = skip;
	GetGPSData = skip;
	GetWIFIData = skip;
	ValidateData =skip;
	CaptureRelativeLocalization = skip;
	UseSpeedSensor = //blablablaN
		ANY
			speedLoc_post
		WHERE
			speedLoc = {speed} * {null |-> null} &
			speedLoc_post : ({speed} --> ((LATITUDE - {null}) * (LONGITUDE - {null})))
		THEN
			speedLoc := speedLoc_post ||
			sensorsLoc := speedLoc_post
		END;
	UseAccellSensor = //blablablaN
		ANY
			accellLoc_post
		WHERE
			accellLoc = {speed} * {null |-> null} &
			accellLoc_post : ({accell} --> ((LATITUDE - {null}) * (LONGITUDE - {null})))
		THEN
			accellLoc := accellLoc_post ||
			sensorsLoc := accellLoc_post
		END;
	FilterData = skip;
	MergeData = skip


 END