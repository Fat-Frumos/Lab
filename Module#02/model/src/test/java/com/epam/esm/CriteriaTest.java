package com.epam.esm;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.criteria.SortDirection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static com.epam.esm.mapper.QueriesContext.*;

@Slf4j
class CriteriaTest {

    @Test
    void testFindAll() { //TODO postman CRUD

        Criteria criteria = Criteria.builder()
                .name("Easter")
                .sortDirection(SortDirection.ASC)
                .build();

        String query = QueryBuilder.builder().criteria(criteria).search().build();
        String queryByTagName = QueryBuilder.builder().byTagName(criteria.getTagName()).build();//todo: , || and
        String queryBy = QueryBuilder.builder().searchBy(criteria).build();

        log.info(queryBy);
        log.info(query);
        log.info(queryByTagName);
        log.info(SELECT_CERTIFICATES_BY_TAG_NAME);
        log.info(GET_CERTIFICATE_BY_ID);
        log.info(GET_ALL_WITH_TAG_ID);
        log.info(GET_ALL_BY_TAG_NAME_ORDER);
    }
}
