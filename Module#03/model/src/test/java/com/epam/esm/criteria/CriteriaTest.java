package com.epam.esm.criteria;

import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;

import javax.swing.SortOrder;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CriteriaTest {

    @Test
    void testCriteriaAddParam() {

        Set<Tag> tags = Collections.singleton(Tag.builder().name("tag").build());
        SortOrder sortOrder = SortOrder.ASCENDING;
        FilterParams filterParams = FilterParams.NAME;
        Integer size = 10;
        Integer page = 1;
        Criteria criteria = new Criteria(sortOrder, filterParams, size, page);

        criteria.addParam(FilterParams.NAME, "value");

        assertEquals(criteria.getParamsMap().get(FilterParams.NAME), "value");
        assertEquals(sortOrder, criteria.getSortOrder());
        assertEquals(filterParams, criteria.getFilterParams());
        assertEquals(size, criteria.getSize());
        assertEquals(page, criteria.getPage());

        criteria.addParam(filterParams, "test");
        EnumMap<FilterParams, Object> paramsMap = new EnumMap<>(FilterParams.class);
        paramsMap.put(filterParams, "test");
        assertEquals(paramsMap, criteria.getParamsMap());
    }
}
