package com.epam.esm.criteria;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueryGenerator {
    private QueryGenerator() {
    }

    public static Map<String, Object> getCriteriaParams(Criteria criteria) {
        Map<String, Object> params = new HashMap<>();

        Field[] fields = criteria.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getAnnotation(Column.class) != null && field.get(criteria) != null) {
                    params.put(field.getAnnotation(Column.class).value(), field.get(criteria));
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Error getting field value", e);
        }
//        params.put("table", Criteria.class.getAnnotation(Table.class).value());
        return params;
    }
}
