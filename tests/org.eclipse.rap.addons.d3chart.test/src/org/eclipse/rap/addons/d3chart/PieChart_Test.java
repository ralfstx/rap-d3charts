/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class PieChart_Test {

  private Display display;
  private Shell shell;
  private RemoteObject remoteObject;
  private Connection connection;

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    connection = fakeConnection( remoteObject );
  }

  @Test
  public void testCreate_createsRemoteObject() {
    new PieChart( shell, SWT.NONE );

    verify( connection ).createRemoteObject( eq( "d3chart.PieChart" ) );
  }

  @Test
  public void testInnerRadius_defaultValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    float innerRadius = pieChart.getInnerRadius();

    assertEquals( 0, innerRadius, 0 );
  }

  @Test
  public void testInnerRadius_changeValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setInnerRadius( 0.5f );

    assertEquals( 0.5, pieChart.getInnerRadius(), 0 );
  }

  @Test
  public void testInnerRadius_isRendered() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setInnerRadius( 0.5f );

    verify( remoteObject ).set( eq( "innerRadius" ), eq( 0.5 ) );
  }

  @Test
  public void testStartAngle_defaultValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    float startAngle = pieChart.getStartAngle();

    assertEquals( 0, startAngle, 0 );
  }

  @Test
  public void testStartAngle_changeValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setStartAngle( 0.5f );

    assertEquals( 0.5, pieChart.getStartAngle(), 0 );
  }

  @Test
  public void testStartAngle_isRendered() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setStartAngle( 0.5f );

    verify( remoteObject ).set( eq( "startAngle" ), eq( 0.5 * Math.PI * 2 ) );
  }

  @Test
  public void testEndAngle_defaultValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    float endAngle = pieChart.getEndAngle();

    assertEquals( 1, endAngle, 0 );
  }

  @Test
  public void testEndAngle_changeValue() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setEndAngle( 0.5f );

    assertEquals( 0.5, pieChart.getEndAngle(), 0 );
  }

  @Test
  public void testEndAngle_isRendered() {
    PieChart pieChart = new PieChart( shell, SWT.NONE );

    pieChart.setEndAngle( 0.5f );

    verify( remoteObject ).set( eq( "endAngle" ), eq( 0.5 * Math.PI * 2 ) );
  }

  private Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    context.replaceConnection( connection );
    return connection;
  }

}
