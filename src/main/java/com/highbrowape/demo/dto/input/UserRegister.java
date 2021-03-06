package com.highbrowape.demo.dto.input;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserRegister {

    @NotNull(message = "email name can't be null")
    String email;

    @NotNull(message = "first name can't be null")
    String firstName;

    @NotNull(message = "last name can't be null")
    String lastName;

    String password;

    String  imageUrl;

    String deleteUrl;

    String thumbnailUrl;

}
