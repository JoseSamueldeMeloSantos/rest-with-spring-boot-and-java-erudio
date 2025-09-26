package br.com.bthirtyeight.repository;

import br.com.bthirtyeight.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    //clearAutomatically = true par apagar o cache do banco
    @Modifying(clearAutomatically = true)//Para Garantir os modelos de ACID no query
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")//para usar consultas personalizadas no( //TEM QUE chamar os atributos do jeito que ta ca classe e nao no banco)
    void disablePerson(@Param("id") Long id);//@Param indicar que e um parametro do springdata(atributo do banco)

    //Nao precissa de modifying pq eu nao vou modificar o banco
    @Query("SELECT p FROM Person p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);
}
