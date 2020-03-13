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
		END;
	CaptureRawLocalizationData = skip;
	GetGPSData = skip;
	GetWIFIData = skip;
	ValidateData =skip;
	CaptureRelativeLocalization = skip;
	UseSpeedSensor = skip;
	UseAccellSensor = skip;
	FilterData = skip;
	MergeData = skip
END