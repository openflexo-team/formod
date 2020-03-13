SYSTEM
    Ontology
SETS
    SUBCOMPONENTS;
    SUBSENSORS
CONSTANTS
    null, LATITUDE, LONGITUDE,
    gps, wifi,
    speed, accell
PROPERTIES
    null = -1 &
    LATITUDE = NAT \/ {null} &
    LONGITUDE = NAT \/ {null} &
    gps : SUBCOMPONENTS &
    wifi : SUBCOMPONENTS &
    gps /= wifi &
    SUBCOMPONENTS = {gps, wifi} &
    speed : SUBSENSORS &
    accell : SUBSENSORS &
    speed /= accell &
    SUBSENSORS = {speed, accell} &
    !xx . (
        ( xx : ((LATITUDE - {null}) * (LONGITUDE - {null})) )
        =>
        ( xx : (LATITUDE*LONGITUDE) )
    )
END
