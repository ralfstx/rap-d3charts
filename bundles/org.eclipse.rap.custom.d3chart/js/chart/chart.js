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

d3chart.Chart = function( parent, renderer ) {
  this._renderer = renderer;
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
    if( that._resized || that._dirty ) {
      if( that._resized ) {
        that._renderer.initialize( that );
        that._resized = false;
      }
      that._renderer.render( that );
      that._dirty = false;
    }
  } );
  parent.addListener( "Resize", function() {
    that._resize( parent.getClientArea() );
  } );
  this._resize( parent.getClientArea() );
};

d3chart.Chart.prototype = {

  getElement: function() {
    return this._element;
  },

  getLayer: function( name ) {
    var layer = this._svg.select( "g." + name );
    if( layer.empty() ) {
      this._svg.append( "svg:g" ).attr( "class", name );
      layer = this._svg.select( "g." + name );
    }
    return layer;
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
    this._resized = true;
  },

  _scheduleUpdate: function() {
    this._dirty = true;
  },

  destroy: function() {
    var element = this._element;
    if( element.parentNode ) {
      element.parentNode.removeChild( element );
    }
  }

};
