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
package org.eclipse.rap.demo.d3chart.internal;

import org.eclipse.rap.custom.d3chart.Chart;
import org.eclipse.rap.custom.d3chart.ChartItem;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class ChartExamplePage implements IExamplePage {

  private Chart chart;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    chart = new Chart( composite, SWT.BORDER );
    chart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayoutWithoutMargin( 1, false ) );
    Button button = new Button( composite, SWT.PUSH );
    button.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, false, false ) );
    button.setText( "Add item" );
    button.addListener( SWT.Selection, new Listener() {
      public void handleEvent( Event event ) {
        ChartItem item = new ChartItem( chart );
        item.setValue( Math.random() );
        item.setColor( createRandomColor( item.getDisplay(), item ) );
      }
    } );
  }

  private static Color createRandomColor( Display display, ChartItem item ) {
    return new Color( display, randomValue(), randomValue(), randomValue() );
  }

  private static int randomValue() {
    return ( int )( 256 * Math.random() );
  }

}
