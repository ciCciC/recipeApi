package com.example.recipeapi.business;

import com.example.recipeapi.business.entity.User;
import com.example.recipeapi.repository.FavoriteRepository;
import com.example.recipeapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    public UserService(UserRepository userRepository, FavoriteRepository favoriteRepository){
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Optional<List<User>> findAll() {
        var users = this.userRepository.findAll();
        var mappedToList = StreamSupport.stream(users.spliterator(), false).toList();
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
        this.favoriteRepository.deleteFavoritesByUserId(id);
    }

}
