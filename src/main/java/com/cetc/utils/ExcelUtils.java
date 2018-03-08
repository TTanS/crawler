package com.cetc.utils;

import com.cetc.pojo.HCProduct;
import com.cetc.pojo.Product;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils
{
  public static void createExcel(String savePath, String name, String[] paras)
    throws Exception
  {
    Workbook wb = new XSSFWorkbook();
    

    Sheet sheet = wb.createSheet("商品信息");
    XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short)9);
    font.setFontName("宋体");
    cellStyle.setFont(font);
    

    Row row = sheet.createRow(0);
    row.setRowStyle(cellStyle);
    for (int i = 0; i < paras.length; i++) {
      row.createCell(i).setCellValue(paras[i]);
    }
    File saveDir = new File(savePath);
    if (!saveDir.exists()) {
      saveDir.mkdirs();
    }
    FileOutputStream fileOut = new FileOutputStream(savePath + name);
    wb.write(fileOut);
    fileOut.close();
  }
  
  public static void createExcel(String savePath, String name, String[] paras, Boolean isOverlap)
    throws Exception
  {
    File file = new File(savePath + name);
    if (!file.exists()) {
      createExcel(savePath, name, paras);
    } else if (isOverlap.booleanValue()) {
      createExcel(savePath, name, paras);
    }
  }
  
  public static void createExcel97(String savePath, String name)
    throws Exception
  {
    File file = new File(savePath + name);
    if (!file.exists())
    {
      Workbook wb = new XSSFWorkbook();
      

      Sheet sheet = wb.createSheet("商品信息");
      

      CellStyle cellStyle = wb.createCellStyle();
      Font font = wb.createFont();
      font.setFontHeightInPoints((short)9);
      font.setFontName("宋体");
      cellStyle.setFont(font);
      

      Row row = sheet.createRow(0);
      row.setRowStyle(cellStyle);
      row.createCell(0).setCellValue("商品名");
      
      row.createCell(1).setCellValue("商品单价");
      row.createCell(2).setCellValue("商品起订数");
      row.createCell(3).setCellValue("商品供货总量");
      row.createCell(4).setCellValue("商品发货期限");
      row.createCell(5).setCellValue("商品发布者所在地");
      row.createCell(6).setCellValue("商品订单有效期");
      row.createCell(7).setCellValue("商品信息最后更新时间");
      

      File saveDir = new File(savePath);
      if (!saveDir.exists()) {
        saveDir.mkdirs();
      }
      FileOutputStream fileOut = new FileOutputStream(savePath + name);
      wb.write(fileOut);
      fileOut.close();
    }
  }
  
  public static void createExcelOfData(Product product, String filePath)
    throws IOException, InvalidFormatException
  {
    FileInputStream fs = new FileInputStream(filePath);
    Workbook wb = WorkbookFactory.create(fs);
    Sheet sheet = wb.getSheetAt(0);
    
    FileOutputStream outFile = new FileOutputStream(filePath);
    

    CellStyle cellStyle = wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short)9);
    font.setFontName("宋体");
    cellStyle.setFont(font);
    
    Row row = sheet.createRow((short)sheet.getLastRowNum() + 1);
    row.setRowStyle(cellStyle);
    
    row.createCell(0).setCellValue(product.getProductName());
    
    row.createCell(1).setCellValue(product.getPrice());
    row.createCell(2).setCellValue(product.getPcs());
    row.createCell(3).setCellValue(product.getSupplyOrder());
    row.createCell(4).setCellValue(product.getDeliver());
    row.createCell(5).setCellValue(product.getLocationOfPublisher());
    row.createCell(6).setCellValue(product.getTermOfValidity());
    row.createCell(7).setCellValue(product.getLastUpdate());
    
    outFile.flush();
    wb.write(outFile);
    outFile.close();
  }
  
  public static void createExcelOfData(HCProduct hcProduct, String filePath)
    throws Exception
  {
    FileInputStream fs = new FileInputStream(filePath);
    Workbook wb = WorkbookFactory.create(fs);
    Sheet sheet = wb.getSheetAt(0);
    
    FileOutputStream outFile = new FileOutputStream(filePath);
    

    XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short)9);
    font.setFontName("宋体");
    cellStyle.setFont(font);
    
    Row row = sheet.createRow((short)sheet.getLastRowNum() + 1);
    row.setRowStyle(cellStyle);
    
    row.createCell(0).setCellValue(hcProduct.getProductName());
    row.createCell(1).setCellValue(hcProduct.getProductPrice());
    row.createCell(2).setCellValue(hcProduct.getProductCompany());
    row.createCell(3).setCellValue(hcProduct.getProductLoacation());
    row.createCell(4).setCellValue(hcProduct.getDate());
    
    outFile.flush();
    wb.write(outFile);
    outFile.close();
  }
  
  public static void createExcelOfData(String[] productData, String filePath)
    throws Exception
  {
    FileInputStream fs = new FileInputStream(filePath);
    Workbook wb = WorkbookFactory.create(fs);
    Sheet sheet = wb.getSheetAt(0);
    
    FileOutputStream outFile = new FileOutputStream(filePath);
    

    XSSFCellStyle cellStyle = (XSSFCellStyle)wb.createCellStyle();
    Font font = wb.createFont();
    font.setFontHeightInPoints((short)9);
    font.setFontName("宋体");
    cellStyle.setFont(font);
    
    Row row = sheet.createRow((short)sheet.getLastRowNum() + 1);
    row.setRowStyle(cellStyle);
    for (int i = 0; i < productData.length; i++) {
      row.createCell(i).setCellValue(productData[i]);
    }
    outFile.flush();
    wb.write(outFile);
    outFile.close();
  }
  
  public static boolean writeWordFile(String path, String fileName, String content)
  {
    boolean w = false;
    try
    {
      if (!"".equals(path))
      {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
          fileDir.mkdirs();
        }
        if (fileDir.exists())
        {
          byte[] b = content.getBytes();
          ByteArrayInputStream bais = new ByteArrayInputStream(b);
          POIFSFileSystem poifs = new POIFSFileSystem();
          DirectoryEntry directory = poifs.getRoot();
          DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
          FileOutputStream ostream = new FileOutputStream(path + fileName);
          poifs.writeFilesystem(ostream);
          bais.close();
          ostream.close();
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return w;
  }
  
  public static void htmlToWord(String htmlFile, String wordFile)
  {
    ActiveXComponent app = new ActiveXComponent("Word.Application");
    System.out.println("*****正在转换...*****");
    try
    {
      app.setProperty("Visible", new Variant(false));
      
      Dispatch wordDoc = app.getProperty("Documents").toDispatch();
      
      wordDoc = Dispatch.invoke(wordDoc, "Add", 1, new Object[0], new int[1]).toDispatch();
      
      Dispatch.invoke(app.getProperty("Selection").toDispatch(), "InsertFile", 1, 
        new Object[] { htmlFile, "", new Variant(false), new Variant(false), new Variant(false) }, 
        new int[3]);
      
      Dispatch.invoke(wordDoc, "SaveAs", 1, new Object[] { wordFile, new Variant(1) }, new int[1]);
      
      Dispatch.call(wordDoc, "Close", new Variant(false));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      app.invoke("Quit", new Variant[0]);
    }
    System.out.println("*****转换完毕********");
  }
}
