$(document).ready(function() {
    // env 칸 추가
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

    // env 칸 제거
    $('#delEnv').click(function() {
        const $div = $('#envKVP')
        if ($div.children().length > 1) {
            $div.children().last().remove();
        }
    });

    // Validation 처리
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