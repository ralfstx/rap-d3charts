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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.internal.protocol.ProtocolUtil;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings( "restriction" )
public class ChartItem_Test {

  private Display display;
  private Shell shell;
  private Chart chart;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display );
    chart = new TestChart( shell, SWT.NONE );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void create_registersWithParent() {
    ChartItem chartItem = new ChartItem( chart );

    assertTrue( Arrays.asList( chart.getItems() ).contains( chartItem ) );
  }

  @Test
  public void create_createsRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    Connection connection = fakeConnection( remoteObject );

    new ChartItem( chart );

    verify( connection ).createRemoteObject( eq( "d3chart.ChartItem" ) );
  }

  @Test
  public void create_setsRemoteParent() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart );

    verify( remoteObject ).set( eq( "parent" ), eq( chart.getRemoteId() ) );
  }

  @Test
  public void dispose_deregistersFromParent() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.dispose();

    assertFalse( Arrays.asList( chart.getItems() ).contains( chartItem ) );
  }

  @Test
  public void dispose_destroysRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    ChartItem chartItem = new ChartItem( chart );

    chartItem.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void value_defaultsToZero() {
    ChartItem chartItem = new ChartItem( chart );

    double result = chartItem.getValue();

    assertEquals( 0, result, 0 );
  }

  @Test
  public void value_canBeChanged() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.setValue( 23.7 );

    assertEquals( 23.7, chartItem.getValue(), 0 );
  }

  @Test
  public void value_isTransferredToRemote() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart ).setValue( 23.7 );

    verify( remoteObject ).set( eq( "value" ), eq( 23.7 ) );
  }

  @Test
  public void color_defaultsToBlack() {
    ChartItem chartItem = new ChartItem( chart );

    Color result = chartItem.getColor();

    assertEquals( new Color( display, 0, 0, 0 ), result );
  }

  @Test
  public void color_canBeChanged() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.setColor( new Color( display, 255, 128, 0 ) );

    assertEquals( new Color( display, 255, 128, 0 ), chartItem.getColor() );
  }

  @Test
  public void color_isTransferredToRemote() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart ).setColor( new Color( display, 255, 128, 0 ) );

    JsonValue expected = ProtocolUtil.getJsonForColor( new Color( display, 255, 128, 0 ), false );
    verify( remoteObject ).set( eq( "color" ), eq( expected ) );
  }

  private static Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeConnection( connection );
    return connection;
  }

}
