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

import org.eclipse.swt.widgets.Composite;


public class BarChart extends Chart {

  private static final String REMOTE_TYPE = "d3chart.BarChart";

  public BarChart( Composite parent, int style ) {
    super( parent, style );
  }

  @Override
  protected String getRemoteType() {
    return REMOTE_TYPE;
  }

}
