package az.finalproject.msuser.service;

import az.finalproject.msuser.dto.UserRequest;
import az.finalproject.msuser.dto.UserResponse;
import az.finalproject.msuser.entity.User;
import az.finalproject.msuser.exceptions.AlreadyExistException;
import az.finalproject.msuser.exceptions.UserNotFoundException;
import az.finalproject.msuser.mapper.AddressMapper;
import az.finalproject.msuser.mapper.UserMapper;
import az.finalproject.msuser.repository.AddressRepository;
import az.finalproject.msuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;


    public void createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new AlreadyExistException("Email already exists");
        }
        userRepository.save(userMapper.toEntity(userRequest));
    }

    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userMapper.updateEntityFromRequest(userRequest, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}