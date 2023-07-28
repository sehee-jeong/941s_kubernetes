$( document ).ready( function() {
    $('#header-checkbox').click( function() {
        $($('input[type=checkbox]')).prop( 'checked', this.checked );
    } );
} );