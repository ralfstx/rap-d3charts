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

import org.eclipse.rap.examples.IExampleContribution;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class Activator implements BundleActivator {

  private ServiceRegistration<?> registration;

  public void start( BundleContext context ) throws Exception {
    ChartExampleContribution contribution = new ChartExampleContribution();
    registration = context.registerService( IExampleContribution.class.getName(),
                                            contribution,
                                            null );
  }

  public void stop( BundleContext context ) throws Exception {
    registration.unregister();
  }

}
