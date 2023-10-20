package com.msil.evaluation.service;

import com.msil.evaluation.dto.AuthRequest;

public interface UserSessionService {
    String loginUser(AuthRequest authRequest);
    String logoutUser(AuthRequest authRequest);
}
