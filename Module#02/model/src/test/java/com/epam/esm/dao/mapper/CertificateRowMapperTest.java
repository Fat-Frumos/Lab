package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Certificate;
import com.epam.esm.mapper.CertificateRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CertificateRowMapperTest {
    private static final String GIFT_ID = "id";
    private static final String GIFT_NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String DURATION = "duration";
    private static final String PRICE = "price";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    private CertificateRowMapper rowMapper;
    ResultSet resultSet = mock(ResultSet.class);

    @BeforeEach
    public void setUp() {
        rowMapper = new CertificateRowMapper();
    }

    @DisplayName("Test mapRow method with valid ResultSet")
    @ParameterizedTest
    @CsvSource({
            "1, Certificate 1, This is a test certificate, 60, 100.00, 2023-03-15 15:30:00, 2023-03-15 15:30:00",
            "2, Certificate 2, This is another test certificate, 90, 200.00, 2023-03-15 16:45:00, 2023-03-15 16:45:00",
            "3, Certificate 3, This is a third test certificate, 120, 300.00, 2023-03-15 17:15:00, 2023-03-15 17:15:00"
    })
    void testMapRowWithValidResultSet(long id, String name, String description, int duration, BigDecimal price, String createDate, String lastUpdateDate)  {

        try (ResultSet resultSet = mock(ResultSet.class)) {
            when(resultSet.getLong(GIFT_ID)).thenReturn(id);
            when(resultSet.getString(GIFT_NAME)).thenReturn(name);
            when(resultSet.getString(DESCRIPTION)).thenReturn(description);
            when(resultSet.getInt(DURATION)).thenReturn(duration);
            when(resultSet.getBigDecimal(PRICE)).thenReturn(price);
            when(resultSet.getTimestamp(CREATE_DATE)).thenReturn(Timestamp.valueOf(createDate));
            when(resultSet.getTimestamp(LAST_UPDATE_DATE)).thenReturn(Timestamp.valueOf(lastUpdateDate));
            Certificate certificate = rowMapper.mapRow(resultSet, 0);

            assertEquals(id, Objects.requireNonNull(certificate).getId());
            assertEquals(name, certificate.getName());
            assertEquals(description, certificate.getDescription());
            assertEquals(duration, certificate.getDuration());
            assertEquals(price, certificate.getPrice());
            assertEquals(Instant.parse(createDate), certificate.getCreateDate());
            assertEquals(Instant.parse(lastUpdateDate), certificate.getLastUpdateDate());

            verify(resultSet).getLong(GIFT_ID);
            verify(resultSet).getString(GIFT_NAME);
            verify(resultSet).getString(DESCRIPTION);
            verify(resultSet).getInt(DURATION);
            verify(resultSet).getBigDecimal(PRICE);
            verify(resultSet).getTimestamp(CREATE_DATE);
            verify(resultSet).getTimestamp(LAST_UPDATE_DATE);
            verifyNoMoreInteractions(resultSet);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Test mapRow with null create_date")
    void testMapRowWithNullCreateDate() throws SQLException {
        when(resultSet.getLong(GIFT_ID)).thenReturn(1L);
        when(resultSet.getString(GIFT_NAME)).thenReturn(null);
        when(resultSet.getString(DESCRIPTION)).thenReturn("Desc");
        when(resultSet.getBigDecimal(PRICE)).thenReturn(BigDecimal.valueOf(10));
        when(resultSet.getInt(DURATION)).thenReturn(30);
        when(resultSet.getTimestamp(CREATE_DATE)).thenReturn(null);
        when(resultSet.getTimestamp(LAST_UPDATE_DATE)).thenReturn(Timestamp.from(Instant.now()));
        assertThrows(NullPointerException.class, () -> rowMapper.mapRow(resultSet, 0));
    }
}
