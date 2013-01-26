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

import static org.eclipse.rap.custom.d3chart.TestUtil.fakeConnection;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class BarChart_Test {

  private Display display;
  private Shell shell;
  private RemoteObject remoteObject;
  private Connection connection;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    connection = fakeConnection( remoteObject );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreate_createsRemoteObject() {
    new BarChart( shell, SWT.NONE );

    verify( connection ).createRemoteObject( eq( "d3chart.BarChart" ) );
  }

  @Test
  public void testBarWidth_defaultValue() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    int result = barChart.getBarWidth();

    assertEquals( 25, result );
  }

  @Test
  public void testBarWidth_changeValue() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setBarWidth( 42 );

    assertEquals( 42, barChart.getBarWidth() );
  }

  @Test
  public void testBarWidth_isRendered() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setBarWidth( 42 );

    verify( remoteObject ).set( eq( "barWidth" ), eq( 42 ) );
  }

  @Test
  public void testBarWidth_notRenderedIfUnchanged() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setBarWidth( barChart.getBarWidth() );

    verify( remoteObject, times( 0 ) ).set( eq( "barWidth" ), anyInt() );
  }

  @Test
  public void testSpacing_defaultValue() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    int result = barChart.getSpacing();

    assertEquals( 2, result );
  }

  @Test
  public void testSpacing_changeValue() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setSpacing( 23 );

    assertEquals( 23, barChart.getSpacing() );
  }

  @Test
  public void testSpacing_isRendered() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setSpacing( 23 );

    verify( remoteObject ).set( eq( "spacing" ), eq( 23 ) );
  }

  @Test
  public void testSpacing_notRenderedIfUnchanged() {
    BarChart barChart = new BarChart( shell, SWT.NONE );

    barChart.setSpacing( barChart.getSpacing() );

    verify( remoteObject, times( 0 ) ).set( eq( "spacing" ), anyInt() );
  }

}
