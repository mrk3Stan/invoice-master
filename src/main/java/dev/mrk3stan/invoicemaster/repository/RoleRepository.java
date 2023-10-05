package dev.mrk3stan.invoicemaster.repository;

import dev.mrk3stan.invoicemaster.domain.Role;

import java.util.Collection;

public interface RoleRepository <T extends Role> {

    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More complex operations */
    void addRoleToUser(Long id, String roleName);
    Role getRoleByUserId(Long userId);
    Role getRoleByUserEmail(String email);
    void updateUserRole(Long userId, String roleName);
}
