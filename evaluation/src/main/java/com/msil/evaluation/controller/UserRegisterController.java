package com.msil.evaluation.controller;


import com.msil.evaluation.constants.PathConstants;
import com.msil.evaluation.constants.StringConstants;
import com.msil.evaluation.dto.ApiResponse;

import com.msil.evaluation.entity.UserDetail;
import com.msil.evaluation.service.UserRegistryService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Log4j2
@RequestMapping(PathConstants.User)
@RestController
public class UserRegisterController {
    @Autowired
    UserRegistryService userRegistryService;
    @PostMapping(PathConstants.User_Registration)           //create a user
    public ResponseEntity<ApiResponse<UserDetail>> addUser(@Valid @RequestBody UserDetail userDetail) {
        log.info(StringConstants.Request_To_Add_User);
        return ResponseEntity.ok(new ApiResponse<>((StringConstants.User_register_success), userRegistryService.registerUser(userDetail), HttpStatus.OK));
    }

}