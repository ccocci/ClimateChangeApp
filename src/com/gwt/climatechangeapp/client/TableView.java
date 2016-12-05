package com.gwt.climatechangeapp.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.i18n.client.DateTimeFormat;

import com.gwt.climatechangeapp.shared.DataPoint;;

/**
 * 
 * The class TableView generates the table and fills in the data.
 * 
 * @author Lina Witzel
 * @history 08-11-2016 LW First version
 * 			09-11-2016 LL Constructor corrected
 * 			12-11-2016 LW Insertion test data
 * 			13-11-2016 LW Change test data
 * 			26-11-2016 LW Add AsyncCallback and data import
 * @version 13-11-2016 LW Version 1
 * @responsabilities This class inititalizes the table and adds the data.
 *
 */

public class TableView extends Composite{
	
	private DockLayoutPanel mainPanel = new DockLayoutPanel(Style.Unit.EM);
	private FlexTable dataFlexTable = new FlexTable();
	private DataServiceAsync dataSvc = GWT.create(DataService.class);
	//private String[] data = {"01-08-2000","20.00","0.10", "New York", "United States of America", "44.99N", "74.56"};
	
	
	
	
	
	public TableView() {
		initWidget(mainPanel);
		fetchData("Aleppo");
		initialize();
	}
	
	/**
	 * 
	 * Initializes the table, setting the header, styling and adding the data.
	 * @pre		dataFlexTable is instance of FlexTable, mainPanel is instantiated
	 * @post	table is visible
	 * 
	 */
	
	private void initialize(){
		dataFlexTable.setText(0, 0, "Date");
		dataFlexTable.setText(0, 1, "Average Temperature");
		dataFlexTable.setText(0, 2, "Average Temperature Uncertainty");
		dataFlexTable.setText(0, 3, "City");
		dataFlexTable.setText(0, 4, "Country");
		
		
		dataFlexTable.getRowFormatter().addStyleName(0, "tableHeader");
		dataFlexTable.addStyleName("table");
		dataFlexTable.getCellFormatter().addStyleName(0, 0, "tableNumericColumn");
		dataFlexTable.getCellFormatter().addStyleName(0, 1, "tableNumericColumn");
		dataFlexTable.getCellFormatter().addStyleName(0, 2, "tableNumericColumn");
		dataFlexTable.getCellFormatter().addStyleName(0, 3, "tableNumericColumn");
		dataFlexTable.getCellFormatter().addStyleName(0, 4, "tableNumericColumn");
	
		
        mainPanel.add(dataFlexTable);
      
		
		
		// Initialize service proxy
		
		
	}
	
	/**
	 * 
	 * Adds the data to the table (currently not imported data).
	 * @pre		dataFlexTable is instance of FlexTable, size of data >= 7
	 * @post	i=7
	 * 
	 */
	
	public void fetchData(String city) {
		AsyncCallback<ArrayList<DataPoint>> callback = new AsyncCallback<ArrayList<DataPoint>>() {
			@Override
			public void onSuccess(ArrayList<DataPoint> result) {
				addData(result);
				//initialize the Table once the data from the database are ready to avoid errors
				initialize();		
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Do something
				GWT.log("Failed");
			}
		};
		// call to server
		dataSvc.temperatureMeasurements(city, callback);
	}
	
	
	private void addData(ArrayList<DataPoint> temperatureMeasurements){
		int numberOfRows = temperatureMeasurements.size();
		for(int i=1; i<=numberOfRows; i++){
			for(DataPoint temperatureMeasurement: temperatureMeasurements){
				dataFlexTable.setText(i, 0, DateTimeFormat.getFormat("dd/MM/yyyy").format(temperatureMeasurement.getDate()));
				dataFlexTable.setText(i, 1, String.valueOf(temperatureMeasurement.getTemperature()));
				dataFlexTable.setText(i, 2, String.valueOf(temperatureMeasurement.getUncertainty()));
				dataFlexTable.setText(i, 3, temperatureMeasurement.getCity());
				dataFlexTable.setText(i, 4, temperatureMeasurement.getCountry());
				}
			}
		
	}
}