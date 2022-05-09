package com.example.aws.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AWSapiController {

	public ResponseEntity<String> uploadFile(MultipartFile filDe);

	public ResponseEntity<List<String>> listFiles();

}
