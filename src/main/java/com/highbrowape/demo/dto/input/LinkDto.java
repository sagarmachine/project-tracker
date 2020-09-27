package com.highbrowape.demo.dto.input;


import lombok.Data;

import javax.persistence.Column;

@Data
public class LinkDto {

    String link;

    @Column(nullable = false)
    String description;

}
