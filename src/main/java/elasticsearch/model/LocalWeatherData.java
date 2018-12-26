package elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class LocalWeatherData {
    @JsonProperty("station")
    public Station station;

    @JsonProperty("dateTime")
    public Date dateTime;

    @JsonProperty("temperature")
    public Float temperature;

    @JsonProperty("windSpeed")
    public Float windSpeed;

    @JsonProperty("stationPressure")
    public Float stationPressure;

    @JsonProperty("skyCondition")
    public String skyCondition;
}
