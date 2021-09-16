package com.pedfav.overlookhotel.usecases;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.exceptions.ResourceNotFoundException;
import com.pedfav.overlookhotel.gateway.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
    }

    public List<User> listUsers(Integer page, Integer pageSize) {
        return userRepository.findAll(Pageable.ofSize(pageSize).withPage(page)).toList();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User modifyUser(Long id, User user) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

        user.setId(id);

        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));

        userRepository.deleteById(id);
    }
}
