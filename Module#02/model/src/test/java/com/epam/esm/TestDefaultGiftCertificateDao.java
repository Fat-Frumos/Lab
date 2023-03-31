package com.epam.esm;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.DefaultCertificateDao;
import com.epam.esm.mapper.CertificateRowMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.fail;

class TestDefaultGiftCertificateDao {
    @Mock
    private static final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Mock
    private static final CertificateRowMapper rowMapper = mock(CertificateRowMapper.class);
    private static final CertificateDao certificateDao = new DefaultCertificateDao(jdbcTemplate, rowMapper);


    @Test
    @DisplayName("Test delete certificate")
    void testDeleteCertificate() {
        long certificateId = 1L;
        doThrow(new RuntimeException("Failed to delete certificate from the database"))
                .when(jdbcTemplate).update(anyString(), anyLong());
        try {
            certificateDao.delete(certificateId);
            fail("Failed to delete certificate from the database");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), containsString(
                    "Failed to delete certificate from the database"));
        }
    }
}
