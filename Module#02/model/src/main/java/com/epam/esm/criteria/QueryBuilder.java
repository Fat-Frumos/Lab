package com.epam.esm.criteria;

import com.epam.esm.domain.Certificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.epam.esm.criteria.QueryGenerator.getCriteriaParams;
import static com.epam.esm.mapper.QueriesContext.*;

@Slf4j
@Component
public class QueryBuilder {

    private QueryBuilder(){
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Criteria criteria;
        private StringBuilder query = new StringBuilder(BASE_QUERY);

        private Map<String, Object> params = new HashMap<>();

        private Builder() {
        }

        public Builder criteria(Criteria criteria) {
            this.criteria = criteria;
            return this;
        }

        public Builder sql(StringBuilder sql) {
            this.query = sql;
            return this;
        }

        public Builder updateQuery(Certificate certificate, String sql) {

            this.query = new StringBuilder(sql);
            this.criteria = toCriteria(certificate);

            if (criteria.getName() != null) {
                query.append(" c.name='").append(criteria.getName()).append("',");
            }
            if (criteria.getDescription() != null) {
                query.append(" c.description='").append(criteria.getDescription()).append("',");
            }
            if (criteria.getPrice() != null) {
                query.append(" c.price='").append(criteria.getPrice()).append("',");
            }
            if (criteria.getDuration() != 0) {
                query.append(" c.duration='").append(criteria.getDuration()).append("',");
            }
            if (criteria.getId() != null) {
                query.append(" c.last_update_date='").append(Instant.now()).append("'");
                query.append(" WHERE c.id=").append(criteria.getId());
            }
            return this;
        }

        private Criteria toCriteria(Certificate certificate) {
            return Criteria.builder()
                    .id(certificate.getId())
                    .name(certificate.getName())
                    .description(certificate.getDescription())
                    .price(certificate.getPrice())
                    .date(certificate.getCreateDate())
                    .duration(certificate.getDuration())
                    .build();
        }

        public String build() {
            return query.append(';').toString();
        }

        public Builder byTagName(String tagName) {
            if (tagName != null && !tagName.isEmpty()) {
                query.append(LEFT_JOIN)
                        .append(LEFT_JOIN_TAG)
                        .append(" WHERE name='")
                        .append(tagName)
                        .append("'");
            } else {
                query.append(WHERE);
            }
            return this;
        }

        public Builder search() {
            this.params = getCriteriaParams(criteria);
            this.params.forEach((key, value) -> query
                    .append(" c.")
                    .append(key)
                    .append(" ='")
                    .append(value)
                    .append("',"));
            return this;
        }

        public Builder searchBy(Criteria criteria) {
            this.criteria = criteria;
            params = getCriteriaParams(this.criteria);
            params.forEach((key, value) -> query
                    .append(" AND c.")
                    .append(key)
                    .append(" LIKE '%")
                    .append(value)
                    .append("%'"));
            return this;
        }
    }
}
