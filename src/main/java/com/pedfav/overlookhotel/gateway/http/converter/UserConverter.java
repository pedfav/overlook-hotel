package com.pedfav.overlookhotel.gateway.http.converter;

import com.pedfav.overlookhotel.entities.User;
import com.pedfav.overlookhotel.gateway.http.datacontracts.UserDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    public UserDataContract userToUserDataContract(User user) {
        return UserDataContract.builder()
                .id(user.getId())
                .documentId(user.getDocumentId())
                .name(user.getName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .build();
    }

    public User userDataContractToUser(UserDataContract dataContract) {
        return User.builder()
                .documentId(dataContract.getDocumentId())
                .name(dataContract.getName())
                .email(dataContract.getEmail())
                .birthday(dataContract.getBirthday())
                .build();
    }
}
