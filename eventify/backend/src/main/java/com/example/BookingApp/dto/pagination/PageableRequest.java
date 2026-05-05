package com.example.BookingApp.dto.pagination;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    @Min(value = 0, message = "Номер страницы должен быть не менее 0")
    private Integer page;

    @Min(value = 1, message = "Размер страницы должен быть не менее 1")
    private Integer size;

    private List<String> sort;

}
