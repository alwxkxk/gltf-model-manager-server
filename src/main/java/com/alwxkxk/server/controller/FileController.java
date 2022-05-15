package com.alwxkxk.server.controller;

import com.alwxkxk.server.entity.StorageFileNotFoundException;
import com.alwxkxk.server.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// TODO:数据库查表存表
// TODO:做一个统一的返回接口
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

	private final StorageService storageService;

	@Autowired
	public FileController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/list")
	public ResponseEntity listUploadedFiles() throws IOException {
		List<String> body =  storageService.loadAll().map(
            path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                    "serveFile", path.getFileName().toString()).build().toUri().toString())
            .collect(Collectors.toList());
		return ResponseEntity.ok().body(body);
	}

	@GetMapping("/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

//	@PostMapping()
//	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
//		storageService.store(file);
//		return ResponseEntity.ok().body("上传成功");
//	}

	@PostMapping()
	public ResponseEntity handleFileUpload(
			@RequestParam("file") MultipartFile file,
			@RequestParam("fileName") String fileName,
			@RequestParam("dirName") String dirName
	) {
		storageService.store(file,fileName,dirName);
		return ResponseEntity.ok().body("上传成功");
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		log.error("handleStorageFileNotFound.");
		return ResponseEntity.notFound().build();
	}

}