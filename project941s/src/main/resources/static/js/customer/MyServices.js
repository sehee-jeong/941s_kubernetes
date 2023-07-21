$(document).ready(function() {
    // MyServices.html
    $('input[name=_selected_all_]').on('change', function(){
        $('input[name=_selected_]').prop('checked', this.checked);
    });
});




