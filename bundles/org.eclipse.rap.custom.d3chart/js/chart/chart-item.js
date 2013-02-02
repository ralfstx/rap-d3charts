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

d3chart.ChartItem = function( parent ) {
  this._parent = parent;
  this._value = 0;
  this._values = [];
  this._parent.addItem( this );
};

d3chart.ChartItem.prototype = {

  getParent: function() {
    return this._parent;
  },

  getValue: function() {
    return this._value;
  },

  setValue: function( value ) {
    this._value = value;
    this._parent._chart._scheduleUpdate();
  },

  getValues: function() {
    return this._values;
  },

  setValues: function( values ) {
    this._values = values;
    this._parent._chart._scheduleUpdate();
  },

  getColor: function() {
    return this._color;
  },

  setColor: function( color ) {
    var hex = function( value ) { return ( value < 16 ? "0" : "" ) + value.toString( 16 ); };
    this._color = "#" + hex( color[0] ) + hex( color[1] ) + hex( color[2] );
    this._parent._chart._scheduleUpdate();
  },

  getText: function() {
    return this._text;
  },

  setText: function( text ) {
    this._text = text;
    this._parent._chart._scheduleUpdate();
  },

  id: function() {
    return this._rwtId;
  },

  destroy: function() {
    this._parent.removeItem( this );
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.ChartItem", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.ChartItem( parent );
  },

  destructor: "destroy",

  properties: [ "value", "values", "color", "text" ]

} );
