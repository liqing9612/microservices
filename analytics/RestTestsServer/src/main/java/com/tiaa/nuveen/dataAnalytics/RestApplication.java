package com.tiaa.nuveen.dataAnalytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfAppearance;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;


@SpringBootApplication
public class RestApplication implements CommandLineRunner {
    private final static float width=125;
    private final static float height = 55;
	private final static String src = "src/main/resources/pdf/test.pdf";
	private final static String dest = "src/main/resources/pdf/output.pdf";

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class);
	}

	@Override
	public void run(String... args) {
		try {
			File file = new File(dest);
			file.getParentFile().mkdirs();

			addingScoringEngineCommentToPdf(src, dest, 1, 200, 200, "this is test contents.\n this is second line.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.exit(0);
	}

	public void addingScoringEngineCommentToPdf(String src, String dest, int pageNum, float coordinateX,
			float coordinateY, String scoringEngineComment) throws IOException {
		try {

			PdfReader reader = new PdfReader(src);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));

			show(reader.getPageSize(1));
			show(reader.getPageSizeWithRotation(1));

			PdfContentByte cb = stamper.getOverContent(pageNum);
			Rectangle rect = new Rectangle(coordinateX, coordinateX, coordinateX + width, coordinateY + height);
			float width = rect.getWidth();
			float height = rect.getHeight();
			PdfAppearance cs = cb.createAppearance(width, height);
			cs.rectangle(0, 0, width, height);
			cs.fill();
			cs.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 12);
			cs.setColorFill(BaseColor.RED);

			 PdfAnnotation annotation = PdfAnnotation.createFreeText(stamper.getWriter(),
			 rect, scoringEngineComment, cs);

			cb.addAnnotation(annotation, true);

			stamper.close();
			reader.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void show(Rectangle rect) {
		System.out.print("llx: ");
		System.out.print(rect.getLeft());
		System.out.print(", lly: ");
		System.out.print(rect.getBottom());
		System.out.print(", urx: ");
		System.out.print(rect.getRight());
		System.out.print(", lly: ");
		System.out.print(rect.getTop());
		System.out.print(", rotation: ");
		System.out.println(rect.getRotation());
	}

	public void updateCreatedPdf() {

		try {

			// Reads and parses a PDF document
			PdfReader reader = new PdfReader(dest);

			// For each PDF page
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {

				// Get a page a PDF page
				PdfDictionary page = reader.getPageN(i);
				// Get all the annotations of page i
				PdfArray annotsArray = page.getAsArray(PdfName.ANNOTS);

				// If page does not have annotations
				if (page.getAsArray(PdfName.ANNOTS) == null) {
					continue;
				}

				// For each annotation
				for (int j = 0; j < annotsArray.size(); ++j) {
					// For current annotation
					PdfDictionary curAnnot = annotsArray.getAsDict(j);
					// Check the annotation subtype and print its text if not null
					writeAnnotation(curAnnot, reader, i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeAnnotation(PdfDictionary annot, PdfReader reader, int pageNumber) {

		if (annot == null) {
			return;
		}

		PdfString text = null;
		boolean mayHaveTextAnnotated = false;

		// Highlights with comments (balloons) and Highliths
		if (PdfName.HIGHLIGHT.equals(annot.get(PdfName.SUBTYPE))) {
			// Only Highlights with comments may have text
			text = (PdfString) annot.get(PdfName.CONTENTS);
			mayHaveTextAnnotated = true;
		} else if (PdfName.UNDERLINE.equals(annot.get(PdfName.SUBTYPE))) {
			text = annot.getAsString(PdfName.CONTENTS);
			mayHaveTextAnnotated = true;
			// A comment (a balloon with a comment)
		} else if (PdfName.TEXT.equals(annot.get(PdfName.SUBTYPE))) {
			text = annot.getAsString(PdfName.CONTENTS);
			
			
		} else {
			text = annot.getAsString(PdfName.CONTENTS);
		}

		if (text != null) {
			System.out.println(" -> " + text);
		}

	}



}
