package com.a406.horsebit.google.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInDTO {
    private String token;      // Authorization Server(google, naver, kakao) 에서 발급받은 token
}
