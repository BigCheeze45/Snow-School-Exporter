function updateGraph() {
  /************* DO NOT CHANGE *************/
  /*
  The variables here serve as constants and refer to specific cells in the WEATHER_REPORT
  Google Spreadsheet
  */
  var NEXT_INDEX_CELL = "G1";
  var DATE_CELL = "A3";
  var DATA_COUNT_CELL = "B3";
 /************* DO NOT CHANGE *************/
  var sourceID = DriveApp.getFilesByName("WEATHER_REPORT").next().getId();

/* Open and set active our souce file */
var sourceActive = SpreadsheetApp.openById(sourceID);
SpreadsheetApp.setActiveSpreadsheet(sourceActive);
/* Get first sheet from open book */
var sourceSheet = SpreadsheetApp.getActiveSpreadsheet().getSheets()[0];

var destID = DriveApp.getFilesByName("Snow Depth and Snow").next().getId();
var destActive = SpreadsheetApp.openById(destID);
SpreadsheetApp.setActiveSpreadsheet(destActive);
  
  /* Delete an exisiting copy of WEATHER_REPORT so the most recent version is bein used */
  var allActiveSheets = SpreadsheetApp.getActive();
  if(allActiveSheets.getSheetByName('Copy of WEATHER_REPORT') != null)
  {
    var sheetToDelete = allActiveSheets.getSheetByName('Copy of WEATHER_REPORT');
    allActiveSheets.deleteSheet(sheetToDelete);
  }
/* Copy WEATHER_REPORT to graph sheet*/
sourceSheet.copyTo(destActive);
/****************************************************************************************************************/

  var sheet = destActive.getSheets()[1]; //WEATHER_REPORT
  var dataCount = destActive.getSheets()[1].getRange(DATA_COUNT_CELL).getValue();
  /* SET DATE */
  var row = destActive.getSheets()[0].getRange(NEXT_INDEX_CELL).getValue();
  destActive.getSheets()[0].getRange("A" + row).setValue(destActive.getSheets()[1].getRange(DATE_CELL).getValue());
  
  /* SNOW DEPTH */
  var snowDestCell = sheet.getRange("C" + row);
  var snowDepth = snowDestCell.setFormula("=SUM(I4:I35)").getValue();
  destActive.getSheets()[0].getRange("C" + row).setValue(snowDepth / dataCount);
  
  /* WATER EQUI */
  var waterDestCell = sheet.getRange("D" + row);
  var waterEqui = waterDestCell.setFormula("=SUM(J4:J35)").getValue();
  destActive.getSheets()[0].getRange("D" + row).setValue(waterEqui / dataCount);
  
  //Update row to the next value
  destActive.getSheets()[0].getRange(NEXT_INDEX_CELL).setValue(row + 1);
}
