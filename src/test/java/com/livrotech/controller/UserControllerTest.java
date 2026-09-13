package com.livrotech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.livrotech.config.AppUserService;
import com.livrotech.dto.PasswordChangeRequestDTO;
import com.livrotech.dto.UserCreateRequestDTO;
import com.livrotech.dto.UserResponseDTO;
import com.livrotech.dto.UserUpdateRequestDTO;
import com.livrotech.entity.AppUser;
import com.livrotech.entity.AppUserRole;

class UserControllerTest {

    @Test
    void shouldCreateUserForAdmin() {
        AppUserService appUserService = mock(AppUserService.class);
        UserController controller = new UserController(appUserService);

        AppUser createdUser = new AppUser("new-user", "encoded-password", AppUserRole.USER);
        createdUser.setId(10L);

        when(appUserService.createUser("new-user", "secret123", "USER")).thenReturn(createdUser);

        ResponseEntity<UserResponseDTO> response = controller.createUser(new UserCreateRequestDTO("new-user", "secret123", "USER"));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(10L, response.getBody().id());
        assertEquals("new-user", response.getBody().username());
        assertEquals("USER", response.getBody().role());
    }

    @Test
    void shouldUpdateUserForAdmin() {
        AppUserService appUserService = mock(AppUserService.class);
        UserController controller = new UserController(appUserService);

        AppUser updatedUser = new AppUser("updated-user", "encoded-password", AppUserRole.ADMIN);
        updatedUser.setId(12L);

        when(appUserService.updateUser(12L, "updated-user", "ADMIN")).thenReturn(updatedUser);

        ResponseEntity<UserResponseDTO> response = controller.updateUser(12L, new UserUpdateRequestDTO("updated-user", "ADMIN"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated-user", response.getBody().username());
        assertEquals("ADMIN", response.getBody().role());
    }

    @Test
    void shouldDeleteUserForAdmin() {
        AppUserService appUserService = mock(AppUserService.class);
        UserController controller = new UserController(appUserService);

        doNothing().when(appUserService).deleteUser(7L);

        ResponseEntity<Void> response = controller.deleteUser(7L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldChangePasswordForAuthenticatedUser() {
        AppUserService appUserService = mock(AppUserService.class);
        UserController controller = new UserController(appUserService);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin", "old-pass"));

        doNothing().when(appUserService).changePassword("admin", "old-pass", "new-pass");

        ResponseEntity<Void> response = controller.changePassword(new PasswordChangeRequestDTO("old-pass", "new-pass"));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        SecurityContextHolder.clearContext();
    }
}
