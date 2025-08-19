package br.com.bthirtyeight.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity//->diz ao JPA que a classe deve ser mapeada para uma tabela no banco de dados.
//para criar a tabela no sql pelo jpa
@Table(name = "person")//-> so e necessario definir se a um nome se a tabela tiver um diferente do da classe
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id//define que o atributo a baixo e um id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//define o tipo de geracao do id
    private Long id;

    //column -> define que o atributo abaixo pertencera a uma coluna na database
    ///         tbm define as propriedades dessa coluna(atributo)
    ///
    ///         se o nome do atributo for igual ao nome da coluna entao nao e preciso colocar o name
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    @Column(nullable = false, length = 100)
    private String address;
    @Column(nullable = false, length = 6)
    private String gender;

    public Person() {

    }

    public Person(Long id, String firstName, String address, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.address = address;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(address, person.address) && Objects.equals(gender, person.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, address, gender);
    }
}
