package com.example.BookingApp.mapper;

import com.example.BookingApp.dto.response.BookingResponse;
import com.example.BookingApp.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface BookingMapper {

    @Mapping(target = "customerEmail", source = "user.email")
    @Mapping(target = "ticketCount", source = "bookingsAmount")
    @Mapping(target = "expiryTime", ignore = true)
    @Mapping(target = "timezone", ignore = true)
    BookingResponse toDto(Booking entity);

    List<BookingResponse> toDtoList(List<Booking> entities);

}

