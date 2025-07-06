package edu.cit.studentclearancesystem.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import edu.cit.studentclearancesystem.entity.ClearanceTask;
import edu.cit.studentclearancesystem.repository.ClearanceTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ClearanceTaskRepository clearanceTaskRepository;

    public byte[] generatePdfReport() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Clearance Task Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 4, 2, 4});

            addTableHeader(table);
            addRows(table);

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Student", "Email", "Status", "Comment")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        List<ClearanceTask> tasks = clearanceTaskRepository.findAll();

        for (ClearanceTask task : tasks) {
            table.addCell(task.getUser().getFullName());
            table.addCell(task.getUser().getEmail());
            table.addCell(task.getStatus().name());
            table.addCell(task.getComment() != null ? task.getComment() : "-");
        }
    }
}
