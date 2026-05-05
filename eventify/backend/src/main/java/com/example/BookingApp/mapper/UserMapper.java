package com.example.BookingApp.mapper;

import com.example.BookingApp.dto.request.RegisterRequest;
import com.example.BookingApp.dto.response.AuthorizeResponse;
import com.example.BookingApp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest dto);

    @Mapping(target = "token", ignore = true)
    AuthorizeResponse toAuthResponse(User user);
}


