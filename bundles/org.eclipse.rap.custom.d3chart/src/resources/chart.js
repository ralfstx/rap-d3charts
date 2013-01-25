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

d3chart = {};

d3chart.Chart = function( parent ) {
  this._type = "bar";
  var element = document.createElement( "div" );
  element.style.position = "absolute";
  element.style.left = "0px";
  element.style.top = "0px";
  element.style.width = "100%";
  element.style.height = "100%";
  this._element = element;
  parent.append( element );
  this._padding = 20;
  this._items = [];
  this._svg = d3.select( this._element ).append( "svg" ).attr( "class", "chart" );
  var that = this;
  rap.on( "render", function() {
    if( that._dirty ) {
      that._redraw();
      that._dirty = false;
    }
  } );
  parent.addListener( "Resize", function() {
    that._resize( parent.getClientArea() );
  } );
  this._resize( parent.getClientArea() );
};

d3chart.Chart.renderers = {};

d3chart.Chart.prototype = {

  getElement: function() {
    return this._element;
  },

  setType: function( type ) {
    this._type = type;
    this._scheduleUpdate();
  },

  getType: function() {
    return this._type;
  },

  _addItem: function( item ) {
    this._items.push( item );
    this._scheduleUpdate();
  },

  _removeItem: function( item ) {
    this._items = this._items.filter( function( element ) {
      return element !== item;
    } );
    this._scheduleUpdate();
  },

  _resize: function( clientArea ) {
    this._width = clientArea[ 2 ];
    this._height = clientArea[ 3 ];
    this._svg.attr( "width", this._width ).attr( "height", this._height );
    this._scheduleUpdate();
  },

  _scheduleUpdate: function() {
    this._dirty = true;
  },

  _redraw: function() {
    d3chart.Chart.renderers[ this._type ]( this );
  }

};

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.Chart", {

  factory : function( properties ) {
    var parent = rap.getObject( properties.parent );
    var chart = new d3chart.Chart( parent );
    return chart;
  },

  destructor : function( widget ) {
    var el = widget.getElement();
    if( el.parentNode ) {
      el.parentNode.removeChild( el );
    }
  },

  properties: [ "type" ]

} );
