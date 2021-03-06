package com.wonders.frame.ams.utils;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class ExcelUtil {
	
    /**  
     * @see 输出Excel   
     * @author wangsq
     */  
	public static HSSFWorkbook getTempletWorkbook(HttpServletRequest request,String templetPath){
        InputStream is = request.getSession().getServletContext().getResourceAsStream(templetPath);
		POIFSFileSystem fs;
		HSSFWorkbook wb = null;
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return wb;
	}
	
	
	/**
	 * Export to Excel works with the ExcelML format. ExcelML is XML-based file format. It complies to the Microsoft XMLSS specification and is supported in Microsoft Office 2003 and later. 
	 */
	public static void output(HttpServletResponse response,String xml,String xlsName){

	     response.setContentType("application/vnd.ms-excel");          

	     try {
			xlsName = URLEncoder.encode(xlsName,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     
	     response.setHeader("Content-disposition", "attachment;filename=" + xlsName + ".xls");
	     PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(xml);
		    pw.flush();        
		    pw.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
    /**  
     * @see 输出Excel   
     * @author wangsq
     */  
	public static void output(HttpServletResponse response,HSSFWorkbook wb,String xlsName){
	     response.setContentType("application/vnd.ms-excel");          
	     
	     try {
			xlsName = URLEncoder.encode(xlsName,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     
	     response.setHeader("Content-disposition", "attachment;filename=" + xlsName + ".xls");       
	     OutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			wb.write(ouputStream); 
		    ouputStream.flush();        
		    ouputStream.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	     

		
	     
	}
	
	
    /**  
     * @see 拷贝Excel sheet  
     * @author wangsq
     * @param fromsheet  
     * @param toSheet  
     * @param firstrow  
     * @param lastrow  
     */  
    public static void copySheet(HSSFSheet fromsheet,HSSFSheet toSheet, int firstrow, int lastrow) {   
        if ((firstrow == -1) || (lastrow == -1) || lastrow < firstrow) {   
            return;   
        }   
        // 拷贝合并的单元格   
        
       CellRangeAddress region = null;   
        for (int i = 0; i < fromsheet.getNumMergedRegions(); i++) {   
            region = fromsheet.getMergedRegion(i);
            if ((region.getFirstRow() >= firstrow)&& (region.getLastRow() <= lastrow)) {   
                toSheet.addMergedRegion(region);   
            }   
        }   
  
        HSSFRow fromRow = null;   
        HSSFRow newRow = null;   
        HSSFCell newCell = null;   
        HSSFCell fromCell = null;   
        // 设置列宽   
        for (int i = firstrow; i <= lastrow; i++) {   
            fromRow = fromsheet.getRow(i);   
            if (fromRow != null) {   
                for (int j = fromRow.getLastCellNum(); j >= fromRow.getFirstCellNum(); j--) {   
                    int colnum = fromsheet.getColumnWidth( j);   
                    if (colnum > 100) {   
                        toSheet.setColumnWidth( j, colnum);   
                    }   
                    if (colnum == 0) {   
                        toSheet.setColumnHidden( j, true);   
                    } else {   
                        toSheet.setColumnHidden( j, false);   
                    }   
                }   
                break;   
            }   
        }   
        // 拷贝行并填充数据   
        for (int i = 0; i <= lastrow; i++) {   
            fromRow = fromsheet.getRow(i);   
            if (fromRow == null) {   
                continue;   
            }   
            newRow = toSheet.createRow(i - firstrow);   
            newRow.setHeight(fromRow.getHeight());   
            for (int j = fromRow.getFirstCellNum(); j < fromRow.getPhysicalNumberOfCells(); j++) {   
                fromCell = fromRow.getCell(j);   
                if (fromCell == null) {   
                    continue;   
                }   
                newCell = newRow.createCell(j);   
                newCell.setCellStyle(fromCell.getCellStyle());   
                int cType = fromCell.getCellType();   
                newCell.setCellType(cType);   
                switch (cType) {   
                case HSSFCell.CELL_TYPE_STRING:   
                    newCell.setCellValue(fromCell.getRichStringCellValue());   
                    break;   
                case HSSFCell.CELL_TYPE_NUMERIC:   
                    newCell.setCellValue(fromCell.getNumericCellValue());   
                    break;   
                case HSSFCell.CELL_TYPE_FORMULA:   
                    newCell.setCellFormula(fromCell.getCellFormula());   
                    break;   
                case HSSFCell.CELL_TYPE_BOOLEAN:   
                    newCell.setCellValue(fromCell.getBooleanCellValue());   
                    break;   
                case HSSFCell.CELL_TYPE_ERROR:   
                    newCell.setCellValue(fromCell.getErrorCellValue());   
                    break;   
                default:   
                    newCell.setCellValue(fromCell.getRichStringCellValue());   
                    break;   
                }   
            }   
        }   

    }




    public static String getCellStringValue(Cell cell){
        String  rtn = "";
        if(cell != null){
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING :
                    rtn = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK :
                    rtn = "";
                    break;
                case Cell.CELL_TYPE_NUMERIC :
                    rtn = new BigDecimal(cell.getNumericCellValue()).toString();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    rtn = cell.getBooleanCellValue() + "";
                    break;
                case Cell.CELL_TYPE_FORMULA :
                    rtn = cell.getCellFormula() + "";
                    break;
                case Cell.CELL_TYPE_ERROR :
                    rtn = "";
                    break;
                default:
                    rtn = "";
                    break;
            }
        }
        return rtn;

    }
    
    

    
    
    
    
    
    
   
}
