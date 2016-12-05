package com.gwt.climatechangeapp.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
//import com.googlecode.gwt.charts.client.*;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.options.DisplayMode;

import com.gwt.climatechangeapp.shared.DataPoint;

import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
	
/**
 * 
 * The class WorldMapView generates the world map and points on the map.
 * 
 * @author Carla Coccia
 * @history 10-11-2016 CC First version
 * 			10-11-2016 LL Initialization 
 * 			11-11-2016 LL Color of markers
 * 			12-11-2016 CC Initialization corrected
 * @version 12-11-2016 CC Version 1
 * @responsabilities This class initializes the world map, draws the map, adds data and draws markers.
 *
 */
	
public class WorldMapView extends Composite{

	private DockLayoutPanel mainPanel = new DockLayoutPanel(Style.Unit.PX);
	private GeoChart geoChart;
	private Integer INITIAL_YEAR=2013;
	private double maxTemperature=40;
	private double minTemperature=-30;
	private double uncertainty=15;
	//all the cities and countries will be shown
	private String city="city";
	private String country="country";
	private DataServiceAsync dataService = GWT.create(DataService.class);
	private ArrayList<DataPoint> cityAtYearData;
	private final Date INITIAL_DATE = DateTimeFormat.getFormat("dd/MM/yyyy").parse("01/01/"+INITIAL_YEAR.toString());

	// Create the MapView
	public WorldMapView() {
		initWidget(mainPanel);
		initialize();
	}

	/**
	 * 
	 * Initializes the map, runs and draws it.
	 * @pre		geoChart is instantiated, mainPanel is instantiated
	 * @post	map is loaded and drawn
	 * 
	 */
		
	private void initialize() {
		ChartLoader chartLoader = new ChartLoader(ChartPackage.GEOCHART);
		chartLoader.loadApi(new Runnable() {

			@Override
			public void run() {
				// Create and attach the chart
				geoChart = new GeoChart();
				mainPanel.add(geoChart);
				updateGeoChart(INITIAL_DATE);
			}
		});
	}

	/**
	 * 
	 * Draws the map and markers (currently with test data).
	 * @pre		geoChart is instantiated, mainPanel is instantiated
	 * @post	map is loaded and drawn
	 * 
	 */
		
	private void draw() {
		// Prepare the data 
		// TODO: Function, which gets the right data form the list.
		DataTable dataTable = prepareData();

			
		// set geochart options
		GeoChartOptions options = GeoChartOptions.create();
		options.setDisplayMode(DisplayMode.MARKERS);
		GeoChartColorAxis geoChartColorAxis = GeoChartColorAxis.create();
		geoChartColorAxis.setColors(getNativeArray());
		options.setColorAxis(geoChartColorAxis);
		options.setDatalessRegionColor("gray");
			
			
		geoChart.draw(dataTable, options);
		
	}

	private DataTable prepareData() {
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "City");
		dataTable.addColumn(ColumnType.NUMBER, "Temperature");
		dataTable.addColumn(ColumnType.NUMBER,"Uncertainty");
		
	/*	dataTable.addRows(1);
		dataTable.setValue(0, 0, "Buenos Aires");
		dataTable.setValue(0, 0,13);
		dataTable.setValue(0, 0,2);
		*/

		dataTable.addRows(cityAtYearData.size());
		for (int i = 0; i < cityAtYearData.size(); i++) {
			dataTable.setValue(i, 0, cityAtYearData.get(i).getCountry());
			dataTable.setValue(i, 1, cityAtYearData.get(i).getTemperature());
			dataTable.setValue(i, 2, cityAtYearData.get(i).getUncertainty());
		}
		
		return dataTable;
	}
	/**
	 * 
	 * Sets the colors for markers.
	 * @pre		-
	 * @post	-
	 * @return	returns array with colors for markers
	 * 
	 */
	private void updateGeoChart(Date date) {
		if(dataService == null){
			dataService = GWT.create(DataService.class);
		}
		
		AsyncCallback<ArrayList<DataPoint>> callback = new AsyncCallback<ArrayList<DataPoint>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<DataPoint> result) {
				if(result.isEmpty()) Window.alert("No data recieved from server...");
				cityAtYearData = result;
				draw();
			}
		};

		dataService.temperatureMeasurementsOfAllCitiesAtDate(date, callback);

	}

	private native JsArrayString getNativeArray() /*-{
		return [ "0000FF", "5858FA", "A9A9F5", "F7819F", "FE2E64", "FF0040" ];
	}-*/;
	
	//returns ArrayList of average temperatures of each and all cities
	public ArrayList<DataPoint> generateCityAverageList(ArrayList<DataPoint> temperaturesAtMonths) {
		// TODO Auto-generated method stub
		ArrayList<DataPoint> averageTemperatures = new ArrayList<DataPoint>();
		double average;
		int numberOfTemperatures=1;
		double sum;
		int i=0;
		
		while(i < temperaturesAtMonths.size()){
			DataPoint currentDataPoint=temperaturesAtMonths.get(i);
			DataPoint nextDataPoint=temperaturesAtMonths.get(i+1);
			sum=currentDataPoint.getTemperature();
			average=sum/numberOfTemperatures;
			
			while(nextDataPoint!=null && currentDataPoint.getCity()==nextDataPoint.getCity()){
				numberOfTemperatures++;
				sum+=nextDataPoint.getTemperature();
				average=sum/numberOfTemperatures;
				i++;
			}
			currentDataPoint.setAverageTemperature(average);
			averageTemperatures.add(currentDataPoint);
			i++;
		}
		
		
		return averageTemperatures;
		
	}
}