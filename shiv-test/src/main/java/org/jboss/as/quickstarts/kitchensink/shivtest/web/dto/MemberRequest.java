package org.jboss.as.quickstarts.kitchensink.shivtest.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberRequest {
    @NotBlank
    @Size(min = 1, max = 25)
    private String name;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{10,12}", message = "Must be 10-12 digits")
    private String phoneNumber;
}
