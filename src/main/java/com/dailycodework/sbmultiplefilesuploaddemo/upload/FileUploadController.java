package com.dailycodework.sbmultiplefilesuploaddemo.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Simpson Alfred
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileUploadController {
    private final IFileUploadService fileUploadService;

    @PostMapping("/upload-files")
    public ResponseEntity<FileResponseMessage> uploadFiles(@RequestParam("file") MultipartFile[] files) {
        String message = null;
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                fileUploadService.save(file);
                fileNames.add(file.getOriginalFilename());
            });
            message = "File(s) uploaded successfully " + fileNames;
            return ResponseEntity.status(OK).body(new FileResponseMessage(message));
        } catch (Exception e) {
            return ResponseEntity.status(EXPECTATION_FAILED).body(new FileResponseMessage(e.getMessage()));
        }
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getFileByName(@PathVariable String fileName) {
        Resource resource = fileUploadService.getFileByName(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filaName=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/all-files")
    public ResponseEntity<List<FileResponse>> loadAllFiles() {
        List<FileResponse> files = fileUploadService.loadAllFiles()
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class,
                                    "getFileByName",
                                    path.getFileName().toString()).build().toString();
                    return new FileResponse(fileName, url);
                }).toList();
        return ResponseEntity.status(OK).body(files);
    }

}
