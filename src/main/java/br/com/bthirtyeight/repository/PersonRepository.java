package br.com.bthirtyeight.repository;

import br.com.bthirtyeight.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
