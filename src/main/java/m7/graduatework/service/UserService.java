package m7.graduatework.service;

import m7.graduatework.dto.user.PasswordDto;
import m7.graduatework.dto.user.UserDto;
import m7.graduatework.dto.user.UserLoginDto;
import m7.graduatework.dto.user.UserRegisterDto;
import m7.graduatework.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    boolean setUserPassword(PasswordDto passwordDto);

    UserDto getCurrentUserDto();

    User getCurrentUser();

    UserDto updateUser(UserDto userDto);

    String updateUserImage(MultipartFile image);

    User register(UserRegisterDto userRegisterDto);

    boolean login(UserLoginDto userLoginDto);

    String getPathToUsersImageStorageFront();

    String getPathToUsersImageStorageRoot();
}
