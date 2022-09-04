package com.example.recipeapi.business;

import com.example.recipeapi.document.User;
import com.example.recipeapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final ElasticsearchRestTemplate es;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository,
            @Qualifier("elsTemp") ElasticsearchRestTemplate es){
        this.userRepository = userRepository;
        this.es = es;
    }

    public Optional<List<User>> findAll() {
        var users = this.userRepository.findAll();
        var mappedToList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        return Optional.of(mappedToList);
    }

    public Optional<User> findById(String id) throws Exception {
        return Optional.ofNullable(this.userRepository.findById(id).orElseThrow(Exception::new));
    }

    public User create(User user){
        return this.userRepository.save(user);
    }

    public User update(User user) throws Exception {
        var updateUser = this.findById(user.getId())
                .orElseThrow(Exception::new);

        updateUser.setFname(user.getFname());
        updateUser.setLname(user.getLname());

        return this.userRepository.save(updateUser);
    }

    public void deleteById(String id){
        this.userRepository.deleteById(id);
    }

}
