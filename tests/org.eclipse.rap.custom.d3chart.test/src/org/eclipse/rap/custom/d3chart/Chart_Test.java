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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.internal.remote.RemoteObject;
import org.eclipse.rap.rwt.internal.remote.RemoteObjectFactory;
import org.eclipse.rap.rwt.lifecycle.WidgetUtil;
import org.eclipse.rap.rwt.testfixture.Fixture;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings( "restriction" )
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
  public void testCreate_createsRemoteObject() {
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    RemoteObject remoteObject = mock( RemoteObject.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeRemoteObjectFactory( factory );

    new Chart( shell, SWT.NONE );

    verify( factory ).createRemoteObject( eq( "d3chart.Chart" ) );
  }

  @Test
  public void testCreate_setsRemoteParent() {
    RemoteObjectFactory factory = mock( RemoteObjectFactory.class );
    RemoteObject remoteObject = mock( RemoteObject.class );
    when( factory.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    Fixture.fakeRemoteObjectFactory( factory );

    Chart chart = new Chart( shell, SWT.NONE );

    verify( remoteObject ).set( eq( "parent" ), eq( WidgetUtil.getId( chart ) ) );
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
  public void testCreate_registeresJavaScriptResource() {
    new Chart( shell, SWT.NONE );

    assertTrue( RWT.getResourceManager().isRegistered( "d3.v3.min.js" ) );
    assertTrue( RWT.getResourceManager().isRegistered( "d3chart.js" ) );
  }

}
