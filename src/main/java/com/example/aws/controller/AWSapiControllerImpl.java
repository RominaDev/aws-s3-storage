package com.example.aws.controller;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.aws.service.AWSS3ServiceImpl;

@RestController
@RequestMapping("/storage")
public class AWSapiControllerImpl {

	private static final String SID = "sid";

	@Autowired
	private AWSS3ServiceImpl service;

	@PostMapping(value = "/upload")
	public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
		String threadID = UUID.randomUUID().toString();
		ThreadContext.put(SID, threadID);
		service.uploadFile(file);
		String response = "El archivo " + file.getOriginalFilename() + " fue cargado correctamente a S3";
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/list")
	public ResponseEntity<List<String>> listFiles() {
		String threadID = UUID.randomUUID().toString();
		ThreadContext.put(SID, threadID);

		return new ResponseEntity<List<String>>(service.getObjectsFromS3(), HttpStatus.OK);
	}

}
