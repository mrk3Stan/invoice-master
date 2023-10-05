package dev.mrk3stan.invoicemaster.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Role {

    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Permission cannot be empty")
    private String permission;

}
