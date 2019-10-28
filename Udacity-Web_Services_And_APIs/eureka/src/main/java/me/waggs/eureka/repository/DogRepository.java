package me.waggs.eureka.repository;

import me.waggs.eureka.entity.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long> {

}
