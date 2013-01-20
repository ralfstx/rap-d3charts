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
package org.eclipse.rap.demo.d3chart.internal.data;

import java.io.IOException;


public class ExampleData {

  private static final String RESOURCE_VERSION_MONTHLY = "browser_version_partially_combined-ww-monthly-200807-201301.csv";
  private static final String RESOURCE_VERSION_QUARTERLY = "browser_version_partially_combined-ww-quarterly-200803-201301.csv";
  private static final String RESOURCE_VERSION_YEARLY = "browser_version_partially_combined-ww-yearly-2008-2013.csv";
  private static final String RESOURCE_BROWSER_MONTHLY = "browser-ww-monthly-200807-201301.csv";
  private static final String RESOURCE_BROWSER_QUARTERLY = "browser-ww-quarterly-200803-201301.csv";
  private static final String RESOURCE_BROWSER_YEARLY = "browser-ww-yearly-2008-2013.csv";

  public static final DataSet VERSION_MONTHLY = createDataSet( RESOURCE_VERSION_MONTHLY );
  public static final DataSet VERSION_QUARTERLY = createDataSet( RESOURCE_VERSION_QUARTERLY );
  public static final DataSet VERSION_YEARLY = createDataSet( RESOURCE_VERSION_YEARLY );
  public static final DataSet BROWSER_MONTHLY = createDataSet( RESOURCE_BROWSER_MONTHLY );
  public static final DataSet BROWSER_QUARTERLY = createDataSet( RESOURCE_BROWSER_QUARTERLY );
  public static final DataSet BROWSER_YEARLY = createDataSet( RESOURCE_BROWSER_YEARLY );

  private static DataSet createDataSet( String resourceName ) {
    try {
      return new DataSet( "resources/" + resourceName );
    } catch( IOException exception ) {
      throw new RuntimeException( "Could not read data", exception );
    }
  }

}
