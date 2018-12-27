package apps;

import csv.parser.CsvDataParser;
import de.bytefish.elasticutils.elasticsearch6.client.ElasticSearchClient;
import de.bytefish.elasticutils.elasticsearch6.client.bulk.configuration.BulkProcessorConfiguration;
import de.bytefish.elasticutils.elasticsearch6.client.bulk.options.BulkProcessingOptions;
import de.bytefish.elasticutils.elasticsearch6.mapping.IElasticSearchMapping;
import de.bytefish.elasticutils.elasticsearch6.utils.ElasticSearchUtils;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import elasticsearch.converter.LocalWeatherDataConverter;
import elasticsearch.mapping.LocalWeatherDataMapper;
import elasticsearch.model.LocalWeatherData;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexAction;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * elasticsearch
 **/
public class elasticsearchApp1 {
    private static final Logger logger = LoggerFactory.getLogger(elasticsearchApp1.class);

    public static void main(String[] args) {
        String host = "192.168.20.48";
        int port = 9300;
        String indexName = "weather_hourly";
        LocalWeatherDataMapper mapper = new LocalWeatherDataMapper();

        BulkProcessingOptions options = BulkProcessingOptions.builder().setBulkActions(200).build();
        BulkProcessorConfiguration configuration = new BulkProcessorConfiguration(options);

        System.setProperty("es.set.netty.runtime.available.processors", "false");
        //es-cluster 为真实配置的集群名称
        Settings settings = Settings.builder().put("cluster.name", "es-cluster")
                .put("client.transport.sniff", true).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        ElasticSearchClient<LocalWeatherData> elasticSearchClient = null;
        Stream<LocalWeatherData> dataStream = null;
        try {
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
            createIndex(client, indexName);
            createMapping(client, indexName, mapper);
            elasticSearchClient = new ElasticSearchClient<>(client, indexName, mapper, configuration);
            dataStream = getWeatherDataStream();
//            Iterator<LocalWeatherData> iterator = dataStream.iterator();
//            while (iterator.hasNext()) {
//                LocalWeatherData localWeatherData = iterator.next();
//                elasticSearchClient.index(localWeatherData);
//            }
            elasticSearchClient.index(dataStream);
            elasticSearchClient.flush();
            logger.info("index into elasticsearch success!");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            logger.error(e.getMessage(), e);
        } finally {
            if (dataStream != null) {
                dataStream.close();
            }
            if (elasticSearchClient != null) {
                try {
                    elasticSearchClient.awaitClose(1, TimeUnit.SECONDS);
                    elasticSearchClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            client.close();
        }
    }

    private static void createIndex(Client client, String indexName) {
        if (!ElasticSearchUtils.indexExist(client, indexName).isExists()) {
            ElasticSearchUtils.createIndex(client, indexName);
        } else {
            client.admin().indices().prepareDelete(indexName).execute().actionGet();
            createIndex(client, indexName);
        }
    }

    private static void createMapping(Client client, String indexName, IElasticSearchMapping mapping) {
        if (ElasticSearchUtils.indexExist(client, indexName).isExists()) {
            ElasticSearchUtils.putMapping(client, indexName, mapping);
        }
    }

    private static Stream<LocalWeatherData> getWeatherDataStream() {
        Path stationPath = FileSystems.getDefault().getPath("C:\\Users\\hasee\\Desktop\\QCLCD201503\\201503station.txt");
        Path weatherPath = FileSystems.getDefault().getPath("C:\\Users\\hasee\\Desktop\\QCLCD201503\\201503hourly.txt");

        Stream<csv.model.Station> stationStream = getStationStream(stationPath);
        Map<String, csv.model.Station> stationMap = stationStream.collect(Collectors.toMap(csv.model.Station::getWban, Function.identity(), (val1, val2) -> val1));

        Stream<csv.model.LocalWeatherData> weatherData = getWeatherData(weatherPath);
        Stream<LocalWeatherData> localWeatherDataStream;
        try {
            localWeatherDataStream = weatherData.filter(weather -> stationMap.containsKey(weather.getWban()))
                    .map(weather -> {
                        csv.model.Station station = stationMap.get(weather.getWban());
                        return LocalWeatherDataConverter.convert(weather, station);
                    });
        } catch (Exception e) {
            logger.error("读取CSV文件数据出错：" + e.getMessage(), e);
            throw e;
        } finally {
            //此处不能关闭流
//            weatherData.close();
//            stationStream.close();
        }
        return localWeatherDataStream;
    }

    private static Stream<csv.model.Station> getStationStream(Path path) {
        return CsvDataParser.stationCsvParser().readFromFile(path, StandardCharsets.US_ASCII)
                .filter(CsvMappingResult::isValid)
                .map(CsvMappingResult::getResult);
    }

    private static Stream<csv.model.LocalWeatherData> getWeatherData(Path path) {
        return CsvDataParser.localWeatherDataCsvParser().readFromFile(path, StandardCharsets.US_ASCII)
                .filter(CsvMappingResult::isValid)
                .map(CsvMappingResult::getResult);
    }
}
