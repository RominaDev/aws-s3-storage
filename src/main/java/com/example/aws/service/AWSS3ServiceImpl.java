package com.example.aws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AWSS3ServiceImpl implements AWSs3Service {

	private static final String METHOD = "Method : {}.";
	private static final String MESSAGEPROCESS = "Solicitud procesada en : {}  milisegundos.";
	private static final String MESSAGEERROR = "Lo sentimos, intentelo nuevamente.";

	private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

	@Autowired
	private AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Override
	public void uploadFile(MultipartFile file) {
		log.log(Level.INFO, METHOD, "uploadFile");
		Long startTime = System.currentTimeMillis();
		File mainFile = new File(file.getOriginalFilename());

		try (FileOutputStream stream = new FileOutputStream(mainFile)) {
			stream.write(file.getBytes());
			String newFileName = System.currentTimeMillis() + "_" + mainFile.getName();

			LOGGER.info("Subiendo archivo con el nombre... " + newFileName);

			PutObjectRequest request = new PutObjectRequest(bucketName, newFileName, mainFile);
			amazonS3.putObject(request);

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			log.log(Level.INFO, MESSAGEERROR);
		}
		Long endTime = System.currentTimeMillis();
		log.log(Level.INFO, MESSAGEPROCESS, (endTime - startTime));

	}

	@Override
	public List<String> getObjectsFromS3() {
		log.log(Level.INFO, METHOD, "getObjectsFromS3");
		Long startTime = System.currentTimeMillis();
		List<String> list = null;
		try {
			ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
			List<S3ObjectSummary> objects = result.getObjectSummaries();
			list = objects.stream().map(item -> {
				return item.getKey();
			}).collect(Collectors.toList());

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			log.log(Level.INFO, MESSAGEERROR);
		}
		Long endTime = System.currentTimeMillis();
		log.log(Level.INFO, MESSAGEPROCESS, (endTime - startTime));
		return list;
	}

}
