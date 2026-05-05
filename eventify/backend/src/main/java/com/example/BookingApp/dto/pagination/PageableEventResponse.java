package com.example.BookingApp.dto.pagination;

import com.example.BookingApp.dto.response.EventResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableEventResponse {

    private List<EventResponse> content;
    private PageableObject pageable;
    private Boolean last;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private Integer number;
    private SortObject sort;
    private Boolean first;
    private Integer numberOfElements;

}
