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

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Chart_Test {

  private Display display;
  private Shell shell;

  @Before
  public void setUp() {
    Fixture.setUp();
    display = new Display();
    shell = new Shell( display );
  }

  @After
  public void tearDown() {
    Fixture.tearDown();
  }

  @Test
  public void testCreate() {
    Chart chart = new Chart( shell, SWT.BORDER );

    assertEquals( shell, chart.getParent() );
    assertEquals( SWT.BORDER, chart.getStyle() & SWT.BORDER );
  }

  @Test
  public void testCreate_registeresJavaScriptResource() {
    new Chart( shell, SWT.NONE );

    assertTrue( RWT.getResourceManager().isRegistered( "d3.v3.min.js" ) );
    assertTrue( RWT.getResourceManager().isRegistered( "d3chart.js" ) );
  }

  @Test
  public void testCreate_createsRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    Connection connection = fakeConnection( remoteObject );

    new Chart( shell, SWT.NONE );

    verify( connection ).createRemoteObject( eq( "d3chart.Chart" ) );
  }

  @Test
  public void testCreate_setsRemoteParent() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    Chart chart = new Chart( shell, SWT.NONE );

    verify( remoteObject ).set( eq( "parent" ), eq( WidgetUtil.getId( chart ) ) );
  }

  @Test
  public void testDispose_destroysRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    Chart chart = new Chart( shell, SWT.NONE );

    chart.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void testGetItems_emptyByDefault() {
    Chart chart = new Chart( shell, SWT.BORDER );

    assertEquals( 0, chart.getItems().length );
  }

  @Test
  public void testAddItem() {
    Chart chart = new Chart( shell, SWT.BORDER );
    ChartItem item = mock( ChartItem.class );

    chart.addItem( item );

    assertEquals( 1, chart.getItems().length );
    assertTrue( Arrays.asList( chart.getItems() ).contains( item ) );
  }

  @Test
  public void testRemoveItem() {
    Chart chart = new Chart( shell, SWT.BORDER );
    ChartItem item = mock( ChartItem.class );
    chart.addItem( item );

    chart.removeItem( item );

    assertEquals( 0, chart.getItems().length );
  }

  @Test
  public void type_defaultsToBar() {
    Chart chart = new Chart( shell, SWT.BORDER );

    String type = chart.getType();

    assertEquals( "bar", type );
  }

  @Test
  public void type_canBeChanged() {
    Chart chart = new Chart( shell, SWT.BORDER );

    chart.setType( "pie" );
    String type = chart.getType();

    assertEquals( "pie", type );
  }

  @Test
  public void type_isRenderedToRemote() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    Chart chart = new Chart( shell, SWT.BORDER );

    chart.setType( "pie" );

    verify( remoteObject ).set( eq( "type" ), eq( "pie" ) );
  }

  private static Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeConnection( connection );
    return connection;
  }

}
