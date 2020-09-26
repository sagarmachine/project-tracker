package com.highbrowape.demo.dto.input;

import com.highbrowape.demo.entity.Authority;
import lombok.Data;

@Data
public class ProjectMemberDto {

    String email;

    Authority authority;

}
