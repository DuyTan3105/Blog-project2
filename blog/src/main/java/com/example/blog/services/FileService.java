package com.example.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadImage (String type, MultipartFile file) throws IOException;
    InputStream getResource (String fileName) throws FileNotFoundException;
}
