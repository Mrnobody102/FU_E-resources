package fpt.edu.eresourcessystem.utils;

import fpt.edu.eresourcessystem.model.CourseLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExportFileExcelUtil {
    public void export(OutputStream outputStream, List<CourseLog> logs) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Course Logs");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Id", "CourseId", "CourseCode", "CourseName", "Lecturer Email", "Action", "Action on object", "Name", "Time"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (CourseLog log : logs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(log.getId());
            row.createCell(1).setCellValue(log.getCourseId());
            row.createCell(2).setCellValue(log.getCourseCode());
            row.createCell(3).setCellValue(log.getCourseName());
            row.createCell(4).setCellValue(log.getEmail());
            row.createCell(5).setCellValue(log.getAction().toString());
            row.createCell(6).setCellValue(log.getObject().toString());
            row.createCell(7).setCellValue(log.getObjectName());
            row.createCell(8).setCellValue(log.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        // Add pagination information to the sheet
//        Row paginationRow = sheet.createRow(rowNum);
//        Cell pageCell = paginationRow.createCell(0);
//        pageCell.setCellValue("Page:");
//        Cell pageValueCell = paginationRow.createCell(1);
//        pageValueCell.setCellValue(page + 1);
//
//        Cell sizeCell = paginationRow.createCell(2);
//        sizeCell.setCellValue("Size:");
//        Cell sizeValueCell = paginationRow.createCell(3);
//        sizeValueCell.setCellValue(size);

        workbook.write(outputStream);
    }
}
