package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.SendVerificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.User;
import com.cengo.muzayedebackendv2.domain.entity.enums.UserRole;
import com.cengo.muzayedebackendv2.domain.request.UserSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.UserUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.user.UserLoginResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserResponse;
import com.cengo.muzayedebackendv2.mapper.config.DefaultMapperConfiguration;
import com.cengo.muzayedebackendv2.mapper.password.EncodedMapping;
import com.cengo.muzayedebackendv2.mapper.password.PasswordEncoderMapper;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;


@Mapper(config = DefaultMapperConfiguration.class, uses = {PasswordEncoderMapper.class})
public interface UserMapper {

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    @Mapping(target = "point", constant = "0")
    User convertToUser(UserSaveRequest request);

    UserResponse convertToUserResponse(User user);

    @Mapping(target = "authorities", source = "role", qualifiedByName = "mapAuthorities")
    @Mapping(target = "userId", source = "id")
    @Mapping(target= "isAdmin", constant = "false")
    UserPrincipal convertToUserPrincipal(User user);

    UserLoginResponse convertToUserLogin(User user);

    UserBuyerResponse convertToUserBuyerResponse(User user);

    SendVerificationDTO convertToVerificationRequest(User savedUser);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    void updatePassword(@MappingTarget User user, String password);

    @Named("mapAuthorities")
    default List<GrantedAuthority> mapAuthorities(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
}
