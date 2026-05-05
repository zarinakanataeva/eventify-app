package com.example.BookingApp.mapper;

import com.example.BookingApp.dto.NotificationDto;
import com.example.BookingApp.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "hoursBeforeHours", target = "notifyBeforeHours")
    NotificationDto toDto(Notification entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "notifyBeforeHours", target = "hoursBeforeHours")
    void updateEntityFromDto(NotificationDto dto, @MappingTarget Notification entity);

}
