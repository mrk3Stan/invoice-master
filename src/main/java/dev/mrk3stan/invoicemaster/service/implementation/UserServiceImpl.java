package dev.mrk3stan.invoicemaster.service.implementation;

import dev.mrk3stan.invoicemaster.domain.User;
import dev.mrk3stan.invoicemaster.dto.UserDTO;
import dev.mrk3stan.invoicemaster.dtopmapper.UserDTOMapper;
import dev.mrk3stan.invoicemaster.repository.UserRepository;
import dev.mrk3stan.invoicemaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }
}
