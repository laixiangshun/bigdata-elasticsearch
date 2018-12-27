package csv.converter;

import de.bytefish.jtinycsvparser.typeconverter.ITypeConverter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author hasee
 * @Date 2018/12/26
 **/
public class IgnoreMissingValuesConverter implements ITypeConverter<Float> {

    private List<String> missingValueRepresentation;

    public IgnoreMissingValuesConverter(String... missingValueRepresentation) {
        this(Arrays.asList(missingValueRepresentation));
    }

    public IgnoreMissingValuesConverter(List<String> missingValueRepresentation) {
        this.missingValueRepresentation = missingValueRepresentation;
    }

    @Override
    public Float convert(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        boolean isMissingValue = missingValueRepresentation.stream().allMatch(s -> s.equals(value));
        if (isMissingValue) {
            return null;
        }
        return Float.parseFloat(value);
    }

    @Override
    public Type getTargetType() {
        return Float.class;
    }
}
