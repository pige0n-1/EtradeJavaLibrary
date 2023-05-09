package blue.etradeJavaLibrary.model.etradeObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class XMLDataFields implements
        Iterable<Map.Entry<String, ?>> {

    // Instance data fields
    TreeMap<String, String> stringFields = new TreeMap<>();
    TreeMap<String, XMLBuildable> objectFields = new TreeMap<>();
    TreeMap<String, ArrayList<? extends XMLBuildable>> arrayListFields = new TreeMap<>();

    public XMLDataFields addStringField(String key, String value) {
        if (!(value == "" || value == null)) stringFields.put(key, value);

        return this;
    }

    public XMLDataFields addStringField(String key, Integer value) {
        return addStringField(key, value.toString());
    }

    public XMLDataFields addStringField(String key, Long value) {
        return addStringField(key, value.toString());
    }

    public XMLDataFields addStringField(String key, Boolean value) {
        return addStringField(key, value.toString());
    }

    public XMLDataFields addStringField(String key, LocalDate value) {
        return addStringField(key, value.toEpochSecond(LocalTime.NOON, ZoneOffset.ofHours(0)) * 1000);
    }

    public XMLDataFields addStringField(String key, LocalDateTime value) {
        return addStringField(key, value.toEpochSecond(ZoneOffset.ofHours(0)) * 1000);
    }

    public XMLDataFields addStringField(String key, Double value) {
        return addStringField(key, value.toString());
    }

    public XMLDataFields addXMLObjectField(String key, XMLBuildable value) {
        if (value != null) objectFields.put(key, value);

        return this;
    }

    public XMLDataFields addArrayListField(String key, ArrayList<? extends XMLBuildable> value) {
        if (value != null) arrayListFields.put(key, value);

        return this;
    }

    @Override
    public Iterator<Map.Entry<String, ?>> iterator() {
        return new Iterator<Map.Entry<String, ?>>() {

            Iterator<Map.Entry<String, String>> stringFieldsIterator = stringFields.entrySet().iterator();
            Iterator<Map.Entry<String, XMLBuildable>> objectFieldsIterator = objectFields.entrySet().iterator();
            Iterator<Map.Entry<String, ArrayList<? extends XMLBuildable>>> arrayListIterator = arrayListFields.entrySet().iterator();

            @Override
            public boolean hasNext() {
                return stringFieldsIterator.hasNext() || objectFieldsIterator.hasNext() || arrayListIterator.hasNext();
            }

            @Override
            public Map.Entry<String, ?> next() {
                if (stringFieldsIterator.hasNext())
                    return stringFieldsIterator.next();

                else if (objectFieldsIterator.hasNext())
                    return objectFieldsIterator.next();

                return arrayListIterator.next();
            }
        };
    }
}
