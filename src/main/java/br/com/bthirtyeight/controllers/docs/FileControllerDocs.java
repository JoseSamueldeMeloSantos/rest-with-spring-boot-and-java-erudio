package br.com.bthirtyeight.controllers.docs;

import br.com.bthirtyeight.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

    UploadFileResponseDTO uploadFIle(MultipartFile file);
    List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);
    ResponseEntity<Resource> downlaodFIle(String fileName, HttpServletRequest request);
}
