/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Vector;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.service.ResourceManager;


public class ChartResources {

  private static final String[] CHART_JS_RESOURCES = new String[] {
    "chart/chart.js",
    "chart/item-list.js",
    "chart/chart-item.js",
    "chart/bar-chart.js",
    "chart/gantt-chart.js",
    "chart/stream-chart.js",
    "chart/pie-chart.js"
  };
  private static final String[] CHART_CSS_RESOURCES = new String[] {
    "chart/gantt.css"
  };
  private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader() {

    @Override
    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      return ChartResources.class.getClassLoader().getResourceAsStream( resourceName );
    }
  };
  static String d3Location;
  static String chartLocation;

  static void ensureJavaScriptResources() {
    ResourceManager resourceManager = RWT.getApplicationContext().getResourceManager();
    if( !resourceManager.isRegistered( "lib/d3.v3.min.js" ) ) {
      try {
        d3Location = register( resourceManager,
                               "lib/d3.v3.min.js",
                               RESOURCE_LOADER.getResourceAsStream( "resources/d3.v3.min.js" ) );
        chartLocation = register( resourceManager,
                                  "d3chart/d3chart.js",
                                  concatResources( RESOURCE_LOADER, CHART_JS_RESOURCES ) );
        register( resourceManager,
                  "d3chart/d3chart.css",
                  concatResources( RESOURCE_LOADER, CHART_CSS_RESOURCES ) );
      } catch( IOException exception ) {
        throw new RuntimeException( "Failed to register resource", exception );
      }
    }
    JavaScriptLoader loader = RWT.getClient().getService( JavaScriptLoader.class );
    loader.require( d3Location );
    loader.require( chartLocation );
  }

  private static String register( ResourceManager resourceManager,
                                  String registerPath,
                                  InputStream inputStream ) throws IOException
  {
    String location;
    try {
      resourceManager.register( registerPath, inputStream );
      location = resourceManager.getLocation( registerPath );
    } finally {
      inputStream.close();
    }
    return location;
  }

  private static InputStream concatResources( ResourceLoader loader, String... resourceNames )
    throws IOException
  {
    Vector<InputStream> inputStreams = new Vector<InputStream>( resourceNames.length );
    for( String resourceName : resourceNames ) {
      inputStreams.add( loader.getResourceAsStream( resourceName ) );
    }
    return new SequenceInputStream( inputStreams.elements() );
  }
}
