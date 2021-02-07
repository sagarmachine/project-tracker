package com.highbrowape.demo.dto.input;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.highbrowape.demo.entity.Category;
import com.highbrowape.demo.entity.Priority;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class MeetingDto {


    String title;

    String description;


}
