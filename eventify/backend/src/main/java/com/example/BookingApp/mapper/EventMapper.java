package com.example.BookingApp.mapper;

import com.example.BookingApp.dto.request.EventCreateRequest;
import com.example.BookingApp.dto.request.EventUpdateRequest;
import com.example.BookingApp.dto.response.EventResponse;
import com.example.BookingApp.entity.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Event toEntity(EventCreateRequest dto);

    @Mapping(target = "availableTickets", expression = "java(entity.getAvailableTickets())")
    EventResponse toDto(Event entity);

    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    void updateEntityFromDto(EventUpdateRequest dto, @MappingTarget Event entity);
}