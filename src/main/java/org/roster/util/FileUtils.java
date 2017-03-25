package org.roster.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.roster.model.DayAndShift;
import org.roster.model.Volunteer;

/**
 * File Utility class.
 * @author cdacr
 */
public final class FileUtils {

	/**
	 * Fetch volunteer information from input spreadsheet. 
	 * @param fileName File name(must be in classpath) or 
	 * complete path of spreadsheet.
	 * @return {@link List} of {@link Volunteer}.
	 * @throws IOException IOException
	 */
	public List<Volunteer> getVolunteerInfo(final String fileName)
			throws IOException {
		final List<Volunteer> volunteers = new ArrayList<>();
		final ClassLoader classLoader = getClass().getClassLoader();
		InputStream inStream = classLoader.getResourceAsStream(fileName);
		try {
			// create file input stream if file is not available in classpath.
			if (inStream == null) {
				inStream = new FileInputStream(fileName);
			}
			final XSSFWorkbook workbook = new XSSFWorkbook(inStream);
			// assuming data is available in first sheet of workbook.
			final XSSFSheet sheet = workbook.getSheetAt(0);
			final Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				final Row row = rowIterator.next();
				// Skip header
				if (row.getRowNum() == 0) {
					continue;
				}
				prepareVolunteerData(row, volunteers);
			}
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (final Exception e2) {
			}
		}
		return volunteers;
	}

	/**
	 * Prepared volunteer data from each row.
	 * @param row Spreadsheet Row object.
	 * @param volunteers {@link List} of {@link Volunteer} 
	 * which will be updated with row data.
	 */
	private void prepareVolunteerData(final Row row,
			final List<Volunteer> volunteers) {
		final Volunteer volunteer = new Volunteer();

		volunteer.setName(row.getCell(0).getStringCellValue());
		int cellCounter = 1;
		final String[] daysHeader = new String[] { "MONDAY", "TUESDAY",
				"WEDNESDAY", "THURSDAY", "FRIDAY" };
		for (final String header : daysHeader) {
			final String cellValue = row.getCell(cellCounter,
					Row.CREATE_NULL_AS_BLANK).getStringCellValue();
			if (!cellValue.equals("")) {
				volunteer.addAvailability(DayAndShift.valueOf(header + "_"
						+ cellValue.toUpperCase()));
			}
			cellCounter++;
		}
		volunteers.add(volunteer);
	}

}
