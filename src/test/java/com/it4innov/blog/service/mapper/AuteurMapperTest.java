package com.it4innov.blog.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AuteurMapperTest {

    private AuteurMapper auteurMapper;

    @BeforeEach
    public void setUp() {
        auteurMapper = new AuteurMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(auteurMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(auteurMapper.fromId(null)).isNull();
    }
}
