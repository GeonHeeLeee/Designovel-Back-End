package Designovel.Capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewFilterDTO {
    private String productId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    private Integer rate;
    private Integer page;
}
