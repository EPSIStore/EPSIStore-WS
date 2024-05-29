package com.epsi.epsistore.impl;

import com.epsi.core.entities.Role;
import com.epsi.core.entities.User;
import com.epsi.core.repositories.RoleRepository;
import com.epsi.core.repositories.UserRepository;
import com.epsi.epsistore.dtos.RegisterDTO;
import com.epsi.epsistore.services.Impls.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void findByEmail_Test() {
        // GIVEN
        User mockUser = new User(1, "username", "email", "password", new Role("ROLE_A", 1), null);
        doReturn(Optional.of(mockUser)).when(userRepository).findByEmail("test");
        // WHEN
        User res = userServiceImpl.findByEmail("test");
        // THEN
        assertEquals(res, mockUser);
        verify(userRepository).findByEmail("test");
    }

    @Test
    public void findByEmail_Throw_Test() {
        // GIVEN
        doReturn(Optional.empty()).when(userRepository).findByEmail("test");
        // WHEN
        // THEN
        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.findByEmail("test"));
        verify(userRepository).findByEmail("test");
    }

    @Test
    public void register_Test() {
        // GIVEN
        RegisterDTO registerDTO = new RegisterDTO("email", "password");
        Role mockRole = new Role("ROLE_USER", 1);
        doReturn(Optional.of(mockRole)).when(roleRepository).findByRoleName("ROLE_USER");
        // WHEN
        String res = userServiceImpl.register(registerDTO);
        // THEN
        assertEquals("ok", res);
        verify(roleRepository).findByRoleName("ROLE_USER");
        verify(userRepository).save(any());
    }

    @Test
    public void register_Empty_Test() {
        // GIVEN
        RegisterDTO registerDTO = new RegisterDTO("email", "password");
        Role mockRole = new Role("ROLE_USER", 1);
        doReturn(Optional.empty()).when(roleRepository).findByRoleName("ROLE_USER");
        doReturn(mockRole).when(roleRepository).save(any());
        // WHEN
        String res = userServiceImpl.register(registerDTO);
        // THEN
        assertEquals("ok", res);
        verify(roleRepository).findByRoleName("ROLE_USER");
        verify(roleRepository).save(any());
        verify(userRepository).save(any());
    }
}
