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
package org.eclipse.rap.addons.d3chart.demo;

import org.eclipse.rap.addons.d3chart.ChartItem;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.addons.d3chart.Colors;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


public class PieChartSnippet extends AbstractEntryPoint {

  @Override
  protected void createContents( Composite parent ) {
    PieChart pieChart = new PieChart( parent, SWT.NONE );
    pieChart.setLayoutData( new GridData( 300, 300 ) );
    pieChart.setInnerRadius( 0.6f );
    ColorStream colors = Colors.cat10Colors( parent.getDisplay() ).loop();

    ChartItem item1 = new ChartItem( pieChart );
    item1.setText( "Chrome" );
    item1.setColor( colors.next() );
    item1.setValue( 0.4f );

    ChartItem item2 = new ChartItem( pieChart );
    item2.setText( "Firefox" );
    item2.setColor( colors.next() );
    item2.setValue( 0.2f );

    ChartItem item3 = new ChartItem( pieChart );
    item3.setText( "IE" );
    item3.setColor( colors.next() );
    item3.setValue( 0.3f );
  }

}
