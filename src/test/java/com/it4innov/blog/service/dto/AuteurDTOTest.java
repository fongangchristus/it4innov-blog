package com.it4innov.blog.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.it4innov.blog.web.rest.TestUtil;

public class AuteurDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuteurDTO.class);
        AuteurDTO auteurDTO1 = new AuteurDTO();
        auteurDTO1.setId(1L);
        AuteurDTO auteurDTO2 = new AuteurDTO();
        assertThat(auteurDTO1).isNotEqualTo(auteurDTO2);
        auteurDTO2.setId(auteurDTO1.getId());
        assertThat(auteurDTO1).isEqualTo(auteurDTO2);
        auteurDTO2.setId(2L);
        assertThat(auteurDTO1).isNotEqualTo(auteurDTO2);
        auteurDTO1.setId(null);
        assertThat(auteurDTO1).isNotEqualTo(auteurDTO2);
    }
}
