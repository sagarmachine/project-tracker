package com.highbrowape.demo.dto.input;

import com.highbrowape.demo.entity.Category;
import com.highbrowape.demo.entity.Priority;
import com.highbrowape.demo.entity.Status;
import lombok.Data;

@Data
public class ObjectiveDto {

    long id;

    String description;

    Status status;

    Priority priority;

    Category category;
}
