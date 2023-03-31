package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.mapper.CertificateRowMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.epam.esm.mapper.QueriesContext.GET_CERTIFICATE_BY_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TestCertificationRuntimeException {
    @Mock
    private static final CertificateRowMapper rowMapper = mock(CertificateRowMapper.class);
    @Mock
    private static final PreparedStatement preparedStatement = mock(PreparedStatement.class);
    @Mock
    private static final ResultSet resultSet = mock(ResultSet.class);
    @Mock
    private static final Connection connection = mock(Connection.class);
    @Mock
    private static final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Mock
    private static final DataSource dataSource = mock(DataSource.class);
    private static final CertificateDao certificateDao = new DefaultCertificateDao(jdbcTemplate, rowMapper);
    private final Long CERTIFICATE_ID = 1L;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        when(dataSource.getConnection()).thenReturn(connection);
        when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
        doNothing().when(preparedStatement).setString(anyInt(), anyString());
        when(preparedStatement.execute()).thenReturn(Boolean.TRUE);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
    }

    @Test
    @DisplayName("Should throw RuntimeException when getById() method catches RuntimeException")
    void shouldThrowRuntimeExceptionWhenQueryForObjectThrowsException() {
        when(jdbcTemplate.queryForObject(
                GET_CERTIFICATE_BY_ID,
                new Object[]{CERTIFICATE_ID},
                rowMapper))
                .thenThrow(new RuntimeException("error"));
        assertThrows(RuntimeException.class,
                () -> certificateDao.getById(CERTIFICATE_ID));
    }

    @Test
    @DisplayName("Should throw RuntimeException when getAll() method catches RuntimeException")
    void shouldThrowRuntimeExceptionWhenGetAllMethodCatchesException()
            throws RuntimeException {
        when(jdbcTemplate.query(anyString(),
                any(CertificateRowMapper.class)))
                .thenThrow(new RuntimeException("test"));
        assertThrows(RuntimeException.class, certificateDao::getAll);
    }

    @Test
    @DisplayName("Should throw RuntimeException when getAll() method catches RuntimeException")
    void shouldThrowDaoSQLExceptionWhenGetAllMethodCatchesSQLException()
            throws RuntimeException {
        when(jdbcTemplate.query(anyString(),
                any(CertificateRowMapper.class)))
                .thenThrow(new RuntimeException("test"));
        assertThrows(RuntimeException.class, certificateDao::getAll);
    }
}
