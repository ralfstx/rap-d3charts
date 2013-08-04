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
package org.eclipse.rap.addons.d3chart.demo.internal.data;

import java.io.IOException;


public class ExampleData {

  public static final DataSet VERSION_MONTHLY = createDataSet( "browser_version_partially_combined-ww-monthly-200807-201301.csv" );
  public static final DataSet VERSION_QUARTERLY = createDataSet( "browser_version_partially_combined-ww-quarterly-200803-201301.csv" );
  public static final DataSet VERSION_YEARLY = createDataSet( "browser_version_partially_combined-ww-yearly-2008-2013.csv" );
  public static final DataSet BROWSER_MONTHLY = createDataSet( "browser-ww-monthly-200807-201301.csv" );
  public static final DataSet BROWSER_QUARTERLY = createDataSet( "browser-ww-quarterly-200803-201301.csv" );
  public static final DataSet BROWSER_QUARTERLY_EUROPE = createDataSet( "browser-eu-quarterly-200803-201301.csv" );
  public static final DataSet BROWSER_QUARTERLY_NORTHAMERICA = createDataSet( "browser-na-quarterly-200803-201301.csv" );
  public static final DataSet BROWSER_QUARTERLY_ASIA = createDataSet( "browser-as-quarterly-200803-201301.csv" );
  public static final DataSet BROWSER_QUARTERLY_AFRICA = createDataSet( "browser-af-quarterly-200803-201301.csv" );
  public static final DataSet BROWSER_YEARLY = createDataSet( "browser-ww-yearly-2008-2013.csv" );

  private static DataSet createDataSet( String resourceName ) {
    try {
      return new DataSet( "resources/" + resourceName );
    } catch( IOException exception ) {
      throw new RuntimeException( "Could not read data", exception );
    }
  }

}
