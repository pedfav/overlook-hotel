package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.gateway.http.converters.UserConverter;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UserDataContract;
import com.pedfav.overlookhotel.usecases.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserConverter userConverter;

    @GetMapping("/{id}")
    public ResponseEntity<UserDataContract> getUserById(@PathVariable("id") Long id) {

        User user = userUseCase.getUserById(id);
        UserDataContract userDataContract = userConverter.userToUserDataContract(user);

        return ResponseEntity.ok(userDataContract);
    }

    @GetMapping
    public ResponseEntity<List<UserDataContract>> listUsers(@RequestParam(defaultValue = "0", name = "page") Integer page,
                                                            @RequestParam(defaultValue = "5", name = "page_size") Integer pageSize) {

        List<UserDataContract> users = userUseCase.listUsers(page, pageSize)
                .stream()
                .map(userConverter::userToUserDataContract)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDataContract> createUser(@Valid @RequestBody UserDataContract userDataContract) {

        User user = userUseCase.createUser(userConverter.userDataContractToUser(userDataContract));

        return ResponseEntity.ok(userConverter.userToUserDataContract(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDataContract> modifyUser(@PathVariable("id") Long id,
                                                       @Valid @RequestBody UserDataContract userDataContract) {

        User user = userUseCase.modifyUser(id, userConverter.userDataContractToUser(userDataContract));

        return ResponseEntity.ok(userConverter.userToUserDataContract(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        userUseCase.deleteUserById(id);

        return ResponseEntity.ok().build();
    }
}
