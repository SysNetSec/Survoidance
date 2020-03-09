package aware.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCamerasRequest {
    private Float latitude;
    private Float longitude;

    @JsonCreator
    public GetCamerasRequest(@JsonProperty("latitude") Float latitude,
                             @JsonProperty("longitude") Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return this.latitude;
    }

    public Float getLongitude() {
        return this.longitude;
    }
}
