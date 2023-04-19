package com.epam.esm.criteria;

import com.epam.esm.entity.Certificate;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static com.epam.esm.mapper.QueriesContext.*;

/**
 * This class provides a builder for SQL queries with variable number of conditions and parameters.
 * <p>
 * It uses the StringBuilder to construct the query string and a List to store the parameters.
 * <p>
 * Once all conditions have been added, the build method can be called to obtain the final query string
 * and a List of parameters, which can be used to execute the query using a PreparedStatement.
 * <p>
 * This class is thread-safe and can be used to construct queries in a multi-threaded environment.
 */
@Component
public final class QueryBuilder {
    /**
     * Private constructor to prevent instantiation.
     */
    private QueryBuilder() {
    }

    /**
     * Returns a new instance of the Builder class to start building a query.
     *
     * @return A new Builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This inner class is used to build the query.
     */
    public static final class Builder {
        private Criteria criteria;
        private StringBuilder query = new StringBuilder();

        private Builder() {
        }

        /**
         * Sets the search criteria for the builder.
         *
         * @param criteria the search criteria to set
         * @return the builder with the updated criteria
         */
        public Builder criteria(final Criteria criteria) {
            this.criteria = criteria;
            return this.selectAll().searchByTag();
        }

        /**
         * Adds a new search term to the query.
         *
         * @param sql The search query to add.
         * @return This Builder instance.
         */
        public Builder sql(final String sql) {
            this.query = new StringBuilder(sql);
            return this;
        }

        /**
         * Builds the final query.
         *
         * @return The final query as a string.
         */
        public String build() {
            return query.append(';').toString();
        }

        public Builder updateQuery(final Certificate certificate, final String sql) {

            this.query = new StringBuilder(sql);

            if (certificate.getName() != null) {
                query.append(" c.name='").append(certificate.getName()).append("',");
            }
            if (certificate.getDescription() != null) {
                query.append(" c.description='").append(certificate.getDescription()).append("',");
            }
            if (certificate.getPrice() != null) {
                query.append(" c.price='").append(certificate.getPrice()).append("',");
            }
            if (certificate.getDuration() != 0) {
                query.append(" c.duration='").append(certificate.getDuration()).append("',");
            }
            if (certificate.getId() != null) {
                query.append(" c.last_update_date='").append(Instant.now()).append("'");
                query.append(" WHERE c.id=").append(certificate.getId());
            }
            return this;
        }

        public Builder selectAll() {
            return this.select().tags().from().leftJoin().leftJoinTags();
        }

        public Builder where() {
            query.append(" WHERE");
            return this;
        }

        public Builder select() {
            query.append(SELECT);
            return this;
        }

        public Builder tags() {
            query.append(TAGS);
            return this;
        }

        public Builder from() {
            query.append(FROM);
            return this;
        }

        public Builder leftJoin() {
            query.append(LEFT_JOIN);
            return this;
        }

        public Builder leftJoinTags() {
            query.append(LEFT_JOIN_TAG);
            return this;
        }

        public Builder searchByTag() {
            if (criteria.getTagName() != null) {
                query.append(" WHERE t.name iLIKE");
                query.append(" '%").append(criteria.getTagName()).append("%'");
            }
            return this;
        }

        public Builder searchBy(final Criteria criteria) {
            this.criteria = criteria;
            int c = 0;
            selectAll();

            if (criteria.getTagName() != null) {
                query.append(" WHERE (t.name iLIKE '%").append(criteria.getTagName()).append("%'");
                c++;
            }
            query.append(c == 0 ? " WHERE (" : "");

            if (criteria.getName() != null) {
                query.append(c == 1 ? " AND " : "");
                query.append(" c.name iLIKE '%").append(criteria.getName()).append("%'");
                c++;
            }
            if (criteria.getDescription() != null) {
                query.append(c == 0 ? "" : " OR");
                query.append(" c.description iLIKE '%").append(criteria.getDescription()).append("%'");
                c++;
            }
            query.append(c != 0 ? " )" : "1=1)");

            if (criteria.getSortField() != null) {
                orderBy(criteria.getSortField(), criteria.getSortOrder());
            }
            return this;
        }

        public Builder orderBy(final SortField field, final SortOrder order) {
            if (field != null && order != null) {
                query.append(" ORDER BY ").append(field.getField()).append(" ").append(order.name());
            }
            return this;
        }

        public Builder sort(final SortField field, final SortOrder order) {
            query.append(" ORDER BY ").append(field.getField());
            if (order.isAsc()) {
                query.append(" ASC");
            } else if (order.isDesc()) {
                query.append(" DESC");
            }
            return this;
        }

        public Builder orderBy(final SortField field) {
            return orderBy(field, SortOrder.UNSORTED);
        }
    }
}
