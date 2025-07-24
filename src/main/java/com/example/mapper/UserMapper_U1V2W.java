src/main/java/com/example/mapper/UserMapper_U1V2W.java

<ctrl60>
<ctrl62>


<ctrl61>
package com.example.mapper;

import com.example.dto.UserDto_D3E4F;
import com.example.model.User_M1N2O;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper_U1V2W {
    UserMapper_U1V2W INSTANCE = Mappers.getMapper(UserMapper_U1V2W.class);

    UserDto_D3E4F toDto(User_M1N2O user);
    
    List<UserDto_D3E4F> toDtoList(List<User_M1N2O> users);
}