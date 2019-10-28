package com.waggs.dogmicroservice.repository;

import com.waggs.dogmicroservice.entity.Dog;
import org.springframework.data.repository.CrudRepository;

public interface DogRepository extends CrudRepository<Dog, Long> {

}
