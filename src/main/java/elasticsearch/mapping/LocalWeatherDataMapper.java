package elasticsearch.mapping;

import de.bytefish.elasticutils.elasticsearch6.mapping.BaseElasticSearchMapping;
import org.apache.lucene.queries.function.valuesource.FloatFieldSource;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.mapper.*;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class LocalWeatherDataMapper extends BaseElasticSearchMapping {

    private static final String indexType = "document";

    public LocalWeatherDataMapper() {
        this(indexType, Version.V_6_2_4);
    }

    public LocalWeatherDataMapper(String indexType, Version version) {
        super(indexType, version);
    }

    @Override
    protected void configureRootObjectBuilder(RootObjectMapper.Builder builder) {
        builder.add(new DateFieldMapper.Builder("dateTime"))
                .add(new NumberFieldMapper.Builder("temperature", NumberFieldMapper.NumberType.FLOAT))
                .add(new NumberFieldMapper.Builder("windSpeed", NumberFieldMapper.NumberType.FLOAT))
                .add(new NumberFieldMapper.Builder("stationPressure", NumberFieldMapper.NumberType.FLOAT))
                .add(new TextFieldMapper.Builder("skyCondition"))
                .add(new ObjectMapper.Builder("station")
                        .add(new TextFieldMapper.Builder("wban"))
                        .add(new TextFieldMapper.Builder("name"))
                        .add(new TextFieldMapper.Builder("state"))
                        .add(new TextFieldMapper.Builder("location"))
                        .add(new ObjectMapper.Builder("coordinates")
                                .add(new NumberFieldMapper.Builder("lat", NumberFieldMapper.NumberType.DOUBLE))
                                .add(new NumberFieldMapper.Builder("lon", NumberFieldMapper.NumberType.DOUBLE))));
    }

    @Override
    protected void configureSettingsBuilder(Settings.Builder builder) {
        builder.put(IndexMetaData.SETTING_VERSION_CREATED, 1)
                .put(IndexMetaData.SETTING_CREATION_DATE, System.currentTimeMillis());
    }
}
