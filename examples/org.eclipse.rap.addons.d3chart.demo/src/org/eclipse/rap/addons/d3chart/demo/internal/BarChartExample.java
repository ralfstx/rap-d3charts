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
package org.eclipse.rap.addons.d3chart.demo.internal;

import java.text.DecimalFormat;

import org.eclipse.rap.addons.d3chart.BarChart;
import org.eclipse.rap.addons.d3chart.Chart;
import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class BarChartExample implements IExamplePage {

  private ColorStream colors;
  private BarChart barChart;
  private PieChart pieChart;

  public void createControl( Composite parent ) {
    colors = Colors.cat10Colors( parent.getDisplay() ).loop();
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    pieChart = new PieChart( composite, SWT.BORDER );
    pieChart.setLayoutData( new GridData( 300, 200 ) );
    barChart = new BarChart( composite, SWT.BORDER );
    barChart.setLayoutData( new GridData( 300, 300 ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 2, true, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Add item", new Listener() {
      public void handleEvent( Event event ) {
        float value = ( float )( Math.random() * 0.8 );
        Color color = colors.next();
        addItem( pieChart, value, color );
        addItem( barChart, value, color );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "Remove item", new Listener() {
      public void handleEvent( Event event ) {
        removeItem( pieChart );
        removeItem( barChart );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "small bars", new Listener() {
      public void handleEvent( Event event ) {
        barChart.setBarWidth( 20 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "large bars", new Listener() {
      public void handleEvent( Event event ) {
        barChart.setBarWidth( 40 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "spacing", new Listener() {
      public void handleEvent( Event event ) {
        barChart.setSpacing( 2 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "no spacing", new Listener() {
      public void handleEvent( Event event ) {
        barChart.setSpacing( 0 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "Resize charts", new Listener() {
      public void handleEvent( Event event ) {
        if( pieChart.getSize().x > 350 ) {
          pieChart.setLayoutData( new GridData( 300, 200 ) );
          barChart.setLayoutData( new GridData( 300, 300 ) );
        } else {
          pieChart.setLayoutData( new GridData( 400, 300 ) );
          barChart.setLayoutData( new GridData( 400, 400 ) );
        }
        barChart.getParent().layout();
      }
    } );
  }

  private static Button createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
    return button;
  }

  private void addItem( Chart chart, float value, Color color ) {
    ChartItem item = new ChartItem( chart );
    item.setValue( value );
    DecimalFormat format = new DecimalFormat( "#.#" );
    item.setText( format.format( value * 100 ) + "%" );
    item.setColor( color );
  }

  private void removeItem( Chart chart ) {
    ChartItem[] items = chart.getItems();
    if( items.length > 0 ) {
      items[ 0 ].dispose();
    }
  }

}
