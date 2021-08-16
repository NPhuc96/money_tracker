package personal.nphuc96.money_tracker.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TransactionGroupDTO {

    private Integer id;


    private String name;

    //icon
}
