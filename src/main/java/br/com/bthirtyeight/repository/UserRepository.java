package br.com.bthirtyeight.repository;

import br.com.bthirtyeight.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    //a atibuto usado no query tem q ter a mesma escrita do atributo da classe
    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    User findByUsername(@Param("userName") String userName);//O Param tem q ta igual ao parametro do metodo
}
