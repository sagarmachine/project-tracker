package com.highbrowape.demo.dto.input;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UserRegister {

    @NotNull(message = "email name can't be null")
    String email;

    @NotNull(message = "first name can't be null")
    String firstName;

    @NotNull(message = "last name can't be null")
    String lastName;

    @NotNull(message = "password  can't be null")
    String password;

    String  imageUrl;

    String deleteUrl;

}
