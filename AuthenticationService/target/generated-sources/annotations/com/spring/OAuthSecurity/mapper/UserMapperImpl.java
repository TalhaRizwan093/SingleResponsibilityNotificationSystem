package com.spring.OAuthSecurity.mapper;

import com.spring.OAuthSecurity.dto.SignupRequest;
import com.spring.OAuthSecurity.model.UserInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-16T16:20:47+0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserInfo singupRequestDtoToUserInfo(SignupRequest signupRequest) {
        if ( signupRequest == null ) {
            return null;
        }

        UserInfo userInfo = new UserInfo();

        return userInfo;
    }

    @Override
    public SignupRequest userInfoToSignupRequest(UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        SignupRequest signupRequest = new SignupRequest();

        return signupRequest;
    }
}
