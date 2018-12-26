package csv.mapping;

import csv.model.Station;
import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class StationMapper extends CsvMapping<Station> {

    public StationMapper(IObjectCreator creator) {
        super(creator);
        mapProperty(0, String.class, Station::setWban);
        mapProperty(1, String.class, Station::setWmo);
        mapProperty(2, String.class, Station::setCallSign);
        mapProperty(3, String.class, Station::setClimateDivisionCode);
        mapProperty(4, String.class, Station::setClimateDivisionStateCode);
        mapProperty(5, String.class, Station::setClimateDivisionStationCode);
        mapProperty(6, String.class, Station::setName);
        mapProperty(7, String.class, Station::setState);
        mapProperty(8, String.class, Station::setLocation);
        mapProperty(9, Float.class, Station::setLatitude);
        mapProperty(10, Float.class, Station::setLongitude);
        mapProperty(11, Integer.class, Station::setGroundHeight);
        mapProperty(12, Integer.class, Station::setStationHeight);
        mapProperty(13, Integer.class, Station::setBarometer);
        mapProperty(14, Integer.class, Station::setTimeZone);
    }
}
