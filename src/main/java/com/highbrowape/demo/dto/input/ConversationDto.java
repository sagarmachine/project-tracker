package com.highbrowape.demo.dto.input;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.util.Date;

@Data
public class ConversationDto {

    String title;

    String conversation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startedOn;

    String startedBy;

    String startedOver;

}
