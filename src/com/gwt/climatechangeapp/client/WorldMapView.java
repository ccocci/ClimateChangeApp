package com.gwt.climatechangeapp.client;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style;
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
	private int currentYear=2013;
	private double maxTemperature=40;
	private double minTemperature=-30;
	private double uncertainty=15;
	//all the cities and countries will be shown
	private String city="city";
	private String country="country";

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
				draw();
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


			DataTable dataTable = DataTable.create();
			dataTable.addColumn(ColumnType.STRING, "City");
			dataTable.addColumn(ColumnType.NUMBER, "Temperature");
			dataTable.addColumn(ColumnType.NUMBER,"Uncertainty");

	/*		dataTable.addRows(getData().size());
			for (int i = 0; i < getData().size(); i++) {
				dataTable.setValue(i, 0, getData().get(i).getRegion());
				dataTable.setValue(i, 1, getData().get(i).getAverageTemperature());
				dataTable.setValue(i, 2, getData().get(i).getUncertainty());
			}
			*/
			
			
		// set geochart options
		GeoChartOptions options = GeoChartOptions.create();
		options.setDisplayMode(DisplayMode.MARKERS);
		GeoChartColorAxis geoChartColorAxis = GeoChartColorAxis.create();
		geoChartColorAxis.setColors(getNativeArray());
		options.setColorAxis(geoChartColorAxis);
		options.setDatalessRegionColor("gray");
			
			
		geoChart.draw(dataTable, options);
		
	}

	/**
	 * 
	 * Sets the colors for markers.
	 * @pre		-
	 * @post	-
	 * @return	returns array with colors for markers
	 * 
	 */
/*	public void fetchData() {
		AsyncCallback<ArrayList<DataPoint>> callback = new AsyncCallback<ArrayList<DataPoint>>() {
			@Override
			public void onSuccess(ArrayList<DataPoint> result) {
				setData(result);
				draw();
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Do something
			}
		};
		// call to server with the correct attributes (default at the beginning)
		getDataService().getMapData(currentYear,minTemperature,maxTemperature,uncertainty,city,country,callback);
	}
	
*/
	private native JsArrayString getNativeArray() /*-{
		return [ "0000FF", "5858FA", "A9A9F5", "F7819F", "FE2E64", "FF0040" ];
	}-*/;
	
	public void fetchCountryList() {
		// TODO Auto-generated method stub
		
	}
}