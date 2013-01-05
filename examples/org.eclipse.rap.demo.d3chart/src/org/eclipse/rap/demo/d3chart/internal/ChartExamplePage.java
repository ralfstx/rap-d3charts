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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class ChartExamplePage implements IExamplePage {

  private Chart barChart;
  private Chart pieChart;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    pieChart = new Chart( composite, SWT.BORDER );
    pieChart.setType( "pie" );
    pieChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    barChart = new Chart( composite, SWT.BORDER );
    barChart.setType( "bar" );
    barChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayoutWithoutMargin( 1, false ) );
    createButton( composite, "Add item", new Listener() {
      public void handleEvent( Event event ) {
        double value = Math.random();
        Color color = createRandomColor();
        addItem( pieChart, value, color );
        addItem( barChart, value, color );
      }
    } );
    createButton( composite, "Remove item", new Listener() {
      public void handleEvent( Event event ) {
        removeItem( pieChart );
        removeItem( barChart );
      }
    } );
  }

  private void createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
  }

  private void addItem( Chart chart, double value, Color color ) {
    ChartItem item = new ChartItem( chart );
    item.setValue( value );
    item.setColor( color );
  }

  private void removeItem( Chart chart ) {
    ChartItem[] items = chart.getItems();
    if( items.length > 0 ) {
      items[ 0 ].dispose();
    }
  }

  private Color createRandomColor() {
    return new Color( barChart.getDisplay(), randomValue(), randomValue(), randomValue() );
  }

  private static int randomValue() {
    return ( int )( 256 * Math.random() );
  }

}
