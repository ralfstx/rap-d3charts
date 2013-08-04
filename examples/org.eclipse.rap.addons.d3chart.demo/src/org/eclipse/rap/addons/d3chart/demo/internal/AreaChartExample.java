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

import java.util.List;

import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.addons.d3chart.StreamChart;
import org.eclipse.rap.addons.d3chart.demo.internal.data.DataSet;
import org.eclipse.rap.addons.d3chart.demo.internal.data.ExampleData;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class AreaChartExample implements IExamplePage {

  private DataSet dataSet;
  private StreamChart chart;
  private ColorStream colors;

  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    dataSet = ExampleData.BROWSER_QUARTERLY_EUROPE;
    colors = Colors.cat10Colors( parent.getDisplay() ).loop();
    createChartPart( parent );
    createControlPart( parent );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    chart = new StreamChart( composite, SWT.BORDER );
    GridData layoutData = new GridData( SWT.FILL, SWT.DEFAULT, true, false );
    layoutData.heightHint = 300;
    chart.setLayoutData( layoutData );
    createItems();
    update();
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Europe", ExampleData.BROWSER_QUARTERLY_EUROPE ).setSelection( true );
    createButton( composite, "North America", ExampleData.BROWSER_QUARTERLY_NORTHAMERICA );
    createButton( composite, "Asia", ExampleData.BROWSER_QUARTERLY_ASIA );
    createButton( composite, "Africa", ExampleData.BROWSER_QUARTERLY_AFRICA );
  }

  private void createItems() {
    List<String> columns = dataSet.getColumns();
    for( String browser : columns ) {
      ChartItem item = new ChartItem( chart );
      item.setText( browser );
      item.setColor( colors.next() );
    }
  }

  private void update() {
    ChartItem[] items = chart.getItems();
    for( int i = 0; i < items.length; i++ ) {
      items[ i ].setValues( dataSet.getValuesForColumn( i ) );
    }
  }

  private Button createButton( Composite composite, String text, final DataSet data ) {
    Button button = createButton( composite, text, new Listener() {
      public void handleEvent( Event event ) {
        dataSet = data;
        update();
      }
    } );
    button.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    return button;
  }

  private static Button createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.RADIO );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
    return button;
  }

}
