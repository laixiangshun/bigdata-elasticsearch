package elasticsearch.converter;

import elasticsearch.model.GeoLocation;
import elasticsearch.model.LocalWeatherData;
import elasticsearch.model.Station;
import utils.DateUtils;

import java.time.ZoneOffset;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class LocalWeatherDataConverter {
    public static LocalWeatherData convert(csv.model.LocalWeatherData localWeatherData, csv.model.Station station) {
        LocalWeatherData weatherData = new LocalWeatherData();
        weatherData.dateTime = DateUtils.from(localWeatherData.getDate(), localWeatherData.getTime(), ZoneOffset.ofHours(station.getTimeZone()));
        weatherData.skyCondition = localWeatherData.getSkyCondition();
        weatherData.stationPressure = localWeatherData.getStationPressure();
        weatherData.temperature = localWeatherData.getDryBulbCelsius();
        weatherData.windSpeed = localWeatherData.getWindSpeed();
        weatherData.station = convert(station);
        return weatherData;
    }

    public static Station convert(csv.model.Station station) {
        Station station1 = new Station();
        station1.location = station.getLocation();
        station1.name = station.getName();
        station1.state = station.getState();
        station1.wban = station.getWban();
        GeoLocation geoLocation = new GeoLocation(station.getLatitude(), station.getLongitude());
        station1.geoLocation = geoLocation;
        return station1;
    }
}
