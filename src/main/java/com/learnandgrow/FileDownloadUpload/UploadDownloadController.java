package com.learnandgrow.FileDownloadUpload;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/v1/file/")
public class UploadDownloadController {

    @Value("${files.path}")
    private String filePath;

    @GetMapping("download")
    public ResponseEntity<InputStreamResource> download() throws Exception{
        String fileName = "Template1.csv";
        File file = new File(filePath+File.separator+fileName);
        if(!file.exists()){
            System.out.println("File Not Found");
            return null;
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(file.length()) //
                .body(resource);

    }

    @GetMapping("generateAndDownload")
    public ResponseEntity<InputStreamResource> generateAndDownload() throws Exception{
        String fileName = "File_"+System.currentTimeMillis()+".csv";
        File file = new File(filePath+File.separator+fileName);

        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        fw.append("Name|age|gender");
        fw.flush();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(file.length()) //
                .body(resource);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "Make a POST request to upload the file")
    public ResponseEntity<String> uploadFile1(@RequestPart("file") MultipartFile file) throws Exception{

        System.out.println("File Name : " + file.getOriginalFilename());
        String line="";

        BufferedReader br = new BufferedReader(new FileReader((File) file));
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }
        return new ResponseEntity<String>("Done", HttpStatus.OK);
    }
}
