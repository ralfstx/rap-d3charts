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

import static org.eclipse.rap.addons.d3chart.TestUtil.fakeConnection;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.eclipse.rap.addons.d3chart.Chart;
import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Chart_Test {

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
  public void testCreate() {
    Chart chart = new Chart( shell, SWT.BORDER, "foo" ) {};

    assertEquals( shell, chart.getParent() );
    assertEquals( SWT.BORDER, chart.getStyle() & SWT.BORDER );
  }

  @Test
  public void testCreate_registeresJavaScriptResource() {
    new Chart( shell, SWT.BORDER, "foo" ) {};

    assertTrue( RWT.getResourceManager().isRegistered( "lib/d3.v3.min.js" ) );
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

  @Test
  public void testGetItems_emptyByDefault() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};

    assertEquals( 0, chart.getItems().length );
  }

  @Test( expected = SWTException.class )
  public void testGetItems_checksWidget() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    chart.dispose();

    chart.getItems();
  }

  @Test
  public void testAddItem() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    ChartItem item = mock( ChartItem.class );

    chart.addItem( item );

    assertEquals( 1, chart.getItems().length );
    assertTrue( Arrays.asList( chart.getItems() ).contains( item ) );
  }

  @Test
  public void testRemoveItem() {
    Chart chart = new Chart( shell, SWT.NONE, "foo" ) {};
    ChartItem item = mock( ChartItem.class );
    chart.addItem( item );

    chart.removeItem( item );

    assertEquals( 0, chart.getItems().length );
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

    verify( remoteObject, times( 1 ) ).listen( "Selection", true );
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

    verify( remoteObject, times( 1 ) ).listen( "Selection", false );
  }

}
