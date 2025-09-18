package br.com.bthirtyeight.repository;

import br.com.bthirtyeight.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying//Para Garantir os modelos de ACID no query
    //TEM QUE USAR do jeito que ta ca classe e nao no banco
    @Query("UPDATE Person p SEt o.enalble = false where p.id =:id")//para usar consultas personalizadas no
    void disablePerson(@Param("id") Long id);//@Param indicar que e um parametro do springdata(atributo do banco)
}
