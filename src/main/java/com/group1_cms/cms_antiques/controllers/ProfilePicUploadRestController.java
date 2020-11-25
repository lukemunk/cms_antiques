package com.group1_cms.cms_antiques.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProfilePicUploadRestController {

    @RequestMapping(method = RequestMethod.POST, value = "/uploadProfileImgFile")
    @ResponseBody
    public ResponseEntity<?> uploadProfileImgFile(@RequestParam("uploadProfileImgFile")MultipartFile uploadProfileImgFile){

        return null;
    }
}
