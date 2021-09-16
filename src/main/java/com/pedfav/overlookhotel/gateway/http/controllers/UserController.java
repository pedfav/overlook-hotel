package com.pedfav.overlookhotel.gateway.http.controllers;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.gateway.http.converter.UserConverter;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UserDataContract;
import com.pedfav.overlookhotel.usecases.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController("/user")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserConverter userConverter;

    @GetMapping("/{id}")
    public ResponseEntity<UserDataContract> getUserById(@PathParam("id") Long id) {

        User user = userUseCase.getUserById(id);
        UserDataContract userDataContract = userConverter.userToUserDataContract(user);

        return ResponseEntity.ok(userDataContract);
    }

    @GetMapping
    public ResponseEntity<List<UserDataContract>> listUsers(@DefaultValue("0") @PathParam("page") Integer page,
                                                              @DefaultValue("5") @PathParam("page_size") Integer pageSize) {

        List<UserDataContract> users = userUseCase.listUsers(page, pageSize)
                .stream()
                .map(userConverter::userToUserDataContract)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDataContract> createUser(@RequestBody UserDataContract userDataContract) {

        User user = userUseCase.createUser(userConverter.userDataContractToUser(userDataContract));

        return ResponseEntity.ok(userConverter.userToUserDataContract(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDataContract> modifyUser(@PathParam("id") Long id,
                                                       @RequestBody UserDataContract userDataContract) {

        User user = userUseCase.modifyUser(id, userConverter.userDataContractToUser(userDataContract));

        return ResponseEntity.ok(userConverter.userToUserDataContract(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathParam("id") Long id) {
        userUseCase.deleteUserById(id);

        return ResponseEntity.ok().build();
    }
}
