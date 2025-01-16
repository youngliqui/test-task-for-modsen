package by.youngliqui.bookstorageservice.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    Authentication fromToken(String token);

}
