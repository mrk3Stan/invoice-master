package dev.mrk3stan.invoicemaster.service;


import dev.mrk3stan.invoicemaster.domain.User;
import dev.mrk3stan.invoicemaster.dto.UserDTO;

public interface UserService {
    UserDTO createUser(User user);
}
