rap = {

  _listeners: {},

  on: function( eventType, listener ) {
    if( !this._listeners[ eventType ] ) {
      this._listeners[ eventType ] = [];
    }
    this._listeners[ eventType ].push( listener );
  },

  off: function( eventType, listener ) {
    if( this._listeners[ eventType ] ) {
      var listeners = this._listeners[ eventType ];
      var index = -1;
      for( var i = 0; i < listeners.length; i++ ) {
        if( listeners[ i ] === listener ) {
          index = i;
        }
      }
      if( index !== -1 ) {
        listeners.splice( i, 1 );
      }
    }
  },

  registerTypeHandler: function() {
  },

  setup: function() {
    this._listeners = {};
  },

  notify: function( eventType ) {
    if( this._listeners[ eventType ] ) {
      var listeners = this._listeners[ eventType ];
      for( var i = 0; i < listeners.length; i++ ) {
        listeners[ i ]();
      }
    }
  }

};
