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
package org.eclipse.rap.custom.d3chart;

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
    "chart.js",
    "chart-item.js",
    "chart-renderer-bar.js",
    "chart-renderer-pie.js"
  };
  private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader() {
    public InputStream getResourceAsStream( String resourceName ) throws IOException {
      return ChartResources.class.getClassLoader().getResourceAsStream( "resources/" + resourceName );
    }
  };

  static void ensureJavaScriptResources() {
    String d3Location = null;
    String chartLocation = null;
    ResourceManager resourceManager = RWT.getApplicationContext().getResourceManager();
    try {
      // TODO register resources only once
      d3Location = register( resourceManager, new Resource( "d3.v3.min.js", RESOURCE_LOADER ) );
      chartLocation = register( resourceManager, new Resource( "d3chart.js", RESOURCE_LOADER ) {
        @Override
        public InputStream getInputStream() throws IOException {
          return concatResources( RESOURCE_LOADER, CHART_JS_RESOURCES );
        }
      } );
    } catch( IOException exception ) {
      throw new RuntimeException( "Failed to register resource", exception );
    }
    JavaScriptLoader loader = RWT.getClient().getService( JavaScriptLoader.class );
    loader.require( d3Location );
    loader.require( chartLocation + "?nocache=" + System.currentTimeMillis() );
  }

  private static String register( ResourceManager resourceManager, Resource resource )
    throws IOException
  {
    String location;
    InputStream inputStream = resource.getInputStream();
    String resourceName = resource.getName();
    try {
      resourceManager.register( resourceName, inputStream );
      location = resourceManager.getLocation( resourceName );
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

  private static class Resource {

    private final String name;
    private final ResourceLoader loader;

    public Resource( String name, ResourceLoader loader ) {
      this.name = name;
      this.loader = loader;
    }

    public String getName() {
      return name;
    }

    public InputStream getInputStream() throws IOException {
      return loader.getResourceAsStream( name );
    }

  }

}
