$(document).ready(function() {

    // MyServices.html
    $('input[name=_selected_all_]').on('change', function(){
        $('input[name=_selected_]').prop('checked', this.checked);
    });

    // NewServices.html
    $(document).ready(function() {
        $('#addEnv').click(function() {
            $('#envKVP').append (
                '<div class="row mt-2">\n' +
                '    <div class="col-lg-3 ms-auto">\n' +
                '        <input type="text" class="form-control" required>\n' +
                '    </div>\n' +
                '    <div class="col-lg-3 me-auto">\n' +
                '        <input type="text" class="form-control" required>\n' +
                '    </div>\n' +
                '</div>'
            );
        });

        (() => {
            'use strict'
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            const forms = document.querySelectorAll('.needs-validation')

            // Loop over them and prevent submission
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        })()
    });


});




