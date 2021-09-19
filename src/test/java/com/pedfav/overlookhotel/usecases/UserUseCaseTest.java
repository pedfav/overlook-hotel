package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.exceptions.EmailAlreadyTakenException;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pedfav.overlookhotel.fixtures.UserFixture.user;
import static com.pedfav.overlookhotel.fixtures.UserFixture.userList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserUseCase userUseCase = new UserUseCase(userRepository);

    @Test
    void testGetUserByIdWhenItExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));

        User userById = userUseCase.getUserById(1L);

        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(userById, user());
    }

    @Test
    void testGetUserByIdWhenDoesntExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userUseCase.getUserById(1L));
    }

    @Test
    void testListAllUsers() {
        Page<User> pagedUser = new PageImpl(userList());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(pagedUser);

        List<User> users = userUseCase.listUsers(0, 3);

        verify(userRepository, times(1)).findAll(any(Pageable.class));
        assertEquals(users.size(), userList().size());
    }

    @Test
    void testListAllUsersEmpty() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        List<User> users = userUseCase.listUsers(0, 3);

        verify(userRepository, times(1)).findAll(any(Pageable.class));
        assertEquals(users.size(), Collections.emptyList().size());
    }

    @Test
    void testCreateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user());

        User user = userUseCase.createUser(user());

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(user, user());
    }

    @Test
    void testCreateUserEmailAlreadyTakenException() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(EmailAlreadyTakenException.class, () -> userUseCase.createUser(user()));
    }

    @Test
    void testUpdateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));

        User user = userUseCase.modifyUser(1L, user());

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(user, user());
    }

    @Test
    void testUpdateUserEmailAlreadyTakenException() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));

        assertThrows(EmailAlreadyTakenException.class, () -> userUseCase.modifyUser(1L, user()));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserDoesntExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userUseCase.modifyUser(1L, user()));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testDeleteUserByIdWhenItExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));

        userUseCase.deleteUserById(1L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteUserByIdWhenDoesntExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userUseCase.deleteUserById(1L));

        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).deleteById(anyLong());
    }
}