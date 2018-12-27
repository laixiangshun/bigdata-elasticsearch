package csv.parser;

import csv.mapping.LocalWeatherDataMapper;
import csv.mapping.StationMapper;
import csv.model.LocalWeatherData;
import csv.model.Station;
import de.bytefish.jtinycsvparser.CsvParser;
import de.bytefish.jtinycsvparser.CsvParserOptions;
import de.bytefish.jtinycsvparser.tokenizer.StringSplitTokenizer;

/**
 * 解析器
 **/
public class CsvDataParser {

    public static CsvParser<LocalWeatherData> localWeatherDataCsvParser() {
        return new CsvParser<>(new CsvParserOptions(true,
                new StringSplitTokenizer(",", true)),
                new LocalWeatherDataMapper(LocalWeatherData::new));
    }

    public static CsvParser<Station> stationCsvParser() {
        return new CsvParser<>(new CsvParserOptions(true,
                new StringSplitTokenizer("\\|", true)),
                new StationMapper(Station::new));
    }
}
