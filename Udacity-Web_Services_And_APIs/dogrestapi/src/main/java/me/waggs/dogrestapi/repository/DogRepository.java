package me.waggs.dogrestapi.repository;

import me.waggs.dogrestapi.entity.Dog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface DogRepository extends CrudRepository<Dog, Long> {
    List<Dog> findAll();

    @Query("select d.id, d.breed from Dog d")
    List<String> findAllDogBreed();

    @Query("select d.id, d.breed from Dog d where d.id=:id")
    Optional<String> findDogBreedById(Long id);

    @Query("select d.id, d.name from Dog d")
    List<String> findAllDogNames();
}
