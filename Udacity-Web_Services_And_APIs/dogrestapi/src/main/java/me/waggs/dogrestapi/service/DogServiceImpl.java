package me.waggs.dogrestapi.service;

import me.waggs.dogrestapi.entity.Dog;
import me.waggs.dogrestapi.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DogServiceImpl implements DogService {

    private DogRepository dogRepository;

    public DogServiceImpl(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    public List<Dog> retrieveDogs() {
        return dogRepository.findAll();
    }

    @Override
    public List<String> retrieveDogBreed() {
        return dogRepository.findAllDogBreed();
    }

    @Override
    public String retrieveDogBreedById(Long id) {
        Optional<String> result = dogRepository.findDogBreedById(id);
        String breed = result.orElseThrow(DogNotFoundException::new);
        return breed;
    }

    @Override
    public List<String> retrieveDogNames() {
        return dogRepository.findAllDogNames();
    }
}
