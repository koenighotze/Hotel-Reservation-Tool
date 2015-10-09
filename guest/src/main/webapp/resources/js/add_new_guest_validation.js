(function () {
  $(document).ready(function () {
    $("#form").validate({
      errorElement: 'span',
      errorClass: "input-error",
      errorPlacement: function (error, element) {
        error.appendTo(element.next("span"));
        error.addClass('errorMessage');
      },
      rules: {
        'form:guest_name:inputTextField': {
          required: true,
          pattern: /^[A-Za-z .\-]*$/,
          minlength: 5,
          maxlength: 20,
        },
        'form:guest_email:inputTextField': {
          email: true
        }
      },
      messages: {
        'form:guest_name:inputTextField': {
          required: "A guest name is required",
          pattern: "A name may only consist of A-Z,a-z and whitespace ( ), hyphen (-) and dot (.)"
        },
        'form:guest_email:inputTextField': {
          email: "An email may only consist of A-Z,a-z,0-9, at (@), dot (.) and hyphen (-)"
        }
      }
    })
  });
})();