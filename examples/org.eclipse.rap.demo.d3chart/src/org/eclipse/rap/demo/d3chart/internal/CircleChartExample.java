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

import java.text.DecimalFormat;
import org.eclipse.rap.custom.d3chart.Chart;
import org.eclipse.rap.custom.d3chart.ChartItem;
import org.eclipse.rap.custom.d3chart.ColorSequence;
import org.eclipse.rap.custom.d3chart.PieChart;
import org.eclipse.rap.demo.d3chart.internal.data.DataSet;
import org.eclipse.rap.demo.d3chart.internal.data.DataSet.Item;
import org.eclipse.rap.demo.d3chart.internal.data.ExampleData;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class CircleChartExample implements IExamplePage {

  private final ColorSequence colors = new ColorSequence( ColorSequence.CAT10_COLORS );
  private final DataSet dataSet = ExampleData.VERSION_YEARLY;
  private Chart chart;
  private int cursor;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
    createItems();
    updateItems( dataSet.getRow( 0 ) );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    chart = new PieChart( composite, SWT.NONE );
    chart.setLayoutData( new GridData( 300, 300 ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 2, true, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Prev", new Listener() {
      public void handleEvent( Event event ) {
        showPrevious();
      }
    } );
    createButton( composite, "Next", new Listener() {
      public void handleEvent( Event event ) {
        showNext();
      }
    } );
  }

  private void createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
  }

  private void createItems() {
    int columnCount = dataSet.getColumnCount();
    for( int i = 0; i < columnCount; i++ ) {
      Color color = colors.next( chart.getDisplay() );
      addItem( chart, color );
    }
  }

  private void addItem( Chart chart, Color color ) {
    ChartItem item = new ChartItem( chart );
    item.setValue( 0 );
    item.setColor( color );
  }

  private void showPrevious() {
    if( cursor > 0 ) {
      cursor--;
      updateItems( dataSet.getRow( cursor ) );
    }
  }

  private void showNext() {
    if( cursor < dataSet.getRowCount() - 1 ) {
      cursor++;
      updateItems( dataSet.getRow( cursor ) );
    }
  }

  private void updateItems( Item row ) {
    float[] values = row.getValues();
    ChartItem[] items = chart.getItems();
    for( int i = 0; i < items.length; i++ ) {
      updateItem( items[ i ], values[ i ] );
    }
  }

  private void updateItem( ChartItem item, double value ) {
    DecimalFormat format = new DecimalFormat( "#.#" );
    String text = value > 5 ? format.format( value ) + "%" : "";
    item.setValue( value );
    item.setText( text );
  }

}
