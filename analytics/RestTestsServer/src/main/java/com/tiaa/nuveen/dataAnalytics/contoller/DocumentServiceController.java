package com.tiaa.nuveen.dataAnalytics.contoller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.activation.FileTypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.tiaa.nuveen.dataAnalytics.model.Greeting;


@RestController
@RequestMapping("/api")
public class DocumentServiceController {
	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceController.class);
	private final static float upperRighterX = 125;
	private final static float upperRighterY =	 55;
	
	@Value("${data.analytics.document.uploaded.folder}")
	private String uploadedFolder;
	
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
    

	@GetMapping("image")
	public ResponseEntity<byte[]> getImage() throws IOException {
		logger.info("Image file download request received");
		
		File img = new File("src/main/resources/static/flower.png");
		return ResponseEntity.ok()
				.contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img)))
				.body(Files.readAllBytes(img.toPath()));
	}

	@GetMapping("pdf")
	public ResponseEntity<byte[]> what() throws IOException {
		logger.info("PDF file download request received");
		
		File file = new File("src/main/resources/static/test.pdf");
		return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
				.contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)))
				.body(Files.readAllBytes(file.toPath()));
	}
	
	 // Single file upload
    @PostMapping("upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {
        logger.info("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity<String>("please select a file!", HttpStatus.OK);
        }

        try {
            saveUploadedFiles(Arrays.asList(uploadfile));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Successfully uploaded - " + uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    
    //Multiple file upload
    @PostMapping("upload/multi")
    public ResponseEntity<?> uploadFileMulti(
            @RequestParam("extraField") String extraField,
            @RequestParam("files") MultipartFile[] uploadfiles) {

        logger.debug("Multiple file upload!");

        // Get file names
        String uploadedFileName = Arrays.stream(uploadfiles)
					        		.map(x -> x.getOriginalFilename())
					                .filter(x -> !StringUtils.isEmpty(x))
					                .collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<String>("please select a file!", HttpStatus.OK);
        }

        try {
            saveUploadedFiles(Arrays.asList(uploadfiles));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);

    }

    
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; 
            }

            byte[] bytes = file.getBytes();
            Path path = FileSystems.getDefault().getPath(uploadedFolder, file.getOriginalFilename());
            
            Files.write(path, bytes);
        }

    }

}
