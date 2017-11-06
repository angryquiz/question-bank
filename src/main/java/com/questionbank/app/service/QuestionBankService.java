package com.questionbank.app.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.questionbank.app.model.QuestionDAO;

@Service
public class QuestionBankService {

	/**
	 * Validator - letters, numbers, spaces, underscore and dashes only.
	 * @param input
	 * @return
	 */
	public boolean isValidName(String input) {
		//https://www.tutorialspoint.com/java/java_regular_expressions.htm
		String pattern = "^[A-Za-z_-][A-Za-z0-9_-]*$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(input);
		if (!m.find() || input.length() > 128) {
			return false;
		}
		return true;		
	}
	
    /**
     * 
     * @param obj
     * @return
     */
	public List<QuestionDAO> loadData(Object obj) {

		List<QuestionDAO> questions = new ArrayList<QuestionDAO>();
		QuestionDAO q = null;	
		
		try {
			FileInputStream file = null;
			XSSFWorkbook workbook = null;
			if(obj instanceof InputStream) {
				workbook = new XSSFWorkbook((InputStream)obj);
			} else {
				file = new FileInputStream(new File(
						obj.toString()));
				workbook = new XSSFWorkbook(file);
				
			}

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			int questionCounter = 1;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				List<Object> items = new ArrayList<Object>();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						items.add(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						items.add(cell.getStringCellValue());
						break;
					}
				}
				if(items.size() == 0) {
					continue;
				}
				q = new QuestionDAO();
				q.setQuestionNumber(questionCounter);
				q.setCorrectAnswerCount(0);
				q.setExplanation(items.get(0).toString());
				q.setQuestion(items.get(1).toString());
				q.setAnswer(items.get(2).toString());
				q.setQuestionType( items.get(2).toString().contains(",") ? "multi" : "single");
				if(items.get(2).toString().contains("=")) {
					q.setQuestionType("match-term");	
				}
				Map<String,String> m1 = new HashMap<String,String>();
				//this can through an error if user put 6 answers or more. will throw business logic in future.
				String[] letters = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","o","p","q","r","s","t","u","v","w","x","y","z"};
				for(int i=3;i<items.size();i++) {
					m1.put((letters[i-3]),items.get(i).toString());	
				}
				q.setSelections(m1);
				questions.add(q);
				questionCounter++;
			}
			if(obj instanceof String) file.close();
		} catch (Exception e) {
			throw new RuntimeException("Excel file error");
		}
		return questions;
	}
    
    
}
