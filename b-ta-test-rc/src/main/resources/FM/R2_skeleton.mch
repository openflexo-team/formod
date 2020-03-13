REFINEMENT R2_skeleton
REFINES R1_skeleton
SEES
	Ontology

	// Grd_CaptureRawLocalizationData => Grd_LocalizeVehicle &
	// Grd_ValidateData => Grd_LocalizeVehicle &
	// Grd_MergeData => Grd_LocalizeVehicle &
	// Act_CaptureRawLocalizationData & Act_ValidateData & Act_MergeData => Act_LocalizeVehicle &

EVENTS
	LocalizeVehicle = skip;
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