package com.highbrowape.demo.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCommentChartData {


    long count;

//    @CreationTimestamp
//    @JsonFormat(pattern = "MM", timezone = "IST")
    int month;

//    @CreationTimestamp
//    @JsonFormat(pattern = "dd", timezone = "IST")
    int date;


}
