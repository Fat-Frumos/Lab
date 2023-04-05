package com.epam.esm;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TestCertificationDaoException.class})
@ExtendWith(SpringExtension.class)
public class TagDaoTest {

}
