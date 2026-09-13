package com.livrotech.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void shouldExposeRoleAsSpringAuthority() {
        AppUser user = new AppUser("admin", "secret", AppUserRole.ADMIN);

        assertEquals("ROLE_ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }
}
