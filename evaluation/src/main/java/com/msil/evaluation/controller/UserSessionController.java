package com.msil.evaluation.controller;
import com.msil.evaluation.constants.PathConstants;
import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.*;
import com.msil.evaluation.service.UserSessionService;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@RequestMapping(PathConstants.Session)
public class UserSessionController {

    @Autowired
    UserSessionService userSessionService;

    @PostMapping(PathConstants.User_Login)//create user sessions and provides token
    public ResponseEntity<ApiResponse<String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info(StringConstants.User_Login_Requested);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.User_LoggedIn_Successfully),userSessionService.loginUser(authRequest)));
    }

    @PostMapping(PathConstants.User_Logout)//user session is invalid
    public ResponseEntity<StringResponse<String>> userLogout(@RequestBody AuthRequest authRequest) {
        log.info(StringConstants.User_Logout_Requested);
        return ResponseEntity.ok(new StringResponse<>(userSessionService.logoutUser(authRequest)));
    }
}
