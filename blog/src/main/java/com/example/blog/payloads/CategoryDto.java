package com.example.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Integer categoryId;
    @NotBlank
    @Size(min=4, message = "min size of title is 4")
    private String categoryTitle;
    @NotBlank
    @Size(max=10, message = "max size of descrip is 10")
    private String categoryDescription;
}
