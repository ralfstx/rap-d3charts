/*******************************************************************************
 * Copyright (c) 2013 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.widgets.Composite;


public class GanttChart extends Chart {

  private static final String REMOTE_TYPE = "d3chart.GanttChart";

  private long chartFrom = 0;
  private long chartTo = 0;
  private int barHeight = 25;
  private int marginTop = 20;
  private int marginRight = 40;
  private int marginBottom = 40;
  private int marginLeft = 50;

  public GanttChart( Composite parent, int style ) {
    super( parent, style, REMOTE_TYPE );
  }

  public int getBarHeight() {
	checkWidget();
	return barHeight;
  }
  public void setBarHeight(int value) {
	checkWidget();
	if (barHeight != value) {
	  barHeight = value;
	  remoteObject.set("barHeight", value);
	}
  }

  public void setMargin(int top, int right, int bottom, int left) {
	checkWidget();
	if(marginTop != top || marginRight != right || marginBottom != bottom || marginLeft != left) {
	  marginTop = top;
	  marginRight = right;
	  marginBottom = bottom;
	  marginLeft = left;
	  JsonObject json = new JsonObject();
	  json.add("top", marginTop);
	  json.add("right",  marginRight);
	  json.add("bottom", marginBottom);
	  json.add("left",  marginLeft);
	  remoteObject.set( "margin", json );
	}
  }
  
  public void setTaskTypes(String [] values) {
	checkWidget();
	JsonArray json = new JsonArray();
	
	for (String text : values) {
	  json.add(text);
	}
	remoteObject.set( "taskTypes", json );
  }

  public JsonObject getTimeRange() {
	checkWidget();
	JsonObject json = new JsonObject();
	json.add("from", chartFrom);
	json.add("to",  chartTo);
	return json;
  }
  
  public void removeAllItems() {
	  remoteObject.set("removeAllItems", new JsonArray());
  }
  
  public void setTimeRange(long from, long to) {
	checkWidget();
	if(chartFrom != from || chartTo != to) {
	  chartFrom = from;
	  chartTo = to;
	  JsonObject json = new JsonObject();
	  json.add("from", chartFrom);
	  json.add("to",  chartTo);
	  remoteObject.set( "timeRange", json );
	}
  }

}
