/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart.demo.internal;

import org.eclipse.rap.examples.IExampleContribution;
import org.eclipse.rap.examples.IExamplePage;


public class ChartExampleContribution implements IExampleContribution {

  @Override
  public String getId() {
    return "chart";
  }

  @Override
  public String getTitle() {
    return "Charts (experimental)";
  }

  @Override
  public IExamplePage createPage() {
    return new ChartExamplePage();
  }

}
