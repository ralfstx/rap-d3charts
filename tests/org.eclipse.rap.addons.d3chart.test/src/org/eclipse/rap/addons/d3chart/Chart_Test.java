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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.rap.rwt.widgets.WidgetUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class Chart_Test {

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
  public void testCreate() {
    Chart chart = new Chart( shell, SWT.BORDER, "foo" ) {};

    assertEquals( shell, chart.getParent() );
    assertEquals( SWT.BORDER, chart.getStyle() & SWT.BORDER );
  }

  @Test
  public void testCreate_registeresJavaScriptResource() {
    new Chart( shell, SWT.BORDER, "foo" ) {};

    assertTrue( RWT.getResourceManager().isRegistered( "lib/d3.min.js" ) );
    assertTrue( RWT.getResourceManager().isRegistered( "d3chart/d3chart.js" ) );
  }

  @Test
  public void testCreate_createsRemoteObject() {
    new Chart( shell, SWT.BORDER, "remote type" ) {};

    verify( connection ).createRemoteObject( eq( "remote type" ) );
  }

  @Test
  public void testCreate_setsRemoteParent() {
    Chart chart = new Chart( shell, SWT.BORDER, "remote type" ) {};

    verify( remoteObject ).set( eq( "parent" ), eq( WidgetUtil.getId( chart ) ) );
  }

  @Test
  public void testDispose_destroysRemoteObject() {
    Chart chart = new Chart( shell, SWT.BORDER, "remote type" ) {};

    chart.dispose();

    verify( remoteObject ).destroy();
  }

  @Test( expected = SWTException.class )
  public void testSetChartData_checksWidget() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    chart.dispose();

    chart.setChartData( new JsonArray() );
  }

  @Test
  public void testSetChartData_isRendered() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    JsonArray data = new JsonArray().add( 23 ).add( 42 );

    chart.setChartData( data );

    verify( remoteObject ).set( "items", data );
  }

  @Test
  public void testAddListener_isRendered() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};

    chart.addListener( SWT.Selection, mock( Listener.class ) );

    verify( remoteObject ).listen( "Selection", true );
  }

  @Test
  public void testAddListener_isRenderedOnlyOnce() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};

    chart.addListener( SWT.Selection, mock( Listener.class ) );
    chart.addListener( SWT.Selection, mock( Listener.class ) );

    verify( remoteObject ).listen( "Selection", true );
  }

  @Test
  public void testRemoveListener_isRendered() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    Listener listener = mock( Listener.class );
    chart.addListener( SWT.Selection, listener );

    chart.removeListener( SWT.Selection, listener );

    verify( remoteObject ).listen( "Selection", false );
  }

  @Test
  public void testRemoveListener_isRenderedOnlyOnce() {
    Chart chart = new Chart( shell, SWT.BORDER, "remote type" ) {};
    Listener listener = mock( Listener.class );
    chart.addListener( SWT.Selection, listener );

    chart.removeListener( SWT.Selection, listener );
    chart.removeListener( SWT.Selection, listener );

    verify( remoteObject ).listen( "Selection", false );
  }

  private Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    context.replaceConnection( connection );
    return connection;
  }

}
