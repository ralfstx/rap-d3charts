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
import java.util.List;

import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.rap.addons.d3chart.demo.internal.data.DataSet;
import org.eclipse.rap.addons.d3chart.demo.internal.data.ExampleData;
import org.eclipse.rap.addons.d3chart.demo.internal.data.DataSet.DataItem;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class PieChartExample implements IExamplePage {

  private ColorStream colors;
  private DataSet dataSet;
  private PieChart pieChart;
  private int cursor;
  private Table table;
  private Label yearLabel;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    dataSet = ExampleData.BROWSER_YEARLY;
    colors = Colors.cat10Colors( parent.getDisplay() ).loop();
    createChartPart( parent );
    createControlPart( parent );
    createItems();
    updateItems( dataSet.getRow( 0 ) );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 2, true, true, true ) );
    pieChart = new PieChart( composite, SWT.NONE );
    pieChart.setInnerRadius( 0.6f );
    GridData layoutData = new GridData( 400, 350 );
    layoutData.horizontalAlignment = SWT.FILL;
    layoutData.horizontalSpan = 2;
    pieChart.setLayoutData( layoutData );
    pieChart.addListener( SWT.Selection, new Listener() {
      public void handleEvent( Event event ) {
        table.select( event.index );
        table.showSelection();
      }
    } );
    createButton( composite, "180°", new Listener() {
      public void handleEvent( Event event ) {
        pieChart.setStartAngle( -0.25f );
        pieChart.setEndAngle( 0.25f );
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    createButton( composite, "360°", new Listener() {
      public void handleEvent( Event event ) {
        pieChart.setStartAngle( 0f );
        pieChart.setEndAngle( 1f );
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
    createButton( composite, "donut", new Listener() {
      public void handleEvent( Event event ) {
        pieChart.setInnerRadius( 0.6f );
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    createButton( composite, "pie", new Listener() {
      public void handleEvent( Event event ) {
        pieChart.setInnerRadius( 0f );
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
    createTable( composite );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 3, true, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Prev", new Listener() {
      public void handleEvent( Event event ) {
        showPrevious();
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    yearLabel = new Label( composite, SWT.NONE );
    yearLabel.setFont( new Font( parent.getDisplay(), "sans-serif", 24, SWT.NORMAL ) );
    yearLabel.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
    createButton( composite, "Next", new Listener() {
      public void handleEvent( Event event ) {
        showNext();
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
    createTable( composite );
  }

  private void createTable( Composite parent ) {
    table = new Table( parent, SWT.SINGLE );
    GridData tableData = new GridData( SWT.FILL, SWT.FILL, true, true );
    tableData.verticalIndent = 20;
    tableData.horizontalSpan = 3;
    table.setLayoutData( tableData );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    column0.setText( " " );
    column0.setWidth( 10 );
    TableColumn column1 = new TableColumn( table, SWT.NONE );
    column1.setText( "Browser" );
    column1.setWidth( 250 );
    TableColumn column2 = new TableColumn( table, SWT.NONE );
    column2.setText( "Market share" );
    column2.setWidth( 120 );
  }

  private Button createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
    return button;
  }

  private void createItems() {
    List<String> columns = dataSet.getColumns();
    for( String column : columns ) {
      addItem( colors.next(), column );
    }
  }

  private void addItem( Color color, String text ) {
    ChartItem chartItem = new ChartItem( pieChart );
    chartItem.setValue( 0 );
    chartItem.setColor( color );
    TableItem tableItem = new TableItem( table, SWT.NONE );
    tableItem.setText( 1, text );
    tableItem.setBackground( 0, color );
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

  private void updateItems( DataItem row ) {
    yearLabel.setText( row.getText() );
    float[] values = row.getValues();
    ChartItem[] items = pieChart.getItems();
    TableItem[] tableItems = table.getItems();
    for( int i = 0; i < items.length; i++ ) {
      updateItems( items[ i ], tableItems[ i ], values[ i ] );
    }
  }

  private void updateItems( ChartItem chartItem, TableItem tableItem, float value ) {
    String text = new DecimalFormat( "#.#" ).format( value ) + "%";
    chartItem.setValue( value );
    chartItem.setText( value > 5 ? text : "" );
    tableItem.setText( 2, text );
  }

}
