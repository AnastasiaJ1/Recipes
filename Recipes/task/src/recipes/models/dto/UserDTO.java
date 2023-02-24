package recipes.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
    @NotBlank
    @Pattern(regexp = "^.+@.+\\..+$")
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;
}
