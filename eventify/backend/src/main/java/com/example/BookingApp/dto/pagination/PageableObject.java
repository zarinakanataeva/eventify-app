package com.example.BookingApp.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableObject {

    private Long offset;
    private SortObject sort;
    private Boolean paged;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean unpaged;

}
