package elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class Station {
    @JsonProperty("wban")
    public String wban;

    @JsonProperty("name")
    public String name;

    @JsonProperty("state")
    public String state;

    @JsonProperty("location")
    public String location;

    @JsonProperty("coordinates")
    public GeoLocation geoLocation;

}
