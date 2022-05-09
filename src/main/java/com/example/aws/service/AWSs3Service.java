package com.example.aws.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AWSs3Service {

	public void uploadFile(MultipartFile file);

	public List<String> getObjectsFromS3();

}
