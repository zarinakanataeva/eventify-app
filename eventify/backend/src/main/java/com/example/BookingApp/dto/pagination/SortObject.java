package com.example.BookingApp.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortObject {

    private Boolean empty;
    private Boolean sorted;
    private Boolean unsorted;

}
