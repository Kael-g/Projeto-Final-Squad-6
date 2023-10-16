package com.projetoSquad6.ApiReceitas.repository;

import com.projetoSquad6.ApiReceitas.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
    UserDetails findByLogin(String login);
}
