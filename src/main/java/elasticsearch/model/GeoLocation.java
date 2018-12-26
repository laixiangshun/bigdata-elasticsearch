package elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class GeoLocation {

    @JsonProperty("lat")
    public double lat;

    @JsonProperty("lon")
    public double lon;

    public GeoLocation() {
    }

    public GeoLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
