package services;

import java.util.List;

import model.User;

/**
 * Created by gibson.luo on 2018-09-21.
 */
public interface UserMapper {

    List<User> all();

    User getUserById(Long id);

}
