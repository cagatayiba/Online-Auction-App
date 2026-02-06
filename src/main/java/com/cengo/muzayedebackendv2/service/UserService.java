package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.SendVerificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.User;
import com.cengo.muzayedebackendv2.domain.entity.enums.UserRole;
import com.cengo.muzayedebackendv2.domain.request.UserSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.UserUpdatePasswordRequest;
import com.cengo.muzayedebackendv2.domain.request.UserUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.user.UserLoginResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserResponse;
import com.cengo.muzayedebackendv2.exception.BadRequestException;
import com.cengo.muzayedebackendv2.exception.message.LoginErrorMessage;
import com.cengo.muzayedebackendv2.mapper.UserMapper;
import com.cengo.muzayedebackendv2.repository.UserRepository;
import com.cengo.muzayedebackendv2.security.UserPrincipal;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.service.verification.VerificationService;
import com.cengo.muzayedebackendv2.validation.user.UserUpdatePasswordValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
public class UserService extends BaseEntityService<User, UserRepository> {
    private final VerificationService verificationService;
    private final UserMapper userMapper;
    private final UserUpdatePasswordValidation userUpdatePasswordValidation;

    @Value("${spring.security.admin.name}")
    private String adminEmail;
    @Value("${spring.security.admin.password}")
    private String adminPassword;


    protected UserService(UserRepository repository, VerificationService verificationService, UserMapper userMapper, UserUpdatePasswordValidation userUpdatePasswordValidation) {
        super(repository);
        this.verificationService = verificationService;
        this.userMapper = userMapper;
        this.userUpdatePasswordValidation = userUpdatePasswordValidation;
    }

    @Transactional
    public UserResponse saveUser(UserSaveRequest userSaveRequest) {
        User user = save(userMapper.convertToUser(userSaveRequest));

        SendVerificationDTO request = userMapper.convertToVerificationRequest(user);
        verificationService.sendVerificationRequest(request);
        return userMapper.convertToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserUpdateRequest userUpdateRequest) {
        User user = getById(userId);
        userMapper.updateUser(user, userUpdateRequest);

        return userMapper.convertToUserResponse(save(user));
    }

    @Transactional
    public UserResponse updateUserPassword(UUID userId, UserUpdatePasswordRequest userUpdatePasswordRequest) {
        User user = getById(userId);

        userUpdatePasswordValidation.validate(new UserUpdatePasswordValidation.Context(userUpdatePasswordRequest));

        userMapper.updatePassword(user, userUpdatePasswordRequest.newPassword());
        return userMapper.convertToUserResponse(save(user));
    }

    public User getUserById(UUID userId) {
        return getById(userId);
    }

    public UserResponse getUserResponseById(UUID id) {
        return userMapper.convertToUserResponse(getById(id));
    }

    public UserLoginResponse getLoggedInUser(UUID userId) {
        return userMapper.convertToUserLogin(getById(userId));
    }

    public UserPrincipal getUserByEmail(String email) {
        if (email.equals(adminEmail)) {
            return UserPrincipal.builder()
                    .userId(UUID.randomUUID())
                    .email(adminEmail)
                    .password(adminPassword)
                    .authorities(List.of(new SimpleGrantedAuthority(UserRole.ADMIN.toString())))
                    .approved(true)
                    .isAdmin(true)
                    .build();
        }
        User user = getRepository().findByEmailIgnoreCase(email).orElseThrow(() -> new BadRequestException(LoginErrorMessage.INVALID_MAIL));
        return userMapper.convertToUserPrincipal(user);
    }


    public void resendVerification(UUID userId) {
        User user = getById(userId);
        SendVerificationDTO request = userMapper.convertToVerificationRequest(user);
        verificationService.sendVerificationRequest(request);
    }

    public UserBuyerResponse getBuyerUserById(UUID userId) {
        if (userId == null){
            return null;
        }
        return userMapper.convertToUserBuyerResponse(getById(userId));
    }

    public void loadPoint(UUID userId, int pointAmount){
        getRepository().updatePointById(userId, pointAmount);
    }
}
